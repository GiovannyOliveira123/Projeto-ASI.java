
package utilitarios;

import java.time.LocalDate;

public abstract class Analisador {
    
    protected int id;
protected int nivelRisco;
protected String resultado;
protected int idUsuario;
protected LocalDate dataAnalise;

public int getId() {
    return id;
}

public void setId(int id) {
    this.id = id;
}

public int getNivelRisco() {
    return nivelRisco;
}

    public void setNivelRisco(int nivelRisco) {
        this.nivelRisco = nivelRisco;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public LocalDate getDataAnalise() {
        return dataAnalise;
    }

    public void setDataAnalise(LocalDate dataAnalise) {
        this.dataAnalise = dataAnalise;
    }



}