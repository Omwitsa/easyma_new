package com.myactivities;


//import com.cheress.R;

import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import Pockdata.PocketPos;
import Rest.ApiClient;
import Rest.ApiInterface;
import model.ProductResp;
import model.SynchData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.AppConstants;
import util.DateUtil;
import util.FontDefine;
import util.Printer;

import static com.myactivities.DailyReportsActivity.Transsdate;
import static network.Urls.BASE_URL;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpResponse;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost;


public class MainPrintActivity extends MyActivity {
    private Button mEnableBtn, mPrintReceiptBtn;
    private static Button mConnectBtn;
    private Spinner mDeviceSp;
    ApiInterface apiService;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    SharedPreferences sharedPreferences;
    TextView tv;
    static SQLiteDatabase db;

    private ProgressDialog mProgressDlg, mConnectingDlg;

    private BluetoothAdapter mBluetoothAdapter;

    private P25Connector mConnector;
    String qty1 = "";// =getIntent().getStringExtra("qty").toString();
    String sno1 = "";
    String pin1 = "";
    String product = "";

    MainActivity mainActivity;
    public static String company, userrrrr, branchhh;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    String comp;

    private static BluetoothSocket mSocket;
    BluetoothDevice selectDevice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.printmain);
        sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Print Report");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mEnableBtn = (Button) findViewById(R.id.btn_enable);
        mPrintReceiptBtn = (Button) findViewById(R.id.btn_print_receipt);
        mDeviceSp = (Spinner) findViewById(R.id.sp_device);


        tv = (TextView) findViewById(R.id.tv);
        sno1 = "";
        product = "";
        qty1 = "2342";//getIntent().getStringExtra("qty").toString();
         // "234234"
        pin1 = "A345455345G";// getIntent().getStringExtra("pin").toString();
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                showDisabled();
            } else {
                showEnabled();

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices != null) {
                    mDeviceList.addAll(pairedDevices);
                    updateDeviceList();
                }
            }

            mProgressDlg = new ProgressDialog(this);
            mProgressDlg.setMessage("Scanning...");
            mProgressDlg.setCancelable(false);
            mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mBluetoothAdapter.cancelDiscovery();
                }
            });

            mConnectingDlg = new ProgressDialog(this);
            mConnectingDlg.setMessage("Connecting...");
            mConnectingDlg.setCancelable(false);
            mConnector = new P25Connector(new P25Connector.P25ConnectionListener() {
                @Override
                public void onStartConnecting() {
                    mConnectingDlg.show();
                }

                @Override
                public void onConnectionSuccess() {
                    mConnectingDlg.dismiss();
                    showConnected();
                }

                @Override
                public void onConnectionFailed(String error) {
                    mConnectingDlg.dismiss();
                }

                @Override
                public void onConnectionCancelled() {
                    mConnectingDlg.dismiss();
                }

                @Override
                public void onDisconnected() {
                    showDisonnected();
                }
            });

            //enable bluetooth
            mEnableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1000);
                }
            });

            //connect/disconnect
            mConnectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    connect();
                }
            });

            mPrintReceiptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    printStruk();
                }
            });
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        if (mConnector != null) {
            try {
                mConnector.disconnect();
            } catch (P25ConnectionException e) {
                e.printStackTrace();
            }
        }

        super.onPause();
    }

    private String[] getArray(ArrayList<BluetoothDevice> data) {
        String[] list = new String[0];
        if (data == null) return list;
        int size = data.size();
        list = new String[size];
        for (int i = 0; i < size; i++) {
            list[i] = data.get(i).getName();
        }
        return list;
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateDeviceList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item2, getArray(mDeviceList));
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        mDeviceSp.setAdapter(adapter);
        mDeviceSp.setSelection(0);
    }

    private void showDisabled() {
        showToast("Bluetooth disabled");
        mEnableBtn.setVisibility(View.VISIBLE);
        mConnectBtn.setVisibility(View.GONE);
        mDeviceSp.setVisibility(View.GONE);
    }

    private void showEnabled() {
        showToast("Bluetooth enabled");
        mEnableBtn.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
        mDeviceSp.setVisibility(View.VISIBLE);
    }

    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");
        mConnectBtn.setEnabled(false);
        mPrintReceiptBtn.setEnabled(false);
        mDeviceSp.setEnabled(false);
    }

    private void showConnected() {
        showToast("Connected");
        mConnectBtn.setText("Disconnect");
        mPrintReceiptBtn.setEnabled(true);
        mDeviceSp.setEnabled(false);
    }

    private void showDisonnected() {
        showToast("Disconnected");
        mConnectBtn.setText("Connect");
        mPrintReceiptBtn.setEnabled(false);
        mDeviceSp.setEnabled(true);
    }

    private void connect() {
        if (mDeviceList == null || mDeviceList.size() == 0) {

            return;
        }

        BluetoothDevice device = mDeviceList.get(mDeviceSp.getSelectedItemPosition());
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                createBond(device);
            } catch (Exception e) {
                showToast("Failed to pair device");
                return;
            }
        }

        try {
            if (!mConnector.isConnected()) {
                mConnector.connect(device);
            } else {
                mConnector.disconnect();

                showDisonnected();
            }
        } catch (P25ConnectionException e) {
            e.printStackTrace();
        }


        //my code
    }

    private void createBond(BluetoothDevice device) throws Exception {

        try {
            Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par = {};
            Method method = cl.getMethod("createBond", par);
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void sendData(byte[] bytes) {
        try {
            mConnector.sendData(bytes);
        } catch (P25ConnectionException e) {
            e.printStackTrace();
        }
    }

    public void fetchProducts() {
        apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductResp> call= apiService.getItems(AppConstants.SACCO_CODE);
        call.enqueue(new Callback<ProductResp>() {
            @Override
            public void onResponse(Call<ProductResp> call, Response<ProductResp> response) {
                model.ProductResp responseData = response.body();
                if (responseData.isSuccess()){
                    ArrayList<String> productList2 = new ArrayList<String>(responseData.getProducts());
                    ArrayList<String> arrProduct = new ArrayList<String>();
                    for (String product: productList2) {
                        arrProduct.add("('"+product+"')");
                    }

                    String strProducts = TextUtils.join(", ", arrProduct);
                    db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS products(type VARCHAR);");
                    db.execSQL("DELETE FROM products");
                    db.execSQL("INSERT INTO products VALUES "+strProducts+";");
                }
            }

            @Override
            public void onFailure(Call<ProductResp> call, Throwable t) {
                showToast("Sorry, An error occurred");
            }
        });
    }

    public void autosynch() {
        Cursor c = db.rawQuery("SELECT * FROM CollectionDB WHERE status='0'", null);
        if (c.getCount() == 0) {
            showMessage("Collection Message", "No new collection found");
            return;
        } else {

            dialog = ProgressDialog.show(MainPrintActivity.this, "",
                    " Detecting new collection, please wait...", true);
           dialog.setCancelable(true);

            new Thread(new Runnable() {
                public void run() {
                    sendToDB();

                    //MainPrintActivity.this.finish();

                    //showAlert();
                    dialog.dismiss();

                }
            }).start();
        }
    }

    public void sendToDB() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        String setDate = sdf.format(Calendar.getInstance().getTime());
        if(Transsdate != null){
            setDate = Transsdate.getText() == null ? setDate : Transsdate.getText().toString();
        }
        setDate =  setDate.trim();
        Cursor c = db.rawQuery("SELECT * FROM CollectionDB WHERE status='0'", null);
        if (c.getCount() == 0) {
            showMessage("Collection Message", "No new collection found");
            return;
        }
        String sup = null;
        String qty = null;
        String branchhh = null;
        String dates = null;
        String auditid = null;
        String product = null;
        String saccoCode = null;

        try {
            while (c.moveToNext()) {
                sup = c.getString(0);
                qty = c.getString(1);
                branchhh = c.getString(2);
                dates = c.getString(3);
                auditid = c.getString(4);
                product=c.getString(6);
                saccoCode=c.getString(7);

                SynchData synchData = new SynchData(sup, qty, branchhh, dates, auditid, product, saccoCode);
                apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<model.Response>call= apiService.login(synchData);
                call.enqueue(new Callback<model.Response>() {
                    @Override
                    public void onResponse(Call<model.Response> call, Response<model.Response> response) {
                        model.Response responseData = response.body();
                        assert responseData != null;
                        String status = responseData.getMessage();
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<model.Response> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "An Error occurred", Toast.LENGTH_LONG).show();
                    }
                });
            }
        } catch (Exception e) {
            dialog.dismiss();
            System.out.println("Exception :" + e.getMessage());
        }
    }

    private void printStruk() {
        Bundle getBundle = this.getIntent().getExtras();
        sno1 = getBundle.getString("sno");
        product = getBundle.getString("product");

        StringBuffer buffer = new StringBuffer();
        MainActivity ma = new MainActivity();
        String strQnt = "0";
        String strAuditId = "";
        String strProduct = "";

        Cursor c1 = db.rawQuery("SELECT sum(quantity),auditId,type FROM CollectionDB WHERE status='0' AND supplier='"+sno1+"' AND saccoCode='"+AppConstants.SACCO_CODE+"'", null);
        while (c1.moveToNext()) {
            strQnt = c1.getString(0);
            strAuditId = c1.getString(1);
            strProduct = c1.getString(2);
        }

        buffer.append("Supplier No    :" + sno1 + "\n");
        buffer.append("Quantity       :" + strQnt + " KGs\n");
        buffer.append("Product       :" + strProduct + "\n");
        buffer.append("Station Name    : MAIN\n");
        buffer.append("Received By    :" + strAuditId+ "\n");

        showMessage("Collection Details", buffer.toString());
        long milis1 = System.currentTimeMillis();
        String date1 = DateUtil.timeMilisToString(milis1, "dd-MM-yyyy");
        String time1 = DateUtil.timeMilisToString(milis1, "  HH:mm a");

        StringBuilder content2Sb = new StringBuilder();
        content2Sb.append("\n" + AppConstants.SACCO_CODE + "\n" + product + " RECEIPT" + "\n\n");
        content2Sb.append("-----------------------------" + "\n");
        content2Sb.append("" + buffer.toString() + "" + "\n");
        content2Sb.append("--------------------------" + "\n");
        content2Sb.append("Date:" + date1 + "" + "," + "Time:" + time1 + "" + "\n");
        content2Sb.append("--------------------------" + "\n");
        content2Sb.append("DESIGNED & DEVELOPED BY" + "\n");
        content2Sb.append("AMTECH TECHNOLOGIES LTD" + "\n");
        content2Sb.append("www.amtechafrica.com" + "\n");
        content2Sb.append("--------------------------" + "\n");

        byte[] content2Byte = Printer.printfont(content2Sb.toString(), FontDefine.FONT_32PX, FontDefine.Align_LEFT, (byte) 0x1A,
                PocketPos.LANGUAGE_ENGLISH);
        byte[] totalByte = new byte[content2Byte.length];
        int offset = 0;
        System.arraycopy(content2Byte, 0, totalByte, offset, content2Byte.length);
        offset += content2Byte.length;
        byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totalByte, 0, totalByte.length);
        sendData(senddata);
    }

    public void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showEnabled();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    showDisabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                updateDeviceList();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED) {
                    showToast("Paired");

                    connect();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}