package fsSim;

public class App {
    public static void main(String[] args) {
        fsSimManager manager = new fsSimManager();

        fsFile passwd = (fsFile) manager.getElementInFs("/etc/passwd");

        System.out.println(passwd.read());

    }
}
