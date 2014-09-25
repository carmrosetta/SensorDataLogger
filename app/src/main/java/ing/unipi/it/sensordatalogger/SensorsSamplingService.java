package ing.unipi.it.sensordatalogger;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;


public class SensorsSamplingService extends Service implements SensorEventListener {

    public static boolean serviceRunning = false;

    public static final int SCREEN_OFF_RECEIVER_DELAY = 500;

    SavingSamplesTimer timer;

    boolean timerStarted = false;

    final long interval = 60000;//1 minute


    public static final String TAG = SensorsSamplingService.class.getName();

    private PowerManager.WakeLock mWakeLock = null;

    List<AccelerationSample> accelerationSamples;
    List<AngularSpeedSample> angularSpeeds;
    List<PressureSample> pressures;


    long lastAccelerometerUpdate = 0;
    long lastGyroscopeUpdate = 0;
    long lastBarometerUpdate = 0;

    long countAcc = 0;
    long countGyr = 0;
    long countBar = 0;

    private NotificationManager notificationManager;

    User user;
    String sensorPosition;
    int sensorSamplingRate;
    String samplingRate;

    String headerData;

    SensorManager sensorManager;

    List<Sensor> sensorList;

    File accelerationFile = null;
    File accelerationDirectory = null;

    File angularSpeedFile = null;
    File angularSpeedDirectory = null;

    File pressureFile = null;
    File pressureDirectory = null;


    public SensorsSamplingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        serviceRunning = true ;

       /* timer = new SavingSamplesTimer(interval, 1000);

        super.onStartCommand(intent, flags, startId);
        if (!timerStarted) {
            timer.start();
            timerStarted = true;
        } else {
            timer.cancel();
            timerStarted = false;
        }
        */

        Bundle extras = intent.getExtras();
        user = (User) extras.get("User data");
        samplingRate = (String) extras.get("Sensor sampling rate");
        sensorSamplingRate = Integer.parseInt(samplingRate);
        sensorPosition = (String) extras.get("Sensor position");
        String androidSamplingRate = "";

        switch (sensorSamplingRate) {
            case SensorManager.SENSOR_DELAY_NORMAL:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_NORMAL";
                break;
            case SensorManager.SENSOR_DELAY_UI:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_NORMAL";
                break;
            case SensorManager.SENSOR_DELAY_GAME:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_GAME";
                break;
            case SensorManager.SENSOR_DELAY_FASTEST:
                androidSamplingRate = "SensorManager.SENSOR_DELAY_FASTEST";
                break;

        }

        headerData = user.toString() + "\n% Sensor position: " + sensorPosition + "\n% Notes:\n";


        long todayDate = System.currentTimeMillis();
        String startDate = Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd");
        String startTime = Utilities.getDateTimeFromMillis(todayDate, "kk-mm-ss");


        String sensorType;
        float sensorMaxRange;
        String unit;
        SensorTrackHeader sth = null;

        String device = Utilities.getDeviceName();
        String androidVersion = Utilities.getAndroidVersion();

