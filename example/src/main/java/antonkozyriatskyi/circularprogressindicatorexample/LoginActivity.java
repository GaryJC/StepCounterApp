package antonkozyriatskyi.circularprogressindicatorexample;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//import static com.example.hserban.sharedprefsnew.Activity2.DEFAULT;

public class LoginActivity extends Activity {
    private static final String DEBUG_TAG = "UserInfo";
    EditText usernameEditText, passwordEditText;
    boolean matched=false;
    public static final String DEFAULT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        usernameEditText = (EditText)findViewById(R.id.editTextUsername);
        passwordEditText = (EditText)findViewById(R.id.editTextPassword);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String sharedUserName=sharedPrefs.getString("username", DEFAULT);
        String sharedUserPassword=sharedPrefs.getString("password", DEFAULT);

        usernameEditText.setText(sharedUserName);
        passwordEditText.setText(sharedUserPassword);
    }


//    public void submit(View view){
//
//        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPrefs.edit();
//        editor.putString("username", usernameEditText.getText().toString());
//        editor.putString("password", passwordEditText.getText().toString());
//        Toast.makeText(this, "You create a new account!", Toast.LENGTH_LONG).show();
//        editor.commit();
//    }

    public void gotoStepCount(View view){

        Intent intent= new Intent(this, MainActivity.class);
        Intent singupIntent= new Intent(this, SignUpActivity.class);

        SharedPreferences sharedPrefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String sharedUserName=sharedPrefs.getString("username", DEFAULT);
        //Log.i(DEBUG_TAG,"username: " + sharedUserName);
        String sharedUserPassword=sharedPrefs.getString("password", DEFAULT);
        //validate(sharedUserName, sharedUserPassword);

        if ( sharedUserName.equals(DEFAULT) || sharedUserPassword.equals(DEFAULT) )
        {
            Toast.makeText(this, "Please enter names and password.", Toast.LENGTH_LONG).show();
        }

        else if (sharedUserName.equals(usernameEditText.getText().toString()) && sharedUserPassword.equals(passwordEditText.getText().toString())){
            matched=true;
            Toast.makeText(this, "Welcome "+ sharedUserName, Toast.LENGTH_LONG).show();
        }

        else{
            matched=false;
            Toast.makeText(this, "Please create an account!", Toast.LENGTH_LONG).show();

        }

        if(matched==true){
            startActivity(intent);
        }

        else if(matched==false){
            startActivity(singupIntent);
        }
    }

    public void openMap(){

    }


}

