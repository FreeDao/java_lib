package com.samnotepaddemo;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class NotesListActivity extends Activity {
	 private static final String TAG = "NotesListActivity";
	 private static final String[] PROJECTION = new String[] {
         NotePad.Notes._ID, // 0
         NotePad.Notes.COLUMN_NAME_TITLE, // 1
 };
	 
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_notes_list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_list, menu);
		return true;
	}

}
