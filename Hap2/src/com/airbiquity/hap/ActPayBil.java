package com.airbiquity.hap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * The "Payment & Billing" screen.
 */
public class ActPayBil extends FragmentActivity
{
    private Button btnChangeCreditCard;
    private Button btnPrevTransactions;

    
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_pay_bil );
        
        btnChangeCreditCard = (Button) findViewById( R.id.btnChangeCreditCard );
        btnPrevTransactions = (Button) findViewById( R.id.btnPrevTransactions );

        OnClickListener oclBtns = new OnClickListener(){public void onClick( View v ) {clicked(v);}};
        btnChangeCreditCard.setOnClickListener( oclBtns );
        btnPrevTransactions.setOnClickListener( oclBtns );
    }
    
    public void clicked( View v )
    {
        Intent i = new Intent();
        switch( v.getId() )
        {
            case R.id.btnChangeCreditCard:
                i.setClass( this, ActMyCards.class );
                break;
            case R.id.btnPrevTransactions:
                i.setClass( this, ActMsg.class );
                break;
            default:
                return;
        }
        startActivity( i );
    }
}
