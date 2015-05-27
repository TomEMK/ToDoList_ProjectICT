package projectict.todolist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Parental_PIN extends Activity {
    private EditText PIN;

    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script location:
    private static final String PIN_URL = "http://todolist2-aphelloworld.rhcloud.com/pincode.php";

    //JSON element ids from response op php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_parental__pin);

        PIN =(EditText)findViewById(R.id.txt_LOG_PIN);

        PIN.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if ((actionId == EditorInfo.IME_ACTION_DONE) ||
                        ((event.isShiftPressed() == false) &&
                         (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) &&
                         (event.getAction() == KeyEvent.ACTION_DOWN)))
                {
                    new CheckPIN().execute();
                    return true;
                }
                else return false;
            }

        });
    }

    class CheckPIN extends AsyncTask<String,String,String>{

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Parental_PIN.this);
            pDialog.setMessage("Checking PIN...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // TODO Auto-generated method stub
            int success;

            SharedPreferences mSession = Parental_PIN.this.getSharedPreferences("Session", 0);
            String user = mSession.getString("userID","");
            String code = PIN.getText().toString();

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UID",user));
                params.add(new BasicNameValuePair("PIN",code));


                Log.d("request!", "starting");
                //Getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(PIN_URL,"POST",params);
                //check log for response
                Log.d("Pin check attempt", json.toString());

                success = json.getInt(TAG_SUCCESS);

                if(success == 1){
                    Log.d("PIN correct", json.toString());

                    Intent i = new Intent(Parental_PIN.this, Parent_All_Kids.class);
                    finish();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                }
                else{
                    Log.d("PIN incorrect!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s != null){
                Toast.makeText(Parental_PIN.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
