package pos.com.pos.Activities.Sync;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;

public class SyncUtilities {
    private static final String SYNC_JOB_TAG = "offlinedb_sync";
    private static boolean sInitialized;

    synchronized public static void scheduleSync(@NonNull final Context context){
        if (sInitialized) return;
        Driver driver = new GooglePlayDriver(context);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);
        Job constraintSyncJob = dispatcher.newJobBuilder()
                .setService(OfflineDatabaseSyncJob.class)
                .setLifetime(Lifetime.FOREVER)
                .setTag(SYNC_JOB_TAG)
                .setRecurring(false)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(false)
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .build();
        dispatcher.schedule(constraintSyncJob);
        sInitialized = true;
        //CHANGE OR ALTER THE CONSTRAINTS LATER
    }
}
