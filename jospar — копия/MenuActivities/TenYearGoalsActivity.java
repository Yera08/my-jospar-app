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
import kz.yerzhan.jospar.DBHelpers.TenYearGoalsHelper;
import kz.yerzhan.jospar.Note;
import kz.yerzhan.jospar.R;

public class TenYearGoalsActivity extends AppCompatActivity {
    private ListView tenYearGoalsListView;
    private Button tenYearGoalsAddNoteClick;
    private ArrayAdapter<Note> tenYearGoalsArrayAdapter;
    private TenYearGoalsHelper tenYearGoalsHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ten_year_goals);
        tenYearGoalsAddNoteClick = findViewById(R.id.tenYearGoalsAddNoteClick);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setSelectedItemId(R.id.menu);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.menu){
                return true;
            } else if (itemID == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            } else if (itemID == R.id.note) {
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
                return true;
            }
            return false;
        });
        tenYearGoalsListView = findViewById(R.id.tenYearListView);
        tenYearGoalsHelper = new TenYearGoalsHelper(this);
        updateTenYearGoalsList();
        tenYearGoalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        tenYearGoalsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteTenYearGoalsId(id);
                updateTenYearGoalsList();
                return true;
            }
        });

        tenYearGoalsAddNoteClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentTenYearGoalsCount = getTenYearGoalsCount();

                if (currentTenYearGoalsCount < 12){
                    showAddTenYearGoals();
                }else {
                    Toast.makeText(TenYearGoalsActivity.this, R.string.twelveToast,Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateTenYearGoalsList() {
        SQLiteDatabase db = tenYearGoalsHelper.getReadableDatabase();
        Cursor cursor = db.query(TenYearGoalsHelper.TABLE_TEN_YEAR_GOALS,null,null,null,null,null,null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(TenYearGoalsHelper.TEN_YEAR_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(TenYearGoalsHelper.TEN_YEAR_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }

        cursor.close();
        db.close();

        tenYearGoalsArrayAdapter = new TenYearGoalsAdapter(this,R.layout.delete_note_layout,notes);
        tenYearGoalsListView.setAdapter(tenYearGoalsArrayAdapter);
    }

    private void deleteTenYearGoalsId(long id) {
        SQLiteDatabase db = tenYearGoalsHelper.getWritableDatabase();
        int deleteRows = db.delete(TenYearGoalsHelper.TABLE_TEN_YEAR_GOALS,TenYearGoalsHelper.TEN_YEAR_COLUMN_ID + " = " + id,null);
        db.close();
    }

    private void showAddTenYearGoals() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveTenYearGoalsToDB(noteText);
                updateTenYearGoalsList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getTenYearGoalsCount() {
        SQLiteDatabase db = tenYearGoalsHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TenYearGoalsHelper.TABLE_TEN_YEAR_GOALS,null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    private void saveTenYearGoalsToDB(String noteText) {
        SQLiteDatabase db = tenYearGoalsHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TenYearGoalsHelper.TEN_YEAR_COLUMN_TEXT,noteText);
        long newRowId = db.insert(TenYearGoalsHelper.TABLE_TEN_YEAR_GOALS,null,values);
        db.close();
    }

    private static class TenYearGoalsAdapter extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public TenYearGoalsAdapter(Context context, int layoutResourceId, List<Note> notes){
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
                    ((TenYearGoalsActivity)getContext()).deleteTenYearGoalsId(id);
                    ((TenYearGoalsActivity)getContext()).updateTenYearGoalsList();
                }
            });
            return view;
        }
    }
}


























