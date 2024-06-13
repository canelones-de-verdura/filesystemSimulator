package fsGui.Handlers;

import fsGui.CommandFactory;
import fsGui.SimpleNano;

public class ENanoCommand extends BaseCommand {
    public ENanoCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "enano", "nano" };
    }

    @Override
    protected void internalHandle(String message, StringBuilder response, fsGui.BigPotatoShell shell) {
        new SimpleNano(!message.isEmpty() ? message : "Sin nombre");
        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: enano-nano [mensaje]\n");
        help.append("Editor de texto enano.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}