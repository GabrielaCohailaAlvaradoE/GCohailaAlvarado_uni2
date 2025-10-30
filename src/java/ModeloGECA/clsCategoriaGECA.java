package ModeloGECA;

import java.io.Serializable;

public class clsCategoriaGECA implements Serializable {

    private int idCategoriaGeca;
    private String nombreCategoriaGeca;
    private String descripcionCategoriaGeca;

    public clsCategoriaGECA() {
    }

    public clsCategoriaGECA(int idCategoriaGeca, String nombreCategoriaGeca, String descripcionCategoriaGeca) {
        this.idCategoriaGeca = idCategoriaGeca;
        this.nombreCategoriaGeca = nombreCategoriaGeca;
        this.descripcionCategoriaGeca = descripcionCategoriaGeca;
    }

    public int getIdCategoriaGeca() {
        return idCategoriaGeca;
    }

    public void setIdCategoriaGeca(int idCategoriaGeca) {
        this.idCategoriaGeca = idCategoriaGeca;
    }

    public String getNombreCategoriaGeca() {
        return nombreCategoriaGeca;
    }

    public void setNombreCategoriaGeca(String nombreCategoriaGeca) {
        this.nombreCategoriaGeca = nombreCategoriaGeca;
    }

    public String getDescripcionCategoriaGeca() {
        return descripcionCategoriaGeca;
    }

    public void setDescripcionCategoriaGeca(String descripcionCategoriaGeca) {
        this.descripcionCategoriaGeca = descripcionCategoriaGeca;
    }
}