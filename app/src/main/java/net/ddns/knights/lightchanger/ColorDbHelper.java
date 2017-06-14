package net.ddns.knights.lightchanger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tejas on 5/31/2017.
 */

public class ColorDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Color.db";

    public static class ColorEntry implements BaseColumns{
        public static final String TABLE_NAME = "color";
        public static final String COLUMN_NAME_RED = "red";
        public static final String COLUMN_NAME_GREEN = "green";
        public static final String COLUMN_NAME_BLUE = "blue";
    }

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ColorEntry.TABLE_NAME +" ("+
            ColorEntry._ID+" INTEGER PRIMARY KEY,"+
            ColorEntry.COLUMN_NAME_RED + " TEXT, "+
            ColorEntry.COLUMN_NAME_GREEN+" TEXT,"+
            ColorEntry.COLUMN_NAME_BLUE+" TEXT)";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE "+ColorEntry.TABLE_NAME;

    public ColorDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        this.onCreate(sqLiteDatabase);
    }

    public void addColor(int r, int g, int b){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ColorEntry.COLUMN_NAME_RED,r);
        values.put(ColorEntry.COLUMN_NAME_GREEN,g);
        values.put(ColorEntry.COLUMN_NAME_BLUE,b);

        db.insert(ColorEntry.TABLE_NAME, null, values);
        db.close();
    }

    public List<Integer> getAllColors(){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {ColorEntry.COLUMN_NAME_RED,
                ColorEntry.COLUMN_NAME_GREEN,
                ColorEntry.COLUMN_NAME_BLUE};
        String sortOrder = ColorEntry._ID;

        Cursor cursor = db.query(true,ColorEntry.TABLE_NAME,projection,null,null,null,null,sortOrder,null);

        List<Integer> colors = new ArrayList<>();
        while(cursor.moveToNext()){
            int r = cursor.getInt(0);
            int g = cursor.getInt(1);
            int b = cursor.getInt(2);
            colors.add(Color.rgb(r,g,b));
        }
        db.close();
        return colors;
    }

    public void deleteColorItem(int color){
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        SQLiteDatabase db = getWritableDatabase();

        db.delete(ColorEntry.TABLE_NAME,ColorEntry.COLUMN_NAME_RED+"=? AND " +
                ColorEntry.COLUMN_NAME_GREEN+"=? AND "+
                ColorEntry.COLUMN_NAME_BLUE + "=?",
                new String[]{String.valueOf(r),String.valueOf(g),String.valueOf(b)});
        db.close();
    }

}
