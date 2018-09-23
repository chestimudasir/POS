package pos.com.pos.Activities.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import me.anwarshahriar.calligrapher.Calligrapher;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FirstActivity extends AppCompatActivity {

    int flag =0;
    SharedPreferences loginPrefs;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Product Sans Bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());



        //Init config
        UserConfig.init(getApplicationContext());

        init();
        mAuth = FirebaseAuth.getInstance();

        loginPrefs = getSharedPreferences("Login_prefs" , Context.MODE_PRIVATE);

        //Check Login State
        if (loginPrefs.getInt(getString(R.string.loginprefs) , 99) == 1) {
            startActivity(new Intent(FirstActivity.this, HolderActivity.class));

        }


    }


    void init(){


        View signIn = findViewById(R.id.signInBottomBar);
        View signUp = findViewById(R.id.signUpBottomBar);

        final BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(signIn);
        final BottomSheetBehavior<View> signUpBar = BottomSheetBehavior.from(signUp);

        final ImageView closer = findViewById(R.id.closer);
        final TextInputEditText[] email = {findViewById(R.id.email)};

        final TextInputEditText[] password = {findViewById(R.id.password)};

        findViewById(R.id.signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =0;
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        findViewById(R.id.signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag =1;
                signUpBar.setState(BottomSheetBehavior.STATE_EXPANDED);
                closer.setImageResource(R.drawable.chevron_down);

            }
        });

        closer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closer.setImageResource(R.drawable.chevron_up);
                signUpBar.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        findViewById(R.id.singInButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Start authentication signin/signup
                startAuth(email[0].getText().toString() , password[0].getText().toString());
            }
        });

        findViewById(R.id.singupButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email[0] = findViewById(R.id.emai_sign_up);
                password[0] = findViewById(R.id.password_signup);

                startAuth(email[0].getText().toString() , password[0].getText().toString());
            }
        });


    }

    //Set fonts for all the views. Use this method to set vviews

    void startAuth(final String email , String password){

        final TextInputEditText name = findViewById(R.id.name_vendor);


        switch (flag){

            case 0:

                mAuth.signInWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //If succesful put login state to 1
                            loginPrefs.edit().putInt(getString(R.string.loginprefs) , 1).apply();
                            startActivity(new Intent(FirstActivity.this ,HolderActivity.class));

                        } else {
                            Log.d("Error" , task.getException().getLocalizedMessage());
                        }

                    }
                });

                break;


            case 1:

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    //Push Data to Firebase , init node branching on the database side
                                    //Better would be to create an HashMap which holds data a weak referenced saving memory

                                    HashMap<String ,String> firebase_vendor = new HashMap<>();
                                    WeakReference<HashMap<String , String>> vendor_info_firebase = new WeakReference<HashMap<String, String>>(firebase_vendor);

                                    //Push the data onto Hashmap and then firebase
                                    //then Call a gc event to get rid of hashmap TODO need to find a better way to get rid of items

                                    vendor_info_firebase.get().put("Name" , name.getText().toString());
                                    vendor_info_firebase.get().put("email" , email);

                                    FirebaseDatabase.getInstance().getReference("Businesses")
                                            .child(FirebaseAuth.getInstance().getUid()).setValue(vendor_info_firebase.get());

                                    firebase_vendor =null;
                                    System.gc();

                                    //If succesful put login state to 1
                                    loginPrefs.edit().putInt(getString(R.string.loginprefs) , 1).apply();
                                    startActivity(new Intent(FirstActivity.this ,HolderActivity.class));

                                    //END

                                } else {
                                    Log.d("Error" , task.getException().getLocalizedMessage());
                                }

                            }
                        });
                break;

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