        for (Sensor sensor : sensorList) {
            switch (sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:

                    List<Attribute> relationAccelerometer = new ArrayList<Attribute>();
                    Attribute timeAcc = new Attribute("time", "s", "numeric");
                    Attribute acceleration_x = new Attribute("acceleration_x", "m/s^2", "numeric");
                    Attribute acceleration_y = new Attribute("acceleration_y", "m/s^2", "numeric");
                    Attribute acceleration_z = new Attribute("acceleration_z", "m/s^2", "numeric");

                    relationAccelerometer.add(timeAcc);
                    relationAccelerometer.add(acceleration_x);
                    relationAccelerometer.add(acceleration_y);
                    relationAccelerometer.add(acceleration_z);

                    RelationHeader rhA = new RelationHeader("Linear_Acceleration", relationAccelerometer);

                    sensorType = "Accelerometer";
                    sensorMaxRange = sensor.getMaximumRange();
                    unit = "m/s^2";

                    sth = new SensorTrackHeader(sensorType, startDate, startTime, device, androidVersion, sensorMaxRange, unit, androidSamplingRate);

                    accelerationDirectory = Utilities.createDirectory("Samples/Accelerometer/"+Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd"));
                    accelerationFile = Utilities.createFile(accelerationDirectory, Utilities.getDateTimeFromMillis(todayDate, "kk-mm")+".arff");


                    if (Utilities.getFileSize(accelerationFile) == 0) {

                        Utilities.writeData(accelerationFile, sth.toString());
                        Utilities.writeData(accelerationFile, headerData);
                        Utilities.writeData(accelerationFile, rhA.toString());
                    }
                    break;




                case Sensor.TYPE_GYROSCOPE:

                    List<Attribute> relationGyroscope = new ArrayList<Attribute>();
                    Attribute timeGyr = new Attribute("time", "s", "numeric");
                    Attribute angularSpeed_x = new Attribute("angular_speed_x", "rad/s", "numeric");
                    Attribute angularSpeed_y = new Attribute("angular_speed_y", "rad/s", "numeric");
                    Attribute angularSpeed_z = new Attribute("angular_speed_z", "rad/s", "numeric");

                    relationGyroscope.add(timeGyr);
                    relationGyroscope.add(angularSpeed_x);
                    relationGyroscope.add(angularSpeed_y);
                    relationGyroscope.add(angularSpeed_z);

                    RelationHeader rhAS = new RelationHeader("Angular_Speed", relationGyroscope);

                    sensorType = "Gyroscope";
                    sensorMaxRange = sensor.getMaximumRange();
                    unit = "rad/s";
                    sth = new SensorTrackHeader(sensorType, startDate, startTime, device, androidVersion, sensorMaxRange, unit, androidSamplingRate);

                    angularSpeedDirectory = Utilities.createDirectory("Samples/Gyroscope/"+Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd"));
                    angularSpeedFile = Utilities.createFile(angularSpeedDirectory, Utilities.getDateTimeFromMillis(todayDate, "kk-mm")+".arff");

                    if (Utilities.getFileSize(angularSpeedFile) == 0) {

                        Utilities.writeData(angularSpeedFile, sth.toString());
                        Utilities.writeData(angularSpeedFile, headerData);
                        Utilities.writeData(angularSpeedFile, rhAS.toString());
                    }

                    break;



                case Sensor.TYPE_PRESSURE:

                    List<Attribute> relationBarometer = new ArrayList<Attribute>();
                    Attribute timeBar = new Attribute("time", "s", "numeric");
                    Attribute pressure = new Attribute("pressure", "hPa", "numeric");

                    relationBarometer.add(timeBar);
                    relationBarometer.add(pressure);


                    RelationHeader rhB = new RelationHeader("Pressure", relationBarometer);

                    sensorType = "Barometer";
                    sensorMaxRange = sensor.getMaximumRange();
                    unit = "hPa";

                    sth = new SensorTrackHeader(sensorType,startDate, startTime, device, androidVersion, sensorMaxRange, unit, androidSamplingRate);

                    pressureDirectory = Utilities.createDirectory("Samples/Barometer/"+Utilities.getDateTimeFromMillis(todayDate, "yy-MM-dd"));
                    pressureFile = Utilities.createFile(pressureDirectory, Utilities.getDateTimeFromMillis(todayDate, "kk-mm")+".arff");


                    if (Utilities.getFileSize(pressureFile) == 0) {

                        Utilities.writeData(pressureFile, sth.toString());
                        Utilities.writeData(pressureFile, headerData);
                        Utilities.writeData(pressureFile, rhB.toString());
                    }
                    break;
            }

        }

        registerListeners();

        mWakeLock.acquire();

        return START_REDELIVER_INTENT;
    }


    private void registerListeners() {

        accelerationSamples = new ArrayList<AccelerationSample>();
        angularSpeeds = new ArrayList<AngularSpeedSample>();
        pressures = new ArrayList<PressureSample>();

        for (Sensor sensor : sensorList) {

            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(sensor.getType()), sensorSamplingRate);
        }


    }

    private void unregisterListener() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        PowerManager manager =
                (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);


