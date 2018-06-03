package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import pos.com.pos.R;

public class SignInDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

         View root = inflater.inflate(R.layout.sign_in , container , false);

         //INITIATE VIEWS
        ImageView next = root.findViewById(R.id.next);
        final EditText username = root.findViewById(R.id.email), password = root.findViewById(R.id.password);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
