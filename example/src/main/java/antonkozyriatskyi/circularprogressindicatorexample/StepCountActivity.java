package antonkozyriatskyi.circularprogressindicatorexample;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;


public class StepCountActivity extends Activity implements SensorEventListener {

    //EditText plantName, plantType, selectType, location, latinName;
    EditText plantType, selectType;
    TextView plantName;
    MyDatabase db;
    public static String userInputType;

    SensorManager sensorManager;
    Sensor sensor;

    boolean running = false;

    private static final String TAG = "LoginActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stepcount);

        plantName = (TextView) findViewById(R.id.plantNameEditText);
        plantType = (EditText)findViewById(R.id.plantTypeEditText);
        selectType = (EditText)findViewById(R.id.selectTypeEditText);
//        location=(EditText)findViewById(R.id.locationEditText);
//        latinName=(EditText)findViewById(R.id.latinEditText);

        db = new MyDatabase(this);

        sensorManager = (SensorManager) getSystemService ( Context.SENSOR_SERVICE);

        if(isServicesOK()){
            init();
        }
    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.mapButton);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StepCountActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }


    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(StepCountActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(StepCountActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume ();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor ( sensor.TYPE_STEP_COUNTER );
        if(countSensor!= null){
            sensorManager.registerListener ( this,countSensor, SensorManager.SENSOR_DELAY_UI );
        }else {
            Toast.makeText ( this,"SENSOR NOT FOUND", Toast.LENGTH_SHORT ).show ();
        }
    }

    @Override
    protected void onPause() {
        super.onPause ();
        running = false;
        //if you unregister the hardware will stop detecting steps
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (running){
            plantName.setText ( String.valueOf ( event.values[0] ) );
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void addPlant (View view)
    {
        String name = plantName.getText().toString();
        String type = plantType.getText().toString();

        long id = db.insertData(name, type);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
        plantName.setText("");
        plantType.setText("");

    }


    public void viewResults(View view)
    {
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }

    public void viewQueryResults (View view)
    {

        userInputType = selectType.getText().toString();

        Intent intent = new Intent(this, RecyclerViewActivity.class);
        //intent.putExtra("type", "flower");
        startActivity(intent);

        //String queryResults = db.getSelectedData(userInputType);
        //Toast.makeText(this, queryResults, Toast.LENGTH_LONG).show();
    }

}



