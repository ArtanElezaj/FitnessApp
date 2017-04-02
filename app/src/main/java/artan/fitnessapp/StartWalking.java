package artan.fitnessapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Calendar;

import artan.fitnessapp.adapter.StatisticsAdapter;
import artan.fitnessapp.notification.NotificationBroadcaster;
import io.realm.Realm;
import io.realm.RealmResults;

// This class we used to welcome the User and to display his daily statistics

public class StartWalking extends AppCompatActivity {

    Button steps;
    TextView mTvWelcome;
    String usernameGetIntent;
    RecyclerView mRvSavedResuslts;
    Realm mRealmDetails;
    RealmResults<UserDetails> resultsCurrentUser;
    Switch mSwReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_walking);
        initBackgroundImage();

        steps = (Button) findViewById(R.id.ibSteps);
        mTvWelcome = (TextView) findViewById(R.id.tv_welcome);
        mRvSavedResuslts = (RecyclerView) findViewById(R.id.rv_saved_results);
        mSwReminder = (Switch) findViewById(R.id.sw_reminder);

        Bundle bundle = getIntent().getExtras();
        usernameGetIntent = (bundle.getString("UsernameEntered"));
        mTvWelcome.setText("Welcome " + usernameGetIntent + "!\nReady to get in shape?");

        mRealmDetails = Realm.getDefaultInstance();
        resultsCurrentUser = mRealmDetails.where(UserDetails.class)
                .equalTo("name", usernameGetIntent)
                .findAll();

        LinearLayoutManager manager = new LinearLayoutManager(this);
        mRvSavedResuslts.setLayoutManager(manager);
        mRvSavedResuslts.setAdapter(new StatisticsAdapter(this, resultsCurrentUser));

        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StartWalking.this, Steps.class);
                i.putExtra("UsernameFromIntent", usernameGetIntent);
                startActivity(i);
            }
        });

        mSwReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    mSwReminder.setText("Reminder On");
                    scheduleNotification();
                } else {
                    mSwReminder.setText("Reminder Off");
                    cancelNotification();
                }
            }
        });

        scheduleNotification();

    }

    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background2);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }

    // Assume user controls the periodic reminder: no reminder at office if user turn off notification
    private void scheduleNotification() {

        Notification notification = createNotification(getString(R.string.app_name), "Go for a walk today.");
        Intent notificationIntent = getIntent(notification);
        PendingIntent pendingIntent = getBroadcast(notificationIntent);

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 20);

        AlarmManager alarmManager = getSystemService();

        // Reminder every 1 hour
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), AlarmManager.INTERVAL_HOUR, pendingIntent);
    }

    private AlarmManager getSystemService() {
        return (AlarmManager) getSystemService(Context.ALARM_SERVICE);
    }

    private PendingIntent getBroadcast(Intent notificationIntent) {
        return PendingIntent.getBroadcast(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @NonNull
    private Intent getIntent(Notification notification) {
        Intent notificationIntent = new Intent(this, NotificationBroadcaster.class);
        notificationIntent.putExtra(NotificationBroadcaster.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationBroadcaster.NOTIFICATION_KEY, notification);
        return notificationIntent;
    }

    private Notification createNotification(String title, String message) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.run);
        builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        Intent resultIntent = new Intent(this, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        builder.setAutoCancel(true);
        return builder.build();
    }

    private void cancelNotification() {
        Intent notificationIntent = new Intent(this, NotificationBroadcaster.class);
        notificationIntent.putExtra(NotificationBroadcaster.NOTIFICATION_ID, 1);
        PendingIntent pendingIntent = getBroadcast(notificationIntent);
        AlarmManager alarmManager = getSystemService();
        alarmManager.cancel(pendingIntent);
    }

}
