package fsGui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimpleTerminal {
    JFrame f;
    JTextPane terminal;
    JTextField inputField;
    SimpleShell shell;
    ArrayList<String> commandHistory; // Historial de comandos ingresados.
    int historyIndex; // Índice del comando actual en el historial.
    Style userCommandStyle;
    Style shellResponseStyle;

    SimpleTerminal() {
        f = new JFrame();
        terminal = new JTextPane();
        terminal.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(terminal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputField = new JTextField();
        shell = new SimpleShell();

        // Estilo para los comandos del usuario.
        StyledDocument doc = terminal.getStyledDocument();
        userCommandStyle = doc.addStyle("UserCommandStyle", null);
        StyleConstants.setForeground(userCommandStyle, Color.BLACK);

        // Estilo para las respuestas del shell.
        Style shellResponseStyle = doc.addStyle("ShellResponseStyle", null);
        StyleConstants.setForeground(shellResponseStyle, Color.GRAY);

        // Ejecuta el comando ingresado en el terminal.
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String command = inputField.getText();
                appendToTerminal(formatUserInput(command), userCommandStyle);
                appendToTerminal(shell.proccessCommand(command), shellResponseStyle);
                commandHistory.add(command);
                historyIndex = commandHistory.size();
                inputField.setText("");
            }
        });

        // Permite navegar por el historial de comandos con las flechas arriba y abajo.
        inputField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (historyIndex > 0) {
                        historyIndex--;
                        inputField.setText(commandHistory.get(historyIndex));
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex < commandHistory.size() - 1) {
                        historyIndex++;
                        inputField.setText(commandHistory.get(historyIndex));
                    } else {
                        inputField.setText("");
                        historyIndex = commandHistory.size();
                    }
                }
            }
        });

        appendToTerminal("####### Welcome to the SimpleTerminal of Eze & Faq #######\n\n", null);
        f.add(scrollPane, BorderLayout.CENTER);
        f.add(inputField, BorderLayout.SOUTH);
        f.setSize(600, 450);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);

        commandHistory = new ArrayList<>();
        historyIndex = 0;
    }

    public String formatUserInput(String input) {
        return shell.getUSER() + "@" + shell.getHOST() + ":" + shell.getPWD() + "$ " + input;
    }

    public void appendToTerminal(String text, Style style) {
        try {
            StyledDocument doc = terminal.getStyledDocument();
            doc.insertString(doc.getLength(), text + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new SimpleTerminal();
    }
}
