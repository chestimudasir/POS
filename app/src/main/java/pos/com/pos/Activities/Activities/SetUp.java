package pos.com.pos.Activities.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import pos.com.pos.Activities.Helpers.UserConfig;
import pos.com.pos.R;

public class SetUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        final EditText password_admin = findViewById(R.id.admin_password);

        findViewById(R.id.next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText admin_pass = findViewById(R.id.admin_password),
                        num_tables = findViewById(R.id.num_tables);

                //init UserConfig
                UserConfig userConfig = new UserConfig();
                UserConfig.init(SetUp.this);

                //If Empty
                if (!admin_pass.getText().toString().equals("") || !num_tables.getText().toString().equals("")) {

                    userConfig.setTableNumber(Integer.parseInt(num_tables.getText().toString()));
                    userConfig.setAdminPassword(admin_pass.getText().toString());
                    userConfig.setSetUpStatus(1);

                }else {
                    userConfig.setSetUpStatus(0);
                }

                FirebaseDatabase.getInstance().getReference("Businesses").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("admin_password").setValue(password_admin.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        finish();
                        startActivity(new Intent(SetUp.this , HolderActivity.class));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });




            }
        });
    }


}
