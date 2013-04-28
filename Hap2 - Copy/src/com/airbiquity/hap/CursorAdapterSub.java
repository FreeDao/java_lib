package com.airbiquity.hap;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.airbiquity.db.DbCars;

/**
 * CursorAdapter for Subscriptions.
 */
public class CursorAdapterSub extends CursorAdapter
{
    private final LayoutInflater mInflater;


    public CursorAdapterSub( Context context, Cursor c )
    {
        super( context, c );
        mInflater = LayoutInflater.from( context );
    }


    @Override
    public void bindView( View view, Context context, Cursor cursor )
    {
        MetaCarSub m = DbCars.cursorToMetaSubscr( cursor );
        TextView tv1 = (TextView) view.findViewById( R.id.line1 );
        TextView tv2 = (TextView) view.findViewById( R.id.line2 );
        TextView tv3 = (TextView) view.findViewById( R.id.line3 );
        tv1.setTypeface( A.a().fontBold );
        tv2.setTypeface( A.a().fontBold );
        tv3.setTypeface( A.a().fontNorm );
        
        int subType;
        switch( m.subType )
        {
            case MetaAppSub.TYPE_BASIC:     subType = R.string.basic;   break;
            case MetaAppSub.TYPE_PREMIUM:   subType = R.string.premium; break;
            default:                        subType = R.string.notActivated;
        }

        String expDate  =  null != m.expDate ? m.expDate : "";
        
        tv1.setText( m.nickname );
        tv2.setText( subType );
        tv3.setText( expDate );
    }


    @Override
    public View newView( Context context, Cursor cursor, ViewGroup parent )
    {
        final View view = mInflater.inflate( R.layout.list_item_sub, parent, false );
        return view;
    }
}
