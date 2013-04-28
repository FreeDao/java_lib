package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * My Credit Cards. (Change/Add Credit Card)
 */
public class ActMyCards extends FragmentActivity
{
    private Button btnAddCard;

    // TODO: use_choreo to get the list of cars.    
    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_my_cards );
        
        btnAddCard = (Button) findViewById( R.id.btnAddCard );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnAddCard.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnAddCard:
                i.setClass( this, ActEditCard.class );
                break;
            default:
                return;
        }
        startActivity( i );
    }
}
