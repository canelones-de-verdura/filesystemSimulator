package fsGui;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        new SimpleTerminal();
        SwingUtilities.invokeLater(() -> new SimpleNano("Untitled"));
    }
}
