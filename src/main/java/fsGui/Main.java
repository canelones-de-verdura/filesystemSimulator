package fsGui;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Runnable createAndStartTerminal = () -> SwingUtilities.invokeLater(() -> {
            SimpleTerminal terminal = new SimpleTerminal();
        });

        // Inicia múltiples instancias de SimpleTerminal
        createAndStartTerminal.run();
        createAndStartTerminal.run();
        createAndStartTerminal.run();
    }
}
