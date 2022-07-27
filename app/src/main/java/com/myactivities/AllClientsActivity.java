package com.myactivities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import Adapters.AllClientsAdapter;
import Adapters.RecyclerViewAdapter;

import static network.Urls.BASE_URL;


public class AllClientsActivity extends AppCompatActivity{

    List<AllClientsAdapter> DataAdapterClassList;

    RecyclerView recyclerView;

    TextView tv_month;

    RecyclerView.LayoutManager recyclerViewlayoutManager;

    RecyclerView.Adapter recyclerViewadapter;

    ProgressBar progressBar;

    JsonArrayRequest jsonArrayRequest;

    ArrayList<String> Remarks;

    RequestQueue requestQueue;

    //String HTTP_SERVER_URL = BASE_URL + "amtech_timesheet/task/DisplayTask.php", email;
    String HTTP_SERVER_URL = BASE_URL + "Dairies.php", phone;

    View ChildView;

    int RecyclerViewClickedItemPOS;
    public static String userr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("All Clients");
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DataAdapterClassList = new ArrayList<>();

        Remarks = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView1);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView.setHasFixedSize(true);

        recyclerViewlayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        // JSON data web call function call from here.
        JSON_WEB_CALL();

        //RecyclerView Item click listener code starts from here.
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            GestureDetector gestureDetector = new GestureDetector(AllClientsActivity.this, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent motionEvent) {

                    return true;
                }

            });

            @Override
            public boolean onInterceptTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

                ChildView = Recyclerview.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

                if (ChildView != null && gestureDetector.onTouchEvent(motionEvent)) {

                    //Getting RecyclerView Clicked item value.
                    RecyclerViewClickedItemPOS = Recyclerview.getChildAdapterPosition(ChildView);

                    //Printing RecyclerView Clicked item clicked value using Toast Message.
                    //Toast.makeText(TaskMainActivity.this, Remarks.get(RecyclerViewClickedItemPOS), Toast.LENGTH_LONG).show();

                }

                return false;
            }


            @Override
            public void onTouchEvent(RecyclerView Recyclerview, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        Bundle bundle = getIntent().getExtras();

        //Extract the data…
        phone = bundle.getString("phone");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.clients, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_client:
                //search Client from db here
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void JSON_WEB_CALL() {
        jsonArrayRequest = new JsonArrayRequest(HTTP_SERVER_URL,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        JSON_PARSE_DATA_AFTER_WEBCALL(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
        };

        requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonArrayRequest);
    }

    public void JSON_PARSE_DATA_AFTER_WEBCALL(JSONArray array) {

        for (int i = 0; i < array.length(); i++) {
            AllClientsAdapter GetDataAdapter2 = new AllClientsAdapter();
            JSONObject json = null;
            try {
                json = array.getJSONObject(i);
                //AdminLogin al = new AdminLogin();
                MainActivity ma=new MainActivity();
                userr = ma.logedInUser;
                //if (userr.equalsIgnoreCase(phone)) {
                GetDataAdapter2.setFull_name(json.getString("full_name"));
                GetDataAdapter2.setId_no(json.getString("id_no"));
                GetDataAdapter2.setHome_address(json.getString("home_address"));

                DataAdapterClassList.add(GetDataAdapter2);
                //}

            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        progressBar.setVisibility(View.GONE);

        Calendar mCalendar = Calendar.getInstance();
        String month = mCalendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        int year=Calendar.getInstance().get(Calendar.YEAR);

        //tv_month.setVisibility(View.VISIBLE);

        //tv_month.setText("TASK RECORDS \n For: "+email.toString()+" "+"Month: "+month.toString()+","+String.valueOf(year));
        //tv_month.setText("Success");

        recyclerViewadapter = new RecyclerViewAdapter(DataAdapterClassList, this);

        recyclerView.setAdapter(recyclerViewadapter);
    }
}
