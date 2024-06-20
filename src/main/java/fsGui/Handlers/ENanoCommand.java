package fsGui.Handlers;

import fsGui.CommandFactory;
import fsGui.SimpleNano;
import java.util.ArrayList;

public class ENanoCommand extends BaseCommand {
    public ENanoCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "enano" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        String absolutePath="/";
        if(message.isEmpty()){
            response.append("No existe el archivo especificado.\n");
            response.append(showHelp());
            return;
        }
        absolutePath = composePath(message, shell.PWD);
        fsSim.fsIElement result = shell.fsManager.getElementInFs(absolutePath);
        if (result instanceof fsSim.fsFile) {
            new SimpleNano((fsSim.fsFile) result);
            return;
        } else if (result == null) {
            if(shell.fsManager.createFile(absolutePath, shell.user)){
                result = shell.fsManager.getElementInFs(absolutePath);
                new SimpleNano((fsSim.fsFile) result);
                return;
            } else {
                response.append("No se pudo crear el archivo.");
            }
            return;
        } else {
            response.append("No se puede editar el archivo especificado.");
            return;
        }
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: enano [nombre-de-archivo]\n");
        help.append("Editor de texto enano.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}