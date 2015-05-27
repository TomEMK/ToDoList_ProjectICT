package projectict.todolist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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


public class Add_Child extends Activity implements View.OnClickListener{

    private Button mCreate;
    private EditText mName;

    private ProgressDialog pDialog;

    //JSON parser class
    JSONParser jsonParser = new JSONParser();

    //create URL
    private static final String URL = "http://todolist2-aphelloworld.rhcloud.com/Add_Child.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add__child);

        mCreate = (Button)findViewById(R.id.btn_Create);
        mCreate.setOnClickListener(this);

        mName =(EditText)findViewById(R.id.txt_ChildName);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_Create:
                new CreateKid().execute();
                break;
            default:
                break;
        }
    }

    class CreateKid extends AsyncTask<String,String,String>{

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Add_Child.this);
            pDialog.setMessage("Creating Child...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            String name = mName.getText().toString();

            SharedPreferences mSession = Add_Child.this.getSharedPreferences("Session", 0);
            String user = mSession.getString("userID","");

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                    params.add(new BasicNameValuePair("name",name));
                    params.add(new BasicNameValuePair("user",user));
                Log.d("request!", "Start");

                //Posting data to script
                JSONObject json = jsonParser.makeHttpRequest(URL,"POST",params);

                //json response
                Log.d("Write Attempt", json.toString());

                //success element
                success = json.getInt(TAG_SUCCESS);
                if(success == 1)
                {
                    Log.d("Child Created",json.toString());

                    Intent i = new Intent(Add_Child.this, Parent_All_Kids.class);
                    finish();
                    startActivity(i);

                    return json.getString(TAG_MESSAGE);
                }
                else{
                    Log.d("Something whent wrong", json.getString(TAG_MESSAGE));
                    return  json.getString(TAG_MESSAGE);
                }

            }
            catch(JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if(s != null)
                Toast.makeText(Add_Child.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
