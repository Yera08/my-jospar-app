package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FiveYearGoalsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fiveYearGoals.db";
    private static final int DATABASE_VERSION = 5;
    public static final String TABLE_FIVE_YEAR_GOALS = "notes";
    public static final String FIVE_YEAR_COLUMN_ID = "_id";
    public static final String FIVE_YEAR_COLUMN_TEXT = "text";

    private static final String FIVE_YEAR_GOALS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_FIVE_YEAR_GOALS + " (" +
                    FIVE_YEAR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FIVE_YEAR_COLUMN_TEXT + " TEXT);";

    public FiveYearGoalsHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FIVE_YEAR_GOALS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FIVE_YEAR_GOALS);
        onCreate(sqLiteDatabase);
    }
}






































