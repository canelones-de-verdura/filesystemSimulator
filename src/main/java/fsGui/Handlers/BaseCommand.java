package fsGui.Handlers;

import java.util.ArrayList;
import java.util.Stack;
import java.util.StringJoiner;

import fsGui.CommandFactory;

/**
 * Clase abstracta que define un comando base para el sistema de manejo de
 * comandos de la simulación.
 * Cada comando debe extender esta clase y sobrescribir el método internalHandle
 * para manejar el mensaje.
 * 
 */
public abstract class BaseCommand {
    protected String[] keywords;
    protected String[] arguments;
    protected CommandFactory commandFactory;

    public BaseCommand(CommandFactory commandFactory) {
        this.commandFactory = commandFactory;
    }

    // Método abstracto que deben sobrescribir las subclases para manejar el mensaje
    protected abstract void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            fsGui.BigPotatoShell shell);

    // Determina si este handler puede procesar el mensaje
    protected boolean canHandle(String message, ArrayList<String> arguments) {
        // Si no tiene argumentos, se puede manejar el mensaje sin comprobar más
        if (this.arguments == null || this.arguments.length == 0) {
            return true;
        }
        // Si alguno de los argumentos no es válido, no se puede manejar el mensaje
        for (String argument : arguments) {
            boolean valid = false;
            for (String validArgument : this.arguments) {
                if (argument.equals(validArgument)) {
                    valid = true;
                    break;
                }
            }
            if (!valid) {
                return false;
            }
        }
        return true;
    }

    // Devuelve la lista de keywords del comando
    public String[] getKeywords() {
        return keywords;
    }

    // Maneja el mensaje.
    public boolean handle(String message, ArrayList<String> arguments, StringBuilder response,
            fsGui.BigPotatoShell shell) {
        if (canHandle(message, arguments)) {
            try {
                internalHandle(message, arguments, response, shell);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    // Muestra la ayuda del comando
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("No hay ayuda disponible para este comando\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }

    /*
     * Método que compone un path absoluto a partir de un path y el path actual.
     * 
     * @param message Path relativo o absoluto.
     * 
     * @param pwd Path actual.
     * 
     * @return Path absoluto en formato string.
     */
    public String composePath(String message, String pwd) {
        Stack<String> stack = new Stack<>();
        if (!message.startsWith("/")) {
            for (String part : pwd.split("/")) {
                if (!part.isEmpty()) {
                    if (part.equals("..") && !stack.isEmpty()) {
                        stack.pop();
                    } else if (!part.equals(".")) {
                        stack.add(part);
                    }
                }
            }
            for (String part : message.split("/")) {
                if (part.equals("..") && !stack.isEmpty()) {
                    stack.pop();
                } else if (!part.isEmpty() && !part.equals(".")) {
                    stack.add(part);
                }
            }
        } else {
            for (String part : message.split("/")) {
                if (part.equals("..") && !stack.isEmpty()) {
                    stack.pop();
                } else if (!part.isEmpty() && !part.equals(".")) {
                    stack.add(part);
                }
            }
        }
        // Se crea un StringJoiner para unir los elementos de la pila en un string
        StringJoiner joiner = new StringJoiner("/");
        for (String part : stack) {
            joiner.add(part);
        }

        return "/" + (joiner.toString().equals("..") ? "" : joiner.toString());
    }
}
