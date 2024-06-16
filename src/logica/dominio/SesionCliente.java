package logica.dominio;

public class SesionCliente {
    private static SesionCliente instancia;
    private int idCliente;

    private SesionCliente() { }

    public static SesionCliente getInstancia() {
        if (instancia == null) {
            instancia = new SesionCliente();
        }
        return instancia;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
}
