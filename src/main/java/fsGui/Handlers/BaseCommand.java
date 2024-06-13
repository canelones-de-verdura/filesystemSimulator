package fsGui.Handlers;

import fsGui.CommandFactory;

/**
 * Clase abstracta que define un comando base para el sistema de manejo de comandos de la simulación.
 * Cada comando debe extender esta clase y sobrescribir el método internalHandle para manejar el mensaje.
 * 
 * @param hasSubOptions Indica si el comando tiene subopciones
 * @param shouldReturnSomething Indica si el comando debe devolver una respuesta
 * @param mayHaveArguments Indica si el comando puede tener argumentos
 * @param keywords Palabras clave que activan el comando
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
    protected abstract void internalHandle(String message, StringBuilder response, fsGui.BigPotatoShell shell);

    // Determina si este handler puede procesar el mensaje
    protected boolean canHandle(String message) {
        if (keywords == null || keywords.length == 0) {
            throw new IllegalStateException("No hay palabras clave que puedan ser procesadas");
        }
        String firstWord = message.split(" ")[0];
        for (String keyword : keywords) {
            if (firstWord.equalsIgnoreCase(keyword)) {
                return true;
            }
        }
        if(arguments == null || arguments.length == 0) {
            return true;
        }
        String argBlock = message.split(" ").length > 1 ? message.split(" ")[1] : "";
        for (String argument : arguments) {
            // Si algun argumento recibido no es válido, no se puede manejar el mensaje
            if (!argBlock.toLowerCase().contains(argument.toLowerCase())) {
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
    public boolean handle(String message, StringBuilder response, fsGui.BigPotatoShell shell) {
        if (canHandle(message)) {
            internalHandle(message, response, shell);
            return true;
        } 
        return false;
    }

    // Muestra la ayuda del comando
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("No hay ayuda disponible para este comando\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
