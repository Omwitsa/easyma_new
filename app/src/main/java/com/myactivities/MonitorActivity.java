package com.myactivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import Rest.ApiClient;
import Rest.ApiInterface;
import model.ProductResp;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
    private static final String CollectionDB = null;
    private final String TAG = "MonitorActivity";
    private Handler _handler = new Handler();
    private final int maxlength = 28;

    public EditText TextView, sTextView, sno,pin;
    public EditText TextViewc,TextViewf;
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
    SQLiteDatabase  db, db1;
    private Object obj1 = new Object();
    private Object obj2 = new Object();
    public static boolean canRead = true;

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
        TextViewc  = findViewById(R.id.TextViewc);
        TextViewf  = findViewById(R.id.TextViewf);
        //TextViewc.setText(String.format("%D",value));
        TextViewc.setText(String.valueOf(CONTAINER_WEIGHT));
        // TextViewf.setText(String.valueOf (fvalue));

        final Spinner spinner = (Spinner) findViewById(R.id.shift);
        final Spinner spinner1 = (Spinner) findViewById(R.id.branch);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shift_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //shift = spinner.getSelectedItem().toString().trim();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //shift = spinner.get(position).getPlant_name();

                shift = spinner.getSelectedItem().toString();
                //String shift = (String) spinner.getSelectedItem();

                //Toast.makeText(MonitorActivity.this,  shift, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.branch_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        //shift = spinner.getSelectedItem().toString().trim();

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //shift = spinner.get(position).getPlant_name();

                fbranch = spinner1.getSelectedItem().toString();
                CONTAINER_WEIGHT = Double.valueOf(fbranch) ;
                TextViewc.setText(String.valueOf(CONTAINER_WEIGHT));
                //String shift = (String) spinner.getSelectedItem();

                //Toast.makeText(MonitorActivity.this,  shift, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        ArrayList productList = new ArrayList<>();
//        productList.add("Leaves");
//        productList.add("Seeds");
//        productList.add("Powder");
//        productList.add("Dry Leaves");
//        productList.add("Moringa Seeds");
//        productList.add("Fresh Leaves");

        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS products(type VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM products", null);

        if (c.getCount() == 0) {
            db.execSQL("INSERT INTO products VALUES('Leaves'), ('Seeds'), ('Powder'), ('Dry Leaves'), ('Moringa Seeds'), ('Fresh Leaves');");
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
        Call<ProductResp> call= apiService.getItems();
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
        db1 = openOrCreateDatabase("loginDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CollectionDB(supplier VARCHAR,quantity VARCHAR,branch VARCHAR,datep DATETIME,date DATETIME, auditId VARCHAR, shift VARCHAR,status VARCHAR,transdate VARCHAR, Type VARCHAR);");
        //db.execSQL("ALTER TABLE CollectionDB  ADD transdate  varchar");
        //db.execSQL("UPDATE CollectionDB   SET transdate  = datep");



        /*Starting new db for accepting data from mysql database*/
        //db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        //db1 = openOrCreateDatabase("loginDB", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS DetailsDB(name VARCHAR,quantity VARCHAR,phone VARCHAR,idno VARCHAR);");
        //db.execSQL("ALTER TABLE CollectionDB  ADD transdate  varchar");
        //db.execSQL("UPDATE CollectionDB   SET transdate  = datep");
        /*Starting new db for accepting data from mysql database*/
    }

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
        StringBuffer buffer = new StringBuffer();
        Intent intent = new Intent(MonitorActivity.this, MainActivity.class);
        Bundle getBundle = this.getIntent().getExtras();
        branch = getBundle.getString("branch");
        company = getBundle.getString("company");
        logedInUser = getBundle.getString("user");

        AlertDialog.Builder build = new AlertDialog.Builder(com.myactivities.MonitorActivity.this);
        build.setTitle("Confirmation :SNo=" + sno.getText() + " and Quantity =" + TextViewf.getText().toString() + "  using:OSARAI");

        //validating the values....
        //double quantity = Double.parseDouble(TextView.getText().toString());
        //NumberFormat nf = new DecimalFormat("##.##");
        // System.out.println(nf.format(quantity));


        //if ((TextView.getText().toString()).equalsIgnoreCase(nf.format(quantity))) {
        //Toast.makeText(ma, "Wait for the scale to stabilize the quantity", Toast.LENGTH_SHORT).show();
        //} else
        //build.setTitle("Confirmation :SNo=" + sno.getText() + " and Quantity =" + TextView.getText().toString() + "  using " + branchhh);
        //final String cont = branch;
        //final String sess= String.valueOf(spinner21.getSelectedItem());
        build.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String user = null;

                        //Cursor c1 = db1.rawQuery("SELECT * FROM admin_login where status=1", null);
                        //if (c1.getCount() == 0) {
                        //showMessage("Oh Noo!", "You are using the default admin session, contact system admin if you dont have a user account");
                        //return;
                        //}
                        //StringBuffer buffer = new StringBuffer();

                        //while (c1.moveToNext()) {
                        //user = c1.getString(0);

                        //}

                        long milis1 = System.currentTimeMillis();
                        String date_print = DateUtil.timeMilisToString(milis1, "yyyy-MM-dd");

                        Calendar c = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date1 = sdf.format(c.getTime());
                        SimpleDateFormat ff = new SimpleDateFormat("yyyy-MM-dd");
                        String trans= ff.format(c.getTime());
                        final String branchhh = "OSARAI";
                        //date_print
                        //Editable sno1=sno.getText();
                        //String quantity=TextView.getText().toString();
                        //trans = trans.trim();

                        db.execSQL("INSERT INTO CollectionDB  VALUES('" + sno.getText() + "','" + (TextViewf.getText() + "','")+ branchhh +"','" + date_print +"','" + date1 + "','MORINGA','" + shift + "','0','"+trans+"', '" + product + "');");
                        showMessage("Success", "Record added");
                        TextViewf.setText("0");

//                        //connectDevice();
                        Intent i = new Intent(getApplicationContext(), MainPrintActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("qty", TextViewf.getText().toString());
                        bundle.putString("sno", sno.getText().toString());
                        bundle.putString("shift", shift);
                        bundle.putString("product", product);
                        i.putExtras(bundle);
                        startActivity(i);
                        //sno.setText("");
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


