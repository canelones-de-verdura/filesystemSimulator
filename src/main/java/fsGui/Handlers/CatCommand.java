package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import java.util.ArrayList;

public class CatCommand extends BaseCommand {
    public CatCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "cat" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        String absolutePath="/";
        if(message.isEmpty()){
            shell.PWD = "/";
            return;
        } 
        absolutePath = composePath(message, shell.PWD);
        fsSim.fsIElement result = shell.fsManager.getElementInFs(absolutePath);
        if (result instanceof fsSim.fsFile) {
            ((fsSim.fsFile) result).open();
            response.append(((fsSim.fsFile) result).read() != null ? ((fsSim.fsFile) result).read() : "");
            ((fsSim.fsFile) result).close();
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