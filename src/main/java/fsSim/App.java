package fsSim;

public class App {
    public static void main(String[] args) {
        fsRoot root = new fsRoot();

        root.create("pruebaArchivo", false);

        root.create("pruebaCarpeta", true);

        fsDir dir = (fsDir) root.getContents("pruebaCarpeta");

        dir.create("otroArchivoMás", false);

        fsFile file = (fsFile) root.getContents("pruebaArchivo");
        file.write("AAAAAAAAaAAAAAAAAAA", true);

        file = (fsFile) dir.getContents("otroArchivoMás");
        file.write("OISVHNABEOIVBUIAJDA", true);

        System.out.println(root.name + root.created_d);
        System.out.println(dir.name + dir.created_d);
        System.out.println(file.name + file.created_d);

        // Tree wannabe
        System.out.println("/");
        root.contents.forEach((k, v) -> {
            if (k != "..")
                System.out.println("\t" + k + "\n");
            if (v.getClass() == fsDir.class) {
                fsDir d = (fsDir) v;
                d.contents.forEach((k2, v2) -> {
                    System.out.println("\t\t" + k2 + "\n");
                });
            }
        });

    }
}
