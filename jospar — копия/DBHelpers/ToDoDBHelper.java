package kz.yerzhan.jospar.DBHelpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 3;
    public static final String TABLE_TO_DO = "notes";
    public static final String TODO_COLUMN_ID = "_id";
    public static final String TODO_COLUMN_TEXT = "text";

    private static final String TODO_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TO_DO + " (" +
                    TODO_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TODO_COLUMN_TEXT + " TEXT);";
    public ToDoDBHelper (Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TODO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_TO_DO);
        onCreate(sqLiteDatabase);
    }
}
