package database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {NoteEntity.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DATABASE_NAME="notesdatabase.db";
    private static volatile AppDatabase appDatabaseInstance;
    private static final Object LOCK=new Object();
    public static final String TAG="TAG";
    public abstract NotesDao notesDao();
    public static AppDatabase getAppDatabaseInstance(Context context) {
        if(appDatabaseInstance == null){
            synchronized (LOCK){
                if(appDatabaseInstance == null)
                    appDatabaseInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME).build();
                Log.i(TAG, "getAppDatabaseInstance: ");
            }
        }
        return appDatabaseInstance;
    }
}

