package com.rikulpatel.tetris;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	      setContentView(new GameView(this));
	}
}
