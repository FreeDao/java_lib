package com.airbiquity.application.manager;

import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DatabaseAdapter for Apps.
 *
 */
public class ApplicationDatabaseAdapter
{
    private static final String DATABASE_NAME = "hap.db";
    private static final String DATABASE_TABLE = "applications";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ROW_ID = "rowId";
    private static final String KEY_NAME = "name";
    private static final String KEY_VERSION = "version";
    private static final String KEY_BASE_ACTIVITY = "baseActivity";
    private SQLiteDatabase db;
    private final Context context;
    private AppsDatabaseOpenHelper dbHelper;


    public ApplicationDatabaseAdapter( Context _context )
    {
        this.context = _context;
        dbHelper = new AppsDatabaseOpenHelper( context, DATABASE_NAME, null, DATABASE_VERSION );
    }


    public void close()
    {
        db.close();
    }


    public void open() throws SQLiteException
    {
        try
        {
            db = dbHelper.getWritableDatabase();
        }
        catch( SQLiteException ex )
        {
            db = dbHelper.getReadableDatabase();
        }
    }


    /**
     * Inserts an application in the database.
     * @param name Name of the application to be inserted.
     * @param version Version of the application to be inserted.
     * @param baseActivity Name of the application's base activity.
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insert( String name, String version, String baseActivity )
    {
        // try to remove an existing row of the same name
        remove( name );
        
        // Create a new row of values to insert.
        ContentValues newTaskValues = new ContentValues();
        
        // Assign values for each row.
        newTaskValues.put( KEY_NAME, name );
        newTaskValues.put( KEY_VERSION, version );
        newTaskValues.put( KEY_BASE_ACTIVITY, baseActivity );
        
        // Insert the row.
        return db.insert( DATABASE_TABLE, null, newTaskValues );
    }


    /**
     * Removes an application from the database.
     * @param name Name of the application to be removed.
     * @return true if the application was successfully removed, false otherwise.
     */
    public boolean remove( String name )
    {
        return db.delete( DATABASE_TABLE, KEY_NAME + "='" + name + "'", null ) > 0;
    }


    /**
     * Deletes all applications from the database.
     * @return true if successful, otherwise false.
     */
    public boolean deleteAll()
    {
        return db.delete( DATABASE_TABLE, null, null ) > 0;
    }

    
    /**
     * Retrieves map of profiles of all registered applications.
     * @return map of profiles of all registered applications. The key for each element in the map is 
     * the application name, and the value is the application version.
     */
    public Map<String, String> getProfile()
    {
        // query the database for all application names and versions
        String[] COLUMNS = new String[] { KEY_NAME, KEY_VERSION };
        final int NAME_INDEX = 0;
        final int VERSION_INDEX = 1;
        Cursor cursor = db.query( DATABASE_TABLE, COLUMNS, null, null, null, null, null, null );
        Map<String, String> profile = new HashMap<String, String>();
        // copy all rows to the map
        for( cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext() )
        {
            profile.put( cursor.getString( NAME_INDEX ), cursor.getString( VERSION_INDEX ) );
        }
        cursor.close();
        return profile;
    }


    /**
     * Retrieves the name of the base activity for the specified application.
     * @param applicationName name of the application to search for
     * @return base activity of the specified application if available, null otherwise
     */
    public String getBaseActivity( String applicationName )
    {
        String[] COLUMNS = new String[] { KEY_BASE_ACTIVITY };
        String selection = KEY_NAME + " = ?";
        String[] args = new String[] { applicationName };
        Cursor result = db.query( DATABASE_TABLE, COLUMNS, selection, args, null, null, null, null );
        String baseActivity = null;
        if( (result.getCount() == 1) && result.moveToFirst() )
        {
            baseActivity = result.getString( 0 );
        }
        result.close();
        return baseActivity;
    }

    /**
   * 
   *
   */
    private static class AppsDatabaseOpenHelper extends SQLiteOpenHelper
    {
        public AppsDatabaseOpenHelper( Context context, String name, CursorFactory factory, int version )
        {
            super( context, name, factory, version );
        }

        // SQL Statement to create a new database.
        private static final String DATABASE_CREATE = "create table " + DATABASE_TABLE + " (" + KEY_ROW_ID + " integer primary key autoincrement, " + KEY_NAME + " text not null, "
                + KEY_VERSION + " text not null, " + KEY_BASE_ACTIVITY + " text not null);";


        @Override
        public void onCreate( SQLiteDatabase _db )
        {
            _db.execSQL( DATABASE_CREATE );
        }


        @Override
        public void onUpgrade( SQLiteDatabase _db, int _oldVersion, int _newVersion )
        {
            Log.w( "DatabaseAdapter", "Upgrading from version " + _oldVersion + " to " + _newVersion + ", which will destroy all old data" );
            
            // Drop the old table.
            _db.execSQL( "DROP TABLE IF EXISTS " + DATABASE_TABLE );
            
            // Create a new one.
            onCreate( _db );
        }
    }
}
