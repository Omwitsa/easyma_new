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

import Rest.ApiInterface;
import util.AppConstants;

public class CollectionActivity extends MainPrintActivity implements OnClickListener {
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

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("MILK COLLECTION");
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView btnCcoll = (ImageView) findViewById(R.id.ccollection);
        Data=findViewById(R.id.btn_Enquiry);


        Data.setOnClickListener(this);

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
//                    fetchProducts();
                    sendToDB();
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
            case R.id.ccollection:
                //if connection not yet established:

                Intent ccollectionIntent = new Intent(getApplicationContext(), MonitorActivity.class);
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
}