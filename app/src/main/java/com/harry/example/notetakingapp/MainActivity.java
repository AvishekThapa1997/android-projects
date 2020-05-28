package com.harry.example.notetakingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import adapter.CustomAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import database.NoteEntity;
import utility.SampleDataProvider;
import viewmodels.ListActivityViewModel;

public class MainActivity extends AppCompatActivity implements CustomAdapter.EditNote {
    private List<NoteEntity> noteEntities = new ArrayList<>();
    private CustomAdapter customAdapter;
    private ListActivityViewModel listActivityViewModel;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.add_note)
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setUpRecyclerView();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
        initViewModel();
    }


    private void initViewModel() {
        Observer<List<NoteEntity>> nListObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                Log.i("TAG", "onChanged: " + noteEntities.size());
                MainActivity.this.noteEntities.clear();
                MainActivity.this.noteEntities.addAll(noteEntities);
                if (customAdapter == null) {
                    customAdapter = new CustomAdapter(MainActivity.this, MainActivity.this.noteEntities);
                    recyclerView.setAdapter(customAdapter);
                } else {
                    customAdapter.notifyDataSetChanged();
                }
            }
        };
        listActivityViewModel = new ViewModelProvider(this).get(ListActivityViewModel.class);
        listActivityViewModel.noteEntityList.observe(MainActivity.this, nListObserver);
    }

    private void showData() {
    }

    private void setUpRecyclerView() {
        recyclerView.hasFixedSize();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                NoteEntity noteEntity = customAdapter.getNoteAt(viewHolder.getAdapterPosition());
                deleteNoteAt(noteEntity.getId());
                Toast.makeText(getApplicationContext(), "DELETED SUCCESSFULLY", Toast.LENGTH_LONG).show();
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void deleteNoteAt(int noteId) {
        listActivityViewModel.deletNoteAt(noteId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menu_id = item.getItemId();
        switch (menu_id) {
            case R.id.add_sample_data:
                addSampleData();
                break;
            case R.id.delete_sample_data:
                deleteSampleData();
        }
        return true;
    }

    private void deleteSampleData() {
        if (listActivityViewModel != null) {
            listActivityViewModel.deleteSampleData();
        }
    }

    private void addSampleData() {
        listActivityViewModel.addSampleData();
    }

    @Override
    public void editNoteOfId(int noteId) {
        Log.i("TAG", "editNoteOfId: " + noteId);
        Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
        intent.putExtra("NOTE_ID", noteId);
        startActivity(intent);
    }
}
