package com.airbiquity.db;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.airbiquity.hap.MetaApp;


/**
 * Database that records apps.
 */
public class DbApps
{
  private static final String TAG = "DbApps";
  private static final String DATABASE_NAME = "apps.db";
  private static final String TABLE_APPS = "apps";
  private static final int DATABASE_VERSION = 2;

    
  // Column names for the "apps" table.
  // We need a column named "_id" if we want to use a CursorAdapter. 
  public static final String APPS_COL00_ID           = "_id";                    // App ID. (64bit long)
  public static final String APPS_COL01_NAME         = MetaApp.ID_APP_NAME     ;
  public static final String APPS_COL02_COMPANY      = MetaApp.ID_COMPANY      ;
  public static final String APPS_COL03_URL_DN_APP   = MetaApp.ID_APP_DOWN_URLS;
  public static final String APPS_COL04_URL_DN_ICON  = MetaApp.ID_ICON_DOWN_URL;
  public static final String APPS_COL05_DESCRIPTION  = MetaApp.ID_DISCRIPTION  ;
  public static final String APPS_COL06_TYPE         = MetaApp.ID_TYPE         ;
  public static final String APPS_COL07_PACKAGE_NAME = MetaApp.ID_PACKAGE_NAME ;
  public static final String APPS_COL08_HAS_NOMADIC  = MetaApp.ID_HAS_NOMADIC  ;
  public static final String APPS_COL_ICON           = MetaApp.ID_ICON         ;
  public static final String APPS_COL_IS_ON          = MetaApp.ID_IS_ON        ;
  
  private static final String[] COLS_ID   = new String[]{ APPS_COL00_ID   };  
  
  private SQLiteDatabase mDb;
  private DatabaseHelper mDatabaseHelper;


  /**
   * Constructor. 
   * @param context : context to use to open or create the database.
   */
  public DbApps( Context context )
  {
    mDatabaseHelper = new DatabaseHelper( context );
    mDb = mDatabaseHelper.getWritableDatabase();
  }


  public void close()
  {
    mDatabaseHelper.close();
  }


  /**
   * Get values of app columns.
   * @param m : meta
   * @return ContentValues
   */
  private ContentValues getVals( MetaApp m )
  {
      byte[] blob = null;
      if( null != m.icon )
      {
          ByteArrayOutputStream out = new ByteArrayOutputStream();
          m.icon.compress(Bitmap.CompressFormat.PNG, 100, out);
          blob = out.toByteArray();
      }
      
    ContentValues v = new ContentValues();
    v.put( APPS_COL00_ID           , m.id          ); 
    v.put( APPS_COL01_NAME         , m.name        ); 
    v.put( APPS_COL02_COMPANY      , m.company     ); 
    v.put( APPS_COL03_URL_DN_APP   , m.urlDownApp  ); 
    v.put( APPS_COL04_URL_DN_ICON  , m.urlDownIcon ); 
    v.put( APPS_COL05_DESCRIPTION  , m.discript    ); 
    v.put( APPS_COL06_TYPE         , m.type        ); 
    v.put( APPS_COL07_PACKAGE_NAME , m.packageName ); 
    v.put( APPS_COL08_HAS_NOMADIC  , m.hasNomadic  ); 
    v.put( APPS_COL_ICON           , blob           ); 
    v.put( APPS_COL_IS_ON          , m.isOn        ); 
    return v;
  }
  
  /**
   * Inserts or update an app in the database. 
   * @param m : app's metadata.
   */
  public void insertApp( MetaApp m )
  {
      ContentValues v = getVals( m );
      mDb.replace( TABLE_APPS, null, v );
      //Log.e( TAG, "meta="+m );
  }

  
  
  /**
   * Set app state ON/OFF.
   * @param id : app ID.
   * @param p : state.
   */
  public void setOnOff( int id, int p )
  {
    ContentValues v = new ContentValues();
    v.put( APPS_COL_IS_ON, p );
    String strWhere = APPS_COL00_ID+" = "+id;
    mDb.update( TABLE_APPS, v, strWhere, null );
  }

  
  
  /**
   * Delete app from the database. 
   * @param id : app ID to delete.
   */
  public void deteteApp( int id )
  {
    mDb.delete( TABLE_APPS, APPS_COL00_ID + "=?", new String[] { ""+id } );
  }


  /**
   * Get apps, filtered by type.
   * @param type : 0=basic, 1=premium
   * @return cursor that contain the selected apps.
   */
  public Cursor getAppsByType( int type )
  {    
    String strWhere = APPS_COL06_TYPE+" = "+type;

    
    //Log.e( TAG, strOrder );
    return mDb.query( TABLE_APPS, null, strWhere, null, null, null, null );
  }


