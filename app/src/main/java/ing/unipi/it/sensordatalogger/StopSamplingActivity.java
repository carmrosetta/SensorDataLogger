package ing.unipi.it.sensordatalogger;

/**
 * Created by carmen on 14/09/14.
 */
import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;



public class StopSamplingActivity extends Activity {
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_sampling);
    }

    public void stopSampling(View v) {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();

        stopService(new Intent(getApplicationContext(), SensorsSamplingService.class));

        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
