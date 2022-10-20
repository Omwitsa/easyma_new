package com.myactivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import util.AppConstants;
import util.DateUtil;

public class TransporterCollection extends AppCompatActivity {
    public EditText TextView,TextViewc,TextViewf,tno;
    SharedPreferences sharedPreferences;
    SQLiteDatabase db;
    String product, transNo;
    private String _qty = "0";
    private String _container = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND, WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_transporter_collection);

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tno = (EditText) findViewById(R.id.tno);
        TextView = (EditText) findViewById(R.id.TextView);
        TextViewc  = findViewById(R.id.TextViewc);
        TextViewf  = findViewById(R.id.TextViewf);
        TextViewc.setText("0");

        Button btnRegister = (Button) findViewById(R.id.save);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextView.getText().toString().trim().length() == 0 ||
                        TextViewf.getText().toString().trim().length() == 0 ||
                        tno.getText().toString().trim().length() == 0) {
                    showMessage("Error", "Please enter all values");
                    return;
                } else{
                    dialog();
                }
            }
        });

        TextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _qty = editable.toString();
                calculateFinalQnty();
            }
        });

        TextViewc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _container = editable.toString();
                calculateFinalQnty();
            }
        });

        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS products(type VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM products", null);

        if (c.getCount() == 0) {
            db.execSQL("INSERT INTO products VALUES('MILK');");
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

        transNo = tno.getText().toString().trim();
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS TransporterCollection(transCode VARCHAR,actualKg VARCHAR,date DATETIME,auditId VARCHAR,status VARCHAR,type VARCHAR,saccoCode VARCHAR,transdate DATETIME,printed VARCHAR);");
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private void calculateFinalQnty() {
        _qty = TextUtils.isEmpty(_qty) ? "0" : _qty ;
        _container = TextUtils.isEmpty(_container) ? "0" : _container ;
        double qty = Double.parseDouble(_qty);
        double container = Double.parseDouble(_container);
        double finalQty = qty - container;
        if (finalQty > 0){
            TextViewf.setText("" + finalQty);
        }
    }

    protected void dialog() {
        StringBuffer buffer = new StringBuffer();
        AlertDialog.Builder build = new AlertDialog.Builder(TransporterCollection.this);
        build.setTitle("Confirmation :TNo=" + tno.getText() + " and Quantity =" + TextViewf.getText().toString() + "  using:Main");
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
                        final String branchhh = "MAIN";

                        String transporter = tno.getText().toString().trim().toUpperCase();
                        String loggenInUser = sharedPreferences.getString("loggedInUser", "");
                        db.execSQL("INSERT INTO TransporterCollection  VALUES('" + transporter + "', '" + TextViewf.getText() + "', '" + date1 +"','" + loggenInUser + "','0','" + product + "','" + AppConstants.SACCO_CODE + "', '"+date_print +"', '0');");
                        showMessage("Success", "Record added");
                        TextViewf.setText("0");
                        TextView.setText("0");
                        tno.setText("");

                        Intent i = new Intent(getApplicationContext(), MainPrintActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isTransporter", true);
                        bundle.putString("qty", TextViewf.getText().toString());
                        bundle.putString("tno", transporter);
                        bundle.putString("product", product);
                        i.putExtras(bundle);
                        startActivity(i);
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


}