  /**
   * Get app by ID.
   * @param id : app ID
   * @return Cursor.
   */
  private Cursor getAppById( long id )
  {
    String strWhere = APPS_COL00_ID+" = "+id;
    Cursor c = mDb.query( TABLE_APPS, null, strWhere, null, null, null, null );
    return c;
  }

  
  /**
   * Get apps without icon.
   * @return Cursor.
   */
  private Cursor getAppsNoIcon()
  {
    String strWhere = APPS_COL_ICON+" = NULL";
    Cursor c = mDb.query( TABLE_APPS, null, strWhere, null, null, null, null );
    return c;
  }

  public LinkedList<MetaApp> getAppsWithoutIcons()
  {
      LinkedList<MetaApp> apps = new LinkedList<MetaApp>();
      Cursor c = getAppsNoIcon();
      while( c.moveToNext() )
        apps.add( cursorToMetaApp( c ) );
      c.close();
      return apps;
  }
  
  /**
   * Get app by ID.
   * @param id : app's ID.
   * @return MetaApp or null if not found.
   */
  public MetaApp getMetaAppById( long id )
  {
    Cursor c = getAppById( id );
    MetaApp m = null;
    if( c.moveToFirst() )
      m = cursorToMetaApp( c );
    c.close();
    return m;
  }
  
  
  /**
   * Get quantity of apps.
   * @return quantity.
   */
  public int getAppsQntt()
  {
    Cursor c = mDb.query( TABLE_APPS, COLS_ID, null, null, null, null, null );
    int qntt = c.getCount();
    c.close();
    return qntt;
  }
  
  
  /** Get app's ID from given cursor. */
  public static int cursorToAppId( Cursor c )
  {
    return c.getInt( c.getColumnIndex(APPS_COL00_ID) );
  }
  
  
  /**
   * Get MetaApp from Cursor.
   * @param c : cursor.
   * @return MetaApp object.
   */
  public static MetaApp cursorToMetaApp( Cursor c )
  {
      Bitmap icon = null;
      byte[] blob = c.getBlob( c.getColumnIndex(APPS_COL_ICON) );
      if( null != blob )
          icon = BitmapFactory.decodeByteArray( blob, 0, blob.length, null );
      
      MetaApp m = new MetaApp(
        c.getLong  ( c.getColumnIndex( APPS_COL00_ID            ) ), 
        c.getString( c.getColumnIndex( APPS_COL01_NAME          ) ),
        c.getString( c.getColumnIndex( APPS_COL02_COMPANY       ) ),
        c.getString( c.getColumnIndex( APPS_COL03_URL_DN_APP    ) ),
        c.getString( c.getColumnIndex( APPS_COL04_URL_DN_ICON   ) ),
        c.getString( c.getColumnIndex( APPS_COL05_DESCRIPTION   ) ),
        c.getInt   ( c.getColumnIndex( APPS_COL06_TYPE          ) ),
        c.getString( c.getColumnIndex( APPS_COL07_PACKAGE_NAME  ) ),
        c.getInt   ( c.getColumnIndex( APPS_COL08_HAS_NOMADIC   ) ) != 0,
        icon,
        c.getInt   ( c.getColumnIndex( APPS_COL_IS_ON           ) ) != 0 
      );
    return m;
  }

  
  
  /**
   * This is a standard helper class for constructing the database.
   */
  private class DatabaseHelper extends SQLiteOpenHelper
  {
    public DatabaseHelper( Context context )
    {
      super( context, DATABASE_NAME, null, DATABASE_VERSION );
    }

    @Override
    public void onCreate( SQLiteDatabase db )
    {
      createTables( db );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion )
    {
      // Production-quality upgrade code should modify the tables when the database version changes 
      // instead of dropping the tables and re-creating them.
      //if( newVersion != DATABASE_VERSION )
      {
        Log.w( TAG, "Database upgrade from old: " + oldVersion + " to: " + newVersion );
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_APPS );
        createTables( db );
        return;
      }
    }

    private void createTables( SQLiteDatabase db )
    {
      db.execSQL( "CREATE TABLE " + TABLE_APPS + "(" +
        APPS_COL00_ID             + " INTEGER PRIMARY KEY, " +
        APPS_COL01_NAME           + " INTEGER, " +
        APPS_COL02_COMPANY        + " INTEGER, " +
        APPS_COL03_URL_DN_APP     + " INTEGER, " +
        APPS_COL04_URL_DN_ICON    + " INTEGER, " +
        APPS_COL05_DESCRIPTION    + " INTEGER, " +
        APPS_COL06_TYPE           + " INTEGER, " +
        APPS_COL07_PACKAGE_NAME   + " INTEGER, " +
        APPS_COL08_HAS_NOMADIC    + " INTEGER, " +
        APPS_COL_ICON             + " BLOB, "    +
        APPS_COL_IS_ON            + " INTEGER)"
        );      
    }
  }
}
