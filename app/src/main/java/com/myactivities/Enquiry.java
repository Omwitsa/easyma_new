package com.myactivities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import Pockdata.PocketPos;
import util.DateUtil;
import util.FontDefine;
import util.Printer;


public class Enquiry extends AppCompatActivity {

    public static final String syncFarmerURL="https://amtechafrica.com/webservice/freshpro/Pullfarmers.php";
    Dialog pDialog;
    SQLiteDatabase db,db2,db3;
    JSONParser jsonParser= new JSONParser();
    private static final String TAG = "Enquiry";
    Button search;
    public EditText suppno;
    public String searchsupp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statement);
        suppno = (EditText) findViewById(R.id.spno);




        db2 = openOrCreateDatabase("FarmersDB", Context.MODE_PRIVATE, null);
        db3 = openOrCreateDatabase("FarmersDB", Context.MODE_PRIVATE, null);
        db2.execSQL("CREATE TABLE IF NOT EXISTS FarmersDB(supply VARCHAR,name VARCHAR,idno VARCHAR,phone VARCHAR);");
        final Button button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new SyncFarmer().execute();
            }
        });
         Button search = findViewById(R.id.collection_search);
        search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (suppno.getText().toString().trim().length() == 0)
                         {
                    showMessage("Error", "Please enter Supply number");
                    return;
                } else{
                    printFarmer();
                }
            }
        });

    }

    private void printFarmer() {
        try {
            String searchsupp = suppno.getText().toString();




             Cursor c = db2.rawQuery("SELECT * FROM FarmersDB WHERE   supply= ?",new String[]{searchsupp}, null);

            //
            // Cursor c = db.rawQuery("SELECT * FROM CollectionDB WHERE transdate="2020-03-04", null);
            if (c.getCount() == 0) {
                showMessage("Details", "Farmer not found ");
                //search.setEnabled(true);
                return ;
            }
            StringBuffer buffer = new StringBuffer();
            String qtyyy = null;


            while (c.moveToNext()) {

                buffer.append(c.getString(0) + "\t" + c.getString(1) + " \t" + c.getString(2)  +"\n");
                qtyyy = c.getString(1);

            }
//            Cursor c1 = db.rawQuery("SELECT * FROM CollectionDB WHERE   supplier ='" +suppno.getText() + "','", null);
//            while (c1.moveToNext()) {
//
//                qtyyy = c1.getString(0);
//
//
//            }
            showMessage("Farmer Name +"+ qtyyy +"", buffer.toString());
            Date dNow = new Date();
            SimpleDateFormat ft = new SimpleDateFormat("E yyyy.MM.dd 'at' hh:mm:ss");
            long milis1 = System.currentTimeMillis();

            String date1 = DateUtil.timeMilisToString(milis1, "MMM dd, yyyy");
            String time1 = DateUtil.timeMilisToString(milis1, "hh:mm a");
            Calendar b = Calendar.getInstance();
            //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //String date = sdf.format(b.getTime());
            int count=c.getCount();






            MainActivity ma=new MainActivity();
//            company = ma.company;
//            userrrrr = ma.logedInUser;
//            branchhh = ma.branch;

            String titleStr =  "\n" + qtyyy +"\n" + "TOTAL DAY COLLECTION RECEIPT" + "\n";

            StringBuilder content2Sb = new StringBuilder();


            content2Sb.append("-----------------------------" + "\n");
            content2Sb.append("Total KGS collected : "+qtyyy+ "\n");
            content2Sb.append("SNo     Qty    Shift " + "\n");
            content2Sb.append("" +buffer.toString() + "" + "\n");
            content2Sb.append("--------------------------" + "\n");
            content2Sb.append("Total KGS collected : "+qtyyy+ "\n");
            //content2Sb.append("Suppliers Served : "+numberOfRows+ "\n");
            content2Sb.append("Suppliers Served : "+count+ "\n");
            content2Sb.append("                         " + "\n");
            content2Sb.append("##############################" + "\n");
            //content2Sb.append("Transporter  Name :KBT 094L  "+"\n");
            //content2Sb.append("Transporter No : KTFL 1  "+"\n");
            content2Sb.append("##############################" + "\n");
            content2Sb.append("                         " + "\n");
            content2Sb.append("Clerk  Name: Rahab/Joshua   "+"\n");
            content2Sb.append("Phone number :" +qtyyy+"\n");
            content2Sb.append("Route :" +qtyyy+"\n");
            content2Sb.append("Date :" +qtyyy+"\n");
            //content2Sb.append("Clerk No : 5  "+"\n");
            content2Sb.append("Signature____________________  "+"\n");
            content2Sb.append("                         " + "\n");
            content2Sb.append("                         " + "\n");
            content2Sb.append("--------------------------" + "\n");
            content2Sb.append("--------------------------" + "\n");
            content2Sb.append("MILK FOR HEALTH AND WEALTH" + "\n");
            content2Sb.append("--------------------------" + "\n");
            content2Sb.append("DESIGNED & DEVELOPED BY" + "\n");
            content2Sb.append("AMTECH TECHNOLOGIES LTD" + "\n");
            content2Sb.append("www.amtechafrica.com" + "\n");

            long milis = System.currentTimeMillis();
            byte[] titleByte = Printer.printfont(titleStr, FontDefine.FONT_32PX, FontDefine.Align_CENTER,
                    (byte) 0x1A, PocketPos.LANGUAGE_ENGLISH);
            byte[] content2Byte = Printer.printfont(content2Sb.toString(), FontDefine.FONT_32PX, FontDefine.Align_LEFT,
                    (byte) 0x1A, PocketPos.LANGUAGE_ENGLISH);
            byte[] totalByte = new byte[titleByte.length + content2Byte.length];
            int offset = 0;
            System.arraycopy(titleByte, 0, totalByte, offset, titleByte.length);
            offset += titleByte.length;
            System.arraycopy(content2Byte, 0, totalByte, offset, content2Byte.length);
            offset += content2Byte.length;
            byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totalByte, 0, totalByte.length);
            //sendData(senddata);
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "No New Records Found", Toast.LENGTH_LONG).show();
            finish();
        }

        return;
    }
    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }


    public Boolean getDuplicateFarmer (String  supply_no) {

        Cursor c = db2.rawQuery("SELECT * FROM FarmersDB WHERE   supply ='"+supply_no+"'", null);
        if (c.getCount() == 0) {
            return true;
        }else {
            return false;
        }

        }

      //new SyncFarmer().execute();


    class SyncFarmer extends AsyncTask<String, String, String> {

        String message = null;
        String success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Enquiry.this);
            ((ProgressDialog) pDialog).setMessage("syncing farmers list... step 2 of 2");
            ((ProgressDialog) pDialog).setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            String supply_no="";
            String full_name;
            String id_no;
            String phone_no;

            try {

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                JSONObject json = jsonParser.makeHttpRequest(syncFarmerURL, "POST", params);
                Log.e(TAG, json.toString());


                success = json.getString("success");

                if (success.equalsIgnoreCase("1")) {

                    JSONArray farmerslist = json.getJSONArray("farmerslist");


                    for (int i = 0; i < farmerslist.length(); i++) {
                        JSONObject farmerslists = farmerslist.getJSONObject(i);
                        supply_no = farmerslists.getString("supply_no");

                        full_name = farmerslists.getString("full_name");

                        id_no= farmerslists.getString("id_no");
                        phone_no= farmerslists.getString("phone_no");

                        if (getDuplicateFarmer(supply_no))
                        db2.execSQL("INSERT INTO FarmersDB  VALUES( '"+supply_no+"','"+full_name+"','"+id_no+"', '"+phone_no+"' );");
                        else {
                            db2.execSQL("UPDATE FarmersDB   SET name='"+ full_name+"', idno='" + id_no +"',phone='"+ phone_no +"' WHERE supply='"+supply_no+"'");
                            //db3.UpdateFarmers(farmer_id, farmer_username, farmer_id_no, farmer_route,Accumulatedkilos,MaximumProd,applylimit,national_id_No);
                        }


                    }
                    message="updated successfully";

                } else {

                    return json.getString("message");
                }
            } catch (Exception e) {
                message = "Failed loading members on " + supply_no;
                e.printStackTrace();
                return message;
            }


            return message;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if (file_url != null) {
                Toast.makeText(Enquiry.this, file_url, Toast.LENGTH_LONG).show();
            }
        }
    }
}





