package projectict.todolist;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Parent_Or_Child extends Activity implements View.OnClickListener {

    private ProgressDialog pDialog;


    private Button Parent, Child, Logout;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    //php login script location:
    private static final String SESSION_URL ="http://todolist2-aphelloworld.rhcloud.com/CheckSession.php";

    //JSON element ids from repsonse of php script:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_parent__or__child);

        //Set buttons
        Parent = (Button)findViewById(R.id.btn_Parent);
        Child = (Button)findViewById(R.id.btn_Child);
        Logout = (Button)findViewById(R.id.btn_logout);

        //Set clicklisteners
        Parent.setOnClickListener(this);
        Child.setOnClickListener(this);
        Logout.setOnClickListener(this);

        /*//SharedPreference to save variable locally
        SharedPreferences mSession = this.getSharedPreferences("Session", 0);
        Toast.makeText(Parent_Or_Child.this, mSession.getString("userID",null), Toast.LENGTH_SHORT).show();
        */
    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.btn_Parent:
                Intent Parent = new Intent(this, Parental_PIN.class);
                startActivity(Parent);
                break;
            case R.id.btn_Child:
                Intent Child = new Intent(this, Child_Login.class);
                startActivity(Child);
                break;
            case R.id.btn_logout:
                SharedPreferences mSession = Parent_Or_Child.this.getSharedPreferences("Session", 0);
                SharedPreferences.Editor edit = mSession.edit();
                edit.clear();
                edit.commit();
                Intent i = new Intent(this, Login.class);
                startActivity(i);
            default:
                break;
        }

    }

}
