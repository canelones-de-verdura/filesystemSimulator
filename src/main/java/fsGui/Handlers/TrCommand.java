package fsGui.Handlers;

import fsGui.CommandFactory;
import java.util.ArrayList;

public class TrCommand extends BaseCommand {
    public TrCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "tr" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            fsGui.BigPotatoShell shell) {
        // inserto en el response el mensaje el thread actual
        response.append(Thread.currentThread().getId() + "\n");
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: tr [caracteres-a-reemplazar] [caracteres-de-reemplazo]\n");
        help.append("Reemplaza los caracteres especificados en el mensaje.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}