package com.myactivities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchDeviceActivity extends MyListActivity {
    public static String branchhh,userrrrr,company;

    private Handler _handler = new Handler();
    /* Get Default Adapter */
    private BluetoothAdapter _bluetooth = BluetoothAdapter.getDefaultAdapter();
    /* Storage the BT devices */
    private List<BluetoothDevice> _devices = new ArrayList<BluetoothDevice>();
    /* Discovery is Finished */
    private volatile boolean _discoveryFinished;
    //android.support.v7.app.AppCompatActivity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        setContentView(R.layout.searchdevice);

        //activity = new android.support.v7.app.AppCompatActivity();

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Devices");
        //activity.setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //activity.getSupportActionBar().setDisplayShowHomeEnabled(true);


		/* Register Receiver */
        IntentFilter discoveryFilter = new IntentFilter(
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(_discoveryReceiver, discoveryFilter);
        IntentFilter foundFilter = new IntentFilter(
                BluetoothDevice.ACTION_FOUND);
        registerReceiver(_foundReceiver, foundFilter);

		/* show a dialog "Scanning..." */
        SamplesUtils.indeterminate(SearchDeviceActivity.this, _handler,
                getResources().getString(R.string.scaning), _discoveryWorkder,
                new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {

                        for (; _bluetooth.isDiscovering(); ) {

                            _bluetooth.cancelDiscovery();
                        }

                        _discoveryFinished = true;
                    }
                }, true);

        // Bundle bundle = getIntent().getExtras();
        //supp_no = bundle.getString("SUPNO");
    }

    private Runnable _discoveryWorkder = new Runnable() {
        public void run() {
            /* Start search device */
            _bluetooth.startDiscovery();
            Log.d("EF-BTBee", ">>Starting Discovery");
            for (; ; ) {
                if (_discoveryFinished) {
                    Log.d("EF-BTBee", ">>Finished");
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    };

    /**
     * Receiver When the discovery finished be called.
     */
    private BroadcastReceiver _foundReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
			/* get the search results */
            BluetoothDevice device = intent
                    .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
			/* add to list */
            _devices.add(device);
			/* show the devices list */
            showDevices();
        }
    };
    private BroadcastReceiver _discoveryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
			/* unRegister Receiver */
            Log.d("EF-BTBee", ">>unregisterReceiver");
            unregisterReceiver(_foundReceiver);
            unregisterReceiver(this);
            _discoveryFinished = true;
        }
    };

    /* Show devices list */
    protected void showDevices() {
        List<String> list = new ArrayList<String>();
        if (_devices.size() > 0) {
            for (int i = 0, size = _devices.size(); i < size; ++i) {
                StringBuilder b = new StringBuilder();
                BluetoothDevice d = _devices.get(i);
                b.append(d.getAddress());
                b.append('\n');
                b.append(d.getName());
                String s = b.toString();
                list.add(s);
            }
        } else

            list.add(getResources().getString(R.string.nodevice));
        // txt_msg.setTextColor(Color.RED);
        // txt_msg.setText();

        Log.d("EF-BTBee", ">>showDevices");
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        _handler.post(new Runnable() {
            public void run() {
                setListAdapter(adapter);
				/* Prompted to select a server to connect */
                Toast.makeText(getBaseContext(),
                        getResources().getString(R.string.selectonedevice),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    /* Select device */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("EF-BTBee", ">>Click device");
        Intent result = new Intent();
        result.putExtra(BluetoothDevice.EXTRA_DEVICE, _devices.get(position));
        result.setClass(SearchDeviceActivity.this, MonitorActivity.class);
        StringBuffer buffer = new StringBuffer();
        final MainActivity ma = new MainActivity();
        company = ma.company;
        userrrrr = ma.logedInUser;
        branchhh = ma.branch;
        startActivity(result);
        finish();
    }
}