package projectict.todolist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Add_Task extends Activity implements View.OnClickListener{

    private DatePicker date;
    private TimePicker time;
    private EditText titel;
    private Button create;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static final String URL="http://todolist2-aphelloworld.rhcloud.com/Add_Task.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_add__task);

        date = (DatePicker)findViewById(R.id.dp_Date);
        time = (TimePicker)findViewById(R.id.tp_Time);
        time.setIs24HourView(true);
        titel = (EditText)findViewById(R.id.txt_Titel);
        create = (Button)findViewById(R.id.Create_Task);

        create.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.Create_Task:
                new CreateTask().execute();
                break;
            default:
                break;
        }
    }

    class CreateTask extends AsyncTask<String,String,String> {

        boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Add_Task.this);
            pDialog.setMessage("Creating Child...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            int success;

            String name = titel.getText().toString();

            int day = date.getDayOfMonth();
            int month = date.getMonth() + 1;
            int year = date.getYear();
            String datum = year +"-"+month+"-"+day;

            int uur = time.getCurrentHour();
            int min = time.getCurrentMinute();
            String tijd = uur+":"+min+":00";

            SharedPreferences mSession = Add_Task.this.getSharedPreferences("Session", 0);
            String child = mSession.getString("childID","");

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("titel",name));
                params.add(new BasicNameValuePair("date",datum));
                params.add(new BasicNameValuePair("time",tijd));
                params.add(new BasicNameValuePair("CID",child));
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

                    Intent i = new Intent(Add_Task.this, Child_Tasks.class);
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
                Toast.makeText(Add_Task.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}
