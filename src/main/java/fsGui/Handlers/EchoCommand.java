package fsGui.Handlers;
import fsGui.CommandFactory;

public class EchoCommand extends BaseCommand {
    public EchoCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "echo", "shout" };
    }

    @Override
    protected void internalHandle(String message, StringBuilder response) {
        response.append(message);
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: echo-shout [mensaje]\n");
        help.append("Muestra el mensaje ingresado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}