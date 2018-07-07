package pos.com.pos.Activities.Sync;

import android.content.Context;
import android.os.AsyncTask;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class OfflineDatabaseSyncJob extends JobService {
    private AsyncTask mBackgroundSync;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundSync = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = OfflineDatabaseSyncJob.this;
                //EXECUTE SYNCINGTASK HERE PASSING IN THE ABOVE CONTEXT
                SyncingTask.executeTask(context);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundSync.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundSync != null) mBackgroundSync.cancel(true);
        return true  ;
    }
}
