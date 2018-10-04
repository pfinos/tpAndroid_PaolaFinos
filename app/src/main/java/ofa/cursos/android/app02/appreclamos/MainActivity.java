package ofa.cursos.android.app02.appreclamos;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ofa.cursos.android.app02.appreclamos.modelo.Reclamo;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAO;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAOSQL;

public class MainActivity extends AppCompatActivity {

    private EditText etReclamo;
    private EditText etCorreo;
    private ImageView imageView;
    private Button btnSubir;
    private Button btnReclamo;
    private Button btnCancelar;

    private String path;
    private Activity mainActivity;

    private static Integer ID_PHOTO=0;
    private Integer idphoto;
    private static final int  REQUEST_IMAGE_CAPTURE=1;

    private ReclamoDAO reclamoDAOSQL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        this.mainActivity = this;

        final LatLng punto = (LatLng)getIntent().getParcelableExtra("punto");
        Log.d("APP_RECLAMOS", "Lat.: "+punto.latitude+" - long.: "+punto.longitude);

        this.etReclamo = (EditText) findViewById(R.id.etReclamo);
        this.etCorreo = (EditText) findViewById(R.id.etCorreo);
        this.imageView = (ImageView) findViewById(R.id.photo);

        this.reclamoDAOSQL = new ReclamoDAOSQL(this.mainActivity);

        this.btnCancelar = (Button) findViewById(R.id.btnCancelar);
        this.btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reclamoDAOSQL.listar();
                etReclamo.setText("");
                etCorreo.setText("");
                imageView.setImageBitmap(null);
                /*
                Reclamo aux = new Reclamo();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                String dato = aux.toJson().toString();
                intent.putExtra("dato",dato);
                setResult(Activity.RESULT_CANCELED,intent);
                */
            }
        });

        this.btnSubir = (Button) findViewById(R.id.btnSubir);
        this.btnSubir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Sacar y subir la foto!

                Intent take = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (take.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(take, REQUEST_IMAGE_CAPTURE);
                }
            }
        });

        this.btnReclamo = (Button) findViewById(R.id.btnReclamo);
        this.btnReclamo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Reclamo aux = new Reclamo();
                aux.setUbicacion(punto);
                aux.setDescripcion(etReclamo.getText().toString());
                aux.setMailContacto(etCorreo.getText().toString());
                aux.setResuelto(false);
                //path a la imagen
                aux.setPathImagen(path);

                reclamoDAOSQL.agregar(aux);//inserta el reclamo en la tabla SQLite!

                etReclamo.setText("");
                etCorreo.setText("");
                imageView.setImageBitmap(null);

                Toast.makeText(MainActivity.this,"El reclamo se registró con exito",Toast.LENGTH_LONG).show();

                //volver al  mapa con el reclamo recien creado marcado en el mapa
                Intent returnMap = new Intent(MainActivity.this, MapsActivity.class);
                String dato = aux.toJson().toString();
                returnMap.putExtra("nuevoreclamo",dato);
                setResult(Activity.RESULT_OK,returnMap);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            this.imageView.setImageBitmap(imageBitmap);
            this.imageView.setVisibility(View.VISIBLE);
            this.path = this.saveStorage(imageBitmap);

        }
    }

    private String saveStorage(Bitmap bitmapimage){
        String path="";

        this.idphoto = ++ID_PHOTO;

            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File output = new File(dir, "photo"+this.idphoto+".jpeg");

            FileOutputStream fos=null;
            try{
                fos=new FileOutputStream(output);
                bitmapimage.compress(Bitmap.CompressFormat.PNG,100, fos);

            }catch (Exception e){
                Log.d("APP_RECLAMOS"," --> "+e.getMessage());
            }finally {
                try{
                    fos.close();
                }catch (IOException e){
                    Log.d("APP_RECLAMOS"," --> "+e.getMessage());
                }
            }

            path=dir.getAbsolutePath();

            Log.d("APP_RECLAMOS","path a la imagen capturada: "+path);

        return "file://" + path + "/photo"+this.idphoto+".jpeg";
    }

}
