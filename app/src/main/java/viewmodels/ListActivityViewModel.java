package viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import database.AppRepository;
import database.NoteEntity;


public class ListActivityViewModel extends AndroidViewModel {
    public LiveData<List<NoteEntity>> noteEntityList;
    private Executor executor= Executors.newSingleThreadExecutor();
    private AppRepository appRepository;
    public static final String TAG="TAG";
    public ListActivityViewModel(@NonNull Application application) {
        super(application);
        Log.i(TAG, "ListActivityViewModel: ");
        appRepository=AppRepository.getInstance(application.getApplicationContext());
        noteEntityList=appRepository.noteEntityList;
    }

    public void addSampleData() {
        appRepository.addSampleData();
    }

    public void deleteSampleData() {
        if(appRepository != null){
            appRepository.deleteSampleData();
        }
    }

    public void deletNoteAt(int noteId) {
     executor.execute(new Runnable() {
         @Override
         public void run() {
             appRepository.deleteNote(noteId);
         }
     });
    }
}
