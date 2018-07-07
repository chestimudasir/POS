package pos.com.pos.Activities.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

import pos.com.pos.Activities.Sync.SyncUtilities;

public class NetworkBroadCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo mobileNetwork = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        final android.net.NetworkInfo wifi = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        if (mobileNetwork.isAvailable() || wifi.isAvailable()){
            //Code for sync
            Toast.makeText(context, "Network Available", Toast.LENGTH_SHORT).show();

        }
    }
}
