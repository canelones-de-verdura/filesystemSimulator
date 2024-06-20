package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsDir;
import fsSim.fsIElement;

import java.util.ArrayList;

public class RmCommand extends BaseCommand {
    public RmCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "rm" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        if (message.isEmpty()) {
            response.append("No se especificó el nombre del archivo.\n");
            response.append(showHelp());
            return;
        }
        String absolutePath = composePath(message, shell.PWD);
        fsIElement element = shell.fsManager.getElementInFs(absolutePath);
        fsIElement parentDir = shell.fsManager.getElementInFs(absolutePath.substring(0, absolutePath.lastIndexOf("/")));
        if (element == null) {
            response.append("El archivo no existe.");
            return;
        }
        ((fsDir) parentDir).remove(element.getName());
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: rm [archivo]\n");
        help.append("Borra el elemento con el nombre especificado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
