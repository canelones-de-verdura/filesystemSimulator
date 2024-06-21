package fsGui;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import fsSim.fsFile;

import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;

public class SimpleNano {
    JFrame frame;
    JTextArea textArea;
    fsFile file;

    public SimpleNano(fsFile file, StringBuilder response) {
        this.file = file;

        if (!file.open()) {
            response.append("No se pudo abrir el archivo.");
            return;
        }

        // Configuración de la ventana
        frame = new JFrame("ENano Editor - " + file.getName());
        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Menú simulado en la parte inferior
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel menuLabel = new JLabel("^S Guardar archivo  ^R Leer archivo  ^K Cortar texto  ^C Posición del cursor");
        bottomPanel.add(menuLabel, BorderLayout.NORTH);

        // Agregar componentes al frame
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Configurar comportamiento de cierre
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                file.close();
            }
        });
        frame.setSize(800, 600);
        frame.setVisible(true);
        // Setea el contenido del archivo
        textArea.setText(file.read());

        // Configuración de los key bindings para simular nano
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.isControlDown()) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_S:
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
                            JOptionPane.showMessageDialog(frame, "Línea: " + (line + 1) + ", Columna: " + (column + 1),
                                    "Posición del cursor", JOptionPane.INFORMATION_MESSAGE);
                            break;
                    }
                }
            }
        });
    }

    private void saveFile() {
        if (file.write(textArea.getText(), true)) {
            JOptionPane.showMessageDialog(frame, "Archivo guardado correctamente.", "Guardado",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(frame, "No se pudo guardar el archivo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        file.close();
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
