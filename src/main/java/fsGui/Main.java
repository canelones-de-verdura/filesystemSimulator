package fsGui;

public class Main {
    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            Thread terminalThread = new Thread(new SimpleTerminal());
            terminalThread.start();
        }
    }
}
