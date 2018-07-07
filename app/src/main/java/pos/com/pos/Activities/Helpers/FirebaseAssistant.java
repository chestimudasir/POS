package pos.com.pos.Activities.Helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pos.com.pos.Activities.Database.OrdersDatabase.OrderEntry;
import pos.com.pos.Activities.Database.OrdersDatabase.OrdersDatabase;

public class FirebaseAssistant {

    private static DatabaseReference database;
    private static Context c;

    public static void initFire(Context context) {
        c = context;
        database = FirebaseDatabase.getInstance().getReference("Businesses");
    }

    public void syncUserConfig(){

    }
    public void syncOrders(){

        final OrderEntry[] orderEntries = OrdersDatabase.getInstance(c).ordersDao().getAllUnsyncedOrders();
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
    }
}
