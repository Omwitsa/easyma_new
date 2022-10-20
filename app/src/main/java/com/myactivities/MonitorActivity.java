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
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import util.AppConstants;
import util.DateUtil;

import static com.myactivities.MainActivity.branch;
import static com.myactivities.MainActivity.logedInUser;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;


public class MonitorActivity extends MyActivity implements OnItemSelectedListener {
    ApiInterface apiService;
    private static final int REQUEST_DISCOVERY = 0x1;
    private static final String CollectionDB = null;
    private final String TAG = "MonitorActivity";
    private Handler _handler = new Handler();
    private final int maxlength = 28;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;

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

    SharedPreferences sharedPreferences;
    public static String branchhh,userrrrr,company;
    private static BluetoothSocket mSocket;
    BluetoothDevice selectDevice = null;
    private String _qty = "0";
    private String _container = "0";

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sTextView = (EditText) findViewById(R.id.sTextView);
        TextView = (EditText) findViewById(R.id.TextView);
        sno = (EditText) findViewById(R.id.sno);
        TextViewc  = findViewById(R.id.TextViewc);
        TextViewf  = findViewById(R.id.TextViewf);
        TextViewc.setText("0");

        Button btnRegister = (Button) findViewById(R.id.save);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextView.getText().toString().trim().length() == 0 ||
                        TextViewf.getText().toString().trim().length() == 0 ||
                        sno.getText().toString().trim().length() == 0) {
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

//        final Spinner spinner1 = (Spinner) findViewById(R.id.branch);
//        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
//                R.array.branch_array, android.R.layout.simple_spinner_item);
//        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner1.setAdapter(adapter1);
//
//        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                fbranch = spinner1.getSelectedItem().toString();
//                CONTAINER_WEIGHT = Double.valueOf(fbranch) ;
//                TextViewc.setText(String.valueOf(CONTAINER_WEIGHT));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

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

        //fetch the supply number from the register farmer activity
        //  sno.setText("gotten one");

        suppp_no = sno.getText().toString().trim();
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CollectionDB(supplier VARCHAR,quantity VARCHAR,branch VARCHAR, date DATETIME, auditId VARCHAR,status VARCHAR, type VARCHAR, saccoCode VARCHAR, transdate DATETIME, printed VARCHAR);");

//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        BluetoothDevice finalDevice = this.getIntent().getParcelableExtra(
//                BluetoothDevice.EXTRA_DEVICE);
//        //Controller app = (Controller) getApplicationContext();
//        Controller app = new Controller();
//        device = app.getDevice();
//        Log.d(TAG, "test1");
//        if (finalDevice == null) {
//            if (device == null) {
//                Log.d(TAG, "test2");
//                Intent intent = new Intent(this, SearchDeviceActivity.class);
//                startActivity(intent);
//                finish();
//                return;
//            }
//            Log.d(TAG, "test4");
//        } else if (finalDevice != null) {
//            Log.d(TAG, "test3");
//            app.setDevice(finalDevice);
//            device = app.getDevice();
//        }
//        new Thread() {
//            public void run() {
//                connect(device);
//            }
//        }.start();
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

    public void onButtonClickclear(View view) throws IOException {
        hexString = new StringBuffer();
        sTextView.setText(hexString.toString());
    }

    /* after select, connect to device */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_DISCOVERY) {
            finish();
            return;
        }
        if (resultCode != RESULT_OK) {
            finish();
            return;
        }
        final BluetoothDevice device = data
                .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        new Thread() {
            public void run() {
                connect(device);
            }

            ;
        }.start();
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            Log.e(TAG, ">>", e);
        }
    }

    protected void connect(BluetoothDevice device) {
        try {
            Log.d(TAG, "Searching");
            // Create a Socket connection: need the server's UUID number of
            // registered
            Method m = device.getClass().getMethod("createRfcommSocket",
                    new Class[]{int.class});
            socket = (BluetoothSocket) m.invoke(device, 1);
            socket.connect();
            Log.d(TAG, ">>Client connectted");
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int read = -1;
            final byte[] bytes = new byte[2048];
            while (true) {
                synchronized (obj1) {
                    read = inputStream.read(bytes);
                    Log.d(TAG, "read:" + read);
                    if (read > 0) {
                        final int count = read;
                        String str = SamplesUtils.byteToHex(bytes, count);
//						Log.d(TAG, "test1:" + str);

                        String hex = hexString.toString();
                        if (hex == "") {
                            hexString.append(",");
                        } else {
                            if (hex.lastIndexOf("GS,") < hex.lastIndexOf(" ")) {
                                hexString.append("\n, ");
                            }
                        }
                        hexString.append(str);
                        hex = hexString.toString();
//						Log.d(TAG, "test2:" + hex);
                        if (hex.length() > maxlength) {
                            try {
                                hex = hex.substring(hex.length() - maxlength,
                                        hex.length());
                                hex = hex.substring(hex.indexOf(" "));
                                hex = "GS," + hex;
                                hexString = new StringBuffer();
                                hexString.append(hex);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "e", e);
                            }
                        }
                        //check it with albert for consistent flow of data
                        _handler.post(new Runnable() {
                            public void run() {
                                String txt = sTextView.toString();
                                String value = txt;
                                bb=TextView.getText().toString();
                                bb = bufferStrToHex(hexString.toString(), false).trim().replaceAll("[^0-9.]", "");
                                bb = bb.trim();
                                if (bb.length()==0 )

                                {
                                    bb="0";
                                    TextView.setText("0");
                                }
                                else {

                                    if (bb.isEmpty()==false) {
                                        TextView.setText((bb.toString()));
                                        String bb1 = bb.replace("W", "");
                                        bb1 = bb.replace("A", "");
                                        bb1 = bb.replace("C", "");
                                        bb1 = bb.replace("B", "");
                                        bb1 = bb.replace("@", "");
                                        bb1 = bb.replace("?", "");
                                        bb1 = bb.replace("W", "");
                                        bb1 = bb1.trim();
                                        float quantity =0;
                                        if (bb1 !="") {
                                            try {
                                                String qty=String.format("%.2f",Float.valueOf(bb1));
                                                quantity = Float.parseFloat(qty);
                                            }
                                            catch(Exception e)
                                            {
                                                Log.e(TAG, ">>", e);
                                                Toast.makeText(getBaseContext(),
                                                        getResources().getString(R.string.ioexception),
                                                        Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        }
                                        else {
                                            quantity =0;
                                        }
                                        double containerWeight = 0;
                                        if (!TextUtils.isEmpty(TextViewc.getText().toString())) {
                                            containerWeight = Double.parseDouble(TextViewc.getText().toString().trim());
                                        }

                                        double finalWeight = quantity - containerWeight;
                                        TextViewf.setText(String.valueOf(finalWeight));
                                        calculate();
                                    }
                                }

                            }

                        });


                    }
                    //TextViewc.setText(String.valueOf(CONTAINER_WEIGHT));

                }
            }

        } catch (Exception e) {
            Log.e(TAG, ">>", e);
            Toast.makeText(getBaseContext(),
                    getResources().getString(R.string.ioexception),
                    Toast.LENGTH_SHORT).show();
            return;
        } finally {
            if (socket != null) {
                try {
                    Log.d(TAG, ">>Client Socket Close");
                    socket.close();
                    socket = null;
                    // this.finish();
                    return;
                } catch (IOException e) {
                    Log.e(TAG, ">>", e);
                }
            }
        }
    }


    private void calculate() {
        DecimalFormat nf = new DecimalFormat(".#");
        float quantity = Float.parseFloat(bb.toString());
        float containerWeight =Float.parseFloat(fbranch) ;
        if (!TextUtils.isEmpty(TextViewc.getText().toString())) {
            containerWeight = Float.parseFloat(TextViewc.getText().toString().trim());
        }

        float finalWeight =  quantity - containerWeight;

        DecimalFormat df = new DecimalFormat("#.#");
        df.setRoundingMode(RoundingMode.FLOOR);
        double result = Double.parseDouble(df.format(finalWeight));
        TextViewf.setText(nf.format(result));
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
        AlertDialog.Builder build = new AlertDialog.Builder(com.myactivities.MonitorActivity.this);
        build.setTitle("Confirmation :SNo=" + sno.getText() + " and Quantity =" + TextViewf.getText().toString() + "  using:Main");
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

                        String loggenInUser = sharedPreferences.getString("loggedInUser", "");
                        db.execSQL("INSERT INTO CollectionDB  VALUES('" + sno.getText() + "', '" + TextView.getText() + "', '" + branchhh +"', '" + date1 +"','" + loggenInUser + "','0','" + product + "','" + AppConstants.SACCO_CODE + "', '"+date_print +"', '0');");
                        showMessage("Success", "Record added");
                        TextViewf.setText("0");

                        Intent i = new Intent(getApplicationContext(), MainPrintActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("qty", TextViewf.getText().toString());
                        bundle.putString("sno", sno.getText().toString());
                        bundle.putString("shift", shift);
                        bundle.putString("product", product);
                        i.putExtras(bundle);
                        startActivity(i);
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

    public void loadUpdate() {


    }


    //my code......
    public void connectDevice() {
        if (mSocket == null) {
            //Log.d(TAG, "Socket is null");
            UUID SPP_UUID = UUID
                    .fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
            @SuppressLint("MissingPermission") Set<BluetoothDevice> bondedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
            //Log.d(TAG, "Size: " + bondedDevices.size());
            /**
             * Select your divice form paired devices
             */
            for (BluetoothDevice bluetoothDevice : bondedDevices) {
                selectDevice = bluetoothDevice;
                //Log.d(TAG, bluetoothDevice.getName()+" "+bluetoothDevice.getAddress());
            }

            if (selectDevice.getBondState() == selectDevice.BOND_BONDED) {
                //Log.d(TAG, selectDevice.getName());
                try {
                    mSocket = selectDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    //Log.d(TAG, "socket not created");
                    e1.printStackTrace();
                }
                try {
                    mSocket.connect();
                } catch (IOException e) {
                    try {
                        mSocket.close();
                        //Log.d(TAG, "Cannot connect");
                    } catch (IOException e1) {
                        //Log.d(TAG, "Socket not closed");
                        e1.printStackTrace();
                    }
                }
            }
        }
    }


    public String bufferStrToHex(String buffer, boolean flag) {
        String all = buffer;
        StringBuffer sb = new StringBuffer();
        String[] ones = all.split("GS, ");
        for (int i = 0; i < ones.length; i++) {
            if (ones[i] != "") {
                String[] twos = ones[i].split("  ");
                for (int j = 0; j < twos.length; j++) {
                    if (twos[j] != "") {
                        if (flag) {
                            sb.append(SamplesUtils.stringToHex(twos[j]));
                        } else {
                            sb.append(SamplesUtils.hexToString(twos[j]));
                        }
                        if (j != twos.length - 1) {
                            if (sb.toString() != "") {
                                sb.append("\n");
                            }
                            sb.append("GS, ");
                        }
                    }
                }
                if (i != ones.length - 1) {
                    if (sb.toString() != "") {
                        sb.append("\n");
                    }
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                               long arg3) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}


