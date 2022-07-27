package com.myactivities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static network.Urls.BASE_URL;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import Rest.ApiClient;
import Rest.ApiInterface;
import model.Response;
import model.Supplier;
import model.SynchData;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterFarmer extends AppCompatActivity implements OnClickListener {

    private EditText editfirst, editid, editphone, email_address, supply_number, coporate_branch,
            editbank, editbbranch, branch_code, editaccount,
            homeaddress, town, district, division, editlocation, village,
            et_dob, et_reg_date, loan_describe;

    private CheckedTextView is_trader, has_loan;
    ApiInterface apiService;

    private Button mSubmit;

    //RadioGroup gender;
    // RadioButton radioButton1;

    private ProgressDialog pDialog;
    ProgressDialog dialog = null;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    private static final String POST_COMMENT_URL = BASE_URL + "webservice/users/registerSupplier";

    //testing from a real server:
    //private static final String POST_COMMENT_URL = "http://www.mybringback.com/webservice/addcomment.php";

    //ids
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private String post_fullname, post_id, post_phone, post_email_address, post_supply_number, post_coporate_branch, post_bankname,
            post_bank_branch, post_branch_code, post_account, post_home_address, post_town, post_district, post_division, post_location, post_village,
            dob, reg_dates, sex, s_trader, s_has_loan, client_phone;


    RadioGroup gender;
    RadioButton radioButton1;

    public String userr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_farmer);

        editfirst = (EditText) findViewById(R.id.editfirst);
        editid = (EditText) findViewById(R.id.editid);
        editphone = (EditText) findViewById(R.id.editphone);
        email_address = (EditText) findViewById(R.id.email_address);
        supply_number = (EditText) findViewById(R.id.supply_number);
        coporate_branch = (EditText) findViewById(R.id.coporate_branch);

        editbbranch = (EditText) findViewById(R.id.editbbranch);
        homeaddress = (EditText) findViewById(R.id.eHomeaddress);
        town = (EditText) findViewById(R.id.town);
        district = (EditText) findViewById(R.id.district);
        division = (EditText) findViewById(R.id.division);
        editlocation = (EditText) findViewById(R.id.editlocation);
        village = (EditText) findViewById(R.id.village);
        // radioButton1 = (RadioButton) findViewById(R.id.gender);

        et_dob = (EditText) findViewById(R.id.dob);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        et_dob.setText(sdf.format(new Date()));

        et_reg_date = (EditText) findViewById(R.id.reg_date);
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        et_reg_date.setText(sdf1.format(new Date()));

        fetchSupplierNo();

        et_dob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();

                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(RegisterFarmer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                + "-" + String.valueOf(dayOfMonth);
                        et_dob.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });


        et_reg_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                final Calendar calendar = Calendar.getInstance();
                int yy = calendar.get(Calendar.YEAR);
                int mm = calendar.get(Calendar.MONTH);
                int dd = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePicker = new DatePickerDialog(RegisterFarmer.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear + 1)
                                + "-" + String.valueOf(dayOfMonth);
                        et_reg_date.setText(date);
                    }
                }, yy, mm, dd);
                datePicker.show();
            }
        });
