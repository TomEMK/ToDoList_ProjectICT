package projectict.todolist;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Login extends Activity implements View.OnClickListener {
    private EditText Name, Pass;
    private Button mLogin;
    private TextView mRegister;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script location:
    private static final String LOGIN_URL = "http://todolist2-aphelloworld.rhcloud.com/Login.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_UID = "uid";

    protected void onCreate(Bundle savedInstanceState) {
        //POTEN AF!!
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        //setup input field
        Name=(EditText)findViewById(R.id.txt_Name);
        Pass=(EditText)findViewById(R.id.txt_Password);

        //setup buttons
        mLogin = (Button)findViewById(R.id.btn_Login);
        mRegister = (TextView)findViewById(R.id.txt_CreateAcc);

        //Register listeners
        mLogin.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        //SharedPreferences to check for logged in user
        SharedPreferences mSession = this.getSharedPreferences("Session", 0);
        //check if user is logged in
        if(mSession.getString("userID",null)!=null)
        {
            Intent i = new Intent(Login.this, Parent_Or_Child.class);
            finish();
            startActivity(i);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        // determine which button was pressed:
        switch (v.getId()) {
            case R.id.btn_Login:
                new AttemptLogin().execute();
                break;
            case R.id.txt_CreateAcc:
                Intent i = new Intent(this, Registration.class);
                startActivity(i);
                break;

            default:
                break;
        }
    }

    //AsyncTask is a seperate thread than the thread that runs the GUI
    //Any type of networking should be done with AsyncTask
    class AttemptLogin extends AsyncTask<String,String,String>{

        //Three methods get called, First preExecute, then do in Background
        //once do in background is complete, onPost execute method will be called
        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String...args) {
            // TODO Auto-generated method stub
            // Check for success tag
            int success;

            String username = Name.getText().toString();
            String password = Pass.getText().toString();

            try {

                //SharedPreference to save variable locally
                SharedPreferences mSession = Login.this.getSharedPreferences("Session", 0);
                //define the editor
                SharedPreferences.Editor editor = mSession.edit();

                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", username));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    Log.d("Login Successful!", json.toString());

                    //saving userID locally into editor
                    editor.putString("userID",json.getString(TAG_UID));
                    //Commiting changes in editor to acces globally
                    editor.commit();

                    Intent i = new Intent(Login.this, Parent_Or_Child.class);
                    finish();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                }
                else{
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            }
            catch(JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        // After completing background task Dismiss the progress dialog
        protected void onPostExecute(String file_url) {
            //dismiss dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Login.this, file_url, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
