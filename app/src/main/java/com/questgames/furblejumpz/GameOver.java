package com.questgames.furblejumpz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class GameOver extends Activity{
	
	//private RelativeLayout mFrame = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_over);
	//	mFrame = (RelativeLayout) findViewById(R.id.frame);
		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(buttonClicker);
		
	}
	
	
	View.OnClickListener buttonClicker = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent i = new Intent(GameOver.this,MainActivity.class);
			startActivity(i);
			finish();
		}
	};
	
	
}
