package com.airbiquity.audio;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StreamCorruptedException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import speex.OggCrc;
import speex.SpeexDecoder;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.airbiquity.connectionmgr.msp.MspAudioMgr;
import com.airbiquity.hap.A;
import com.airbiquity.util_net.HttpHeader;
import com.airbiquity.util_net.HttpUtil;
import com.airbiquity.util_net.UrlMaker;

public class SpxTextToSpeechPlayer implements Runnable {
	private static final String TAG = "SpxTextToSpeechPlayer";

	private final static int DELAY_TIME = 1000;

	private int sampleRateInHz = 16000;
	private static final int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
	private static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	protected SpeexDecoder speexDecoder;
	private AudioTrack audioTrack = null;
	private MspAudioMgr mAudioMgr = null;

	/** Defines whether or not the perceptual enhancement is used. */
	protected boolean enhanced = true;

	/** If input is raw, defines the decoder mode (0=NB, 1=WB and 2-UWB). */
	private int mode = 1;

	/** If input is raw, defines the number of frmaes per packet. */
	private int nframes = 1;

	/** If input is raw, defines the sample rate of the audio. */
	private int sampleRate = -1;

	/** If input is raw, defines th number of channels (1=mono, 2=stereo). */
	private int channels = 1;

	private final Object mutex = new Object();

	private volatile boolean isPlaying = false;
	private volatile boolean isPausing = false;

	private String text;
	private String language;
	private int ttsId;

	public SpxTextToSpeechPlayer(String text, String language) {
		this.text = text;
		this.language = language;
	}

