package com.myactivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;

import java.util.List;

public class SuperAdminLogin extends Activity implements OnClickListener {

	Button b,quitBtn;
	EditText et,pass;
	TextView tv;
	HttpPost httppost;
	StringBuffer buffer;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	ProgressDialog dialog = null;
	SQLiteDatabase db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_login);
        
        b = (Button)findViewById(R.id.Button012);
        et = (EditText)findViewById(R.id.username22);
        pass= (EditText)findViewById(R.id.password22);
		tv=(TextView)findViewById(R.id.tv223);
        //Button btnRegister = (Button) findViewById(R.id.Button01);
        
        db=openOrCreateDatabase("loginDB", Context.MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS SuperAdminLogin(username VARCHAR,password VARCHAR);");
		Cursor c=db.rawQuery("SELECT * FROM SuperAdminLogin", null);
		if(c.getCount()==0)
		{
			db.execSQL("INSERT INTO SuperAdminLogin VALUES('ADMIN','admin.123');");
		}
		
		 
        b.setOnClickListener(new OnClickListener() {
        	
			@Override
			public void onClick(View v) {
				dialog = ProgressDialog.show(SuperAdminLogin.this, "",
                        "Validating user...", true);
				/*Intent i = new Intent(getApplicationContext(), EasyMaActivty.class);
                startActivity(i);*/
				 new Thread(new Runnable() {
					    public void run() {
					    	login();					      
					    }
					  }).start();				
			}
			
		});

    }
	public void clear()
	{
		et.setText("");
		pass.setText("");
	}
	public void login(){
		
		try{	
		Cursor c=db.rawQuery("SELECT * FROM SuperAdminLogin WHERE username='"+et.getText()+"' and password='"+pass.getText()+"'", null);
		
		if(c.getCount()==0)
		{
			tv.setText("Enter the correct username or password");
			tv.setTextColor(Color.RED);
			Toast.makeText(SuperAdminLogin.this,"Invalid details", Toast.LENGTH_SHORT).show();
			showAlert();
		}
		else{ runOnUiThread(new Runnable() {
		    public void run() {
				tv.setText("Welcome:" + et.getText());
				tv.setTextColor(Color.GREEN);
				runOnUiThread(new Runnable() {
				    public void run() {
				    	Toast.makeText(SuperAdminLogin.this,"Login Success", Toast.LENGTH_SHORT).show();
				    }
				});
				Intent intent = new Intent(SuperAdminLogin.this, SuperAdminActivity.class);// New activity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
		    }
		});
		}

		dialog.dismiss();
		}catch(Exception e){
			dialog.dismiss();
			System.out.println("Exception : " + e.getMessage());
		}
	}
	
	public void showAlert(){
		SuperAdminLogin.this.runOnUiThread(new Runnable() {
		    public void run() {
		    	AlertDialog.Builder builder = new AlertDialog.Builder(SuperAdminLogin.this);
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}