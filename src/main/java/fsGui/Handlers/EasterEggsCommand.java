package fsGui.Handlers;
import java.util.ArrayList;

import fsGui.CommandFactory;

public class EasterEggsCommand extends BaseCommand {
    public EasterEggsCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "bigpotato", "magical_numbers" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        response.append("Easter egg encontrado: ");
        response.append(message);
        // Genera numeros mágicos
        response.append("\nNúmeros mágicos: ");
        for (int i = 0; i < 10; i++) {
            response.append((int) (Math.random() * 100));
            response.append(" ");
        }
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: bigpotato-magical_numbers [mensaje]\n");
        help.append("Easter egg de números mágicos.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
