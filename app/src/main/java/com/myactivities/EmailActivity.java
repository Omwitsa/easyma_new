package com.myactivities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EmailActivity extends AppCompatActivity {
    EditText editTextTo, editTextSubject, editTextMessage;
    Button send;

    private  String subject,message,to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Send your views or comments");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        editTextTo = (EditText) findViewById(R.id.editText1);
        editTextSubject = (EditText) findViewById(R.id.editText2);
        editTextMessage = (EditText) findViewById(R.id.editText3);

        send = (Button) findViewById(R.id.button1);
        editTextTo.setText("kemboihillz@gmail.com");

        send.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
               to=send.getText().toString();

                if (!validate()) {
                    return;
                } else {
                    send.setEnabled(false);

                    Intent email = new Intent(android.content.Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
                    email.putExtra(Intent.EXTRA_SUBJECT, subject);
                    email.putExtra(Intent.EXTRA_TEXT, message);

                    //need this to prompts email client only
                    email.setType("message/rfc822");
                    startActivity(Intent.createChooser(email, "Choose an Email client :"));

                   // sentEmail(mContext,to,subject,message);
                }

            }

        });
    }


    public boolean validate() {
        boolean valid = true;

        subject = editTextSubject.getText().toString();
        message = editTextMessage.getText().toString();


        if (subject.isEmpty()) {
            editTextSubject.setError("Please Enter message Subject");
            editTextSubject.requestFocus();
            valid = false;
        } else {
            editTextSubject.setError(null);

            if (message.isEmpty()) {
                editTextMessage.requestFocus();
                editTextMessage.setError("Please Enter message Body");
                valid = false;
            } else {
                editTextMessage.setError(null);
            }
        }
        return valid;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
