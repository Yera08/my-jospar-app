package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FourMonthPlanHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "fourMonthPlan.db";
    private static final int DATABASE_VERSION = 8;
    public static final String TABLE_FOUR_MONTH_PLAN = "notes";
    public static final String FOUR_YEAR_COLUMN_ID = "_id";
    public static final String FOUR_YEAR_COLUMN_TEXT = "text";

    private static final String FOUR_MONTH_PLAN_TABLE_CREATE =
            "CREATE TABLE " + TABLE_FOUR_MONTH_PLAN + " (" +
                    FOUR_YEAR_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FOUR_YEAR_COLUMN_TEXT + " TEXT);";

    public FourMonthPlanHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(FOUR_MONTH_PLAN_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_FOUR_MONTH_PLAN);
        onCreate(sqLiteDatabase);
    }
}

