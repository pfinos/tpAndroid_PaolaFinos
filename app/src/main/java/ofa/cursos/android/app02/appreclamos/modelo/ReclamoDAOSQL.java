package ofa.cursos.android.app02.appreclamos.modelo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ReclamoDAOSQL implements ReclamoDAO {

    DataBaseManager db;
    MyReclamoOpenHelper myReclamoOpenHelper;

    public ReclamoDAOSQL(Context ctx){
        Log.d("APP_RECLAMOS","new ReclamoDAOSQL()");
        myReclamoOpenHelper =new MyReclamoOpenHelper(ctx);
        Log.d("APP_RECLAMOS","new MyReclamoOpenHelper(");
        DataBaseManager.initializeInstance(myReclamoOpenHelper);
        DataBaseManager.getInstance().openDatabase();
    }

    @Override
    public void agregar(Reclamo rec) {
        ContentValues values = new ContentValues();

        //DESCRIPCION TEXT, LAT FLOAT, LNG FLOAT, MAIL TEXT, RESUELTO INTEGER, PATHIMG TEXT
        values.put("DESCRIPCION",rec.getDescripcion());
        values.put("LAT",rec.getUbicacion().latitude);
        values.put("LNG",rec.getUbicacion().longitude);
        values.put("MAIL",rec.getMailContacto());
        values.put("RESUELTO",0);
        values.put("PATHIMG",rec.getPathImagen());
        long result=myReclamoOpenHelper.getWritableDatabase().insert("RECLAMOS",null,values);
        Log.d("APP_RECLAMOS", "ReclamoDAO: agregar Reclamo --> result insert="+result);

        DataBaseManager.getInstance().closeDatabase();

    }

    @Override
    public List<Reclamo> listar() {

        List<Reclamo> lista = new ArrayList<>();

        Cursor cursor=myReclamoOpenHelper.getReadableDatabase().rawQuery("SELECT * FROM RECLAMOS ORDER BY _id ASC ",null);

        while (cursor.moveToNext()){
            Reclamo aux = new Reclamo();
            aux.setId(cursor.getInt(0));
            aux.setDescripcion(cursor.getString(1));
            LatLng ub = new LatLng(cursor.getDouble(2),cursor.getDouble(3));
            aux.setUbicacion(ub);
            aux.setMailContacto(cursor.getString(4));
            aux.setResuelto((cursor.getInt(5)==1));
            aux.setPathImagen(cursor.getString(6));
            Log.d("APP_RECLAMOS","ReclamoDAOSQL.listar() --> reclamo: "+aux.toString());
            lista.add(aux);
        }
        cursor.close();
        DataBaseManager.getInstance().closeDatabase();


        return lista;
    }

    @Override
    public void resolverReclamo(Reclamo rec) {

        DataBaseManager.getInstance().openDatabase();

        String[] clause=new String[1];
        clause[0]=rec.getId()+"";

        ContentValues values = new ContentValues();
        values.put("RESUELTO",1);

        int can=myReclamoOpenHelper.getWritableDatabase().update("RECLAMOS",values,"_id=?",clause);

        DataBaseManager.getInstance().closeDatabase();
    }
}
