package com.airbiquity.connectionmgr.util;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/*
    String zipFile = Environment.getExternalStorageDirectory() + "/files.zip"; 
    String unzipLocation = Environment.getExternalStorageDirectory() + "/unzipped/"; 
     
    MyZip d = new MyZip(zipFile, unzipLocation); 
    d.unzip(); 
*/

/**
 * Zip utility methods.
 */
public class MyZip
{
    /**
     * Unzip given file into given folder.
     * @param fileZip : zip file to unzip.
     * @param dirOut  : folder where to put the unzipped files.
     */
    public static void unzip( File fileZip, File dirOut )
    {
        try
        {
            byte[] buf = new byte[10*1024];
            if( !fileZip.canRead() )
                throw new IllegalArgumentException( fileZip.getAbsolutePath() );
            if( !dirOut.isDirectory() )
                dirOut.mkdirs();
            if( !dirOut.isDirectory() )
                throw new IllegalArgumentException( dirOut.getAbsolutePath() );
            
            FileInputStream fin = new FileInputStream( fileZip );
            ZipInputStream zin = new ZipInputStream( fin );
            ZipEntry ze = null;
            while( (ze = zin.getNextEntry()) != null )
            {
                File fileZe = new File( dirOut, ze.getName() );
                Log.i("Zip", fileZe.getAbsolutePath() );
                if( ze.isDirectory() )
                    continue;
                assureParentExists( fileZe );
                BufferedOutputStream fout = new BufferedOutputStream( new FileOutputStream( fileZe ) );
                while( true )
                {
                    int read = zin.read( buf );
                    if( -1 == read )
                      break;  // End of file.                    
                    fout.write( buf, 0, read );                    
                }
                zin.closeEntry();
                fout.close();
            }
            zin.close();
        }
        catch( Exception e )
        {
            Log.e( "MyZip", "unzip", e );
        }
    }

    
    /**
     * Make sure parent folder of given file exists.
     * @param f : file.
     */
    private static void assureParentExists( File f )
    {
        File dir = f.getParentFile();
        if( !dir.isDirectory() )
            dir.mkdirs();        
    }
}
