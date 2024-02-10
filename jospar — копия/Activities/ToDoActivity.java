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
import kz.yerzhan.jospar.DBHelpers.ToDoDBHelper;
import kz.yerzhan.jospar.Note;
import kz.yerzhan.jospar.R;

public class ToDoActivity extends AppCompat {
    private ListView todoListView;
    private ArrayAdapter<Note> noteArrayAdapter;
    private ToDoDBHelper toDoDBHelper;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);

        todoListView = findViewById(R.id.todoListView);
        bottomNavigationView = findViewById(R.id.bottomNav);

        bottomNavigationView.setSelectedItemId(R.id.note);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            if (itemID == R.id.note){
                return true;
            } else if (itemID == R.id.menu) {
                startActivity(new Intent(getApplicationContext(), MenuActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            }else if (itemID == R.id.main) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.slide_left, R.anim.slide_right);
                return true;
            }
            return false;
        });

        toDoDBHelper = new ToDoDBHelper(this);
        updateToDoList();

        todoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {}
        });
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                deleteToDoById(id);
                updateToDoList();
                return true;
            }
        });
    }

    private void updateToDoList() {
        SQLiteDatabase db = toDoDBHelper.getReadableDatabase();
        Cursor cursor = db.query(ToDoDBHelper.TABLE_TO_DO,null,null,null,null,null,null);

        List<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            long noteId = cursor.getLong(cursor.getColumnIndexOrThrow(ToDoDBHelper.TODO_COLUMN_ID));
            String text = cursor.getString(cursor.getColumnIndexOrThrow(ToDoDBHelper.TODO_COLUMN_TEXT));

            Note note = new Note();
            note.setId(noteId);
            note.setDescription(text);
            notes.add(note);
        }
        cursor.close();
        db.close();

        noteArrayAdapter = new NoteAdapter(this,R.layout.delete_note_layout,notes);
        todoListView.setAdapter(noteArrayAdapter);
    }
    private void deleteToDoById(long id){
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();
        int deleteRows = db.delete(ToDoDBHelper.TABLE_TO_DO,ToDoDBHelper.TODO_COLUMN_ID + " = " + id, null);
        db.close();
    }
    public void addNoteClick(View view){
        int currentNoteCount = getNoteCount();

        if (currentNoteCount < 15){
            showAddNoteDialog();
        }else {
            Toast.makeText(this, R.string.toDoToast, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddNoteDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_note);

        EditText noteET = dialog.findViewById(R.id.noteET);
        Button saveBtn = dialog.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteText = noteET.getText().toString();
                saveNoteToDB(noteText);
                updateToDoList();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private int getNoteCount() {
        SQLiteDatabase db = toDoDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + ToDoDBHelper.TABLE_TO_DO,null);

        int count = 0;
        if (cursor.moveToFirst()){
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return count;
    }
    private void saveNoteToDB(String noteText) {
        SQLiteDatabase db = toDoDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoDBHelper.TODO_COLUMN_TEXT, noteText);
        long newRowId = db.insert(ToDoDBHelper.TABLE_TO_DO,null,values);
        db.close();
    }

    private static class NoteAdapter extends ArrayAdapter<Note>{
        private List<Note> notes;
        private int layoutResourceId;
        private LayoutInflater inflater;

        public NoteAdapter (Context context,int layoutResourceId,List<Note> notes){
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
                    ((ToDoActivity)getContext()).deleteToDoById(id);
                    ((ToDoActivity)getContext()).updateToDoList();
                }
            });
            return view;
        }
    }
}


































