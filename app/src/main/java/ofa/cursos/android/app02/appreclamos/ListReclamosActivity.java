package ofa.cursos.android.app02.appreclamos;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import ofa.cursos.android.app02.appreclamos.modelo.Reclamo;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAO;
import ofa.cursos.android.app02.appreclamos.modelo.ReclamoDAOSQL;

public class ListReclamosActivity extends AppCompatActivity {

    private ReclamoDAO reclamoDAO;
    private ArrayAdapter<Reclamo> adapterReclamo;

    private Button buttonV;
    private ListView listareclamos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_reclamos);

        this.reclamoDAO = new ReclamoDAOSQL(this);
        this.adapterReclamo = new ArrayAdapter<>(ListReclamosActivity.this,android.R.layout.simple_list_item_1,this.reclamoDAO.listar());
        this.listareclamos = (ListView) this.findViewById(R.id.listItems);
        this.listareclamos.setAdapter(this.adapterReclamo);
        this.listareclamos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Reclamo item = (Reclamo) listareclamos.getItemAtPosition(i);
                reclamoDAO.resolverReclamo(item);
                Toast.makeText(ListReclamosActivity.this,"¡Reclamo Resuelto!",Toast.LENGTH_LONG).show();
                return false;
            }
        });

        this.buttonV = (Button) findViewById(R.id.button2);
        this.buttonV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //volver al  mapa
                Intent returnMap = new Intent(ListReclamosActivity.this,MapsActivity.class);
                setResult(Activity.RESULT_OK,returnMap);
                finish();
            }
        });
    }
}
