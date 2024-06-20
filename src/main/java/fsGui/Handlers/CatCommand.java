package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.*;

import java.util.ArrayList;

public class CatCommand extends BaseCommand {
    public CatCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "cat" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        String absolutePath = composePath(message, shell.PWD);
        fsSim.fsIElement result = shell.fsManager.getElementInFs(absolutePath);
        if (result instanceof fsLink)
            result = ((fsLink) result).getReference();

        if (result instanceof fsFile) {
            ((fsFile) result).open();
            response.append(((fsFile) result).read() != null ? ((fsFile) result).read() : "");
            ((fsFile) result).close();
            return;
        }
        response.append("No existe el archivo especificado.");
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: cat [archivo]\n");
        help.append("Muestra el contenido del archivo especificado.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}
