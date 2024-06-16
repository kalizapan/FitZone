package logica.dominio;

public class SesionAdministrador {
    private static SesionAdministrador instancia;
    private int idAdministrador;

    private SesionAdministrador() { }

    public static SesionAdministrador getInstancia() {
        if (instancia == null) {
            instancia = new SesionAdministrador();
        }
        return instancia;
    }

    public int getIdAdministrador() {
        return idAdministrador;
    }

    public void setIdAdministrador(int idAdministrador) {
        this.idAdministrador = idAdministrador;
    }
}