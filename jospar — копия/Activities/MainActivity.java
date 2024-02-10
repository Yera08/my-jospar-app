package kz.yerzhan.jospar.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

import kz.yerzhan.jospar.SaveLang.AppCompat;
import kz.yerzhan.jospar.DBHelpers.MainDayDBHelper;
import kz.yerzhan.jospar.DBHelpers.MainMonthHelper;
import kz.yerzhan.jospar.DBHelpers.SecondDayDBHelper;
import kz.yerzhan.jospar.Note;
import kz.yerzhan.jospar.R;


public class MainActivity extends AppCompat {
    private ListView mainDayListView, secondaryListView, mainMonthListView;
    private ArrayAdapter<Note> noteArrayAdapterDay, noteArrayAdapterSecond, mainMonthAdapter;
    private MainMonthHelper mainMonthHelper;
    private MainDayDBHelper mainDayDBHelper;
    private SecondDayDBHelper secondDayDBHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainMonthListView = findViewById(R.id.mainMonthListView);
        mainDayListView = findViewById(R.id.mainDayListView);
        secondaryListView = findViewById(R.id.secondaryListView);
        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.main);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.main) {
                return true;
            } else if (itemID == R.id.menu) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            } else if (itemID == R.id.note) {
                startActivity(new Intent(getApplicationContext(), ToDoActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            }
            return false;
        });

        mainMonthHelper = new MainMonthHelper(this);
        updateMainMonthList();
        mainDayDBHelper = new MainDayDBHelper(this);
        updateNoteList_day();
        secondDayDBHelper = new SecondDayDBHelper(this);
        updateNoteList_second();

        mainMonthListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        });
        mainMonthListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteMainMonthId(id);
                updateMainMonthList();
                return true;
            }
        });

        mainDayListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        });
        mainDayListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteNoteById_day(id);
                updateNoteList_day();
                return true;
            }
        });

        secondaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        });
        secondaryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteNoteById_second(id);
                updateNoteList_second();
                return true;
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

        mainMonthAdapter = new MainActivity.MainMonthAdapter(this,R.layout.note_layout,notes);
        mainMonthListView.setAdapter(mainMonthAdapter);
    }

    private void deleteMainMonthId(long id) {
        SQLiteDatabase db = mainMonthHelper.getWritableDatabase();
        int deleteRows = db.delete(MainMonthHelper.TABLE_MAIN_MONTH,MainMonthHelper.MAIN_MONTH_COLUMN_ID + " = " + id,null);
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

            noteText.setText(note.getDescription());
            return view;
        }
    }

    private void updateNoteList_day() {
        SQLiteDatabase db = mainDayDBHelper.getReadableDatabase();
        Cursor cursor = db.query(MainDayDBHelper.TABLE_NOTES_MAIN_DAY, null, null, null, null, null, null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(MainDayDBHelper.MAIN_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(MainDayDBHelper.MAIN_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        noteArrayAdapterDay = new NoteAdapterDay(this, R.layout.delete_note_layout,notes);
        mainDayListView.setAdapter(noteArrayAdapterDay);
    }

    private void deleteNoteById_day(long id) {
        SQLiteDatabase db = mainDayDBHelper.getWritableDatabase();
        int deletedRows = db.delete(MainDayDBHelper.TABLE_NOTES_MAIN_DAY,MainDayDBHelper.MAIN_COLUMN_ID + " = " + id, null);
        db.close();
    }

    public void addNoteClickMainDay(View view) {
        int currentNoteCount = getNoteCount_day();

        if(currentNoteCount < 3){
            showAddNoteDialog_day();
        }else {
            Toast.makeText(this, R.string.mainDayToast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddNoteDialog_day() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveNoteToDB_day(noteText);
                updateNoteList_day();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private int getNoteCount_day() {
        SQLiteDatabase db = mainDayDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + MainDayDBHelper.TABLE_NOTES_MAIN_DAY, null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return count;
    }

    private void saveNoteToDB_day(String noteText) {
        SQLiteDatabase db = mainDayDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MainDayDBHelper.MAIN_COLUMN_TEXT, noteText);
        long newRowId = db.insert(MainDayDBHelper.TABLE_NOTES_MAIN_DAY,null,values);
        db.close();

    }

    private static class NoteAdapterDay extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public NoteAdapterDay(Context context, int layoutResourceId, List<Note> notes){
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
                    ((MainActivity)getContext()).deleteNoteById_day(id);
                    ((MainActivity)getContext()).updateNoteList_day();
                }
            });
            return view;
        }
    }

    private void updateNoteList_second() {
        SQLiteDatabase db = secondDayDBHelper.getReadableDatabase();
        Cursor cursor = db.query(SecondDayDBHelper.TABLE_NOTES_SECONDARY, null, null, null, null, null, null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(SecondDayDBHelper.SECOND_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(SecondDayDBHelper.SECOND_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        noteArrayAdapterSecond = new NoteAdapterSecond(this, R.layout.delete_note_layout,notes);
        secondaryListView.setAdapter(noteArrayAdapterSecond);
    }

    private void deleteNoteById_second(long id) {
        SQLiteDatabase db = secondDayDBHelper.getWritableDatabase();
        int deletedRows = db.delete(SecondDayDBHelper.TABLE_NOTES_SECONDARY,SecondDayDBHelper.SECOND_COLUMN_ID + " = " + id, null);
        db.close();
    }

    public void addNoteClickSecond(View view) {
        int currentNoteCount = getNoteCount_second();

        if(currentNoteCount < 5){
            showAddNoteDialog_second();
        }else {
            Toast.makeText(this,R.string.secondaryToast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddNoteDialog_second() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveNoteToDB_second(noteText);
                updateNoteList_second();
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private int getNoteCount_second() {
        SQLiteDatabase db = secondDayDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + SecondDayDBHelper.TABLE_NOTES_SECONDARY, null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }

    private void saveNoteToDB_second(String noteText) {
        SQLiteDatabase db = secondDayDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SecondDayDBHelper.SECOND_COLUMN_TEXT, noteText);
        long newRowId = db.insert(SecondDayDBHelper.TABLE_NOTES_SECONDARY,null,values);
        db.close();
    }

    private static class NoteAdapterSecond extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public NoteAdapterSecond (Context context, int layoutResourceId, List<Note> notes ){
            super(context, layoutResourceId, notes);
            this.notes = notes;
            this.layoutResourceId = layoutResourceId;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = convertView;
            if (view == null){
                view = inflater.inflate(layoutResourceId, parent,false);
            }
            Note note = notes.get(position);
            TextView noteText = view.findViewById(R.id.noteText);
            ImageView deleteIcon = view.findViewById(R.id.deleteIcon);

            noteText.setText(note.getDescription());
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long id = note.getId();
                    ((MainActivity)getContext()).deleteNoteById_second(id);
                    ((MainActivity)getContext()).updateNoteList_second();
                }
            });
            return view;
        }
    }

}








































