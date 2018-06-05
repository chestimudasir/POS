package pos.com.pos.Activities.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pos.com.pos.Activities.DialogFragments.SignInDialog;
import pos.com.pos.Activities.DialogFragments.SignUpDialog;
import pos.com.pos.R;

public class FirstActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.signin).setOnClickListener(this);

        findViewById(R.id.signUp).setOnClickListener(this);
        final SharedPreferences sharedPreferences = getSharedPreferences("Login_prefs" , Context.MODE_PRIVATE);

        //Check Login State
        if (sharedPreferences.getInt("logged_in" , 99) == 1) {
            startActivity(new Intent(FirstActivity.this, HolderActivity.class));

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
