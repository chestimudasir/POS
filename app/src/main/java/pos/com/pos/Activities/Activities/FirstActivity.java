package pos.com.pos.Activities.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import pos.com.pos.Activities.DialogFragments.SignInDialog;
import pos.com.pos.Activities.DialogFragments.SignUpDialog;
import pos.com.pos.Activities.Helpers.FontsOverride;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserConfig.init(getApplicationContext());

        //Set system wide font to product sans
        FontsOverride.setDefaultFont(this, "DEFAULT", "fonts/Product Sans Regular.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Product Sans Regular.ttf");
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Product Sans Regular.ttf");
        FontsOverride.setDefaultFont(this, "SANS_SERIF", "fonts/Product Sans Regular.ttf");

        // handle sign in and sign up
        findViewById(R.id.signin).setOnClickListener(this);
        findViewById(R.id.signUp).setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("login_prefs" , Context.MODE_PRIVATE);

        //Check Login State
        if (sharedPreferences != null &&  sharedPreferences.getInt("logged_in" , 99) == 1) {
            Toast.makeText(getApplicationContext() , sharedPreferences.getInt("logged_in",99)+"" , Toast.LENGTH_LONG).show();
            startActivity(new Intent(FirstActivity.this, SetUp.class));
            finish();

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signin:
                new SignInDialog().show(getFragmentManager(),"");
                break;
            case R.id.signUp:
                new SignUpDialog().show(getFragmentManager() ,"signUp");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
