package com.myactivities;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Rest.ApiClient;
import Rest.ApiInterface;
import model.ProductResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.AppConstants;
import util.DateUtil;

import static com.myactivities.MainActivity.branch;
import static com.myactivities.MainActivity.logedInUser;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;


public class MonitorActivity extends MyActivity {
    ApiInterface apiService;
    private static final int REQUEST_DISCOVERY = 0x1;
    private final String TAG = "MonitorActivity";
    private Handler _handler = new Handler();
    private final int maxlength = 28;

    public EditText TextView, sTextView, sno;
    private OutputStream outputStream;
    private InputStream inputStream;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    String[] iplTeam = {"AM", "PM1", "PM2"};

    Button btnCalendar, btnTimePicker;
    SQLiteDatabase  db;
    private Object obj1 = new Object();
    private Object obj2 = new Object();
    public static boolean canRead = true;
    SharedPreferences sharedPreferences;

    AppStatus appstatus = new AppStatus();

    public static StringBuffer hexString = new StringBuffer();
    ScrollView mScrollView;

    String suppp_no,shift,fbranch, product;
    double CONTAINER_WEIGHT=0;
    public  String bb;


    // String bvalue= TextView.getText().toString();
    //String bvalue=String.valueOf(TextView.getText().toString());
    // double bvalue = Double.parseDouble(TextView.getText().toString());
    // double value1 = Double.valueOf(bvalue);
    //double fvalue = value1-value;

    //String bvalue= TextView.getText().toString();
    //double bvalue = Double.parseDouble(TextView.getText().toString());
    //double value1 = Double.valueOf(bvalue);
    //double fvalue = value1-value;




    public static String branchhh,userrrrr,company;


    private static BluetoothSocket mSocket;
    BluetoothDevice selectDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.monitor);

        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Measure Collection");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button btnRegister = (Button) findViewById(R.id.save);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        sTextView = (EditText) findViewById(R.id.sTextView);
        TextView = (EditText) findViewById(R.id.TextView);
        //TextView.setText("0");
        sno = (EditText) findViewById(R.id.sno);
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS products(type VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM products", null);

        if (c.getCount() == 0) {
            db.execSQL("INSERT INTO products VALUES('Milk');");
        }

        ArrayList productList = new ArrayList<>();
        c = db.rawQuery("SELECT type FROM products", null);
        try{
            while (c.moveToNext()) {
                String product = c.getString(0);
                productList.add(product);
            }
        }
        catch (Exception e){
            System.out.println("Exception :" + e.getMessage());
        }
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResp> call= apiService.getItems(AppConstants.SACCO_CODE);
        call.enqueue(new Callback<ProductResp>() {
            @Override
            public void onResponse(Call<ProductResp> call, Response<ProductResp> response) {

            }

            @Override
            public void onFailure(Call<ProductResp> call, Throwable t) {

            }
        });

        ArrayAdapter<String> productAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, productList);
        productAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner productSpinner = (Spinner) findViewById(R.id.product);
        productSpinner.setAdapter(productAdapter);

        productSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                product = productSpinner.getSelectedItem().toString();
//                String product = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

        //fetch the supply number from the register farmer activity
        //  sno.setText("gotten one");

        suppp_no = sno.getText().toString().trim();
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CollectionDB(supplier VARCHAR,quantity VARCHAR,branch VARCHAR, date DATETIME, auditId VARCHAR,status VARCHAR, type VARCHAR, saccoCode VARCHAR);");
    }
// supplier,
    public void onButtonClickclear(View view) throws IOException {
        hexString = new StringBuffer();
        sTextView.setText(hexString.toString());
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    protected void dialog() {
        final String branchhh = "Kieni";
        AlertDialog.Builder build = new AlertDialog.Builder(com.myactivities.MonitorActivity.this);
        build.setTitle("Confirmation :SNo=" + sno.getText() + " and Quantity =" + TextView.getText().toString() + "  using:" + branchhh);
        build.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String user = null;

                        long milis1 = System.currentTimeMillis();
                        String date_print = DateUtil.timeMilisToString(milis1, "yyyy-MM-dd");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = sdf.format(c.getTime());
                        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
                        String trans= ff.format(c.getTime());
                        String loggenInUser = sharedPreferences.getString("loggedInUser", "");
                        db.execSQL("INSERT INTO CollectionDB  VALUES('" + sno.getText() + "', '" + TextView.getText() + "', '" + branchhh +"', '" + date1 +"','" + loggenInUser + "','0','" + product + "','" + AppConstants.SACCO_CODE + "');");
                        showMessage("Success", "Record added");
                        TextView.setText("0");

                        clear();
                    }
                });

        build.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        build.create().show();
    }

    private void clear() {
        //TextView.setText("");
        //TextViewf.setText("");
        sno.setText("");
    }

    private SQLiteDatabase getWritableDatabase() {
        // TODO Auto-generated method stub
        return null;
    }
}


