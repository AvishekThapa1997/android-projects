package database;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import utility.SampleDataProvider;

public class AppRepository {
    public static final String TAG="TAG";
    private AppDatabase appDatabase;
    private static AppRepository appRepository;
    private Executor executor= Executors.newSingleThreadExecutor();
    public LiveData<List<NoteEntity>>  noteEntityList;
    private AppRepository(Context context){
        Log.i("TAG", "AppRepository: ");
        appDatabase=AppDatabase.getAppDatabaseInstance(context);
        noteEntityList=getAllNotes();

    }
    public static AppRepository getInstance(Context context) {
        return appRepository=new AppRepository(context) ;
    }

    public void addSampleData() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: "+"App Sample Data");
                appDatabase.notesDao().insertAll(SampleDataProvider.getSampleData());
            }
        });
    }
    private LiveData<List<NoteEntity>> getAllNotes(){
        Log.i(TAG, "getAllNotes: ");
        return appDatabase.notesDao().getAllNotes();
    }

    public void deleteSampleData() {
        if(appDatabase != null){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    appDatabase.notesDao().deleteAllNotes();
                }
            });
        }
    }

    public NoteEntity getNote(int noteId) {
        return appDatabase.notesDao().getNoteById(noteId);
    }

    public void updateNote(NoteEntity noteEntity) {
        appDatabase.notesDao().insertNote(noteEntity);
    }

    public void deleteNote(int noteId) {
        appDatabase.notesDao().deleteNote(noteId);
    }
}
