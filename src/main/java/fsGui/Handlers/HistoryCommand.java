package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;

public class HistoryCommand extends BaseCommand {
    public HistoryCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "history" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        for(String command : shell.commandHistory){
            response.append(command + "\n");
        }
        response.deleteCharAt(response.length() - 1);
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: history\n");
        help.append("Muestra el historial de comandos\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}
