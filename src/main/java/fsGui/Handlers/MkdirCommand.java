package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsIElement;

import java.util.ArrayList;

public class MkdirCommand extends BaseCommand {
    public MkdirCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "mkdir" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        if (message.isEmpty()) {
            response.append("No se especificó el nombre del directorio.\n");
            response.append(showHelp());
            return;
        }
        String absolutePath = composePath(message, shell.PWD);
        fsIElement element = shell.fsManager.getElementInFs(absolutePath);
        if (element != null) {
            response.append("El directorio ya existe.");
            return;
        }
        if(!shell.fsManager.createDir(absolutePath, shell.user)){
            response.append("No se pudo crear el directorio.");
        }
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: mkdir [directorio]\n");
        help.append("Crea un directorio con el nombre especificado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}