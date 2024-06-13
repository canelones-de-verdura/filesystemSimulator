package fsGui;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;

public class SimpleNano {
    JFrame frame;
    JTextArea textArea;
    String filename;

    public SimpleNano(String filename) {
        this.filename = filename;

        // Configuración de la ventana
        frame = new JFrame("ENano Editor - " + filename);
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Menú simulado en la parte inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel menuLabel = new JLabel("^S Guardar archivo  ^R Leer archivo  ^K Cortar texto  ^C Posición del cursor");
        bottomPanel.add(menuLabel, BorderLayout.NORTH);

        // Agregar componentes al frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Configurar comportamiento de cierre
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Configuración de los key bindings para simular nano
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_S:
                            // Si el archivo no tiene nombre, pedirlo
                            if (filename.equals("Sin nombre")) {
                                String name = JOptionPane.showInputDialog(frame, "Nombre del archivo:");
                                if (name != null) {
                                    SimpleNano.this.filename = name;
                                    frame.setTitle("ENano Editor - " + name);
                                }
                            }
                            saveFile();
                            break;
                        case KeyEvent.VK_R:
                            readFile();
                            break;
                        case KeyEvent.VK_K:
                            textArea.cut();
                            break;
                        case KeyEvent.VK_C:
                            int pos = textArea.getCaretPosition();
                            int line = 0;
                            int column = 0;
                            try {
                                line = textArea.getLineOfOffset(pos);
                                column = pos - textArea.getLineStartOffset(line);
                            } catch (BadLocationException ex) {
                                ex.printStackTrace();
                            }
                            JOptionPane.showMessageDialog(frame, "Línea: " + (line + 1) + ", Columna: " + (column + 1), "Posición del cursor", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                }
            }
        });
    }

    private void saveFile() {
        // TODO Implementar la lógica para guardar el archivo en el sistema de archivos
        frame.dispose();
    }

    private void readFile() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
            try {
                FileReader reader = new FileReader(fileChooser.getSelectedFile());
                textArea.read(reader, null);
                reader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}