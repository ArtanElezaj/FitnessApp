package artan.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import artan.fitnessapp.adapter.StatisticsAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_walking);

        steps=(Button)findViewById(R.id.ibSteps);
        mTvWelcome = (TextView)findViewById(R.id.tv_welcome);
        mRvSavedResuslts = (RecyclerView)findViewById(R.id.rv_saved_results);

        Bundle bundle = getIntent().getExtras();
        usernameGetIntent = (bundle.getString("UsernameEntered"));
        mTvWelcome.setText("Welcome " + usernameGetIntent+"!\nReady to get in shape?");

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
                Intent i=new Intent(StartWalking.this,Steps.class);
                i.putExtra("UsernameFromIntent", usernameGetIntent);
                startActivity(i);
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

}
