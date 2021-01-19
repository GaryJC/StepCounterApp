package antonkozyriatskyi.circularprogressindicatorexample;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        SeekBar.OnSeekBarChangeListener, ColorPickerDialogFragment.OnColorSelectedListener, SensorEventListener {

    private Button dotColor;
    private SeekBar dotWidth;

    private CircularProgressIndicator circularProgress;

    SensorManager sensorManager;
    Sensor sensor;
    boolean running = false;

    int stepCount;
    int goalNumInt=10000;

    TextView goalNum;
    String stepCountString;

    private static final String TAG = "LoginActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    TextView date;
    MyDatabase db;

    private static final String CHANNEL_ID="simplified_coding";
    private static final String CHANNEL_NAME="Simplified_Coding";
    private static final String CHANNEL_DESC="Simplified Coding Notifications";
    private final int NOTIFICATION_ID=001;

    //private int progressWidth, dotWidthDp, fontSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgress = findViewById(R.id.circular_progress);
        circularProgress.setMaxProgress(10000);

        Button progressColor = findViewById(R.id.btn_progress_color);
        //Button progressBackgroundColor = findViewById(R.id.btn_background_color);
        //Button textColor = findViewById(R.id.btn_text_color);
        //dotColor = findViewById(R.id.btn_dot_color);

        progressColor.setOnClickListener(this);
        //progressBackgroundColor.setOnClickListener(this);
        //textColor.setOnClickListener(this);
        //dotColor.setOnClickListener(this);

        SeekBar progress = findViewById(R.id.sb_progress);
//        SeekBar progressStrokeWidth = findViewById(R.id.sb_progress_width);
        //final SeekBar progressBackgroundStrokeWidth = findViewById(R.id.sb_progress_background_width);
        SeekBar textSize = findViewById(R.id.sb_text_size);
//        dotWidth = findViewById(R.id.sb_dot_width);

        progress.setOnSeekBarChangeListener(this);
//        progressStrokeWidth.setOnSeekBarChangeListener(this);
        //progressBackgroundStrokeWidth.setOnSeekBarChangeListener(this);
        textSize.setOnSeekBarChangeListener(this);
//        dotWidth.setOnSeekBarChangeListener(this);

        CheckBox drawDot = findViewById(R.id.cb_draw_dot);
        drawDot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circularProgress.setShouldDrawDot(isChecked);
                //dotWidth.setEnabled(isChecked);
                //dotColor.setEnabled(isChecked);
            }
        });


        CheckBox fillBackground = findViewById(R.id.cb_fill_background);
        fillBackground.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circularProgress.setFillBackgroundEnabled(isChecked);
            }
        });


        circularProgress.setOnProgressChangeListener(new CircularProgressIndicator.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(double progress, double maxProgress) {
                Log.d("PROGRESS", String.format("Current: %1$.0f, max: %2$.0f", progress, maxProgress));
            }
        });

        Switch animationSwitch = findViewById(R.id.sw_enable_animation);
        animationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                circularProgress.setAnimationEnabled(isChecked);
            }
        });

        //******
