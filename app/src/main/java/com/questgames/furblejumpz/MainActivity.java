package com.questgames.furblejumpz;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

	private RelativeLayout mFrame = null;
	
	private Bitmap mCloud, mFeet, mFurbleBody, mGround_a, mGround_b, mGround_c, mGround_float;
	
	private int mDisplayWidth, mDisplayHeight;
	
	private String TAG  = "Furble";
	private GestureDetector mGestureDetector;
	private GroundView ground, ground2, ground3,ground4;
	private GroundView[] land;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d(TAG,"Entered onCreate");
		
		View decorView = getWindow().getDecorView();
		// Hide the status bar.
		int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
		decorView.setSystemUiVisibility(uiOptions);
		// Hide action bar
		ActionBar actionBar = getActionBar();
		actionBar.hide();
		
		mFrame = (RelativeLayout) findViewById(R.id.frame);
		mCloud = BitmapFactory.decodeResource(getResources(), R.drawable.cloud);
		//mFeet = BitmapFactory.decodeResource(getResources(), R.drawable.feet);
		mFurbleBody = BitmapFactory.decodeResource(getResources(), R.drawable.furble);
		Log.d(TAG,"ok to here");
		mGround_a = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		Log.d(TAG,"ok to here2");
		mGround_b = BitmapFactory.decodeResource(getResources(), R.drawable.ground2);
		Log.d(TAG,"ok to here3");
		mGround_c = BitmapFactory.decodeResource(getResources(), R.drawable.ground_long);
		Log.d(TAG,"ok to here4");
		mGround_float = BitmapFactory.decodeResource(getResources(), R.drawable.ground_float);
		Log.d(TAG, "made it past"); 
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG,"Entered onResume");
		
	}
	public boolean onTouchEvent(MotionEvent event) {

		
		Log.d(TAG, "Touch event" );
		int count = mFrame.getChildCount();
		for(int i=0;i<count;i++)
		{
			
			
			if (mFrame.getChildAt(i) instanceof FurbleView)
			{
				FurbleView temp = (FurbleView) mFrame.getChildAt(i);
				if(!temp.mIsJump)
				{
					temp.setJump(true);
				}
				
			}
		}
			
		return false;
	
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		
		mFrame.removeAllViewsInLayout();
		//super.onStop();
	}
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
 
			// Get the size of the display so this view knows where borders are
			mDisplayWidth = mFrame.getWidth();
			mDisplayHeight = mFrame.getHeight();
			String size = "Size X:" + mDisplayWidth + " Y:" + mDisplayHeight;
			Log.d(TAG, size);
			this.createClouds();
			FurbleView body = new FurbleView(this.getApplicationContext(), (float)(mDisplayWidth/6) ,(float)(2*mDisplayHeight/3), false,mFurbleBody);
			body.start();
		//	FurbleView feet = new FurbleView(this.getApplicationContext(), (float)(mDisplayWidth/6)+30,(float)(2*mDisplayHeight/3) +75, true,mFeet);
		//	feet.start();
			ground = new GroundView(this.getApplicationContext(),100,0,mGround_c);
			ground2 = new GroundView(this.getApplicationContext(),mDisplayWidth+200,0,mGround_a);
			ground3 = new GroundView(this.getApplicationContext(),mDisplayWidth+530,0,mGround_b);
			ground4 = new GroundView(this.getApplicationContext(),mDisplayWidth+900 , mDisplayHeight-900,mGround_float);
			mFrame.addView(ground3);
			mFrame.addView(ground2);
			mFrame.addView(ground4);
			mFrame.addView(ground);
			
			land = new GroundView[] {ground,ground2,ground3,ground4};
		//	mFrame.addView(feet);
			mFrame.addView(body);
		}
	}
	private void createClouds()
	{
		Random r = new Random();
		for(int i=0;i<r.nextInt(9)+3;i++)
		{
			int randX = mDisplayWidth/(r.nextInt(3)+3);
			int randY = mDisplayHeight/(r.nextInt(5)+1);
			String msg = "cloud #" + i + " X:" + randX + " Y:" + randY;
			Log.d(TAG, msg);
			mFrame.addView(new CloudView(this.getApplicationContext(), randX, randY));
		}
	}
	
private class CloudView extends View{
		
		private final Paint mPainter = new Paint();
		private float mXPos, mYPos, mDx, mDy;
		private int mScaledBitmapHeight, mScaledBitmapWidth;
		private ScheduledFuture<?> mMoverFuture;
		private static final int REFRESH_RATE = 40;
		private Bitmap mScaledBitmap;
		
