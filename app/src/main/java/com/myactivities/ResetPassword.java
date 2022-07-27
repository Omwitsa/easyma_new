package com.myactivities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResetPassword extends AppCompatActivity {

    private ImageButton button;
    private TextView title,feedback;
    @Bind(R.id.input_email)EditText _emailText;
    @Bind(R.id.btn_reset)Button _reset;
    @Bind(R.id.label)
    TextInputLayout label;
    protected void onCreate(Bundle savedInsatanceState) {
        super.onCreate(savedInsatanceState);
        setContentView(R.layout.activity_resetpassword);
        ButterKnife.bind(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Reset Password");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    private void resetCheck() {

        if (!validate()) {
            return;
        }
        _reset.setEnabled(false);

        String email = _emailText.getText().toString();
        reset(email);

    }

    private void reset(String email) {

    }

    private boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            _emailText.requestFocus();
            valid = false;
        } else {
            _emailText.setError(null);
        }
        return valid;
    }
}
