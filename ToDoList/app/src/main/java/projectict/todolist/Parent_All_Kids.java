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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Parent_All_Kids extends Activity implements View.OnClickListener{

    private Button mAdd;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    public String name;

    private static final String TAG_MESSAGE = "message";
    private static final String TAG_RESULT = "result";
    private static final String TAG_SUCCESS = "success";
    private static final String URL ="http://todolist2-aphelloworld.rhcloud.com/db_Get_Children.php";

    private static final String ID_URL ="http://todolist2-aphelloworld.rhcloud.com/Get_Child_ID.php";

    String[] values;

    private ListView Child;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_parent__all__kids);

        mAdd=(Button)findViewById(R.id.btn_Add_Kid);
        mAdd.setOnClickListener(this);

        //Get ListView object
        Child = (ListView)findViewById(R.id.lv_my_kids);

        //define Array walues to show in listview

        new GetKids().execute();

        //Define new ADAPTER (Context, Layout for Row, ID of TaxtView to show data, Array of Data)
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1,values);

        //Assign adapter to Listview
        //Child.setAdapter(adapter);

        //Listview Item Click Listener
        Child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = (String)parent.getItemAtPosition(position);
                //Toast.makeText(Child_Login.this,name, Toast.LENGTH_SHORT);
                new getChildID().execute();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_Add_Kid:
                Intent i = new Intent(this, Add_Child.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    class GetKids extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Parent_All_Kids.this);
            pDialog.setMessage("Checking for Kids...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... args) {

            int success;
            SharedPreferences mSession = Parent_All_Kids.this.getSharedPreferences("Session", 0);
            String user = mSession.getString("userID","");

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UID",user));

                JSONObject json = jsonParser.makeHttpRequest(URL,"POST",params);
                success = json.getInt(TAG_SUCCESS);

                if(success == 1)
                {
                    String Res = json.get(TAG_RESULT).toString();
                    values = new String[]{Res};
                    String strng1 = values[0];
                    String[] str2 = strng1.split("\\[\"");
                    String s2 = str2[1];
                    String[] str3 = s2.split("\"\\]");
                    String s3 = str3[0];
                    values = s3.split("\",\"");

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
                Toast.makeText(Parent_All_Kids.this, s, Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Parent_All_Kids.this, android.R.layout.simple_list_item_1, android.R.id.text1,values);
            Child.setAdapter(adapter);
        }
    }

    class getChildID extends AsyncTask<String, String, String>{

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Parent_All_Kids.this);
            pDialog.setMessage("logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... args) {

            SharedPreferences mSession = Parent_All_Kids.this.getSharedPreferences("Session", 0);
            SharedPreferences.Editor edit = mSession.edit();
            String user = mSession.getString("userID","");

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UID",user));
                params.add(new BasicNameValuePair("Name",name));
                JSONObject json = jsonParser.makeHttpRequest(ID_URL,"POST",params);
                String t = json.getString(TAG_RESULT);
                if(t != null) {
                    edit.putString("childID", t);
                    edit.commit();
                    Intent i = new Intent(Parent_All_Kids.this,Child_Tasks.class);
                    finish();
                    startActivity(i);
                }

                return json.getString(TAG_MESSAGE);
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
                Toast.makeText(Parent_All_Kids.this, s, Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Parent_All_Kids.this, android.R.layout.simple_list_item_1, android.R.id.text1,values);
            Child.setAdapter(adapter);
        }
    }
}
