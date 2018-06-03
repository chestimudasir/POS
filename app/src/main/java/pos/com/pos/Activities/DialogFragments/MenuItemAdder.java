package pos.com.pos.Activities.DialogFragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import pos.com.pos.Activities.Models.MenuModel;
import pos.com.pos.R;

public class MenuItemAdder extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.menu_adder_dialog, container, false);

        final EditText item_name = root.findViewById(R.id.item_name),
                item_price = root.findViewById(R.id.item_price);

        root.findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (!item_name.getText().toString().equals("") && !item_price.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference(getString(R.string.business_parent_node))
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(getString(R.string.Menu_node))
                            .child(item_name.getText().toString())
                            .setValue(new MenuModel(item_name.getText().toString(), Integer.valueOf(item_price.getText().toString()))).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //CONFORMATION MESSAGE
                            Snackbar.make(v, "Menu Addition Sucessful", Snackbar.LENGTH_SHORT).show();
                            dismiss();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
