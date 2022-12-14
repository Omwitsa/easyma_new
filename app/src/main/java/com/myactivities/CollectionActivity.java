package com.myactivities;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import Rest.ApiClient;
import Rest.ApiInterface;
import model.ProductResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import myactivities.R;

public class CollectionActivity extends MainPrintActivity implements OnClickListener {

    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();

    ProgressDialog dialog = null;
    private ProgressDialog mProgressDlg, mConnectingDlg;

    private BluetoothAdapter mBluetoothAdapter;
    private Button Data;
    ApiInterface apiService;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.collection);
        if (_bluetooth == null) {
            showUnsupported();
        }

        //Data=findViewById(R.id.btn_Enquiry);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("MORINGA COLLECTION");
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        ImageView btndcol = (ImageView) findViewById(R.id.dcollection);
        ImageView btnCcoll = (ImageView) findViewById(R.id.ccollection);
        Data=findViewById(R.id.btn_Enquiry);


        Data.setOnClickListener(this);

        btndcol.setOnClickListener(this);
        btnCcoll.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu3, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected

            case R.id.action_synch:
                if (isOnline()) {
                    fetchProducts();
                    sendToDB();
//                    autosynch();
                } else {
                    Toast.makeText(CollectionActivity.this, "Check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                }
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
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

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dcollection:
                Intent pcollectionIntent = new Intent(getApplicationContext(), DailyReportsActivity.class);
                startActivity(pcollectionIntent);
                break;
            case R.id.ccollection:
                //if connection not yet established:

                Intent ccollectionIntent = new Intent(getApplicationContext(), SearchDeviceActivity.class);
                startActivity(ccollectionIntent);
                break;
            case R.id.btn_Enquiry:
                //if connection not yet established:

                Intent btn_EnquiryIntent = new Intent(getApplicationContext(), Enquiry.class);
                startActivity(btn_EnquiryIntent);
                break;
            default:
                break;
        }

    }

    private void showDisabled() {
        showToast("Bluetooth disabled");

    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");

    }


}