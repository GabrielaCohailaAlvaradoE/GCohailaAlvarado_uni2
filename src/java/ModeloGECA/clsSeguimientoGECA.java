package ModeloGECA;

import java.io.Serializable;
import java.time.LocalDateTime;

public class clsSeguimientoGECA implements Serializable {

    private int idSeguimientoGeca;
    private int idReclamoGeca;
    private int idUsuarioGeca;
    private String accionGeca;
    private String observacionesGeca;
    private LocalDateTime fechaSeguimientoGeca;
    private String nuevoEstadoGeca;
    private String nombreUsuarioGeca;

    public clsSeguimientoGECA() {
    }

    public int getIdSeguimientoGeca() {
        return idSeguimientoGeca;
    }

    public void setIdSeguimientoGeca(int idSeguimientoGeca) {
        this.idSeguimientoGeca = idSeguimientoGeca;
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

    public String getAccionGeca() {
        return accionGeca;
    }

    public void setAccionGeca(String accionGeca) {
        this.accionGeca = accionGeca;
    }

    public String getObservacionesGeca() {
        return observacionesGeca;
    }

    public void setObservacionesGeca(String observacionesGeca) {
        this.observacionesGeca = observacionesGeca;
    }

    public LocalDateTime getFechaSeguimientoGeca() {
        return fechaSeguimientoGeca;
    }

    public void setFechaSeguimientoGeca(LocalDateTime fechaSeguimientoGeca) {
        this.fechaSeguimientoGeca = fechaSeguimientoGeca;
    }

    public String getNuevoEstadoGeca() {
        return nuevoEstadoGeca;
    }

    public void setNuevoEstadoGeca(String nuevoEstadoGeca) {
        this.nuevoEstadoGeca = nuevoEstadoGeca;
    }

    public String getNombreUsuarioGeca() {
        return nombreUsuarioGeca;
    }

    public void setNombreUsuarioGeca(String nombreUsuarioGeca) {
        this.nombreUsuarioGeca = nombreUsuarioGeca;
    }
}
