package com.airbiquity.connectionmgr.msp;

public class MspAudioMgr {

	private MspLayer mLayer;
	
	public MspAudioMgr(){
	}

	public MspAudioMgr(MspLayer layer) {
		this.mLayer = layer;
	}

	/**
	 * Request to start stream audio
	 * @param operation 0x01:start 0x02:end
	 */
	public void sendStreamAudioRequest(int operation) {
		byte[] payload = new byte[] { (byte)operation, 0 };
		MspMsg msgResp = new MspMsg(payload, MspConst.AUDIO_INDICATION, (short) 0);
		mLayer.writeData(msgResp);
	}


	/**
	 * 
	 * @param operation 0x01:start 0x02:end
	 * @param language 
	 * @param support_information  0x01:support 0x02:not support
	 */
	public void sendTtsRequet(int operation,int language, int support_information) {
		byte[] payload = new byte[] { (byte)operation , (byte) language, (byte)support_information, 0 };
		MspMsg msgResp = new MspMsg(payload, MspConst.TTS_REQUEST, (short) 0);
		mLayer.writeData(msgResp);
	}

	/**
	 * Send off board VR request
	 * @param operaion 0x01:start 0x02:end
	 */
	public void sendOffBoardVrRequest(int operaion) {
		byte[] payload = new byte[] { (byte)operaion, 0 };
		MspMsg msgResp = new MspMsg(payload, MspConst.OFFBOARD_VR, (short) 0);
		mLayer.writeData(msgResp);
	}
	
	/**
	 * set the layer 
	 * @param layer
	 */
	public void setLayer(MspLayer layer){
		this.mLayer = layer;
	}
}
