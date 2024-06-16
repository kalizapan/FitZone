package logica.dominio;

/**
 *
 * @author Vero
 */
public class clase_cliente {
    
    private int idClase;
    private int idCliente;

    public clase_cliente() {
    }

    public clase_cliente(int idClase, int idCliente) {
        this.idClase = idClase;
        this.idCliente = idCliente;
    }

    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final clase_cliente other = (clase_cliente) obj;
        if (this.idClase != other.idClase) {
            return false;
        }
        return this.idCliente == other.idCliente;
    }
    
    
    
}
