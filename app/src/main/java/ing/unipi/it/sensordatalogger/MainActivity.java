package ing.unipi.it.sensordatalogger;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity {



    //TODO possibilità di scegliere quali sensori attivare tra quelli disponibili e scegliere la frequenza di campionamento per ciascun sensore

    boolean serviceStarted = false;

    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    EditText ageEdit;
    EditText heightEdit;
    EditText weightEdit;
    Spinner sensorPositionSpinner;
    Spinner sensorDelaySpinner;

    TextView tvErrors;

    String sex = null;
    String age = null;
    String height = null;
    String weight = null;
    String sensorPosition = null;
    String arraySensorPos[];
    int sensorDelay;


    public void initGUI() {

        ArrayAdapter<CharSequence> adapter = null;

        arraySensorPos = getResources().getStringArray(R.array.sensor_position_array);


        radioSexGroup = (RadioGroup) findViewById(R.id.radio_sex);

        ageEdit = (EditText) findViewById(R.id.age);

        heightEdit = (EditText) findViewById(R.id.height);

        weightEdit = (EditText) findViewById(R.id.weight);

        sensorPositionSpinner = (Spinner) findViewById(R.id.sensor_position);
        adapter = ArrayAdapter.createFromResource(this, R.array.sensor_position_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorPositionSpinner.setAdapter(adapter);
        sensorPositionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sensorPosition = arraySensorPos[position];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sensorDelaySpinner = (Spinner) findViewById(R.id.sensor_delay);
        adapter = ArrayAdapter.createFromResource(this, R.array.delay_rates_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sensorDelaySpinner.setAdapter(adapter);

        sensorDelaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                    case 1:
                        sensorDelay = SensorManager.SENSOR_DELAY_UI;
                        break;
                    case 2:
                        sensorDelay = SensorManager.SENSOR_DELAY_GAME;
                        break;
                    case 3:
                        sensorDelay = SensorManager.SENSOR_DELAY_FASTEST;
                        break;
                    default:
                        sensorDelay = SensorManager.SENSOR_DELAY_NORMAL;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        tvErrors = (TextView) findViewById(R.id.tvErrors);

    }


    public void startSampling(View v) {
        int selectedId = radioSexGroup.getCheckedRadioButtonId();
        radioSexButton = (RadioButton) findViewById(selectedId);
        sex = radioSexButton.getText().toString();

        age = ageEdit.getText().toString();

        height = heightEdit.getText().toString();

        weight = weightEdit.getText().toString();


        //TODO controlli sull'obbligatorietà dei campi
        if ((sex.equals("")) || (age.equals("")) || (height.equals("")) || (weight.equals("")) || (sensorPosition.equals("")) ) {

            tvErrors.setText("All fields are required!");
            tvErrors.setTextColor(Color.RED);

        } else {
            Intent intent = new Intent(this.getApplicationContext(), SensorsSamplingService.class);
            User user = new User(sex, age, height, weight);
            intent.putExtra("User data", user);
            intent.putExtra("Sensor sampling rate", String.valueOf(sensorDelay));
            intent.putExtra("Sensor position", sensorPosition);

            Log.d("Data sent to service", "From main activity to service");
            startService(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initGUI();


    }


}