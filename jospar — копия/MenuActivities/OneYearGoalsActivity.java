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
import kz.yerzhan.jospar.DBHelpers.OneYearGoalsHelper;

import kz.yerzhan.jospar.Note;
import kz.yerzhan.jospar.R;

public class OneYearGoalsActivity extends AppCompatActivity {
    private ListView oneYearGoalsListView;
    private ArrayAdapter<Note> oneYearGoalsArrayAdapter;
    private OneYearGoalsHelper oneYearGoalsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);
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
        oneYearGoalsListView = findViewById(R.id.goalsListView);
        oneYearGoalsHelper = new OneYearGoalsHelper(this);
        updateOneYearGoalsList();
        oneYearGoalsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        oneYearGoalsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int positio, long id) {
                deleteOneYearGoalsId(id);
                updateOneYearGoalsList();
                return true;
            }
        });
    }

    private void updateOneYearGoalsList() {
        SQLiteDatabase db = oneYearGoalsHelper.getReadableDatabase();
        Cursor cursor = db.query(OneYearGoalsHelper.TABLE_ONE_YEAR_GOALS,null,null,null,null,null,null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(OneYearGoalsHelper.GOALS_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(OneYearGoalsHelper.GOALS_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        oneYearGoalsArrayAdapter = new GoalsAdapter(this,R.layout.delete_note_layout,notes);
        oneYearGoalsListView.setAdapter(oneYearGoalsArrayAdapter);
    }

    private void deleteOneYearGoalsId(long id) {
        SQLiteDatabase db = oneYearGoalsHelper.getWritableDatabase();
        int deleteRows = db.delete(OneYearGoalsHelper.TABLE_ONE_YEAR_GOALS, OneYearGoalsHelper.GOALS_COLUMN_ID + " = " + id, null);
        db.close();
    }

    public void goalOneYearAddNoteClick(View view){
        int currentOneYearGoalsCount = getOneYearGoalsCount();

        if (currentOneYearGoalsCount < 12){
            showAddOneYearGoals();
        }else {
            Toast.makeText(this, R.string.twelveToast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddOneYearGoals() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveOneYearGoalsToDB(noteText);
                updateOneYearGoalsList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getOneYearGoalsCount() {
        SQLiteDatabase db = oneYearGoalsHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + OneYearGoalsHelper.TABLE_ONE_YEAR_GOALS,null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return  count;
    }
    private void saveOneYearGoalsToDB(String noteText) {
        SQLiteDatabase db = oneYearGoalsHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(OneYearGoalsHelper.GOALS_COLUMN_TEXT,noteText);
        long newRowId = db.insert(OneYearGoalsHelper.TABLE_ONE_YEAR_GOALS,null,values);
        db.close();
    }

    private static class GoalsAdapter extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public GoalsAdapter(Context context, int layoutResourceId,List<Note> notes){
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
                    ((OneYearGoalsActivity)getContext()).deleteOneYearGoalsId(id);
                    ((OneYearGoalsActivity)getContext()).updateOneYearGoalsList();
                }
            });
            return view;
        }
    }
}




































