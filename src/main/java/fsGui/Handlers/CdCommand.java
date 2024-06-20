package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsLink;

import java.util.ArrayList;

public class CdCommand extends BaseCommand {
    public CdCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "cd" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        String absolutePath="/";
        if(message.isEmpty()){
            shell.PWD = shell.user.getHome();
            return;
        } 
        absolutePath = composePath(message, shell.PWD);
        fsSim.fsIElement result = shell.fsManager.getElementInFs(absolutePath);
        if (result instanceof fsSim.fsLink)
            result = ((fsLink) result).getReference();

        if (result instanceof fsSim.fsDir) {
            shell.PWD = absolutePath;
            return;
        }
        response.append("No existe el directorio especificado.");
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: cd [directorio]\n");
        help.append("Cambia el directorio actual al especificado.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}
