package com.myactivities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import Adapters.DairyFarmSpinnerAdapter;

//import com.jaredrummler.materialspinner.MaterialSpinner;

public class AddUserActivity extends AppCompatActivity {
    Button add;
    EditText username, pass, pass1;
    //ProgressDialog dialog = null;
    SQLiteDatabase db;
    TextView alert;
    AutoCompleteTextView acTextView;

    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    HttpPost httppost;
    HttpResponse response;
    Spinner company;
    DairyFarmSpinnerAdapter dairyFarmSpinnerAdapter;


//    private static final String PATH_TO_FARM_SERVER = BASE_URL + "dairies.php";
//    protected List<DataObject> f_spinnerData;
//    private RequestQueue f_queue;
//    String coooporativeee;

    //ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_admin);


//        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
//        myToolbar.setTitle("Register New User");
//        setSupportActionBar(myToolbar);
//        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        username = (EditText) findViewById(R.id.uname123);
        pass = (EditText) findViewById(R.id.pass123);
        pass1 = (EditText) findViewById(R.id.pass23);

        add = (Button) findViewById(R.id.btnAddUser);
        db = openOrCreateDatabase("loginDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS admin_login(username VARCHAR,company VARCHAR,branch VARCHAR, password VARCHAR,datepp DATETIME, status VARCHAR);");

        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String phonee = username.getText().toString();
                String brancch = "";
                String password = pass.getText().toString();
                String Coop = "";
                String confirm_pass = pass1.getText().toString();

                if (phonee.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter Phone Number", Toast.LENGTH_LONG).show();
                }
                else if (password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please enter password", Toast.LENGTH_LONG).show();
                }
                else if(confirm_pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Please enter Confirm Password", Toast.LENGTH_LONG).show();
                }
                else if(!password.equals(confirm_pass))
                {
                    Toast.makeText(getApplicationContext(), "Passwords are not matching", Toast.LENGTH_LONG).show();
                }

                else {
                    insertDataToSqlite();
                }
            }
        });
//        add.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                insertDataToSqlite();
//            }
//        });

        //f_queue = Volley.newRequestQueue(this);
        //requestJsonDFarmObject();
    }


//    private void requestJsonDFarmObject() {
//        RequestQueue queue = Volley.newRequestQueue(this);
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, PATH_TO_FARM_SERVER, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                GsonBuilder builder = new GsonBuilder();
//                Gson mGson = builder.create();
//                try {
//                    f_spinnerData = Arrays.asList(mGson.fromJson(response, DataObject[].class));
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//                //display first question to the user
//                if (null != f_spinnerData) {
//
//                    company = (Spinner) findViewById(R.id.company);
//                    assert company != null;
//                    company.setVisibility(VISIBLE);
//                    dairyFarmSpinnerAdapter = new DairyFarmSpinnerAdapter(AddUserActivity.this, f_spinnerData);
//                    company.setAdapter(dairyFarmSpinnerAdapter);
//                    company.setPrompt("Select Your Coporative");
//
//                    progressBar.setVisibility(View.GONE);
//                    company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                        @Override
//                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                            coooporativeee = f_spinnerData.get(position).getPlant_name();
//                        }
//
//                        @Override
//                        public void onNothingSelected(AdapterView<?> parent) {
//
//                        }
//                    });
//                }else{
//                    add.setEnabled(false);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        });
//        queue.add(stringRequest);
//
//
//    }

//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        switch (v.getId()) {
//            case R.id.btnAddUser:
//                insertDataToSqlite();
//                break;
//            default:
//                break;
//        }
//    }
   /* protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }*/

    public void insertDataToSqlite() {

        Calendar cc = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date_pp = sdf.format(cc.getTime());

        db.rawQuery("SELECT * FROM admin_login", null);
        db.execSQL("INSERT INTO admin_login VALUES('" + username.getText() + "','','','" + pass.getText() + "','" + date_pp + "','1');");
        Toast.makeText(this, "Success, you can Login now as an authorised user", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(getApplicationContext(), AdminLogin.class);
        //ComponentName cn = intent.getComponent();
        //Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        startActivity(intent);
        //finish();
    }












    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

//    public void storeAdminToDB() {
//        Cursor c = db.rawQuery("SELECT * FROM admin_login WHERE status='1'", null);
//        String phone = null;
//        String password = null;
//        String datepp = null;
//
//        while (c.moveToNext()) {
//            phone = c.getString(0);
//            password = c.getString(3);
//            datepp = c.getString(4);
//
//
//            try {
//                httpclient = new DefaultHttpClient();
//                httppost = new HttpPost("https://amtechafrica.com/webservice/submitAdmin.php");
//                nameValuePairs = new ArrayList<NameValuePair>(2);
//                nameValuePairs.add(new BasicNameValuePair("phone", phone.toString().trim()));
//                nameValuePairs.add(new BasicNameValuePair("password", password.toString().trim()));
//                nameValuePairs.add(new BasicNameValuePair("datepp", datepp.toString().trim()));
//                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//                response = httpclient.execute(httppost);
//                ResponseHandler<String> responseHandler = new BasicResponseHandler();
//                final String response = httpclient.execute(httppost, responseHandler);
//                System.out.println("Response : " + response);
//                runOnUiThread(new Runnable() {
//                    public void run() {
//                        try {
//                            dialog.dismiss();
//                            Toast.makeText(AddUserActivity.this, "success", Toast.LENGTH_LONG).show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//
//
//                if (response.equalsIgnoreCase("Successful")) {
//                    //set status='2'
//                    runOnUiThread(new Runnable() {
//                        public void run() {
//                            db.execSQL("UPDATE CollectionDB set status='2'  where status='1';");
//                            Toast.makeText(AddUserActivity.this, "User stored Successful", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//
//                } else {
//                    Toast.makeText(AddUserActivity.this, "Something is wrong, check internet and try again", Toast.LENGTH_SHORT).show();
//                }
//
//            } catch (Exception e) {
//                dialog.dismiss();
//                System.out.println("Exception : " + e.getMessage());
//            }
//        }
//    }
}
