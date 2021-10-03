package dk.simonsejse.loenberegning;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dk.simonsejse.loenberegning.database.AppDatabase;

public class ShiftApplication extends Application {
    public static AppDatabase database;
    public static ExecutorService THREAD_1 = Executors.newSingleThreadExecutor();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        ShiftApplication.database = Room.databaseBuilder(this, AppDatabase.class, "shift").build();

    }

}
