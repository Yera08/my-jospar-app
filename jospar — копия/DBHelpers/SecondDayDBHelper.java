package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SecondDayDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "secondary.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NOTES_SECONDARY = "notes";
    public static final String SECOND_COLUMN_ID = "_id";
    public static final String SECOND_COLUMN_TEXT = "text";

    private static final String SECOND_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTES_SECONDARY + " (" +
                    SECOND_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SECOND_COLUMN_TEXT + " TEXT);";

    public SecondDayDBHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SECOND_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_SECONDARY);
        onCreate(sqLiteDatabase);
    }
}
