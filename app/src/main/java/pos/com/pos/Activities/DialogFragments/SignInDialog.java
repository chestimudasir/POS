package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
import android.content.Context;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pos.com.pos.Activities.Activities.HolderActivity;
import pos.com.pos.Activities.Helpers.FirebaseAssistant;
import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class SignInDialog extends DialogFragment {
    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View root = inflater.inflate(R.layout.sign_in , container , false);

         //INITIATE VIEWS
        ImageView next = root.findViewById(R.id.next);
        final EditText username = root.findViewById(R.id.email),
                passwordTextView = root.findViewById(R.id.password);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("login_prefs" , Context.MODE_PRIVATE);

        auth = FirebaseAuth.getInstance();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = username.getText().toString();
                String password = passwordTextView.getText().toString();
                if (!email.isEmpty() || password.length() > 6) {
                    auth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        FirebaseAssistant.initFire(getActivity());

                                        sharedPreferences.edit().putInt("logged_in" ,1).apply();


                                        //Check if tables have been synced or not
                                        if(new UserConfig().getTableCount() == 0) {
                                            //Start Syncing User Configs and move on
                                            //Also init Fire for layer use
                                            FirebaseAssistant assistant = new FirebaseAssistant();
                                            assistant.setUpUserConfig();
                                        }else{
                                            startActivity(new Intent(getActivity(), HolderActivity.class));
                                        }
                                    }
                                }
                            });
                }
            }
        });
         return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setWindowAnimations(
                R.style.dialog_animation_fade);
    }
}
