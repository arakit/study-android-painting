package com.example.paintingapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {

	TouchPaintView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new TouchPaintView(this, null);
        setContentView(view);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    //メニューがクリックされた時の処理
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case R.id.action_save:
			//保存ボタンを押された時
			view.saveToFile();
			return true;
		}


		return super.onOptionsItemSelected(item);
	}




}
