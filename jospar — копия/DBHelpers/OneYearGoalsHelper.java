package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class OneYearGoalsHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "oneYearGoals.db";
    private static final int DATABASE_VERSION = 4;
    public static final String TABLE_ONE_YEAR_GOALS = "notes";
    public static final String GOALS_COLUMN_ID = "_id";
    public static final String GOALS_COLUMN_TEXT = "text";

    private static final String ONE_YEAR_GOALS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ONE_YEAR_GOALS + " (" +
                    GOALS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GOALS_COLUMN_TEXT + " TEXT);";

    public OneYearGoalsHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ONE_YEAR_GOALS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ONE_YEAR_GOALS);
        onCreate(sqLiteDatabase);
    }
}












