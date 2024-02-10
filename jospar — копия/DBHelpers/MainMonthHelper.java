package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainMonthHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mainMonth.db";
    private static final int DATABASE_VERSION = 7;
    public static final String TABLE_MAIN_MONTH = "notes";
    public static final String MAIN_MONTH_COLUMN_ID = "_id";
    public static final String MAIN_MONTH_COLUMN_TEXT = "text";

    private static final String MAIN_MONTH_TABLE_CREATE =
            "CREATE TABLE " + TABLE_MAIN_MONTH + " (" +
                    MAIN_MONTH_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MAIN_MONTH_COLUMN_TEXT + " TEXT);";

    public MainMonthHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MAIN_MONTH_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MAIN_MONTH);
        onCreate(sqLiteDatabase);
    }
}

