package artan.fitnessapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;


// This class we used to measure the User's steps and distance

public class Steps extends Activity implements SensorEventListener {
    String mUserName = "";

    private TextView mTvShowSteps, mTvShowDistance;
    private SensorManager mSensorManager ;
    private Sensor mStepCounterSensor ;
    private Sensor mStepDetectorSensor ;

    private int stepsInSensor = 0;
    private int stepsAtReset;
    private int distanceInSensor = 0;
    private int distanceAtReset;

    Button mBtnReset, mBtnSave;
    static boolean activityRunning = false;

    Realm mRealmDetails;
    RealmResults<UserDetails> results;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stepping);

        mTvShowSteps = (TextView) findViewById(R.id.textview) ;
        mTvShowDistance = (TextView) findViewById(R.id.textView1) ;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE) ;
        mStepCounterSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) ;
        mStepDetectorSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR) ;
        mBtnReset = (Button)findViewById(R.id.btn_reset);
        mBtnSave = (Button)findViewById(R.id.btn_save);
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", MODE_PRIVATE);
        stepsAtReset = sharedPreferences.getInt("stepsAtReset", 0);
        distanceAtReset = sharedPreferences.getInt("distanceAtReset", 0);

        Bundle bundle = getIntent().getExtras();
        mUserName = (bundle.getString("UsernameFromIntent"));

        mRealmDetails = Realm.getDefaultInstance();
        results = mRealmDetails.where(UserDetails.class).equalTo("name", mUserName).findAll();

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });

        mBtnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset();
            }
        });
        initBackgroundImage();
    }


    private void initBackgroundImage(){
        ImageView background = (ImageView)findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if(activityRunning){
            stepsInSensor = Math.round(event.values[0]);
            distanceInSensor =  (int)(stepsInSensor * 2.5);
            int stepsSinceReset = stepsInSensor - stepsAtReset;
            int distanceSinceReset = distanceInSensor - distanceAtReset;
            mTvShowSteps.setText(String.valueOf(stepsSinceReset));
            mTvShowDistance.setText(String.valueOf(distanceSinceReset));

            if (distanceSinceReset != 0 && distanceSinceReset % 1000 == 0){
                Toast.makeText(this, "Congrats, you achieved "+distanceSinceReset+" feet! Keep it going...", Toast.LENGTH_LONG).show();
            }

        }else{
            event.values[0] = 0;
        }

    }
    @Override
    public void onStart() {
        super.onStart();
        activityRunning = true;
    }

    protected void onResume()
    {
        super.onResume();
        mSensorManager.registerListener(this, mStepCounterSensor, SensorManager.SENSOR_DELAY_FASTEST) ;
        mSensorManager.registerListener(this, mStepDetectorSensor, SensorManager.SENSOR_DELAY_FASTEST) ;
    }

    protected void onStop()
    {
        super.onStop();
        reset();
        activityRunning = false;
        mSensorManager.unregisterListener(this, mStepCounterSensor);
        mSensorManager.unregisterListener(this, mStepDetectorSensor);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void reset() {
        stepsAtReset = stepsInSensor;
        distanceAtReset = distanceInSensor;

        SharedPreferences.Editor editor =
                getSharedPreferences("MyData", MODE_PRIVATE).edit();
        editor.putInt("stepsAtReset", stepsAtReset);
        editor.putInt("distanceAtReset", distanceAtReset);
        editor.commit();

        mTvShowSteps.setText(String.valueOf(0));
        mTvShowDistance.setText(String.valueOf(0));
    }

    public void save() {

        int steps = Integer.parseInt( mTvShowSteps.getText().toString());
        int distance = Integer.parseInt(mTvShowDistance.getText().toString());
        long timeNow = System.currentTimeMillis();

        mRealmDetails = Realm.getDefaultInstance();
        UserDetails userDetails = new UserDetails(mUserName, steps, distance, timeNow);
        mRealmDetails.beginTransaction();
        mRealmDetails.copyToRealm(userDetails);
        mRealmDetails.commitTransaction();
        mRealmDetails.close();

        Toast.makeText(this, "Successfully saved!", Toast.LENGTH_SHORT).show();
    }

}