package kz.yerzhan.jospar.MenuActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import kz.yerzhan.jospar.Activities.MainActivity;
import kz.yerzhan.jospar.Activities.ToDoActivity;
import kz.yerzhan.jospar.DBHelpers.FourMonthPlanHelper;
import kz.yerzhan.jospar.DBHelpers.MainMonthHelper;
import kz.yerzhan.jospar.Note;
import kz.yerzhan.jospar.R;

public class FourMonthPlanActivity extends AppCompatActivity {
    private ListView mainMonthLV,fourMonthPlanLV;
    private Button fourMonthAddNoteClick,mainMonthAddNoteClick;
    private ArrayAdapter<Note> fourMonthPlanArrayAdapter, mainMonthArrayAdapter;
    private MainMonthHelper mainMonthHelper;
    private FourMonthPlanHelper fourMonthPlanHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four_month_plan);
        mainMonthAddNoteClick = findViewById(R.id.mainMonthAddNoteClick);
        fourMonthAddNoteClick = findViewById(R.id.fourMonthAddNoteClick);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.menu) {
                return true;
            } else if (itemID == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            } else if (itemID == R.id.note) {
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
                return true;
            }
            return false;
        });
        mainMonthLV = findViewById(R.id.mainMonthLV);
        mainMonthHelper = new MainMonthHelper(this);
        updateMainMonthList();
        mainMonthLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        mainMonthLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteMainMonthId(id);
                updateMainMonthList();
                return true;
            }
        });

        mainMonthAddNoteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentMainMonthCount = getMainMonthCount();

                if (currentMainMonthCount < 3){
                    showAddMainMonth();
                }else {
                    Toast.makeText(FourMonthPlanActivity.this, R.string.mainDayToast, Toast.LENGTH_SHORT).show();
                }
            }
        });

        fourMonthPlanLV = findViewById(R.id.fourMonthPlanLV);
        fourMonthPlanHelper = new FourMonthPlanHelper(this);
        updateFourMonthPlanList();
        fourMonthPlanLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        fourMonthPlanLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteFourMonthPlanId(id);
                updateFourMonthPlanList();
                return true;
            }
        });

        fourMonthAddNoteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentFourMonthPlanCount = getFourMonthPlanCount();

                if (currentFourMonthPlanCount < 9){
                    showAddFourMonthPlan();
                }else {
                    Toast.makeText(FourMonthPlanActivity.this, R.string.fourMonthToast, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateMainMonthList() {
        SQLiteDatabase db = mainMonthHelper.getReadableDatabase();
        Cursor cursor = db.query(MainMonthHelper.TABLE_MAIN_MONTH,null,null,null,null,null,null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(MainMonthHelper.MAIN_MONTH_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(MainMonthHelper.MAIN_MONTH_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        mainMonthArrayAdapter = new FourMonthPlanActivity.MainMonthAdapter(this,R.layout.delete_note_layout,notes);
        mainMonthLV.setAdapter(mainMonthArrayAdapter);
    }

    private void deleteMainMonthId(long id) {
        SQLiteDatabase db = mainMonthHelper.getWritableDatabase();
        int deleteRows = db.delete(MainMonthHelper.TABLE_MAIN_MONTH,MainMonthHelper.MAIN_MONTH_COLUMN_ID + " = " + id,null);
        db.close();
    }

    private void showAddMainMonth() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveMainMonthToDB(noteText);
                updateMainMonthList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getMainMonthCount() {
        SQLiteDatabase db = mainMonthHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + MainMonthHelper.TABLE_MAIN_MONTH,null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    private void saveMainMonthToDB(String noteText) {
        SQLiteDatabase db = mainMonthHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MainMonthHelper.MAIN_MONTH_COLUMN_TEXT,noteText);
        long newRowId = db.insert(MainMonthHelper.TABLE_MAIN_MONTH,null,values);
        db.close();
    }

    private static class MainMonthAdapter extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public MainMonthAdapter(Context context, int layoutResourceId, List<Note> notes){
            super(context,layoutResourceId,notes);
            this.notes = notes;
            this.layoutResourceId = layoutResourceId;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null){
                view = inflater.inflate(layoutResourceId,parent,false);
            }
            Note note = notes.get(position);
            TextView noteText = view.findViewById(R.id.noteText);
            ImageView deleteIcon = view.findViewById(R.id.deleteIcon);

            noteText.setText(note.getDescription());
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long id = note.getId();
                    ((FourMonthPlanActivity)getContext()).deleteMainMonthId(id);
                    ((FourMonthPlanActivity)getContext()).updateMainMonthList();
                }
            });
            return view;
        }
    }

    private void updateFourMonthPlanList() {
        SQLiteDatabase db = fourMonthPlanHelper.getReadableDatabase();
        Cursor cursor = db.query(FourMonthPlanHelper.TABLE_FOUR_MONTH_PLAN,null,null,null,null,null,null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(FourMonthPlanHelper.FOUR_YEAR_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(FourMonthPlanHelper.FOUR_YEAR_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        fourMonthPlanArrayAdapter = new FourMonthPlanActivity.FourMonthPlanAdapter(this,R.layout.delete_note_layout,notes);
        fourMonthPlanLV.setAdapter(fourMonthPlanArrayAdapter);
    }

    private void deleteFourMonthPlanId(long id) {
        SQLiteDatabase db = fourMonthPlanHelper.getWritableDatabase();
        int deleteRows = db.delete(FourMonthPlanHelper.TABLE_FOUR_MONTH_PLAN,FourMonthPlanHelper.FOUR_YEAR_COLUMN_ID + " = " + id,null);
        db.close();
    }

    private void showAddFourMonthPlan() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveFourMonthPlanToDB(noteText);
                updateFourMonthPlanList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getFourMonthPlanCount() {
        SQLiteDatabase db = fourMonthPlanHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + FourMonthPlanHelper.TABLE_FOUR_MONTH_PLAN,null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    private void saveFourMonthPlanToDB(String noteText) {
        SQLiteDatabase db = fourMonthPlanHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FourMonthPlanHelper.FOUR_YEAR_COLUMN_TEXT,noteText);
        long newRowId = db.insert(FourMonthPlanHelper.TABLE_FOUR_MONTH_PLAN,null,values);
        db.close();
    }

    private static class FourMonthPlanAdapter extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public FourMonthPlanAdapter(Context context, int layoutResourceId, List<Note> notes){
            super(context,layoutResourceId,notes);
            this.notes = notes;
            this.layoutResourceId = layoutResourceId;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null){
                view = inflater.inflate(layoutResourceId,parent,false);
            }
            Note note = notes.get(position);
            TextView noteText = view.findViewById(R.id.noteText);
            ImageView deleteIcon = view.findViewById(R.id.deleteIcon);

            noteText.setText(note.getDescription());
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long id = note.getId();
                    ((FourMonthPlanActivity)getContext()).deleteFourMonthPlanId(id);
                    ((FourMonthPlanActivity)getContext()).updateFourMonthPlanList();
                }
            });
            return view;
        }
    }
}








