//
//
//        is_trader.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (is_trader.isChecked()) {
//// set cheek mark drawable and set checked property to false
//                    is_trader.setCheckMarkDrawable(R.drawable.checked);
//                    is_trader.setChecked(false);
//                    loan_describe.setVisibility(View.VISIBLE);
//                } else {
//// set cheek mark drawable and set checked property to true
//                    is_trader.setCheckMarkDrawable(R.drawable.ic_checked);
//                    is_trader.setChecked(true);
//                }
//                /*Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();*/
//            }
//        });
//
//        has_loan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (has_loan.isChecked()) {
//// set cheek mark drawable and set checked property to false
//                    has_loan.setCheckMarkDrawable(R.drawable.checked);
//                    has_loan.setChecked(false);
//                } else {
//// set cheek mark drawable and set checked property to true
//                    has_loan.setCheckMarkDrawable(R.drawable.ic_checked);
//                    has_loan.setChecked(true);
//                }
//                /*Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();*/
//            }
//
//        });
//

        mSubmit = (Button) findViewById(R.id.btnreg);
        mSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFarmerDetails();
            }
        });

        TextView link_login = (TextView) findViewById(R.id.link_login);
        link_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent collectionIntent = new Intent(getApplicationContext(), CollectionActivity.class);
                startActivity(collectionIntent);
            }
        });

        Bundle bundle = getIntent().getExtras();
        //Extract the data…
        userr = bundle.getString("phone");


    }

    private void fetchSupplierNo() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call= apiService.getSupplierNo();
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                model.Response responseData = response.body();
                assert responseData != null;
                String sno = responseData.getData();
                supply_number.setText(sno);
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.synch_farmer, menu);
        return true;
    }
    //to be used later maybe

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_synch_users:
                try {

                    dialog = ProgressDialog.show(RegisterFarmer.this, "",
                            " Submitting new farmer,please wait...", true);
                    dialog.setCancelable(false);
                    Toast.makeText(RegisterFarmer.this, "No new data found.", Toast.LENGTH_LONG).show();
                    //method to fetch from phone memory loading soon
                    //ConnectToDatabase();
                    dialog.dismiss();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }

        // return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        try {
            checkConnection();
        } catch (Exception e) {
            e.printStackTrace();

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

    public void checkConnection() {

        if (!validate()) {
            return;
        } else {
            if (isOnline()) {
                mSubmit.setEnabled(false);

//                new PostComment().execute();
            } else {
                Toast.makeText(RegisterFarmer.this, "Check your Internet Connection and try again", Toast.LENGTH_LONG).show();
                mSubmit.setEnabled(true);
                pDialog.dismiss();

            }

        }
    }

    private void saveFarmerDetails(){
        int sno = Integer.parseInt(supply_number.getText().toString());
        String names = editfirst.getText().toString();
        String idNo = editid.getText().toString();
        String phoneNo = editphone.getText().toString();
        String email = email_address.getText().toString();
        String branch = coporate_branch.getText().toString();
        String BBranch = editbbranch.getText().toString();
        String address = homeaddress.getText().toString();
        String town1 = town.getText().toString();
        String district1 = district.getText().toString();
        String division1 = division.getText().toString();
        String location = editlocation.getText().toString();
        String village1 = village.getText().toString();
        String strDOB = et_dob.getText().toString();
        String strRegDate = et_reg_date.getText().toString();
        Date dob = null;
        Date regdate = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
        try {
            dob = dateFormat.parse(strDOB);
            regdate = dateFormat.parse(strRegDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Supplier supplier = new Supplier(sno, names, idNo, phoneNo, email, branch, BBranch, address, town1, district1,
                division1, location, village1, dob, regdate);
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Response> call= apiService.registerSupplier(supplier);
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                model.Response responseData = response.body();
                assert responseData != null;
                String status = responseData.getMessage();
                Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "An Error occurred", Toast.LENGTH_LONG).show();
            }
        });
    }

    class PostComment extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegisterFarmer.this);
            pDialog.setMessage("Posting Clients...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {
            // post_branch_code,
            // TODO Auto-generated method stub
            // Check for success tag
            int success;
            //

            validate();


            //We need to change this:
            //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddComment.this);
            //String post_username = sp.getString("username", "anon");

            try {

                // int selectedId = gender.getCheckedRadioButtonId();

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("Names", post_fullname));
                params.add(new BasicNameValuePair("IdNo", post_id));
                params.add(new BasicNameValuePair("PhoneNo", post_phone));
                params.add(new BasicNameValuePair("Email", post_email_address));
                params.add(new BasicNameValuePair("SNo", post_supply_number));
                params.add(new BasicNameValuePair("Branch", post_coporate_branch));

                params.add(new BasicNameValuePair("bankname", post_bankname));
                params.add(new BasicNameValuePair("BBranch", post_bank_branch));
                params.add(new BasicNameValuePair("bankbranchcode", post_branch_code));
                params.add(new BasicNameValuePair("bankaccountno", post_account));

                params.add(new BasicNameValuePair("Address", post_home_address));
                params.add(new BasicNameValuePair("Town", post_town));
                params.add(new BasicNameValuePair("District", post_district));
                params.add(new BasicNameValuePair("Division", post_division));
                params.add(new BasicNameValuePair("Location", post_location));
                params.add(new BasicNameValuePair("Village", post_village));

                params.add(new BasicNameValuePair("gender", sex));
                params.add(new BasicNameValuePair("dob", dob));
                params.add(new BasicNameValuePair("Regdate", reg_dates));
                params.add(new BasicNameValuePair("is_trader", s_trader));
                params.add(new BasicNameValuePair("has_loan", s_has_loan));

                params.add(new BasicNameValuePair("client_phone", client_phone));


                //Posting user data to script
                JSONObject json = jsonParser.makeHttpRequest(
                        POST_COMMENT_URL, "POST", params);

                // full json response
                Log.d("Post Comment attempt", json.toString());

                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    Log.d("Comment Added!", json.toString());
                    finish();
                    return json.getString(TAG_MESSAGE);
                } else {
                    Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                    return json.getString(TAG_MESSAGE);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once product deleted
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(RegisterFarmer.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean validate() {
        boolean valid = true;

        post_fullname = editfirst.getText().toString();
        post_id = editid.getText().toString();
        post_phone = editphone.getText().toString();
        post_email_address = email_address.getText().toString();
        post_supply_number = supply_number.getText().toString();
        post_coporate_branch = coporate_branch.getText().toString();

        post_bankname = editbank.getText().toString();
        post_bank_branch = editbbranch.getText().toString();
        post_branch_code = branch_code.getText().toString();
        post_account = editaccount.getText().toString();


        post_home_address = homeaddress.getText().toString();
        post_town = town.getText().toString();
        post_district = district.getText().toString();
        post_division = division.getText().toString();
        post_location = editlocation.getText().toString();
        post_village = village.getText().toString();
        final Pattern pattern = Pattern.compile("07(\\d){8}");

        int selectedId1 = gender.getCheckedRadioButtonId();
        radioButton1 = (RadioButton) findViewById(selectedId1);
        sex = radioButton1.getText().toString();

        dob = et_dob.getText().toString();
        reg_dates = et_reg_date.getText().toString();
        
        client_phone=userr;
        if (is_trader.isChecked()) {
            s_trader = "Yes";
        } else {
            s_trader = "No";
        }

        if (has_loan.isChecked()) {
            s_has_loan = "Yes";
        } else {
            s_has_loan = "No";
        }


        if (post_fullname.isEmpty() || post_fullname.length() < 3) {
            editfirst.requestFocus();
            editfirst.setError("at least 3 characters");
            valid = false;
        } else {
            editfirst.setError(null);

            if (post_supply_number.isEmpty() || post_supply_number.length() < 1) {
                supply_number.requestFocus();
                supply_number.setError("enter a valid supply number");
                valid = false;
            } else {
                supply_number.setError(null);
                if (post_phone.isEmpty() || !pattern.matcher(post_phone).matches()) {
                    editphone.setError("enter a valid phone number");
                    editphone.requestFocus();
                    valid = false;
                } else {
                    editphone.setError(null);


                    if (post_email_address.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(post_email_address).matches()) {
                        email_address.setError("enter a valid email address");
                        email_address.requestFocus();
                        valid = false;
                    } else {
                        email_address.setError(null);


                        if (post_id.isEmpty() || post_id.length() < 1) {
                            editid.setError("enter a valid id number");
                            editid.requestFocus();
                            valid = false;
                        } else {
                            editid.setError(null);

                            if (post_home_address.isEmpty() || post_home_address.length() < 1) {
                                homeaddress.setError("enter a valid value");
                                homeaddress.requestFocus();
                                valid = false;
                            } else {
                                homeaddress.setError(null);

                                if (post_town.isEmpty() || post_town.length() < 1) {
                                    town.setError("enter a valid town name");
                                    town.requestFocus();
                                    valid = false;
                                } else {
                                    town.setError(null);

                                    if (post_district.isEmpty() || post_district.length() < 1) {
                                        district.setError("enter a valid district name");
                                        district.requestFocus();
                                        valid = false;
                                    } else {
                                        district.setError(null);

                                        if (post_division.isEmpty() || post_division.length() < 1) {
                                            division.setError("enter a valid division name");
                                            division.requestFocus();
                                            valid = false;
                                        } else {
                                            division.setError(null);

                                            if (post_location.isEmpty() || post_location.length() < 1) {
                                                editlocation.setError("enter a valid location name");
                                                editlocation.requestFocus();
                                                valid = false;
                                            } else {
                                                editlocation.setError(null);

                                                if (post_village.isEmpty() || post_village.length() < 1) {
                                                    village.setError("enter a valid village name");
                                                    village.requestFocus();
                                                    valid = false;
                                                } else {
                                                    village.setError(null);

                                                    if (post_bankname.isEmpty() || post_bankname.length() < 1) {
                                                        editbank.setError("enter a valid bank name");
                                                        editbank.requestFocus();
                                                        valid = false;
                                                    } else {
                                                        editbank.setError(null);

                                                        if (post_bank_branch.isEmpty() || post_bank_branch.length() < 1) {
                                                            editbbranch.setError("enter a valid bank branch");
                                                            editbbranch.requestFocus();
                                                            valid = false;
                                                        } else {
                                                            editbbranch.setError(null);

                                                            if (post_coporate_branch.isEmpty() || post_coporate_branch.length() < 1) {
                                                                coporate_branch.setError("enter a valid value");
                                                                coporate_branch.requestFocus();
                                                                valid = false;
                                                            } else {
                                                                coporate_branch.setError(null);
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return valid;
    }


}
