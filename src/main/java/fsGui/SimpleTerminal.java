package fsGui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SimpleTerminal {
    JFrame f;
    JTextPane terminal;
    BigPotatoShell shell;
    int historyIndex;
    Style promptStyle;
    Style shellResponseStyle;
    int promptPosition;

    public SimpleTerminal() {
        f = new JFrame();
        terminal = new JTextPane();
        terminal.setFont(new Font("Monospaced", Font.PLAIN, 13));
        terminal.setCaretColor(Color.BLACK);
        f.setTitle("BigPotato Terminal");
        JScrollPane scrollPane = new JScrollPane(terminal);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        shell = new BigPotatoShell();
        
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        StyledDocument doc = terminal.getStyledDocument();
        promptStyle = doc.addStyle("PromptStyle", null);
        StyleConstants.setForeground(promptStyle, Color.BLUE);
        shellResponseStyle = doc.addStyle("ShellResponseStyle", null);
        StyleConstants.setForeground(shellResponseStyle, Color.GRAY);

        ((AbstractDocument) doc).setDocumentFilter(new TerminalDocumentFilter());

        terminal.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int pos = terminal.getCaretPosition();
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        String command = doc.getText(promptPosition, pos - promptPosition).trim();
                        // solo tomo el comando después del caracter $
                        command = command.substring(command.indexOf("$") + 1).trim();
                        appendToTerminal("\n", promptStyle);
                        String response;
                        if (command.isEmpty()) {
                            response = "";
                        } else {
                            response = shell.proccessCommand(command);
                            shell.commandHistory.add(command);
                            historyIndex = shell.commandHistory.size();
                        }
                        if (response.equals("dirtyterminal_pleasecleanme")) {
                            try {
                                StyledDocument doc = terminal.getStyledDocument();
                                // Es la forma que encontré de bypassear el filtro del document
                                // Pd: Sí, es mi fecha de cumpleaños en negativo :D (Soy Facu)
                                doc.remove(-25092004, -25092004);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                            showPrompt();
                            promptPosition = doc.getLength();
                            terminal.setCaretPosition(doc.getLength());
                            return;
                        }
                        appendToTerminal(response.isEmpty() ? "" : response + "\n", shellResponseStyle);
                        terminal.setCaretPosition(doc.getLength());
                        showPrompt();
                        promptPosition = doc.getLength();
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    if (historyIndex > 0) {
                        historyIndex--;
                        replaceCurrentCommand(shell.commandHistory.get(historyIndex));
                    }
                    e.consume();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (historyIndex < shell.commandHistory.size() - 1) {
                        historyIndex++;
                        replaceCurrentCommand(shell.commandHistory.get(historyIndex));
                    } else {
                        replaceCurrentCommand("");
                        historyIndex = shell.commandHistory.size();
                    }
                    e.consume();
                } else if (pos < promptPosition) {
                    terminal.setCaretPosition(doc.getLength());
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
                int pos = terminal.getCaretPosition();
                if (pos < promptPosition) {
                    terminal.setCaretPosition(doc.getLength());
                }
            }
        });

        f.add(scrollPane, BorderLayout.CENTER);
        f.setSize(600, 450);
        f.setVisible(true);

        shell.commandHistory = new ArrayList<>();
        historyIndex = 0;

        showPrompt();
        terminal.setCaretPosition(doc.getLength());
    }

    public void showPrompt() {
        try {
            if (shell.user == null) {
                // inserto la frase de bienvenida
                StyledDocument doc = terminal.getStyledDocument();
                String welcome = "Bienvenido a BigPotato!\nLogin $";
                doc.insertString(doc.getLength(), welcome, promptStyle);
                promptPosition = doc.getLength();
                return;
            }
            StyledDocument doc = terminal.getStyledDocument();
            String prompt = shell.USER + "@" + shell.HOST + ":" + shell.PWD + " $ ";
            promptPosition = doc.getLength();
            doc.insertString(promptPosition, prompt, promptStyle);
            terminal.setCaretPosition(doc.getLength());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendToTerminal(String text, Style style) {
        try {
            StyledDocument doc = terminal.getStyledDocument();
            doc.insertString(doc.getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void replaceCurrentCommand(String text) {
        try {
            StyledDocument doc = terminal.getStyledDocument();
            doc.remove(promptPosition, doc.getLength() - promptPosition);
            doc.insertString(promptPosition, text, promptStyle);
            SwingUtilities.invokeLater(() -> terminal.setCaretPosition(doc.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    class TerminalDocumentFilter extends DocumentFilter {
        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (offset == -25092004 && length == -25092004) {
                super.remove(fb, 0, fb.getDocument().getLength());
            }
            if (offset >= promptPosition) {
                super.remove(fb, offset, length);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (offset >= promptPosition) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            if (offset >= promptPosition) {
                super.replace(fb, offset, length, text, attrs);
            }
        }
    }
}
