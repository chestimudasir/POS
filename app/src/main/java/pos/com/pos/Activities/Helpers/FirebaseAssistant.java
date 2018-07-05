package pos.com.pos.Activities.Helpers;

import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAssistant {

    private static FirebaseDatabase database;

    static void initFire(Context context){
        database = FirebaseDatabase.getInstance();
    }


}
