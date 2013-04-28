package com.airbiquity.hap;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.airbiquity.db.DbApps;

/**
 * CursorAdapter for App.
 */
public class CursorAdapterApp extends CursorAdapter
{
  private final LayoutInflater mInflater;


  public CursorAdapterApp( Context context, Cursor c )
  {
    super( context, c );
    mInflater = LayoutInflater.from( context );
  }


  @Override
  public void bindView( View view, Context context, Cursor cursor )
  {
    MetaApp m = DbApps.cursorToMetaApp( cursor );    
    TextView tv1 = (TextView) view.findViewById( R.id.line1 );
    TextView tv2 = (TextView) view.findViewById( R.id.line2 );
    ImageView icon = (ImageView) view.findViewById( R.id.icon );
    ToggleButton sw = (ToggleButton) view.findViewById( R.id.sw );

    tv1.setTypeface( A.a().fontBold );
    tv2.setTypeface( A.a().fontNorm );
    tv1.setText( m.name );
    tv2.setText( m.company );
    sw.setChecked( m.isOn );
    if( null != m.icon )
        icon.setImageBitmap( m.icon );
    //Log.e( "", "icon="+m.icon );
  }


  @Override
  public View newView( Context context, Cursor cursor, ViewGroup parent )
  {
    final View view = mInflater.inflate( R.layout.list_item_app, parent, false );
    return view;
  }

}