		public CloudView(Context context, float x, float y)
		{
			super(context);
			//Log.d(TAG,"CloudView created at Y:" + y);
			mPainter.setAntiAlias(true);
			Random r = new Random();
			int rSize = r.nextInt(3)+2;
			mScaledBitmapHeight = mCloud.getHeight()/rSize;
			mScaledBitmapWidth = mCloud.getWidth()/rSize;
			mScaledBitmap = createScaledBitmap(mCloud);
			mXPos = x;
			mYPos = y;
			int mDirection = r.nextInt(2);
			if(mDirection == 0)
			{
				mDirection -=1;
			}
			mDx = (r.nextInt(3)+3) * mDirection;
		}
		protected synchronized void onDraw(Canvas canvas) {
			//Log.d(TAG,"Entered onDraw");
			//  - save the canvas
			canvas.save();
			this.move();
			
			canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter); 
			//	mPainter.setColor(Color.GREEN);
			//	canvas.drawCircle(mXPos, mYPos, mScaledBitmapWidth, mPainter);
			//	mPainter.setColor(Color.BLUE);
			//	canvas.drawCircle(mXPos, mYPos, mScaledBitmapWidth/2, mPainter);
			
			
			//  - restore the canvas
			canvas.restore();
			CloudView.this.postInvalidate();
			
		}
		private void start() {

			// Creates a WorkerThread
			ScheduledExecutorService executor = Executors
					.newScheduledThreadPool(1);

			// Execute the run() in Worker Thread every REFRESH_RATE
			// milliseconds
			// Save reference to this job in mMoverFuture
			mMoverFuture = executor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					//  - implement movement logic.
					Log.d(TAG,"Entered run");
					
					CloudView.this.postInvalidate();
					
				}
			}, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
		}
		private void move()
		{
			mXPos += mDx;
			if(mXPos > mDisplayWidth + mScaledBitmapWidth)
			{
				mXPos = 0 - mScaledBitmapWidth;
			}
			else if(mXPos < 0 - mScaledBitmapWidth)
			{
				mXPos = mDisplayWidth + mScaledBitmapWidth;
			}
			
		}
		private Bitmap createScaledBitmap(Bitmap myBitmap) {
			//  - create the scaled bitmap using size set above
			return Bitmap.createScaledBitmap(myBitmap,mScaledBitmapWidth,mScaledBitmapHeight,true);
		}
	}
	
private class FurbleView extends View{
		
		private final Paint mPainter = new Paint();
		private float mXPos, mYPos, mDx, mDy, mRot, mDr;
		private int mScaledBitmapHeight, mScaledBitmapWidth, mJumpCount, mNumJump;
		public ScheduledFuture<?> mMoverFurble;
		private static final int REFRESH_RATE = 40;
		private Bitmap mScaledBitmap;
		private boolean mRotate;
		private Bitmap mFurble;
		private boolean mIsJump;
		
		public FurbleView(Context context, float x, float y, boolean rotate,Bitmap image)
		{
			super(context);
			String logT = "FurbleView created at X:" + x +" Y:" + y;
			Log.d(TAG,logT);
			mPainter.setAntiAlias(true);
			mFurble = image;
			//mScaledBitmapHeight = mFurble.getHeight()/10;
			//mScaledBitmapWidth = mFurble.getWidth()/10;
            mScaledBitmapHeight = mDisplayWidth/20;
            mScaledBitmapWidth = mDisplayWidth/20;
			if(mDisplayWidth > 790)
			{
				mScaledBitmapHeight *=2;
				mScaledBitmapWidth *=2;
			}
			
			mScaledBitmap = createScaledBitmap(mFurble);
			mXPos = x;
			mYPos = y;
			mRotate = rotate;
			mRot = 0;
			mDr = 15;
			mDx = 0;
			mIsJump = false;
			mNumJump = 0;
			
		}
		protected synchronized void onDraw(Canvas canvas) {
			//Log.d(TAG,"Entered onDraw");
			//  - save the canvas
			canvas.save();
			move();
			
			if(mRotate)
			{
				mRot += mDr;
				canvas.rotate(mRot, mXPos+(mScaledBitmapWidth/2), mYPos+(mScaledBitmapWidth/2));
			}
			canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter); 
			
			//mPainter.setColor(Color.GREEN);
			//canvas.drawCircle(500, 500, 200, mPainter);
			
