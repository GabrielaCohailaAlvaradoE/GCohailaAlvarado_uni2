package ModeloGECA;

import java.io.Serializable;

public class clsnombreGECA implements Serializable {

    private String etiquetaGeca;
    private long totalGeca;

    public clsnombreGECA() {
    }

    public clsnombreGECA(String etiquetaGeca, long totalGeca) {
        this.etiquetaGeca = etiquetaGeca;
        this.totalGeca = totalGeca;
    }

    public String getEtiquetaGeca() {
        return etiquetaGeca;
    }

    public void setEtiquetaGeca(String etiquetaGeca) {
        this.etiquetaGeca = etiquetaGeca;
    }

    public long getTotalGeca() {
        return totalGeca;
    }

    public void setTotalGeca(long totalGeca) {
        this.totalGeca = totalGeca;
    }
}