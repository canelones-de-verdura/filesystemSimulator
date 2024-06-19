package fsGui.Handlers;

import fsGui.CommandFactory;
import java.util.ArrayList;

public class ExitCommand extends BaseCommand {
    public ExitCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "exit" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        shell.popUserFromStack();
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: exit\n");
        help.append("Se desloguea del usuario actual\n");
        help.append("-----------------------------");
        return help.toString();
    }
}