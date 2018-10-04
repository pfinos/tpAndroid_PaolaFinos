package ofa.cursos.android.app02.appreclamos;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ofa.cursos.android.app02.appreclamos.modelo.Reclamo;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAO;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAOSQL;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    LatLngBounds latLngBounds = new LatLngBounds(new LatLng(-31.700,-60.860), new LatLng(-31.480,-60.500));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reclamos_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuListaReclamos: {
                Intent intent = new Intent();
                intent.setClass(MapsActivity.this,ListReclamosActivity.class);
                startActivityForResult(intent,49);
            }
            default:
                return super.onOptionsItemSelected(item);
        }
        //return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng location = new LatLng(-31.6186951, -60.7019561);
        mMap.setLatLngBoundsForCameraTarget(latLngBounds);

        /* Show rationale and request permission. */
        // Add a marker in Sydney and move the camera
        mMap.addMarker(new MarkerOptions().position(location).title("Santa Fe"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setMinZoomPreference((float)13);



        ReclamoDAO reclamoDAO = new ReclamoDAOSQL(this);
        List<Reclamo> reclamos = reclamoDAO.listar();

        for(Reclamo rec : reclamos){
            //marcar en el mapa
            float color = rec.getResuelto()?BitmapDescriptorFactory.HUE_BLUE:BitmapDescriptorFactory.HUE_YELLOW; //reclamo no resuelto
            MarkerOptions mark = new MarkerOptions();
            mark.position(rec.getUbicacion()).title(rec.getMailContacto()).icon(BitmapDescriptorFactory
                    .defaultMarker(color));
            mMap.addMarker(mark);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {

                String punto="Lat.: "+latLng.latitude + " - Long: "+latLng.longitude;

                Intent intent = new Intent();
                intent.setClass(MapsActivity.this,MainActivity.class);
                intent.putExtra("punto",latLng);
                startActivityForResult(intent,99);
            }
        });

        Log.d("APP_RECLAMOS", "ACA ESTOY");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if ((requestCode == 99) && (resultCode == RESULT_OK)){
            String reclamoJSON = data.getStringExtra("nuevoreclamo");

            Reclamo rec = new Reclamo();
            try {
                rec.loadFromJson(new JSONObject(reclamoJSON));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //marcar en el mapa el nuevo reclamo
            float color = rec.getResuelto()? BitmapDescriptorFactory.HUE_BLUE:BitmapDescriptorFactory.HUE_YELLOW; //reclamo no resuelto
            MarkerOptions mark = new MarkerOptions();
            mark.position(rec.getUbicacion()).title(rec.getMailContacto()).icon(BitmapDescriptorFactory
                    .defaultMarker(color));
            mMap.addMarker(mark);
        }else
            if ((requestCode == 49) && (resultCode == RESULT_OK)) {

                ReclamoDAO reclamoDAO = new ReclamoDAOSQL(this);
                List<Reclamo> reclamos = reclamoDAO.listar();

                for (Reclamo rec : reclamos) {
                    //marcar en el mapa
                    float color = rec.getResuelto() ? BitmapDescriptorFactory.HUE_BLUE : BitmapDescriptorFactory.HUE_YELLOW; //reclamo no resuelto
                    MarkerOptions mark = new MarkerOptions();
                    mark.position(rec.getUbicacion()).title(rec.getMailContacto()).icon(BitmapDescriptorFactory
                            .defaultMarker(color));
                    mMap.addMarker(mark);
                }
        }

    }
}
