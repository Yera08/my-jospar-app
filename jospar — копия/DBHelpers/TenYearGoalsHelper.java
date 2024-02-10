package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TenYearGoalsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tenYearGoals.db";
    private static final int DATABASE_VERSION = 6;
    public static final String TABLE_TEN_YEAR_GOALS = "notes";
    public static final String TEN_YEAR_COLUMN_ID = "_id";
    public static final String TEN_YEAR_COLUMN_TEXT = "text";

    private static final String TEN_YEAR_GOALS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TEN_YEAR_GOALS + " (" +
                    TEN_YEAR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TEN_YEAR_COLUMN_TEXT + " TEXT);";

    public TenYearGoalsHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TEN_YEAR_GOALS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TEN_YEAR_GOALS);
        onCreate(sqLiteDatabase);
    }
}
