package ofa.cursos.android.app02.appreclamos.modelo;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseManager {

    private int opencounter;
    private static  DataBaseManager instance;
    private static SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper){
        if (instance==null){
            instance = new DataBaseManager();
            dbHelper = helper;
        }
    }

    public static synchronized DataBaseManager getInstance(){
        if (instance == null){
            throw new IllegalStateException(DataBaseManager.class.getSimpleName() + "no esta inicializada, primero call initializeInstance() method ");

        }
        return instance;
    }

    public synchronized SQLiteDatabase openDatabase(){
        opencounter++;
        if (opencounter == 1){
            db = dbHelper.getWritableDatabase();
        }
        return db;
    }

    public synchronized void closeDatabase(){
        opencounter--;
        if (opencounter == 0){
            db.close();
        }
    }

}
