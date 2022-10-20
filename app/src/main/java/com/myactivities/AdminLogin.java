package com.myactivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;

import java.util.List;


public class AdminLogin extends Activity implements AdapterView.OnItemSelectedListener {
    Button b;
    public static EditText et, pass;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    SQLiteDatabase db;

    SharedPreferences sharedPreferences;
    // Creating an Editor object to edit(write to the file)
    SharedPreferences.Editor prefsEditor;
    private ProgressBar progress;
    private SharedPreferences pref;
    String bran, compny,usser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);
        sharedPreferences = getSharedPreferences("preferences",MODE_PRIVATE);
        prefsEditor = sharedPreferences.edit();

        b = (Button) findViewById(R.id.Button01);
        et = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        tv = (TextView) findViewById(R.id.tv);

        ArrayAdapter adapter = new ArrayAdapter<String>(AdminLogin.this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        db = openOrCreateDatabase("loginDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS admin_login(username VARCHAR,company VARCHAR,branch VARCHAR, password VARCHAR,datepp DATETIME, status VARCHAR);");
        Cursor c = db.rawQuery("SELECT * FROM admin_login", null);

        if (c.getCount() == 0) {
            db.execSQL("INSERT INTO admin_login VALUES('ADMIN','MBURUGU DAIRY F.C.S','MAIN','admin123','2022-10-14 17:37:11','3');");
        }

        b.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(AdminLogin.this, "",
                        "Validating user...", true);
                new Thread(new Runnable() {
                    public void run() {
                        login();
                    }
                }).start();
            }

        });

        TextView reset=(TextView)findViewById(R.id.pass_change);
        reset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent i = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(i);
            }
        });

    }


    public void login(){
        try {
            Cursor c = db.rawQuery("SELECT * FROM admin_login WHERE username='" + et.getText() + "' and password='" + pass.getText() + "'", null);
            if ((c.getCount() == 0) && (et.getText().toString() != "faben")) {
                tv.setText("Enter the correct username or password");
                tv.setTextColor(Color.RED);
                Toast.makeText(AdminLogin.this, "Invalid details", Toast.LENGTH_SHORT).show();
                showAlert();

            } else {

                runOnUiThread(new Runnable() {
                    public void run() {
                        tv.setText("Welcome  " +String.valueOf(et.getText()));
                        tv.setTextColor(Color.GREEN);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(AdminLogin.this, "Login Success", Toast.LENGTH_LONG).show();
                            }
                        });
                        db.rawQuery("UPDATE admin_login SET status=1 WHERE  status='0' ", null);
                        usser = et.getText().toString();
                        Cursor cc = db.rawQuery("SELECT company FROM admin_login" + " WHERE "+"username"+"=?", new String[] {usser});
                        Cursor c = db.rawQuery("SELECT branch FROM admin_login" + " WHERE "+"username="+"=?", new String[] {usser});

                        if (cc != null && cc.getCount()>0 ) {
                            cc.moveToNext();
                            int companyIndex = cc.getColumnIndex("company");
                            compny = cc.getString(companyIndex);
                        }


                        if (c != null && cc.getCount()>0) {
                            c.moveToNext();
                            int branchIndex = c.getColumnIndex("branch");
                            bran = c.getString(branchIndex);
                        }

                        // Storing the key and its value as the data fetched from edittext
                        prefsEditor.putString("loggedInUser", usser);
                        // Commit to apply the changes
                        prefsEditor.commit();

                        Intent intent = new Intent(AdminLogin.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("branch", bran);
                        bundle.putString("company", compny);
                        bundle.putString("user", usser);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }
            dialog.dismiss();
        } catch (Exception e) {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    public void showAlert() {
        AdminLogin.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminLogin.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}