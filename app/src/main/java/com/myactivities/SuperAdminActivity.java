package com.myactivities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static network.Urls.BASE_URL;

public class  SuperAdminActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnAddPlant;
    EditText new_dairy_plant;
    SQLiteDatabase db;

    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    HttpPost httppost;
    HttpResponse response;
    ProgressDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Super User Management Panel");
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        new_dairy_plant = (EditText) findViewById(R.id.new_dairy_plant);
        btnAddPlant = (Button) findViewById(R.id.btnAddPlant);
        db = openOrCreateDatabase("plantDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS d_plants(plant_name VARCHAR, datep DATETIME, status VARCHAR);");

        btnAddPlant.setOnClickListener(this);

        ImageView btnuser = (ImageView) findViewById(R.id.new_user);
        btnuser.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_addplant, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected

            case R.id.action_synch_plant:
                if (isOnline()) {
                    try {
                        synchroniser();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(SuperAdminActivity.this, "Check your Internet Connection and synchronise", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }

        // return true;
        return super.onOptionsItemSelected(item);
    }
    public void insertPlantToSqlite() {

        Calendar cc = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date_key = sdf.format(cc.getTime());

        Log.d("my date", date_key);

        if (!validate()) {
            return;
        } else {
            btnAddPlant.setEnabled(false);
            dialog = ProgressDialog.show(SuperAdminActivity.this, "",
                    "Adding Dairy Plant...", true);
            dialog.dismiss();
            db.execSQL("INSERT INTO d_plants VALUES('" + new_dairy_plant.getText() + "','" + date_key + "','1');");
            synchroniser();
        }
    }

    public boolean validate() {
        boolean valid = true;

        String plant_name = new_dairy_plant.getText().toString();

        if (plant_name.isEmpty() || plant_name.length() < 4) {
            new_dairy_plant.setError("enter a valid dairy plant name");
            new_dairy_plant.requestFocus();
            valid = false;
        } else {
            new_dairy_plant.setError(null);
        }


        return valid;
    }

    public void storeDairyPlantToDB() {
        Cursor c = db.rawQuery("SELECT * FROM d_plants WHERE status='1'", null);
        String plant_name = null;
        String datep = null;

        while (c.moveToNext()) {
            plant_name = c.getString(0);
            datep = c.getString(1);
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost(BASE_URL + "submitDairyPlants.php");
            nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("plant_name", plant_name.toString().trim()));
            nameValuePairs.add(new BasicNameValuePair("datep", datep.toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            response = httpclient.execute(httppost);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        dialog.dismiss();
                       // Toast.makeText(SuperAdminActivity.this, response, Toast.LENGTH_LONG).show();
                        Toast.makeText(SuperAdminActivity.this, "success", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            if (response.equalsIgnoreCase("Successful")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        db.execSQL("UPDATE plant_name set status='2'  where status='1';");
                        Toast.makeText(SuperAdminActivity.this, "Dairy Plant stored Successful", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(SuperAdminActivity.this, "Something is wrong, check internet and try again", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
        }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_user:
                Intent user = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivity(user);
                break;
            case R.id.btnAddPlant:
                insertPlantToSqlite();
            default:
                break;
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void  synchroniser(){
        Cursor cursor = db.rawQuery("SELECT count(*) FROM d_plants", null);
        cursor.moveToFirst();
        if (cursor.getInt(0) > 0) {
            dialog = ProgressDialog.show(SuperAdminActivity.this, "",
                    " Submitting new dairy Plant(s),please wait...", true);
            dialog.setCancelable(true);
            new Thread(new Runnable() {
                public void run() {
                    storeDairyPlantToDB();
                    dialog.dismiss();

                    Intent i = new Intent(getApplicationContext(), AddUserActivity.class);
                    startActivity(i);
                }
            }).start();
        } else {
            Toast.makeText(SuperAdminActivity.this, "No new dairy plant records found, add dairy plant and refresh", Toast.LENGTH_LONG).show();
        }
    }
}
