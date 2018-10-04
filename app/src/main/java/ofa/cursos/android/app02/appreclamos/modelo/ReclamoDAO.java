package ofa.cursos.android.app02.appreclamos.modelo;

import java.util.List;

public interface ReclamoDAO {

    public void agregar(Reclamo rec);
    public List<Reclamo> listar();
    public void resolverReclamo(Reclamo rec);
}
