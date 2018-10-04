package ofa.cursos.android.app02.appreclamos.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyReclamoOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "misreclamos.db";
    public static final int version=1;

    public MyReclamoOpenHelper(Context context){
        super(context,DATABASE_NAME,null,version);
    }
    /*
    private String descripcion;
    private LatLng ubicacion;
    private Boolean resuelto;
    private String mailContacto;
    private String pathImagen;
    */
    public void onCreate (SQLiteDatabase base){
        base.execSQL("CREATE TABLE RECLAMOS (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                     "DESCRIPCION TEXT, LAT DOUBLE, LNG DOUBLE, MAIL TEXT, RESUELTO INTEGER, PATHIMG TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase base, int i, int i1) {



    }


}
