package com.airbiquity.mcs.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.os.Environment;
import android.util.Log;

/**
 * Various utility methods.
 */
public class U
{
    private static final String TAG = "U";    
    
    
    /**
     * Write chunk of bytes from the buffer into given file. (used for debugging)
     * @param buf : buffer that contains the data.
     * @param offset : index of the 1st byte to write.
     * @param size   : size of the data to write.
     * @param filename : name of the file. (it will be created in /sdcard/ folder).
     */
    public static void saveFile( byte[] buf, int offset, int size, String filename )
    {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        path.mkdirs();
        File file = new File( path, filename );
                
        FileOutputStream out = null;
        try
        {
            out = new FileOutputStream( file );
            out.write( buf, offset, size );
            out.close();
        }
        catch( Exception e )
        {
            Log.e( TAG, "saveFile", e );
        }
    }
    
}