			//  - restore the canvas
			canvas.restore();
			FurbleView.this.postInvalidate();
			
		}
		private void start() {

			// Creates a WorkerThread
			ScheduledExecutorService executor = Executors
					.newScheduledThreadPool(1);

			// Execute the run() in Worker Thread every REFRESH_RATE
			// milliseconds
			// Save reference to this job in mMoverFuture
			mMoverFurble = executor.scheduleWithFixedDelay(new Runnable() {
				@Override
				public void run() {
					//  - implement movement logic.
					//Log.d(TAG,"Entered run");
					move();
					FurbleView.this.postInvalidate();
					
					
				}
				
			}, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
		}
		private void move()
		{
			mYPos += mDy;
			mXPos += mDx;
			this.jump();
			if(mYPos > mDisplayHeight)
			{
				Intent i = new Intent(getApplicationContext(), GameOver.class);
				startActivity(i);
				onPause();
				finish();
			}
						
		}
		private void jump()
		{
			for(int i=0;i<land.length; i++)
			{
				if(land[i].onGround(mXPos,mYPos,mScaledBitmapWidth,mScaledBitmapHeight,mIsJump))
					{
						mDy=0;
						mYPos = land[i].getYPos() - mScaledBitmapHeight;
						mNumJump=0;
						
						return;
					}
			}	
			
			if (mJumpCount == 0 && mIsJump && mNumJump<=2)
			{
				mDy = -45;
				mJumpCount++;
				
			}
			else if (mJumpCount <=25 && mIsJump)
			{
				mJumpCount++;
				mDy *= 0.9;
				
			}
			else if(mDy <= 0 )
			{
				mJumpCount = 0;
				mIsJump = false;
				mDy = 3;
				
			}
			else if(mDy>0)
			{
				mDy *= 1.2;
				if(mDy > 20)
				{
					mDy = 20;
				}
				if(mYPos > mDisplayHeight -5)
				{
					mDy = 0;
				}
			}
			
			
		}
		private Bitmap createScaledBitmap(Bitmap myBitmap) {
			//  - create the scaled bitmap using size set above
			return Bitmap.createScaledBitmap(myBitmap,mScaledBitmapWidth,mScaledBitmapHeight,true);
		}
		public void setJump(boolean j)
		{
			
				mIsJump = j;
				mNumJump++;
			
			
		}
	}
	



private class GroundView extends View{
	
	private final Paint mPainter = new Paint();
	private float mXPos, mYPos, mDx, scaleFactor;
	private int mScaledBitmapHeight, mScaledBitmapWidth;
	public ScheduledFuture<?> mMoverGround;
	private static final int REFRESH_RATE = 40;
	private Bitmap mScaledBitmap;

	
	private Bitmap mGround;
	
	public GroundView(Context context, float x, float y,Bitmap image)
	{
		super(context);
		String logT = "GroundView created at X:" + x +" Y:" + y;
		Log.d(TAG,logT);
		mPainter.setAntiAlias(true);
		mGround = image;
		mScaledBitmapHeight = mGround.getHeight()/5;
		mScaledBitmapWidth = mGround.getWidth()/5;
        /*scaleFactor = 593/(float)(mDisplayWidth/2);
        Log.d(TAG, "Scale factor: " + scaleFactor);
        mScaledBitmapWidth = (int)(mGround.getWidth() * scaleFactor);
        Log.d(TAG, ""+mScaledBitmapWidth);
        mScaledBitmapHeight = (int)(mGround.getHeight() * scaleFactor);
        Log.d(TAG, ""+mScaledBitmapHeight);
		/*if(mDisplayWidth > 790)
		{
			mScaledBitmapHeight *=2;
			mScaledBitmapWidth *=2;
		} */
		
		mScaledBitmap = createScaledBitmap(mGround);
		mXPos = x;
		if(y==0)
		{
			mYPos = mDisplayHeight - mScaledBitmapHeight/2;
		}
		else
		{
			mYPos = y;
		}
		
		mDx = -10;
		
		
	}
	protected synchronized void onDraw(Canvas canvas) {
		//Log.d(TAG,"Entered onDraw");
		//  - save the canvas
		canvas.save();
		move();
		
		
		canvas.drawBitmap(mScaledBitmap, mXPos, mYPos, mPainter); 
		
		
		
		//  - restore the canvas
		canvas.restore();
		GroundView.this.postInvalidate();
		
	}
	private void start() {

		// Creates a WorkerThread
		ScheduledExecutorService executor = Executors
				.newScheduledThreadPool(1);

		// Execute the run() in Worker Thread every REFRESH_RATE
		// milliseconds
		// Save reference to this job in mMoverFuture
		mMoverGround = executor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				//  - implement movement logic.
				//Log.d(TAG,"Entered run");
				move();
				GroundView.this.postInvalidate();
				
				
			}
			
		}, 0, REFRESH_RATE, TimeUnit.MILLISECONDS);
	}
	private void move()
	{
		mXPos += mDx;
		if(mXPos < 0-mScaledBitmapWidth)
		{
			Random r = new Random();
			int pos = r.nextInt(2)+1;
			mXPos = mDisplayWidth+(mScaledBitmapWidth);
		}
	}
	
	private Bitmap createScaledBitmap(Bitmap myBitmap) {
		//  - create the scaled bitmap using size set above
		return Bitmap.createScaledBitmap(myBitmap,mScaledBitmapWidth,mScaledBitmapHeight,true);
	}
	public Boolean onGround(float x, float y, int w,int h, boolean isJump)
	{
		//String temp = "onGround, X=" +x+" Y=" +y;
		//Log.d(TAG, temp);
		if(x>=(mXPos-w) && x<mXPos+(mScaledBitmapWidth-(w/2)))
		{
			//Log.d(TAG, "Within X range");
			if(y < (mYPos+15) && y > (mYPos -(h+2)))
			{	
				//Log.d(TAG, "Within Y range");
				if(!isJump)
				{
					return true;
				}
				
			}
		}
		return false;
		
		
	}
	
	public float getYPos()
	{
		return mYPos;
	}
	
	
}

}



















