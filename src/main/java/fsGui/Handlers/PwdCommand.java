package fsGui.Handlers;

import java.util.ArrayList;
import fsGui.BigPotatoShell;
import fsGui.CommandFactory;


public class PwdCommand extends BaseCommand {
    public PwdCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "pwd" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        response.append(shell.PWD);
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: pwd\n");
        help.append("Muestra el directorio actual.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}