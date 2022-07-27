package com.myactivities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import fragments.Home;


public class MainActivity extends MainPrintActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ViewPager viewPager;

    //public static String userr;

    Intent serviceIntent;
    public ProgressDialog dialog= null;

    private BluetoothAdapter mBluetoothAdapter;
    public static Context mContext;

    NavigationView navigationView;
    SharedPreferences sharedPreferences;

    boolean loggedIn;
    private SharedPreferences pref;
    public static String branch,company,logedInUser;

    private BroadcastReceiver mNetworkReceiver;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_one);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mContext = this;

            Bundle getBundle = this.getIntent().getExtras();
            branch = getBundle.getString("branch");
            company = getBundle.getString("company");
            logedInUser = getBundle.getString("user");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);

        TextView tvUser = (TextView) hView.findViewById(R.id.textViewUser);
        tvUser.setText("welcome," + logedInUser);

        //mNetworkReceiver = new NetworkChangeReceiver();
    }

    @Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();

        } else {
            getFragmentManager().popBackStack();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_all_clients:
                Intent i = new Intent(getApplicationContext(), AllClientsActivity.class);

                String phone =logedInUser;
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                i.putExtras(bundle);
                startActivity(i);
                break;

            case R.id.action_settings:
                Intent setIntent = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(setIntent);
                //  finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        sharedPreferences = getSharedPreferences(Settings.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        loggedIn = sharedPreferences.getBoolean(Settings.LOGGEDIN_SHARED_PREF, false);
       if (id == R.id.register_farmer){
           Intent regIntent = new Intent(getApplicationContext(), RegisterFarmer.class);
           String phone =logedInUser;
           Bundle bundle = new Bundle();
           bundle.putString("phone", phone);
           regIntent.putExtras(bundle);
           startActivity(regIntent);

        } else if (id == R.id.help) {
            Intent intent_news = new Intent(MainActivity.this, EmailActivity.class);
            startActivity(intent_news);

        } else if (id == R.id.nav_add_user) {
            Intent userIntent = new Intent(getApplicationContext(), SuperAdminLogin.class);
            startActivity(userIntent);

        } else if (id == R.id.nav_share) {
            //ApplicationInfo app = getApplicationContext().getApplicationInfo();
           // String filePath = app.sourceDir;

            //Intent intent = new Intent(Intent.ACTION_SEND);
           // intent.setType("*/*");
            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(filePath)));
            //startActivity(Intent.createChooser(intent, "Share app via"));

           Toast.makeText(MainActivity.this, "Contact system admin", Toast.LENGTH_LONG).show();
        } else if (id == R.id.nav_change_logins) {
            Toast.makeText(MainActivity.this, "Not Authorised To change Logins for Now", Toast.LENGTH_LONG).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home(), "FARMING");

        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        adapter.notifyDataSetChanged();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
    }

//    public  void synchronize(boolean value){
//        if(value) {
//            //Toast.makeText(MainActivity.this, "success", Toast.LENGTH_LONG).show();
//            try {
//
//                if (isOnline()) {
//                    autosynch();
//                    Toast.makeText(MainActivity.this, "Checking new collection", Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(MainActivity.this, "Check your Internet Connection and try again", Toast.LENGTH_LONG).show();
//                }
//                //checkConnection();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


//    protected boolean isOnline() {
//        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo netInfo = cm.getActiveNetworkInfo();
//        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
//            return true;
//        } else {
//            return false;
//        }
//    }

    private void registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }

    protected void unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterNetworkChanges();
    }
    @Override
    public  void  onResume(){
        super.onResume();
        registerNetworkBroadcastForNougat();
    }




//    public class NetworkChangeReceiver extends BroadcastReceiver
//    {
//        @Override
//        public void onReceive(Context context, Intent intent)
//        {
//            try
//            {
//                if (isOnline(context)) {
//                    synchronize(true);
//                } else {
//                    synchronize(false);
//                   }
//            } catch (NullPointerException e) {
//                e.printStackTrace();
//            }
//        }

        private boolean isOnline(Context context) {
            try {
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                return (netInfo != null && netInfo.isConnected());
            } catch (NullPointerException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

