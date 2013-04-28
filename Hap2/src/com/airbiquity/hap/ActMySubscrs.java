package com.airbiquity.hap;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.airbiquity.db.DbCars;

/**
 * My Vehicle Subscriptions.
 */
public class ActMySubscrs extends FragmentActivity
{
    //private static final String TAG = "ActMySubscrs";
    private CursorAdapterSub adapter;   // Adapter for subscriptions.
    private Cursor cursr;
    private boolean needRefresh;  // Whether we need to re-query the DB because the list has changed.
    
    private Button btnAddCar;
    private ListView list;
    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_my_subscrs );
        
        btnAddCar = (Button) findViewById( R.id.btnAddCar );
        btnAddCar.setTypeface( A.a().fontNorm );
        list = (ListView) findViewById( R.id.list );

        list.setOnItemClickListener( oicl );
        //list.setEmptyView( findViewById(R.id.em) );
        needRefresh = true;
    }
    
    private OnItemClickListener oicl = new OnItemClickListener()
    {
        @Override public void onItemClick( AdapterView<?> parent, View view, int position, long id )
        {
            showDetails( position );
        }        
    };
    
    private void showDetails( int position )
    {
        Cursor c = (Cursor) adapter.getItem( position );        
        int idSub = DbCars.cursorToSubId( c );
        
        Intent i = new Intent();
        i.setClass( this, ActSubInfo.class );
        i.putExtra( DbCars.KEY_SUB_ID, idSub );
        startActivity( i );                    
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
     * Re-fetch the list from DB.
     */
    private void refreshList()
    {
      if( null != cursr )
        cursr.close();
      cursr = A.a().dbSubs.getSubs();
      adapter = new CursorAdapterSub( this, cursr );    
      list.setAdapter( adapter );
    }
    
    /* Button touch listeners. They are called when the user touches button with the same name. */
    public void btnAddCar( View v ) 
    {
        Intent i = new Intent();
        i.setClass( this, ActIsOwner.class );
        startActivity( i );
    }
    
}
