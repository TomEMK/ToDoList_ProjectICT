package projectict.todolist;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.OnClickListener;


public class Child_Login extends Activity{

    // Progress Dialog
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
        setContentView(R.layout.activity_child__login);

        //Get ListView object
        Child = (ListView)findViewById(R.id.lv_child_login);

        //define Array walues to show in listview
        new GetKids().execute();

        //Listview Item Click Listener
        Child.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                name = (String)parent.getItemAtPosition(position);
                Toast.makeText(Child_Login.this,name, Toast.LENGTH_SHORT);
                new getChildID().execute();
            }
        });
    }

    class GetKids extends AsyncTask<String, String, String>{

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Child_Login.this);
            pDialog.setMessage("Checking for Kids...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... args) {

            SharedPreferences mSession = Child_Login.this.getSharedPreferences("Session", 0);
            String user = mSession.getString("userID",null);
            int success;

            try{
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("UID",user));

                JSONObject json = jsonParser.makeHttpRequest(URL,"POST",params);

                success = json.getInt(TAG_SUCCESS);
                if(success == 1)
                {
                    values = new String[]{json.get(TAG_RESULT).toString()};
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
                Toast.makeText(Child_Login.this, s, Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Child_Login.this, android.R.layout.simple_list_item_1, android.R.id.text1,values);
            Child.setAdapter(adapter);
        }
    }

    class getChildID extends AsyncTask<String, String, String>{

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Child_Login.this);
            pDialog.setMessage("logging in...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(final String... args) {

            SharedPreferences mSession = Child_Login.this.getSharedPreferences("Session", 0);
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
                    Intent i = new Intent(Child_Login.this,Child_Tasks_child.class);
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
                Toast.makeText(Child_Login.this, s, Toast.LENGTH_SHORT).show();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Child_Login.this, android.R.layout.simple_list_item_1, android.R.id.text1,values);
            Child.setAdapter(adapter);
        }
    }
}

