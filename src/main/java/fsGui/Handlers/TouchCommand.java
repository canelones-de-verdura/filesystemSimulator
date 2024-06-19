package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsIElement;

import java.util.ArrayList;

public class TouchCommand extends BaseCommand {
    public TouchCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "touch" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        if (message.isEmpty()) {
            response.append("No se especificó el nombre del archivo.\n");
            response.append(showHelp());
            return;
        }
        String absolutePath = composePath(message, shell.PWD);
        fsIElement element = shell.fsManager.getElementInFs(absolutePath);
        if (element != null) {
            response.append("El archivo ya existe.");
            return;
        }
        if(!shell.fsManager.createFile(absolutePath, shell.user)){
            response.append("No se pudo crear el archivo.");
        }
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: touch [archivo]\n");
        help.append("Crea un archivo vacío con el nombre especificado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}