package com.harry.example.notetakingapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import database.NoteEntity;
import viewmodels.EditorViewModel;

public class EditorActivity extends AppCompatActivity {

    private EditorViewModel editorViewModel;
    private String oldContent;
    private String newContent;
    private boolean canBeDeleted;
    private boolean orientation_state_changed;
    private int noteId;
    @BindView(R.id.edit_content)
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orientation_state_changed=checkPrevoiusInstance(savedInstanceState);
        initViewModel();
        Intent intent = getIntent();
        if (intent.hasExtra("NOTE_ID")) {
            getSupportActionBar().setTitle("Edit Note");
            noteId = intent.getIntExtra("NOTE_ID", -1);
            getNote(noteId);
            canBeDeleted = true;
        } else {
            getSupportActionBar().setTitle("New Note");
            canBeDeleted = false;
        }
    }

    private boolean checkPrevoiusInstance(Bundle instance) {
        if(instance != null){
            return instance.getBoolean("ORIENTATION_STATE");
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        outState.putBoolean("ORIENTATION_STATE",true);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (canBeDeleted) {
            getMenuInflater().inflate(R.menu.menu_editor, menu);
        }
        return true;
    }

    private void getNote(int noteId) {
        editorViewModel.getNote(noteId);
    }

    private void initViewModel() {
        Observer<NoteEntity> noteEntityObserver = new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {
                if (noteEntity != null && !orientation_state_changed) {
                    oldContent = noteEntity.getContent();
                    editText.setText(oldContent);
                }
            }
        };
        editorViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        editorViewModel.mutableLiveData.observe(EditorActivity.this, noteEntityObserver);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAndExit();
        } else if (item.getItemId() == R.id.delete_note) {
            deleteAndExit();
        }
        return true;
    }

    private void deleteAndExit() {
        editorViewModel.deleteNote(noteId);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAndExit();

    }

    private void saveAndExit() {
        newContent = editText.getText().toString();
        NoteEntity noteEntity = editorViewModel.mutableLiveData.getValue();
        if (noteEntity != null && !newContent.equalsIgnoreCase(oldContent)) {
            noteEntity.setContent(newContent.trim());
            noteEntity.setDate(new Date());
        } else {
            if (!TextUtils.isEmpty(newContent.trim())) {
                noteEntity = new NoteEntity(new Date(), newContent.trim());
            } else {
                finish();
                return;
            }
        }
        editorViewModel.insertNote(noteEntity);
        finish();
    }
}
