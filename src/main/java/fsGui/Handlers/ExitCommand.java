package fsGui.Handlers;

import fsGui.CommandFactory;

public class ExitCommand extends BaseCommand {
    public ExitCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "exit" };
    }

    @Override
    protected void internalHandle(String message, StringBuilder response) {
        System.exit(0);
        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: exit\n");
        help.append("Cierra la aplicación.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}