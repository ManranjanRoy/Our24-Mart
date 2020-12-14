package com.manoranjan.applecart.Sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Cart.db";
    public static final String TABLE_NAME = "Carttable";

    //String id,title,details,image,price,category_name,color,quantity,totalprice;
    public static final String COL_1 = "id";
    public static final String COL_2 = "title";
    public static final String COL_3 = "details";
    public static final String COL_4 = "image";
    public static final String COL_5 = "price";
    public static final String COL_6 = "category_name";
    public static final String COL_7 = "color";
    public static final String COL_8 = "quantity";
    public static final String COL_9 = "totalprice";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT,title TEXT,details TEXT,image TEXT,price TEXT,category_name TEXT,color TEXT,quantity TEXT,totalprice TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // //String id,title,details,image,price,category_name,color,quantity,totalprice;
    public boolean insertData(String id, String title, String details, String image, String price,
                              String category_name, String color, String quantity, String totalprice) {
        if (CheckIsDataAlreadyInDBorNot(id)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, id);
            contentValues.put(COL_2, title);
            contentValues.put(COL_3, details);
            contentValues.put(COL_4, image);
            contentValues.put(COL_5, price);
            contentValues.put(COL_6, category_name);
            contentValues.put(COL_7, color);
            contentValues.put(COL_8, quantity);
            contentValues.put(COL_9, totalprice);
            long result = db.insert(TABLE_NAME, null, contentValues);
            if (result == -1)
                return false;
            else
                return true;
        } else {
            Log.d("insertstatus", "Already Exixt");
        }
        return false;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        return res;
    }

    public boolean updateData(String id, String quantity, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id);
        contentValues.put(COL_8, quantity);
        contentValues.put(COL_9, price);
        db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        return true;
    }

    public Integer deleteData(String id) {
        if (!id.equals("")){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
        }
      return 0;
    }

    public  boolean CheckIsDataAlreadyInDBorNot( String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + COL_1 + " = " + id;
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public boolean deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
         db.delete(TABLE_NAME,null,null);
        db.execSQL("delete from "+ TABLE_NAME);
        return true;


    }
}