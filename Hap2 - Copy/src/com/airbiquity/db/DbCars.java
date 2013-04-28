package com.airbiquity.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.airbiquity.hap.MetaCarSub;


/**
 * Database that holds user's car subscriptions.
 */
public class DbCars
{
  private static final String TAG = "DbCars";
  public static final String KEY_SUB_ID = "DbCars.KEY_SUB_ID";
  
  private static final String DATABASE_NAME = "cars.db";
  private static final String TABLE_SUBS = "subscrs";
  private static final int DATABASE_VERSION = 2;

    
  // Column names for the "apps" table.
  // We need a column named "_id" if we want to use a CursorAdapter. 
  public static final String SUB_COL00_ID           = "_id";                    // MetaSubscr.ID_VEHICLE_ID
  public static final String SUB_COL01_NICKNAME     = MetaCarSub.ID_NICKNAME       ;
  public static final String SUB_COL02_MODEL        = MetaCarSub.ID_MODEL       ;
  public static final String SUB_COL03_YEAR         = MetaCarSub.ID_YEAR        ;
  public static final String SUB_COL04_IS_ACTIVATED = MetaCarSub.ID_IS_ACTIVATED;
  public static final String SUB_COL05_HUID         = MetaCarSub.ID_HUID        ;
  public static final String SUB_COL06_VIN          = MetaCarSub.ID_VIN         ;
  public static final String SUB_COL07_SUB_TYPE     = "subType";
  public static final String SUB_COL08_EXP_DATE     = "expDate";
  
  private static final String[] COLS_ID   = new String[]{ SUB_COL00_ID   };  
  
  private SQLiteDatabase mDb;
  private DatabaseHelper mDatabaseHelper;


  /**
   * Constructor. 
   * @param context : context to use to open or create the database.
   */
  public DbCars( Context context )
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
  private ContentValues getVals( MetaCarSub m )
  {      
    ContentValues v = new ContentValues();
    v.put( SUB_COL00_ID           , m.id          ); 
    v.put( SUB_COL01_NICKNAME     , m.nickname    ); 
    v.put( SUB_COL02_MODEL        , m.model       ); 
    v.put( SUB_COL03_YEAR         , m.year        ); 
    v.put( SUB_COL04_IS_ACTIVATED , m.isActivated ); 
    v.put( SUB_COL05_HUID         , m.huid        ); 
    v.put( SUB_COL06_VIN          , m.vin         ); 
    v.put( SUB_COL07_SUB_TYPE     , m.subType     ); 
    v.put( SUB_COL08_EXP_DATE     , m.expDate     ); 
    return v;
  }
  
  /**
   * Inserts or update an app in the database. 
   * @param m : app's metadata.
   */
  public void insertSub( MetaCarSub m )
  {
      ContentValues v = getVals( m );
      mDb.replace( TABLE_SUBS, null, v );
      //Log.e( TAG, "meta="+m );
  }

  
  /**
   * Delete subscription from the database. 
   * @param id : sub ID to delete.
   */
  public void detete( int id )
  {
    mDb.delete( TABLE_SUBS, SUB_COL00_ID + "=?", new String[] { ""+id } );
  }

  
  /**
   * Get subscriptions.
   * @return cursor that contain the selected apps.
   */
  public Cursor getSubs()
  {
    //Log.e( TAG, strOrder );
    return mDb.query( TABLE_SUBS, null, null, null, null, null, null );
  }

  /**
   * Get subscription by ID.
   * @param id : sub ID
   * @return Cursor.
   */
  private Cursor getSubById( long id )
  {
    String strWhere = SUB_COL00_ID+" = "+id;
    Cursor c = mDb.query( TABLE_SUBS, null, strWhere, null, null, null, null );
    return c;
  }

  
  /**
   * Get app by ID.
   * @param id : app's ID.
   * @return MetaApp or null if not found.
   */
  public MetaCarSub getMetaSubById( long id )
  {
    Cursor c = getSubById( id );
    MetaCarSub m = null;
    if( c.moveToFirst() )
      m = cursorToMetaSubscr( c );
    c.close();
    return m;
  }

  
  /**
   * Get quantity of subs.
   * @return quantity.
   */
  public int getQntt()
  {
    Cursor c = mDb.query( TABLE_SUBS, COLS_ID, null, null, null, null, null );
    int qntt = c.getCount();
    c.close();
    return qntt;
  }
  
  
  /** Get sub's ID from given cursor. */
  public static int cursorToSubId( Cursor c )
  {
    return c.getInt( c.getColumnIndex(SUB_COL00_ID) );
  }
  
  
  /**
   * Get MetaSubscr from Cursor.
   * @param c : cursor.
   * @return MetaSubscr object.
   */
  public static MetaCarSub cursorToMetaSubscr( Cursor c )
  {      
      MetaCarSub m = new MetaCarSub(
        c.getInt   ( c.getColumnIndex( SUB_COL00_ID            ) ), 
        c.getString( c.getColumnIndex( SUB_COL01_NICKNAME      ) ),
        c.getString( c.getColumnIndex( SUB_COL02_MODEL         ) ),
        c.getInt   ( c.getColumnIndex( SUB_COL03_YEAR          ) ),
        c.getInt   ( c.getColumnIndex( SUB_COL04_IS_ACTIVATED  ) ) != 0 ,
        c.getString( c.getColumnIndex( SUB_COL05_HUID          ) ),
        c.getString( c.getColumnIndex( SUB_COL06_VIN           ) ),
        c.getInt   ( c.getColumnIndex( SUB_COL07_SUB_TYPE      ) ),
        c.getString( c.getColumnIndex( SUB_COL08_EXP_DATE      ) )
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
        db.execSQL( "DROP TABLE IF EXISTS " + TABLE_SUBS );
        createTables( db );
        return;
      }
    }

    private void createTables( SQLiteDatabase db )
    {
      db.execSQL( "CREATE TABLE " + TABLE_SUBS + "(" +
        SUB_COL00_ID           + " INTEGER PRIMARY KEY, " +
        SUB_COL01_NICKNAME     + " TEXT, " +
        SUB_COL02_MODEL        + " TEXT, " +
        SUB_COL03_YEAR         + " INTEGER, " +
        SUB_COL04_IS_ACTIVATED + " INTEGER, " +
        SUB_COL05_HUID         + " TEXT, " +
        SUB_COL06_VIN          + " TEXT, " +
        SUB_COL07_SUB_TYPE     + " INTEGER, " +
        SUB_COL08_EXP_DATE     + " TEXT)"
        );      
    }
  }
}
