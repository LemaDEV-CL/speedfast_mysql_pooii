package cl.lema.models;

/**
 * Representa un repartidor almacenado en la base de datos.
 * Contiene su identificador y nombre.
 */
public class Repartidor {

    int id;
    String nombre;

    public Repartidor(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Repartidor () {
    }

    public Repartidor (String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}
