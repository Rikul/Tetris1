package com.rikulpatel.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder;
	
	public GameView(Context context) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		Canvas c = holder.lockCanvas(null);
        onDraw(c);
        holder.unlockCanvasAndPost(c);
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
}
