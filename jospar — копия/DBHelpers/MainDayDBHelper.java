package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MainDayDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mainDay.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NOTES_MAIN_DAY = "notes";
    public static final String MAIN_COLUMN_ID = "_id";
    public static final String MAIN_COLUMN_TEXT = "text";

    private static final String MAIN_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES_MAIN_DAY + " (" +
                    MAIN_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    MAIN_COLUMN_TEXT + " TEXT);";

    public MainDayDBHelper (Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(MAIN_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_MAIN_DAY);
        onCreate(sqLiteDatabase);
    }
}