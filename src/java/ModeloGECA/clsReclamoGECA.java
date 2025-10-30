package ModeloGECA;

import java.io.Serializable;
import java.time.LocalDateTime;

public class clsReclamoGECA implements Serializable {

    private int idReclamoGeca;
    private int idUsuarioGeca;
    private Integer idCategoriaGeca;
    private String tituloReclamoGeca;
    private String descripcionReclamoGeca;
    private LocalDateTime fechaCreacionGeca;
    private LocalDateTime fechaActualizacionGeca;
    private String estadoGeca;
    private String prioridadGeca;
    private String nombreCategoriaGeca;

    public clsReclamoGECA() {
    }

    public int getIdReclamoGeca() {
        return idReclamoGeca;
    }

    public void setIdReclamoGeca(int idReclamoGeca) {
        this.idReclamoGeca = idReclamoGeca;
    }

    public int getIdUsuarioGeca() {
        return idUsuarioGeca;
    }

    public void setIdUsuarioGeca(int idUsuarioGeca) {
        this.idUsuarioGeca = idUsuarioGeca;
    }

    public Integer getIdCategoriaGeca() {
        return idCategoriaGeca;
    }

    public void setIdCategoriaGeca(Integer idCategoriaGeca) {
        this.idCategoriaGeca = idCategoriaGeca;
    }

    public String getTituloReclamoGeca() {
        return tituloReclamoGeca;
    }

    public void setTituloReclamoGeca(String tituloReclamoGeca) {
        this.tituloReclamoGeca = tituloReclamoGeca;
    }

    public String getDescripcionReclamoGeca() {
        return descripcionReclamoGeca;
    }

    public void setDescripcionReclamoGeca(String descripcionReclamoGeca) {
        this.descripcionReclamoGeca = descripcionReclamoGeca;
    }

    public LocalDateTime getFechaCreacionGeca() {
        return fechaCreacionGeca;
    }

    public void setFechaCreacionGeca(LocalDateTime fechaCreacionGeca) {
        this.fechaCreacionGeca = fechaCreacionGeca;
    }

    public LocalDateTime getFechaActualizacionGeca() {
        return fechaActualizacionGeca;
    }

    public void setFechaActualizacionGeca(LocalDateTime fechaActualizacionGeca) {
        this.fechaActualizacionGeca = fechaActualizacionGeca;
    }

    public String getEstadoGeca() {
        return estadoGeca;
    }

    public void setEstadoGeca(String estadoGeca) {
        this.estadoGeca = estadoGeca;
    }

    public String getPrioridadGeca() {
        return prioridadGeca;
    }

    public void setPrioridadGeca(String prioridadGeca) {
        this.prioridadGeca = prioridadGeca;
    }

    public String getNombreCategoriaGeca() {
        return nombreCategoriaGeca;
    }

    public void setNombreCategoriaGeca(String nombreCategoriaGeca) {
        this.nombreCategoriaGeca = nombreCategoriaGeca;
    }
}
