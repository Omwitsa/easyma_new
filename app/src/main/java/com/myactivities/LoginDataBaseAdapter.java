package com.myactivities;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
 
public class LoginDataBaseAdapter
{
        static final String DATABASE_NAME = "admin_login.db";
        static final int DATABASE_VERSION = 1;
        public static final int NAME_COLUMN = 1;
        // TODO: Create public field for each column in your table.
        // SQL Statement to create a new database.
        static final String DATABASE_CREATE = "create table "+"LOGIN"+
                                     "( " +"ID"+" integer primary key autoincrement,"+ "qty  text,sup_no text, pin text); ";
        // Variable to hold the database instance
        public  SQLiteDatabase db;
        // Context of the application using the database.
        private final Context context;
        // Database open/upgrade helper
        private DatabaseHelper dbHelper;
        public  LoginDataBaseAdapter(Context _context)
        {
            context = _context;
            dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public  LoginDataBaseAdapter open() throws SQLException
        {
            db = dbHelper.getWritableDatabase();
            return this;
        }
        public void close()
        {
            db.close();
        }
 
        public  SQLiteDatabase getDatabaseInstance()
        {
            return db;
        }
 
        public void insertEntry(String qty,String sub_no,String pin)
        {
           ContentValues newValues = new ContentValues();
            // Assign values for each row.
            newValues.put("qty", qty);
            newValues.put("sub_no",sub_no);
            newValues.put("pin",pin);
            
            // Insert the row into your table
            db.insert("LOGIN", null, newValues);
            ///Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
        }
        public int deleteEntry(String sub_no)
        {
            //String id=String.valueOf(ID);
            String where="sub_no=?";
            int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{sub_no}) ;
           // Toast.makeText(context, "Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted, Toast.LENGTH_LONG).show();
            return numberOFEntriesDeleted;
        }    
        public String getSinlgeEntry(String sub_no)
        {
            Cursor cursor=db.query("LOGIN", null, " sub_no=?", new String[]{sub_no}, null, null, null);
            if(cursor.getCount()<1) // UserName Not Exist
            {
                cursor.close();
                return "NOT EXIST";
            }
            cursor.moveToFirst();
            String password= cursor.getString(cursor.getColumnIndex("pin"));
            cursor.close();
            return password;                
        }
        public void  updateEntry(String userName,String password)
        {
            // Define the updated row content.
            ContentValues updatedValues = new ContentValues();
            // Assign values for each row.
            updatedValues.put("USERNAME", userName);
            updatedValues.put("PASSWORD",password);
 
            String where="USERNAME = ?";
            db.update("LOGIN",updatedValues, where, new String[]{userName});              
        }        
}
