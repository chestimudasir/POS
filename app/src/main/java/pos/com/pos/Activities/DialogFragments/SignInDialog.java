package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
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
                                        //Update UI
                                    } else {
                                        //DO SOMETHING
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
