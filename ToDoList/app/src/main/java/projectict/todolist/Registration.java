package projectict.todolist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Registration extends Activity implements View.OnClickListener {

    private EditText user, userCheck, pass, passCheck, pin, pinCheck;
    private Button mSubmit;

    //Progress Dialog
    private ProgressDialog pDialog;

    //JSON parser class
    JSONParser jsonParser = new JSONParser();

    //create URL
    private static final String REG_URL = "http://todolist2-aphelloworld.rhcloud.com/Register.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_registration);

        user = (EditText)findViewById(R.id.txt_Email);
        userCheck = (EditText)findViewById(R.id.txt_Confirm_Email);

        pass = (EditText)findViewById(R.id.txt_Password);
        passCheck = (EditText)findViewById(R.id.txt_Confirm_Password);

        pin = (EditText)findViewById(R.id.txt_PIN);
        pinCheck = (EditText)findViewById(R.id.txt_Confirm_PIN);

        mSubmit = (Button)findViewById(R.id.btn_Submit);
        mSubmit.setOnClickListener(this);
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
        //TODO Auto-generated method stub

        new CreateUser().execute();
    }

    class CreateUser extends AsyncTask<String, String, String>{

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Registration.this);
            pDialog.setMessage("Creating User...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int success;

            String username = user.getText().toString();
            String usernameCheck = userCheck.getText().toString();

            String password = pass.getText().toString();
            String passwordCheck = passCheck.getText().toString();

            String code = pin.getText().toString();
            String codeCheck = pinCheck.getText().toString();

            try{
                //Building Params
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username",username));
                        params.add(new BasicNameValuePair("usernameCheck",usernameCheck));

                        params.add(new BasicNameValuePair("password",password));
                        params.add(new BasicNameValuePair("passwordCheck",passwordCheck));

                        params.add(new BasicNameValuePair("code",code));
                        params.add(new BasicNameValuePair("codeCheck",codeCheck));
                Log.d("request!", "starting");

                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(REG_URL,"POST",params);

                //full json response
                Log.d("Write Attempt", json.toString());

                //json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1)
                {
                    Log.d("Account Created!", json.toString());

                    Intent i = new Intent(Registration.this, Login.class);
                    finish();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                }
                else{
                    Log.d("Creation Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }

            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null){
                Toast.makeText(Registration.this,file_url, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
