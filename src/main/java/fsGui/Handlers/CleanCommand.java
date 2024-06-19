package fsGui.Handlers;

import fsGui.CommandFactory;
import java.util.ArrayList;

public class CleanCommand extends BaseCommand {
    public CleanCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "clean" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        response.append("dirtyterminal_pleasecleanme");
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: clean\n");
        help.append("Limpia el historial de comandos\n");
        help.append("-----------------------------");
        return help.toString();
    }
}