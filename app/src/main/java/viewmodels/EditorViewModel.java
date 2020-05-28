package viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.harry.example.notetakingapp.R;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import database.AppRepository;
import database.NoteEntity;

public class EditorViewModel extends AndroidViewModel {
    private Executor executor= Executors.newSingleThreadExecutor();
    public MutableLiveData<NoteEntity> mutableLiveData=new MutableLiveData<>();
    private AppRepository appRepository;
    public EditorViewModel(@NonNull Application application) {
        super(application);
        appRepository=AppRepository.getInstance(application.getApplicationContext());
    }

    public void getNote(int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                NoteEntity noteEntity=appRepository.getNote(noteId);
                mutableLiveData.postValue(noteEntity);
            }
        });
    }

    public void insertNote(NoteEntity noteEntity) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appRepository.updateNote(noteEntity);
            }
        });
    }

    public void deleteNote(int noteId) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                appRepository.deleteNote(noteId);
            }
        });
    }
}
