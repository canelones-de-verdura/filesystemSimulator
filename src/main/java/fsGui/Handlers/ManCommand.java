package fsGui.Handlers;
import fsGui.CommandFactory;
import java.util.ArrayList;

public class ManCommand extends BaseCommand {
    public ManCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "man", "help" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        if (message.isEmpty()) {
            for (String commandName : commandFactory.commandMap.keySet()) {
                response.append("Comando: " + commandName + "\n");
                response.append(commandFactory.getCommand(commandName).showHelp());
                response.append("\n");
            }
        } else {
            BaseCommand command = commandFactory.getCommand(message);
            if (command != null) {
                response.append(command.showHelp());
            } else {
                response.append("Comando \"" + message + "\" no encontrado");
            }
        }
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