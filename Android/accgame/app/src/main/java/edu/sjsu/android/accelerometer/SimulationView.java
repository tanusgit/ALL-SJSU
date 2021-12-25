package edu.sjsu.android.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

public class SimulationView extends View implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private Display mDisplay;

    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;

    private Particle mBall = new Particle();

    private Bitmap mField;
    private Bitmap mBasket;
    private Bitmap mBitMAP;
    private static final int BALL_SIZE = 64;
    private static final int BASKET_SIZE = 80;

    private float mXOrigin;
    private float mYOrigin;
    private float mHorizontalBound;
    private float mVerticalBound;
    public SimulationView(Context context)
    {
        super(context); // Initialize images from drawable
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBitMAP = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true);
        Bitmap basket = BitmapFactory.decodeResource(getResources(), R.drawable.basket);
        mBasket = Bitmap.createScaledBitmap(basket, BASKET_SIZE, BASKET_SIZE, true);
        BitmapFactory.Options opts = new BitmapFactory.Options(); //NEW CODE
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        mField = BitmapFactory.decodeResource(getResources(), R.drawable.field, opts);

        WindowManager mWindowManager = (WindowManager)
                context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    protected void onSizeChanged(int w, int h, int width, int height)
    {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;

        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;
    }

    public void startSimulation() {
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
    }

    public void stopSimulation() {
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Rect dest = new Rect(0, 0, getWidth(), getHeight());
        Paint paint = new Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(mField, null, dest, paint);


      //  need to position basket
        canvas.drawBitmap(mBasket, 490f, 540f, null);
        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        Bitmap m = RoundMe(mBitMAP);

        canvas.drawBitmap(m,
                (mXOrigin - BALL_SIZE / 2) + mBall.mPosX,
                (mYOrigin - BALL_SIZE / 2) - mBall.mPosY, null);

        invalidate();
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        if (mDisplay.getRotation() == Surface.ROTATION_0) {
            mSensorX = event.values[0];
            mSensorY = event.values[1];
        }

        else if (mDisplay.getRotation() == Surface.ROTATION_90) {
            mSensorX = -event.values[1];
            mSensorY = event.values[0];
        }
        else if (mDisplay.getRotation() == Surface.ROTATION_180) {
            mSensorX = -event.values[1];
            mSensorY = event.values[0];
        }
        else if (mDisplay.getRotation() == Surface.ROTATION_270) {
            mSensorX = -event.values[1];
            mSensorY = event.values[0];
        }


        mSensorZ = event.values[2];
        mSensorTimeStamp = event.timestamp;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public static Bitmap RoundMe(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = 65;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}