//        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        int dotWidth=sharedPrefs.getInt("dotWidth", 8);
//        int progressWidth=sharedPrefs.getInt("progressWidth",8);
//        int fontSize=sharedPrefs.getInt("fontSize", 24);
//        circularProgress.setDotWidthDp(dotWidth);
//        circularProgress.setProgressStrokeWidthDp(progressWidth);
//        circularProgress.setTextSizeSp(fontSize);

        sensorManager = (SensorManager) getSystemService ( Context.SENSOR_SERVICE);
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        int stepCount = sharedPrefs.getInt("stepCount",0);
//        circularProgress.setCurrentProgress(0);

        goalNum=(TextView)findViewById(R.id.goalNum);
        goalNum.setText("Goal: "+ sharedPrefs.getInt("goalNum",10000));

        if(isServicesOK()){
            init();
        }

        //create a date string.
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        //get hold of textview.
        date  = (TextView) findViewById(R.id.dateText);
        //set it as current date.
        date.setText("Date: "+ date_n);

        db = new MyDatabase(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

//        if(stepCount>goalNumInt){
//            displayNotifications();
//        }

        goalNumInt=sharedPrefs.getInt("goalNum", 10000);
    }

    @Override
    public void onClick(View v) {
        ColorPickerDialogFragment dialog = new ColorPickerDialogFragment();
        dialog.setOnColorSelectedListener(this);
        String tag = null;
        switch (v.getId()) {
            case R.id.btn_progress_color:
                tag = "progressColor";
                break;
//            case R.id.btn_background_color:
//                tag = "progressBackgroundColor";
//                break;
//            case R.id.btn_text_color:
//                tag = "textColor";
//                break;
//            case R.id.btn_dot_color:
//                tag = "dotColor";
//                break;
        }

        dialog.show(getSupportFragmentManager(), tag);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        switch (seekBar.getId()) {
            case R.id.sb_progress:
                circularProgress.setMaxProgress(progress);
                circularProgress.setCurrentProgress(stepCount);
                goalNum.setText("Goal: "+progress);
                goalNumInt=progress;
                editor.putInt("goalNum", progress);
                break;
//            case R.id.sb_progress_width:
//                editor.putInt("progressWidth", progress);
//                circularProgress.setProgressStrokeWidthDp(progress);
//                break;
//            case R.id.sb_dot_width:
//                editor.putInt("dotWidth", progress);
//                circularProgress.setDotWidthDp(progress);
//                break;
            case R.id.sb_text_size:
                editor.putInt("fontSize", progress);
                circularProgress.setTextSizeSp(progress);
                break;
//            case R.id.sb_progress_background_width:
//                circularProgress.setProgressBackgroundStrokeWidthDp(progress);
//                break;
        }
        editor.commit();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onColorChosen(ColorPickerDialogFragment dialog, int r, int g, int b) {

        String tag = dialog.getTag();
        int color = Color.rgb(r, g, b);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putInt("red",r);
        editor.putInt("green",g);
        editor.putInt("blue",b);

        assert tag != null;

        switch (tag) {
            case "progressColor":
                circularProgress.setProgressColor(color);
                circularProgress.setDotColor(color);
                circularProgress.setTextColor(color);
                break;
            case "progressBackgroundColor":
                circularProgress.setProgressBackgroundColor(color);
                break;
            case "textColor":
                circularProgress.setTextColor(color);
                break;
            case "dotColor":
                circularProgress.setDotColor(color);
                break;
        }
        editor.commit();
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
        //running = false;
        //if you unregister the hardware will stop detecting steps
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (running){
            //plantName.setText ( String.valueOf ( event.values[0] ) );
//            SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPrefs.edit();
            circularProgress.setCurrentProgress(event.values[0]);
            stepCount=(int)event.values[0];
            stepCountString=String.valueOf(event.values[0]);
//            editor.putInt("stepCount", stepCount);
//            editor.commit();
            displayNotifications();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void init(){
        Button btnMap = (Button) findViewById(R.id.mapButton);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void addPlant (View view)
    {
        String name = "Steps: "+ stepCountString;
        String type = date.getText().toString();

        long id = db.insertData(name, type);
        if (id < 0)
        {
            Toast.makeText(this, "fail", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
        }
//        plantName.setText("");
//        plantType.setText("");

    }


    public void viewResults(View view)
    {
        Intent intent = new Intent(this, RecyclerActivity.class);
        startActivity(intent);
    }

    private void displayNotifications(){
        if(stepCount>goalNumInt) {
            NotificationCompat.Builder mBuilder
                    = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                    .setContentTitle("You have a new notification!")
                    .setContentText("Congratulations! You have reached today's goal!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
            mNotificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
        }
        else if(stepCount>goalNumInt && stepCount>=10000){
            NotificationCompat.Builder mBuilder
                    = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                    .setContentTitle("You have a new notification!")
                    .setContentText("Congratulations! You have reached 10000 steps today! Take a reset!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
            mNotificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
        }

//        if(stepCount>goalNumInt && stepCount>=10000){
//             .setContentText("Congratulations! You have reached 10000 steps today! Take a reset!");
//        }
//        else if(stepCount>goalNumInt){
//             .setContentText("Congratulations! You have reached today's goal!");
//        }
    }

}
