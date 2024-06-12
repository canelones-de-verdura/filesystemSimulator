package fsSim;

// Tiene sentido crear otra clase solo para esto?
public class fsRoot extends fsDir {

    public fsRoot() {
        // Llamamos el constructor de la clase padre
        super("", null);
        // Arreglamos la referencia al anterior
        this.contents.put("..", this);
    }

}
