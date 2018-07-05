package pos.com.pos.Activities.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.widget.Toast;

public class NetworkBroadCastReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo networkInfo = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        final android.net.NetworkInfo info = connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (networkInfo.isAvailable() || info.isAvailable()){
            //Code for sync
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
    }
}
