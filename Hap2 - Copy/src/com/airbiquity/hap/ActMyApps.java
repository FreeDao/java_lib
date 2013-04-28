package com.airbiquity.hap;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

/**
 * Manage My Apps. (apps screen)
 */
public class ActMyApps extends FragmentActivity
{
    //private static final String TAG = "ActMyApps";
    
    private CursorAdapterApp adapter;   // Adapter for apps metadata.
    private Cursor cursr;
    private boolean needRefresh;  // Whether we need to re-query the DB because the list has changed.
    private ListView  v_list; // List of items.
    
    @Override
    protected void onCreate( Bundle b )
    {
        super.onCreate( b );
        setContentView( R.layout.act_my_apps );
        
        v_list = (ListView) findViewById( R.id.list );
        needRefresh = true;
    }
    
    
    @Override
    public void onResume()
    {
      super.onResume();
      
      if( needRefresh )
      {
        needRefresh = false;
        refreshList();
      }
    }
    
    
    @Override
    protected void onDestroy()
    {
      if( null != cursr )
        cursr.close();
      super.onDestroy();
    }


    /**
     * Get list of books from database and refresh UI.
     */
    private void refreshList()
    {
      if( null != cursr )
        cursr.close();
      cursr = A.a().dbApps.getAppsByType( 0 );
      adapter = new CursorAdapterApp( this, cursr );    
      v_list.setAdapter( adapter );
    }

}
