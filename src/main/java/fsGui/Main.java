package fsGui;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SimpleTerminal terminal = new SimpleTerminal();
            Thread terminalThread = new Thread(terminal);
            terminalThread.start();
        });

        SwingUtilities.invokeLater(() -> {
            SimpleTerminal terminal = new SimpleTerminal();
            Thread terminalThread = new Thread(terminal);
            terminalThread.start();
        });

        SwingUtilities.invokeLater(() -> {
            SimpleTerminal terminal = new SimpleTerminal();
            Thread terminalThread = new Thread(terminal);
            terminalThread.start();
        });
    }
}
