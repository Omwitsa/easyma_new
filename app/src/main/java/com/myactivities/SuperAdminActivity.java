package com.myactivities;

import static com.myactivities.DailyReportsActivity.Transsdate;

import android.app.AlertDialog;
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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;

import Rest.ApiClient;
import Rest.ApiInterface;
import model.Response;
import model.SynchData;
import model.TCollection;
import retrofit2.Call;
import retrofit2.Callback;
import util.AppConstants;

public class  SuperAdminActivity extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase db;
    ApiInterface apiService;
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

        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);

        ImageView btnuser = (ImageView) findViewById(R.id.new_user);
        ImageView btnCcoll = (ImageView) findViewById(R.id.ctcollection);
        btnuser.setOnClickListener(this);
        btnCcoll.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_collection, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected

            case R.id.synch_collection:
                if (isOnline()) {
                    try {
                        sendToDB();
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

    public void sendToDB() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        Cursor c = db.rawQuery("SELECT * FROM TransporterCollection WHERE status='0'", null);
        if (c.getCount() == 0) {
            showMessage("Collection Message", "No new collection found");
            return;
        }

        ArrayList<TCollection> intakes = new ArrayList<TCollection>();
        while (c.moveToNext()) {
            String transCode = c.getString(0);
            String actualKg = c.getString(1);
            String date = c.getString(2);
            String saccoCode = c.getString(6);
            String transDate = c.getString(7);

            intakes.add(new TCollection(transCode, actualKg, transDate, saccoCode, AppConstants.BRANCH, date));
        }

        try {
            apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<Response> call= apiService.transporterIntake(intakes);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    model.Response responseData = response.body();
                    assert responseData != null;
                    String status = responseData.getMessage();
                    Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    db.execSQL("UPDATE TransporterCollection set status='1' where status='0';");
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "An Error occurred", Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            dialog.dismiss();
            System.out.println("Exception :" + e.getMessage());
        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_user:
                Intent user = new Intent(getApplicationContext(), AddUserActivity.class);
                startActivity(user);
                break;
            case R.id.ctcollection:
                Intent ccollectionIntent = new Intent(getApplicationContext(), TransporterCollection.class);
                startActivity(ccollectionIntent);
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

}