	public void run() {
		HttpURLConnection connection = null;
		DataInputStream dataInputStream = null;
		DataOutputStream dataOutputStream = null;

		try {
			URL urlForTextToSpeech = UrlMaker.getUrlForTextToSpeech(this.language, URLEncoder.encode("audio/x-speex;rate=16000"));
			connection = HttpUtil.openCon(urlForTextToSpeech);

			// add media type text/plain
			connection.addRequestProperty(HttpHeader.NAME_CONTENT_TYPE, HttpHeader.VAL_TEXT);
			// Add mip id & auth token
			connection.addRequestProperty(HttpHeader.NAME_MIP_ID, A.getMipId());
			connection.addRequestProperty(HttpHeader.NAME_AUTH_TOKEN, A.getAuthToken());

			// send text
			connection.setDoOutput(true);
			dataOutputStream = new DataOutputStream(new BufferedOutputStream(connection.getOutputStream()));
			dataOutputStream.write(this.text.getBytes());
			dataOutputStream.flush();

			final int resCode = connection.getResponseCode(); // Get response
																// code.

			if (resCode == HttpURLConnection.HTTP_OK) {
				dataInputStream = new DataInputStream(new BufferedInputStream(connection.getInputStream()));
				// TODO ttsReady here
				A.a().loadUrlOnUiThread("javascript:ttsReady(" + ttsId + ")");
			} else {
				Log.d(TAG, "tts response error" + resCode);
				Log.d(TAG, streamToString(connection.getErrorStream()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		byte[] header = new byte[2048];
		byte[] payload = new byte[65536];
		byte[] decdat = new byte[44100 * 2 * 2];
		final int OGG_HEADERSIZE = 27;
		final int OGG_SEGOFFSET = 26;
		final String OGGID = "OggS";
		int segments = 0;
		int curseg = 0;
		int bodybytes = 0;
		int decsize = 0;
		int packetNo = 0;

		// Play or Stop
		synchronized (mutex) {
			while (!isPlaying) {
				try {
					mutex.wait();
				} catch (InterruptedException e) {
					return;
				}
			}
		}

		int bufferSizeInBytes = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioEncoding);
		audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz, channelConfig, audioEncoding, 2 * bufferSizeInBytes, AudioTrack.MODE_STREAM);
		audioTrack.play();
		int origchksum;
		int chksum;
		try {
			while (isPlaying && dataInputStream != null) {
				
				// Pause or Resume
				synchronized (mutex) {
					while (isPausing) {
						try {
							mutex.wait();
						} catch (InterruptedException e) {
							return;
						}
					}
				}
				// int a = mDataInputStream.available();
				// Log.d( "--->", "avaliable  =  " + a );
				// read the OGG header
				dataInputStream.readFully(header, 0, OGG_HEADERSIZE);
				// Log.d( TAG, "header = " + new String( header, 0, OGG_HEADERSIZE ) );
				origchksum = readInt(header, 22);
				// Log.d( TAG, "Origin Check Sum = " + origchksum );
				header[22] = 0;
				header[23] = 0;
				header[24] = 0;
				header[25] = 0;
				chksum = OggCrc.checksum(0, header, 0, OGG_HEADERSIZE);
				// Log.d( TAG, "Check Sum = " + chksum );
				// make sure it's an OGG header
				if (!OGGID.equals(new String(header, 0, 4))) {
					Log.e(TAG, "missing ogg id!");
					break;
				}
				/* how many segments are there? */
				segments = header[OGG_SEGOFFSET] & 0xFF;
				dataInputStream.readFully(header, OGG_HEADERSIZE, segments);
				chksum = OggCrc.checksum(chksum, header, OGG_HEADERSIZE, segments);
				/* decode each segment, writing output to wav */
				for (curseg = 0; curseg < segments; curseg++) {
					/* get the number of bytes in the segment */
					bodybytes = header[OGG_HEADERSIZE + curseg] & 0xFF;
					if (bodybytes == 255) {
						Log.e(TAG, "sorry, don't handle 255 sizes!");
						break;
					}
					dataInputStream.readFully(payload, 0, bodybytes);
					chksum = OggCrc.checksum(chksum, payload, 0, bodybytes);
					/* decode the segment */
					/* if first packet, read the Speex header */
					if (packetNo == 0) {
						if (readSpeexHeader(payload, 0, bodybytes)) {
							// Log.d( TAG, "File Format: Ogg Speex" );
							// Log.d( TAG, "Sample Rate: " + sampleRate );
							// Log.d( TAG, "Channels: " + channels );
							// Log.d( TAG, "Encoder mode: " + (mode == 0 ?
							// "Narrowband" : (mode == 1 ? "Wideband" : "UltraWideband")) );
							// Log.d( TAG, "Frames per packet: " + nframes );
							packetNo++;
						} else {
							packetNo = 0;
						}
					} else if (packetNo == 1) { 
						// Ogg Comment packet
						packetNo++;
					} else {
						speexDecoder.processData(payload, 0, bodybytes);
						for (int i = 1; i < nframes; i++) {
							speexDecoder.processData(false);
						}
						/* get the amount of decoded data */
						if ((decsize = speexDecoder.getProcessedData(decdat, 0)) > 0) {
							// writer.writePacket(decdat, 0, decsize);
							audioTrack.write(decdat, 0, decsize);
						}
						packetNo++;
					}
				}
				if (chksum != origchksum) {
					// throw new IOException( "Ogg CheckSums do not match" );
					Log.e(TAG, "Ogg CheckSums do not match");
					break;
				}
			}
		} catch (StreamCorruptedException e) {
			Log.e(TAG, "StreamCorruptedException", e);
		} catch (EOFException eof) {
			Log.e(TAG, "EOFException", eof);
		} catch (IOException e) {
			Log.e(TAG, "IOException", e);
		} finally {
			try {
				if (audioTrack != null) {
					audioTrack.flush();
					audioTrack.stop();
					audioTrack.release();
					audioTrack = null;
				}

				if (dataInputStream != null) {
					dataInputStream.close();
					dataInputStream = null;
				}

				if (dataOutputStream != null) {
					dataOutputStream.close();
					dataOutputStream = null;
				}

				if (connection != null) {
					connection.disconnect();
					connection = null;
				}

				// TODO delay one second to send ttsEnd and stream audio
				// finished request.
				Thread.sleep(DELAY_TIME);
				// TODO endTTS here
				A.a().loadUrlOnUiThread("javascript:ttsEnd(" + ttsId + ")");
				mAudioMgr.sendStreamAudioRequest(2);

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Reads the header packet.
	 * 
	 * <pre>
	 *  0 -  7: speex_string: "Speex   "
	 *  8 - 27: speex_version: "speex-1.0"
	 * 28 - 31: speex_version_id: 1
	 * 32 - 35: header_size: 80
	 * 36 - 39: rate
	 * 40 - 43: mode: 0=narrowband, 1=wb, 2=uwb
	 * 44 - 47: mode_bitstream_version: 4
	 * 48 - 51: nb_channels
	 * 52 - 55: bitrate: -1
	 * 56 - 59: frame_size: 160
	 * 60 - 63: vbr
	 * 64 - 67: frames_per_packet
	 * 68 - 71: extra_headers: 0
	 * 72 - 75: reserved1
	 * 76 - 79: reserved2
	 * </pre>
	 * 
	 * @param packet
	 * @param offset
	 * @param bytes
	 * @return
	 */
	private boolean readSpeexHeader(final byte[] packet, final int offset, final int bytes) {
		if (bytes != 80) {
			Log.e(TAG, "bytes!=80 " + bytes);
			return false;
		}
		if (!"Speex   ".equals(new String(packet, offset, 8))) {
			return false;
		}
		mode = packet[40 + offset] & 0xFF;
		sampleRate = readInt(packet, offset + 36);
		channels = readInt(packet, offset + 48);
		nframes = readInt(packet, offset + 64);
		speexDecoder = new SpeexDecoder();
		return speexDecoder.init(mode, sampleRate, channels, enhanced);
	}

	/**
	 * Converts Little Endian (Windows) bytes to an int (Java uses Big Endian).
	 * 
	 * @param data
	 *            the data to read.
	 * @param offset
	 *            the offset from which to start reading.
	 * @return the integer value of the reassembled bytes.
	 */
	protected static int readInt(final byte[] data, final int offset) {
		// no 0xff on the last one to keep the sign
		return (data[offset] & 0xff) | ((data[offset + 1] & 0xff) << 8) | ((data[offset + 2] & 0xff) << 16) | (data[offset + 3] << 24);
	}

	public boolean setPlaying(boolean isPlaying) {
		boolean ret = false;
		synchronized (mutex) {
			this.isPlaying = isPlaying;
			if (this.isPlaying) {
				mutex.notify();
			}
			ret = true;
		}
		return ret;
	}

	public boolean isPlaying() {
		synchronized (mutex) {
			return isPlaying;
		}
	}

	public boolean setPausing(boolean isPausing) {
		boolean ret = false;
		synchronized (mutex) {
			this.isPausing = isPausing;
			if (!this.isPausing) {
				mutex.notify();
			}
			ret = true;
		}
		return ret;

	}

	public boolean isPausing() {
		synchronized (mutex) {
			return isPausing;
		}
	}

	public String streamToString(InputStream is) {

		StringBuffer sb = new StringBuffer();
		try {
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, count));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	public void setTtsId(int id) {
		this.ttsId = id;
	}

	public void setAudioMgr(MspAudioMgr audioMgr) {
		this.mAudioMgr = audioMgr;
	}

}
