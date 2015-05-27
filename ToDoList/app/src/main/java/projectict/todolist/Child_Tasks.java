package projectict.todolist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Child_Tasks extends Activity implements View.OnClickListener{

    private ListView Task;
    private Button Add;
    public String task;
    String[] values;

    JSONParser jsonParser = new JSONParser();
    private static final String URL = "http://todolist2-aphelloworld.rhcloud.com/db_Get_Tasks.php";

    private static final String TAG_MESSAGE = "message";
    private static final String TAG_RESULT = "result";
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_child__tasks);

        //Get ListView object
        Task = (ListView)findViewById(R.id.lv_child_tasks);
        Add = (Button)findViewById(R.id.btn_Add_Task);
        Add.setOnClickListener(this);

        new GetTasks().execute();

        //Listview Item Click Listener
       /*Task.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                task = (String)parent.getItemAtPosition(position);
                //Toast.makeText(Child_Login.this, name, Toast.LENGTH_SHORT);
                //new getChildID().execute();
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_Add_Task:
                Intent i = new Intent(this, Add_Task.class);
                startActivity(i);
        }
    }

    class GetTasks extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Child_Tasks.this);
            pDialog.setMessage("Checking for Tasks...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... args) {


            SharedPreferences mSession = Child_Tasks.this.getSharedPreferences("Session", 0);
            String child = mSession.getString("childID","");
            int success;

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("CID",child));

                JSONObject json = jsonParser.makeHttpRequest(URL,"POST",params);

                success = json.getInt(TAG_SUCCESS);
                if(success == 1)
                {
                    values = new String[]{json.get(TAG_RESULT).toString()};
                    /*String strng1 = values[0];
                    String[] str2 = strng1.split("\\[\"");
                    String s2 = str2[1];
                    String[] str3 = s2.split("\"\\]");
                    String s3 = str3[0];
                    values = s3.split("\",\"");*/
                    return json.getString(TAG_MESSAGE);
                }
                else
                {
                    values = new String[]{"Empty"};
                    return json.getString(TAG_MESSAGE);
                }

            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            if (s != null){
                Toast.makeText(Child_Tasks.this, s, Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Child_Tasks.this, R.layout.list_item, R.id.txt_Title,values);
            Task.setAdapter(adapter);
        }
    }
}
