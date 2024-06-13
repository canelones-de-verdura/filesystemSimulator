package fsGui.Handlers;
import fsGui.CommandFactory;

public class ManCommand extends BaseCommand {
    public ManCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "man", "help" };
    }

    @Override
    protected void internalHandle(String message, StringBuilder response, fsGui.BigPotatoShell shell) {
        // Si el mensaje no tiene argumentos, se muestra el mensaje de ayuda general
        if (message.isEmpty()) {
            // Recorre todos los comandos registrados y muestra su ayuda
            for (String commandName : commandFactory.commandMap.keySet()) {
                response.append("Comando: " + commandName + "\n");
                response.append(commandFactory.getCommand(commandName).showHelp());
                response.append("\n");
            }
        } else {
            // Muestra la ayuda del comando específico
            BaseCommand command = commandFactory.getCommand(message);
            if (command != null) {
                response.append(command.showHelp());
            } else {
                response.append("Comando \"" + message + "\" no encontrado");
            }
        }
        return;
    }
    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: man-help [comando]\n");
        help.append("Muestra la ayuda del comando ingresado.\n");
        help.append("Si no se especifica un comando, se muestra la ayuda general.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}