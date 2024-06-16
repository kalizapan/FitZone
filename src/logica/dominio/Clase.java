package logica.dominio;

import java.util.Objects;

public class Clase {
    private int idClase;
    private String nombre;
    private String tipo;
    private int capacidad;
    private String fechaClase;
    private String horaInicio;
    private String horaFin;
    private int precio;
    private int estado;
    private int idEntrenador;

    // Constructor con todos los parámetros, incluyendo idClase
    public Clase(int idClase, String nombre, String tipo, int capacidad, String fechaClase, String horaInicio, 
            String horaFin, int precio, int estado, int idEntrenador) {
        this.idClase = idClase;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.fechaClase = fechaClase;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
        this.estado = estado;
        this.idEntrenador = idEntrenador;
    }

    // Constructor sin idClase, para usar cuando el id es generado automáticamente por la base de datos
    public Clase(String nombre, String tipo, int capacidad, String fechaClase, String horaInicio, String horaFin, 
            int precio, int estado, int idEntrenador) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.fechaClase = fechaClase;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.precio = precio;
        this.estado = estado;
        this.idEntrenador = idEntrenador;
    }

    // Getters y setters para todos los campos
    public int getIdClase() {
        return idClase;
    }

    public void setIdClase(int idClase) {
        this.idClase = idClase;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getFechaClase() {
        return fechaClase;
    }

    public void setFechaClase(String fechaClase) {
        this.fechaClase = fechaClase;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getIdEntrenador() {
        return idEntrenador;
    }

    public void setIdEntrenador(int idEntrenador) {
        this.idEntrenador = idEntrenador;
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
        final Clase other = (Clase) obj;
        if (this.idClase != other.idClase) {
            return false;
        }
        if (this.capacidad != other.capacidad) {
            return false;
        }
        if (this.precio != other.precio) {
            return false;
        }
        if (this.estado != other.estado) {
            return false;
        }
        if (this.idEntrenador != other.idEntrenador) {
            return false;
        }
        if (!Objects.equals(this.nombre, other.nombre)) {
            return false;
        }
        if (!Objects.equals(this.tipo, other.tipo)) {
            return false;
        }
        if (!Objects.equals(this.fechaClase, other.fechaClase)) {
            return false;
        }
        if (!Objects.equals(this.horaInicio, other.horaInicio)) {
            return false;
        }
        return Objects.equals(this.horaFin, other.horaFin);
    }
}