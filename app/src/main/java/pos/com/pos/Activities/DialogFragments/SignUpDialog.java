package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import pos.com.pos.Activities.Activities.SetUp;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class SignUpDialog extends DialogFragment {

    FirebaseAuth auth;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         final View root = inflater.inflate(R.layout.sign_up,container , false);

         //INIT VIEWS
        ImageView next = root.findViewById(R.id.next);
        final EditText email = root.findViewById(R.id.editText2);
        final EditText password = root.findViewById(R.id.editText3);
        final EditText name = root.findViewById(R.id.editText);

        //INIT VARS
        auth = FirebaseAuth.getInstance();
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Login_prefs" , Context.MODE_PRIVATE);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                root.findViewById(R.id.login_status).setVisibility(View.VISIBLE);

                auth.createUserWithEmailAndPassword(email.getText().toString() , password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        //Set Business name to firebase
                        FirebaseDatabase.getInstance().getReference("Businesses").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Business_name").setValue(name.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {


                                //Set data to UserConfig
                                UserConfig userConfig = new UserConfig();
                                UserConfig.init(getActivity());
                                userConfig.setName(name.getText().toString());

                                //Start the set up
                                startActivity(new Intent(getActivity() , SetUp.class));

                                getActivity().finish();
                                //SAVE login state
                                sharedPreferences.edit().putInt("logged_in" ,1).apply();

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity() , "Failed because "+ e.getLocalizedMessage() ,Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity() , "Failed because "+ e.getLocalizedMessage() ,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
         return root;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

    }

    @Override
    public void onStart() {
        super.onStart();

        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);
    }

}
