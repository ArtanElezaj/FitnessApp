package artan.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import io.realm.Realm;
import io.realm.RealmResults;

//This is the launcher class, we used this for Login and Sign Up

public class MainActivity extends AppCompatActivity {

    EditText mEtUsername, mEtPassword;
    Button mBtnLogin, mBtnSignUp;
    TextView mTvMsg;
    Realm mRealmUser;
    RealmResults<User> resultsAllUsers;
    RealmResults<User> resultsOneUser;
    RealmResults<User> resultsTwoUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEtUsername = (EditText) findViewById(R.id.et_username);
        mEtPassword = (EditText) findViewById(R.id.et_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mTvMsg = (TextView) findViewById(R.id.tv_msg);

        mRealmUser = Realm.getDefaultInstance();
        resultsAllUsers = mRealmUser.where(User.class).findAllAsync();

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mRealmUser = Realm.getDefaultInstance();
                resultsTwoUser = mRealmUser.where(User.class)
                        .equalTo("username", mEtUsername.getText().toString())
                        .equalTo("password", mEtPassword.getText().toString())
                        .findAll();
                int exist = resultsTwoUser.size();

                if (exist == 1) {
                    Intent intent = new Intent(MainActivity.this, StartWalking.class);
                    intent.putExtra("UsernameEntered", mEtUsername.getText().toString());
                    startActivity(intent);
                } else {
                    mTvMsg.setText("Unable to login: wrong username or password!");
                }
            }
        });

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealmUser = Realm.getDefaultInstance();
                resultsOneUser = mRealmUser.where(User.class)
                        .equalTo("username", mEtUsername.getText().toString())
                        .findAll();
                int alreadyExist = resultsOneUser.size();

                if (alreadyExist == 1) {
                    mTvMsg.setText("Username already exist! Please choose different username.");
                } else {
                    mRealmUser = Realm.getDefaultInstance();
                    User user = new User(mEtUsername.getText().toString(), mEtPassword.getText().toString());
                    mRealmUser.beginTransaction();
                    mRealmUser.copyToRealm(user);
                    mRealmUser.commitTransaction();
                    mRealmUser.close();

                    mTvMsg.setText("User successfully signed up!");

                }

            }
        });
        initBackgroundImage();
    }

    //this is the background image inserted programmatically.We did in this way because the image was to big and it shows an error if we do in xml.
    private void initBackgroundImage() {
        ImageView background = (ImageView) findViewById(R.id.iv_background);
        Glide.with(this)
                .load(R.drawable.background)
                .centerCrop()
                .into(background);
    }


}