        registerReceiver(actionScreenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));


        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Utilities.showNotification(this, notificationManager, "Service running", MainActivity.class);


    }

    public void onDestroy() {
        unregisterReceiver(actionScreenOffReceiver);

        unregisterListener();

       // saveSamples();

        mWakeLock.release();

        notificationManager.cancelAll();

//        timer.cancel();
        timerStarted = false;


        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {


        switch (event.sensor.getType()) {

            case Sensor.TYPE_ACCELEROMETER:
                long accVarTime = System.currentTimeMillis();
                getAcceleration(event, accVarTime);
                break;

            case Sensor.TYPE_GYROSCOPE:
                long gyrVarTime = System.currentTimeMillis();
                getAngularSpeed(event, gyrVarTime);
                break;

            case Sensor.TYPE_PRESSURE:
                long barVarTime = System.currentTimeMillis();
                getPressure(event, barVarTime);
                break;

            default:
                break;

        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void getAcceleration(SensorEvent event, long sampleTime) {

        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];



        if (lastAccelerometerUpdate == 0){
            lastAccelerometerUpdate = sampleTime;
        }
        long diff = sampleTime - lastAccelerometerUpdate;
        countAcc += diff;
        lastAccelerometerUpdate = sampleTime;

        String timestamp = Utilities.getTimeInSeconds(countAcc) ;
        AccelerationSample a = new AccelerationSample(x, y, z, timestamp);
        Utilities.writeData(accelerationFile, a.toString());
        //Log.e("Acceleration sample ", a.toString());
        //accelerationSamples.add(a);
    }


    private void getAngularSpeed(SensorEvent event, long sampleTime) {

        float[] values = event.values;

        float x = values[0];
        float y = values[1];
        float z = values[2];



        if (lastGyroscopeUpdate == 0){
            lastGyroscopeUpdate  = sampleTime;
        }
        long diff = sampleTime - lastGyroscopeUpdate;
        countGyr += diff;
        lastGyroscopeUpdate = sampleTime;

        String timestamp = Utilities.getTimeInSeconds(countGyr) ;
        AngularSpeedSample a = new AngularSpeedSample(x, y, z, timestamp);
        Utilities.writeData(angularSpeedFile, a.toString());
        //angularSpeeds.add(a);
    }

    private void getPressure(SensorEvent event, long sampleTime) {

        float[] values = event.values;

        float pressure = values[0];


        if (lastBarometerUpdate == 0){
            lastBarometerUpdate = sampleTime;
        }
        long diff = sampleTime - lastBarometerUpdate;
        countBar += diff;
        lastBarometerUpdate = sampleTime;

        String timestamp = Utilities.getTimeInSeconds(countBar) ;
        PressureSample a = new PressureSample(pressure, timestamp);
        Utilities.writeData(pressureFile, a.toString());
        //pressures.add(a);
    }


    public BroadcastReceiver actionScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                return;
            }
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    unregisterListener();
                    registerListeners();
                    notificationManager.cancelAll();
                    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    Utilities.showNotification(getApplicationContext(), notificationManager, "Service running", MainActivity.class);

                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);

        }
    };

    public void saveSamples() {

        unregisterListener();


        if (!accelerationSamples.isEmpty()) {

            for (AccelerationSample as : accelerationSamples) {
                Utilities.writeData(accelerationFile, as.toString());
            }
            accelerationSamples.clear();

        }

        if (!angularSpeeds.isEmpty()) {

            for (AngularSpeedSample ass : angularSpeeds) {
//                Utilities.writeData(angularSpeedFile, ass.toString());
            }
            angularSpeeds.clear();

        }
        if (!pressures.isEmpty()) {

            for (PressureSample p : pressures) {
     //           Utilities.writeData(pressureFile, p.toString());
            }

            pressures.clear();

        }

        registerListeners();
    }

    class SavingSamplesTimer extends CountDownTimer {

        public SavingSamplesTimer(long startTime, long interval) {

            super(startTime, interval);
            Log.d("timer started ", "Timer started");

        }

        @Override
        public void onTick(long millisUntilFinished) {

        }


        @Override
        public void onFinish() {
            Log.e("time's up", "time's up! Let's save samples");
            saveSamples();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.e("Samples saved", "Samples saved");
            this.start();
        }


    }


}
