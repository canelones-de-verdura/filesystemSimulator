package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.*;

public class LnCommand extends BaseCommand {
    public LnCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "ln" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] lnArgs = message.split(" ");

        // Verificamos que las dos rutas son correctas
        if (lnArgs.length != 2) {
            response.append(showHelp());
            return;
        }

        // Armamos rutas
        String reference_path = composePath(lnArgs[0], shell.PWD);
        String new_path = composePath(lnArgs[1], shell.PWD);

        boolean res = shell.fsManager.createLink(reference_path, new_path, shell.user);

        if (!res) {
            response.append("Una o ambas rutas son inválidas.");
        }
        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: mv [opción] [origen] [destino]\n");
        help.append("Mueve el archivo origen al destino.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
