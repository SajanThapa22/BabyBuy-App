package com.example.babybuy.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.babybuy.DataModels.CatDataModel;
import com.example.babybuy.DataModels.ItemDataModel;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {


    //final variable declaration for Database
    public static final String DBASE_NAME = "babybuy.db";


    //final variable declaration for Registration and Login Table
    public static final String TABLE_AUTHEN = "auth";
    public static final String ID_AUTHEN = "id";
    public static final String COL_AUTHEN_1 = "firstname";
    public static final String COL_AUTHEN_2 = "lastname";
    public static final String COL_AUTHEN_3 = "email";
    public static final String COL_AUTHEN_4 = "password";
    public static final String COL_AUTHEN_5 = "image";
    public static final String AUTH_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_AUTHEN +
            "(" + ID_AUTHEN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_AUTHEN_1 + " varchar(100), "
            + COL_AUTHEN_2 + " varchar(100), "
            + COL_AUTHEN_3 + " varchar(100), "
            + COL_AUTHEN_4 + " varchar(100), "
            + COL_AUTHEN_5 + " BLOB)";


    //final variable declaration for Category Table
    public static final String TABLE_CAT = "category";
    public static final String ID_CAT = "categoryid";
    public static final String COL_CAT_1 = "categoryname";
    public static final String COL_CAT_2 = "categoryimage";
    public static final String CATEGORY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_CAT +
            "(" + ID_CAT + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CAT_1 + " varchar(100), "
            + COL_CAT_2 + " BLOB)";



    public static final String TABLE_ITEM = "item";
    public static final String ID_ITEM = "itemid";
    public static final String COL_ITEM_1 = "itemname";
    public static final String COL_ITEM_2 = "itemquantity";
    public static final String COL_ITEM_3 = "itemprice";
    public static final String COL_ITEM_4 = "itemdescription";
    public static final String COL_ITEM_5 = "itemstatus";
    public static final String COL_ITEM_6 = "itemimage";
    public static final String COL_ITEM_7 = "itemlat";
    public static final String COL_ITEM_8 = "itemlng";
    public static final String COL_ITEM_9 = "address";
    public static final String COL_ITEM_10 = "itemcategoryid";


    public static final String ITEM_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_ITEM +
            "(" + ID_ITEM + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_ITEM_1 + " varchar(100), "
            + COL_ITEM_2 + " INTEGER, "
            + COL_ITEM_3 + " REAL, "
            + COL_ITEM_4 + " varchar(100), "
            + COL_ITEM_5 + " INTEGER, "
            + COL_ITEM_6 + " BLOB, "
            + COL_ITEM_7 + " REAL, "
            + COL_ITEM_8 + " REAL, "
            + COL_ITEM_9 + " varchar(100), "
            + COL_ITEM_10 + " INTEGER REFERENCES " + TABLE_CAT + "(" + COL_CAT_1 + " ))";



    public Database(Context context) {
        super(context, DBASE_NAME, null, 1);
    }


    //Method for table creation
    @Override
    public void onCreate(SQLiteDatabase db) {
        //execute above query
        db.execSQL(AUTH_TABLE_CREATE);
        db.execSQL(CATEGORY_TABLE_CREATE);
        db.execSQL(ITEM_TABLE_CREATE);
    }


    //Method for table upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUTHEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM);
        onCreate(db);
    }


    //Insert query for Registration
    public int updateimage(byte[] image, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_AUTHEN_5, image);
        return db.update(TABLE_AUTHEN, v, COL_AUTHEN_3 + " = ? ", new String[]{email});

    }

    //Insert query for Registration
    public boolean insert(String firstname, String lastname, String email, String password) {
        SQLiteDatabase dbb = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_AUTHEN_1, firstname);
        v.put(COL_AUTHEN_2, lastname);
        v.put(COL_AUTHEN_3, email);
        v.put(COL_AUTHEN_4, password);
        long res = dbb.insert(TABLE_AUTHEN, null, v);
        if (res == -1)
            return false;
        else
            return true;
    }

    //select method for category
    public byte[] authfetchforimage(String email) {
        SQLiteDatabase datbase = this.getWritableDatabase();
        Cursor getdata = datbase.rawQuery("select * from " + TABLE_AUTHEN + " where " + COL_AUTHEN_3 + " = ?" ,new String[]{email} );
        byte[] imagebyte = new byte[0];
        while (getdata.moveToNext()) {
            imagebyte = getdata.getBlob(5);
        }
        return imagebyte;
    }



    //Method for preventing multiple registration using same email address
    public Boolean checkemail(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor getlistemail = database.rawQuery("select * from " + TABLE_AUTHEN + " where " + "email = ?", new String[]{email});
        if (getlistemail.getCount() > 0)
            return false;
        else
            return true;
    }


    //Method for login validation
    public Boolean checkemailandpassword(String email, String password) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from " + TABLE_AUTHEN + " where" +
                        " email = ? and password = ?",
                new String[]{email, password});
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }


    public String getfullname(String email) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor getdata = database.rawQuery("select * from " + TABLE_AUTHEN + " where " + COL_AUTHEN_3 + " = ?", new String[]{email});
        String fullname = "";
        while (getdata.moveToNext()) {
            String firstname = getdata.getString(1);
            String lastname = getdata.getString(2);
            fullname = firstname + " " + lastname;
        }

        return fullname;
    }


    //insert method for category
    public long categoryadd(String catname, byte[] catimage) {
        SQLiteDatabase catdb = getWritableDatabase();
        ContentValues catv = new ContentValues();
        catv.put(COL_CAT_1, catname);
        catv.put(COL_CAT_2, catimage);
        return catdb.insert(TABLE_CAT, null, catv);
    }


    //select method for category
    public ArrayList<CatDataModel> categoryfetchdata() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor getdata = database.rawQuery("select * from " + TABLE_CAT, null);
        ArrayList<CatDataModel> catarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            CatDataModel cat = new CatDataModel();
            cat.setIdcat(getdata.getInt(0));
            cat.setNamecat(getdata.getString(1));
            cat.setImgcat(getdata.getBlob(2));
            catarray.add(cat);
        }
        return catarray;
    }


    //update method for category
    public int updatecategoryname(String catname, int catid) {
        SQLiteDatabase udb = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_CAT_1, catname);
        return udb.update(TABLE_CAT, cv, "categoryid = ?", new String[]{String.valueOf(catid)});
    }


    // delete method for category
    public int deletecat(int catid) {
        SQLiteDatabase ddb = getWritableDatabase();
        return ddb.delete(TABLE_CAT, ID_CAT + " = ?", new String[]{String.valueOf(catid)});
    }


    //insert method for items
    public long itemadd(ItemDataModel ItemDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_ITEM_1, ItemDataModel.getNameitem());
        v.put(COL_ITEM_2, ItemDataModel.getQuanitem());
        v.put(COL_ITEM_3, ItemDataModel.getPriceitem());
        v.put(COL_ITEM_4, ItemDataModel.getDescripitem());
        v.put(COL_ITEM_5, ItemDataModel.getStatusitem());
        v.put(COL_ITEM_6, ItemDataModel.getImgitem());
        v.put(COL_ITEM_7, ItemDataModel.getLatitem());
        v.put(COL_ITEM_8, ItemDataModel.getLongitem());
        v.put(COL_ITEM_9, ItemDataModel.getAddressitem());
        v.put(COL_ITEM_10, ItemDataModel.getIdcatimg());
        return db.insert(TABLE_ITEM, null, v);
    }


    //change status of item purchased or not
    public int itempurchased(int itemsid, int itemid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_ITEM_5, itemsid);
        return db.update(TABLE_ITEM, v, ID_ITEM + " = ?", new String[]{String.valueOf(itemid)});
    }


    //select method for itemlist
    public ArrayList<ItemDataModel> itemfetchdata(int catid) {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor getdata = database.rawQuery("select * from " + TABLE_ITEM + " where " + COL_ITEM_10 + " = ? ", new String[]{String.valueOf(catid)});
        ArrayList<ItemDataModel> productarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel item = new ItemDataModel();
            item.setIditem(getdata.getInt(0));
            item.setNameitem(getdata.getString(1));
            item.setQuanitem(getdata.getInt(2));
            item.setPriceitem(getdata.getDouble(3));
            item.setDescripitem(getdata.getString(4));
            item.setStatusitem(getdata.getInt(5));
            item.setImgitem(getdata.getBlob(6));
            productarray.add(item);
        }
        return productarray;
    }

    public ArrayList<ItemDataModel> itemfetchdataformap() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor getdata = database.rawQuery("select * from " + TABLE_ITEM, null);
        ArrayList<ItemDataModel> productarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel item = new ItemDataModel();
            item.setIditem(getdata.getInt(0));
            item.setNameitem(getdata.getString(1));
            item.setQuanitem(getdata.getInt(2));
            item.setPriceitem(getdata.getDouble(3));
            item.setDescripitem(getdata.getString(4));
            item.setStatusitem(getdata.getInt(5));
            item.setImgitem(getdata.getBlob(6));
            item.setLatitem(getdata.getDouble(7));
            item.setLongitem(getdata.getDouble(8));
            item.setIdcatimg(getdata.getInt(10));

            productarray.add(item);
        }
        return productarray;
    }

    //select method for itemlist
    public ArrayList<ItemDataModel> itemfetchdataformapload(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + TABLE_ITEM + " where " + ID_ITEM + " = ? ", new String[]{String.valueOf(id)});
        ArrayList<ItemDataModel> productarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel item = new ItemDataModel();
            item.setIditem(getdata.getInt(0));
            item.setNameitem(getdata.getString(1));
            item.setQuanitem(getdata.getInt(2));
            item.setPriceitem(getdata.getDouble(3));
            item.setDescripitem(getdata.getString(4));
            item.setStatusitem(getdata.getInt(5));
            item.setImgitem(getdata.getBlob(6));
            item.setLatitem(getdata.getDouble(7));
            item.setLongitem(getdata.getDouble(8));
            item.setAddressitem(getdata.getString(9));
            item.setIdcatimg(getdata.getInt(10));


            productarray.add(item);
        }
        return productarray;
    }


    //purchased item list for manageitem fragment
    public ArrayList<ItemDataModel> itemfetchdataforpurchased(int productsts) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor getdata = db.rawQuery("select * from " + TABLE_ITEM + " where " + COL_ITEM_5 + " = ?", new String[]{String.valueOf(productsts)});
        ArrayList<ItemDataModel> productarray = new ArrayList<>();
        while (getdata.moveToNext()) {
            ItemDataModel item = new ItemDataModel();
            item.setIditem(getdata.getInt(0));
            item.setNameitem(getdata.getString(1));
            item.setQuanitem(getdata.getInt(2));
            item.setPriceitem(getdata.getDouble(3));
            item.setDescripitem(getdata.getString(4));
            item.setStatusitem(getdata.getInt(5));
            item.setImgitem(getdata.getBlob(6));
            productarray.add(item);
        }
        return productarray;
    }

    //update method for item
    public int updateitem(ItemDataModel ItemDataModel, int productid) {
        SQLiteDatabase udb = getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COL_ITEM_1, ItemDataModel.getNameitem());
        v.put(COL_ITEM_2, ItemDataModel.getQuanitem());
        v.put(COL_ITEM_3, ItemDataModel.getPriceitem());
        v.put(COL_ITEM_4, ItemDataModel.getDescripitem());
        v.put(COL_ITEM_5, ItemDataModel.getStatusitem());
        v.put(COL_ITEM_6, ItemDataModel.getImgitem());
        v.put(COL_ITEM_7, ItemDataModel.getLatitem());
        v.put(COL_ITEM_8, ItemDataModel.getLongitem());
        v.put(COL_ITEM_9, ItemDataModel.getAddressitem());

        return udb.update(TABLE_ITEM, v,  ID_ITEM + " = ?", new String[]{String.valueOf(productid)});
    }


    //delete item
    public int deleteitem(int id) {
        SQLiteDatabase database = getWritableDatabase();
        return database.delete(TABLE_ITEM, ID_ITEM + " = ?",
                new String[]{String.valueOf(id)});
    }


    //delete items when category deleted
    public int deleteitembycatid(int itemcatid) {
        SQLiteDatabase ddb = getWritableDatabase();
        return ddb.delete(TABLE_ITEM, COL_ITEM_7 + " = ?",
                new String[]{String.valueOf(itemcatid)});
    }
}
