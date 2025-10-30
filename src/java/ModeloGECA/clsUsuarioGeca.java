package ModeloGECA;

import java.io.Serializable;
import java.time.LocalDateTime;

public class clsUsuarioGeca implements Serializable {

    private int idUsuarioGeca;
    private String nombreGeca;
    private String emailGeca;
    private String passwordGeca;
    private String rolGeca;
    private String ipPermitidaGeca;
    private LocalDateTime fechaRegistroGeca;
    private String estadoGeca;

    public clsUsuarioGeca() {
    }

    public clsUsuarioGeca(int idUsuarioGeca, String nombreGeca, String emailGeca, String passwordGeca,
            String rolGeca, String ipPermitidaGeca, LocalDateTime fechaRegistroGeca, String estadoGeca) {
        this.idUsuarioGeca = idUsuarioGeca;
        this.nombreGeca = nombreGeca;
        this.emailGeca = emailGeca;
        this.passwordGeca = passwordGeca;
        this.rolGeca = rolGeca;
        this.ipPermitidaGeca = ipPermitidaGeca;
        this.fechaRegistroGeca = fechaRegistroGeca;
        this.estadoGeca = estadoGeca;
    }

    public int getIdUsuarioGeca() {
        return idUsuarioGeca;
    }

    public void setIdUsuarioGeca(int idUsuarioGeca) {
        this.idUsuarioGeca = idUsuarioGeca;
    }

    public String getNombreGeca() {
        return nombreGeca;
    }

    public void setNombreGeca(String nombreGeca) {
        this.nombreGeca = nombreGeca;
    }

    public String getEmailGeca() {
        return emailGeca;
    }

    public void setEmailGeca(String emailGeca) {
        this.emailGeca = emailGeca;
    }

    public String getPasswordGeca() {
        return passwordGeca;
    }

    public void setPasswordGeca(String passwordGeca) {
        this.passwordGeca = passwordGeca;
    }

    public String getRolGeca() {
        return rolGeca;
    }

    public void setRolGeca(String rolGeca) {
        this.rolGeca = rolGeca;
    }

    public String getIpPermitidaGeca() {
        return ipPermitidaGeca;
    }

    public void setIpPermitidaGeca(String ipPermitidaGeca) {
        this.ipPermitidaGeca = ipPermitidaGeca;
    }

    public LocalDateTime getFechaRegistroGeca() {
        return fechaRegistroGeca;
    }

    public void setFechaRegistroGeca(LocalDateTime fechaRegistroGeca) {
        this.fechaRegistroGeca = fechaRegistroGeca;
    }

    public String getEstadoGeca() {
        return estadoGeca;
    }

    public void setEstadoGeca(String estadoGeca) {
        this.estadoGeca = estadoGeca;
    }

    public boolean estaActivoGECA() {
        return "Activo".equalsIgnoreCase(estadoGeca);
    }
}
