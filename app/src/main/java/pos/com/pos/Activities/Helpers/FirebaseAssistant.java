package pos.com.pos.Activities.Helpers;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;

import pos.com.pos.Activities.Activities.HolderActivity;
import pos.com.pos.Activities.Database.OrdersDatabase.OrderEntry;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;

import static android.content.ContentValues.TAG;

public class FirebaseAssistant {

    private static DatabaseReference database;
    private static Context c;

    //PUN INTENDE
    public static void initFire(Context context) {
        c = context;
        database = FirebaseDatabase.getInstance().getReference("Businesses");
    }

    public void setUpUserConfig(){

        final UserConfig userConfig = new UserConfig();

        ValueEventListener _ucl = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue()!=null) {
                    userConfig.setTableNumber(Integer.parseInt(dataSnapshot.getValue(String.class)));
                    Log.d(TAG, "onDataChange: " + " " + userConfig.getTableCount());
                }else {
                    syncTableCount(userConfig.getTableCount());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        database.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("no_of_tables")
                .addValueEventListener(_ucl);

        database.removeEventListener(_ucl);

        // AVARACA DABRA
        c.startActivity(new Intent(c , HolderActivity.class));

    }


    public void syncUserConfig(){

        UserConfig userConfig = new UserConfig();
        WeakReference<UserConfig> userConfigWeakReference = new WeakReference<UserConfig>(userConfig);

        database.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("no_of_tables")
                .setValue(userConfigWeakReference.get().getTableCount()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    public void syncOrders(){

        final OrderEntry[] orderEntries = OrdersDatabase.getInstance(c).ordersDao().getAllUnsyncedOrders();

        new AsyncTask<Void , Void , Void>(){
            @Override
            protected Void doInBackground(Void... voids) {

                if (orderEntries.length > 0){
                    for (int i = 0; i < orderEntries.length ; i++) {
                        final int finalI = i;

                        database.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("Orders")
                                .child(orderEntries[i].getDate_time())
                                .push().setValue(orderEntries[i]).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                orderEntries[finalI].setSynched(1);
                                OrdersDatabase.getInstance(c).ordersDao().updateOrder(orderEntries[finalI]);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Sync Failed :" , e.getMessage());
                            }
                        });
                    }
                    Log.d("Sync Status : " , "Complete");
                }else {
                    Log.d("Sync Status : " , "No Orders to Sync");

                }


                return null;
            }
        }.execute();


    }

    public void syncTableCount(int no){

        database.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("no_of_tables").setValue(String.valueOf(no))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }



}
