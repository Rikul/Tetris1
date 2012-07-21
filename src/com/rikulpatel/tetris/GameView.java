package com.rikulpatel.tetris;

import java.util.logging.Level;
import java.util.logging.Logger;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private SurfaceHolder holder;
	private PaintThread thread;
	private TetrisGame myGame = null;
	private Context mContext;
	
	public GameView(Context context) {
		super(context);
		
		holder = getHolder();
		holder.addCallback(this);
		
		myGame = new TetrisGame();
		thread = new PaintThread(holder, context, new Handler(), myGame);
		mContext = context;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {	
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if(thread.state==PaintThread.PAUSED){
            //When game is opened again in the Android OS
            thread = new PaintThread(getHolder(), mContext, new Handler(), myGame);
            thread.start();
        }else{
            //creating the game Thread for the first time
            thread.start();
        }
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		boolean retry = true;
        //code to end gameloop
        thread.state=PaintThread.PAUSED;
        while (retry) {
            try {
                //code to kill Thread
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
	}
	
	private class PaintThread extends Thread {
		SurfaceHolder mSurfaceHolder = null;
		TetrisGame game; 
		
		//state of game (Running or Paused).
	    int state = 1;
	    public final static int RUNNING = 1;
	    public final static int PAUSED = 2;
	    
	    //for consistent rendering
	    private long sleepTime;
	    
	    //amount of time to sleep for (in milliseconds)
	    private long delay=70;
	    
		public PaintThread(SurfaceHolder surfaceHolder, Context context, Handler handler, TetrisGame tGame) {
			mSurfaceHolder = surfaceHolder;
			game = tGame;
		}
		
		@Override
		public void run() {
			 while (state==RUNNING) {
		           //time before update
		           long beforeTime = System.nanoTime();
		           
				Canvas c = null;
				try {
					 //lock canvas so nothing else can use it
			           c = mSurfaceHolder.lockCanvas(null);
			           synchronized (mSurfaceHolder) {		                		               
			        	   game.draw(c);		        	   
			           }
			     } 
				
				catch (Exception e) {
					Log.d("PaintThread.run", "exception");
			     }
				
				finally {
			         // do this in a finally so that if an exception is thrown
			         // during the above, we don't leave the Surface in an
			         // inconsistent state
			         if (c != null) {
			             mSurfaceHolder.unlockCanvasAndPost(c);
			         }
			     }			
						
				//SLEEP
			     //Sleep time. Time required to sleep to keep game consistent
			     //This starts with the specified delay time (in milliseconds) then subtracts from that the
			     //actual time it took to update and render the game. This allows our game to render smoothly.
			     this.sleepTime = delay-((System.nanoTime()-beforeTime)/1000000L);
	
	            try {
	                //actual sleep code
	                if(sleepTime>0){
	                this.sleep(sleepTime);
	                }
	            } catch (InterruptedException ex) {
	                Logger.getLogger(PaintThread.class.getName()).log(Level.SEVERE, null, ex);
	            }
	        
			}
			
		}
	}
	
	private class TetrisGame {
		private int dy ;
		
		public TetrisGame() {
			dy=0;
		}
		
		public void draw(Canvas canvas) {
			
			Paint paint = new Paint();
			
			paint.setColor(Color.WHITE);
			paint.setStyle(Style.STROKE);
			
			canvas.drawColor(Color.BLACK);
					
			Path p = new Path();		
			p.lineTo(50, 0);
			p.lineTo(50, 150);
			p.lineTo(0, 150);
			p.lineTo(0, 0);
			p.moveTo(0, 50);
			p.lineTo(50, 50);
			p.moveTo(0, 100);
			p.lineTo(50, 100);
			
			/*
			canvas.drawPath(p, paint);
			canvas.save();
			canvas.translate(200, 200);
			canvas.drawPath(p, paint);
			canvas.restore();
			
			canvas.save();
			canvas.scale(0.5f, 0.5f);
			canvas.drawPath(p, paint);
			canvas.restore();
			*/
			
			canvas.save();					
			if (dy > canvas.getHeight())
				dy = 0;
			else dy += 10;
			
			
			canvas.translate(300, dy);
			canvas.rotate(90);
			
			paint.setColor(Color.RED);
			paint.setStyle(Style.FILL);
			canvas.drawPath(p, paint);
			paint.setColor(Color.WHITE);
			paint.setStyle(Style.STROKE);
			canvas.drawPath(p, paint);		
			canvas.restore();
						
		}
	}
}
