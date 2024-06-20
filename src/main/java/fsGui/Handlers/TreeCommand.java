package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.*;

import java.util.ArrayList;
import java.util.Set;

public class TreeCommand extends BaseCommand {
    public TreeCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "tree" };
        super.arguments = new String [] { "l" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, fsGui.BigPotatoShell shell) {
        String absolutePath = composePath(message, shell.PWD);
        boolean showMetadata = false;
        if (arguments.contains("l")) {
            showMetadata = true;
        }
        fsIElement baseDir = shell.fsManager.getElementInFs(absolutePath);
        if (baseDir instanceof fsLink) baseDir = ((fsLink) baseDir).getReference();
        if (baseDir == null) {
            response.append("No existe el directorio especificado.");
            return;
        }
        if (!(baseDir instanceof fsDir)) {
            response.append("No se puede mostrar la estructura de un archivo.");
            return;
        }
        baseDir = (fsDir) baseDir;
        printTree((fsDir) baseDir, response, showMetadata, shell);
        response.deleteCharAt(response.length() - 1);
    }

    public void printTree(fsDir baseDir, StringBuilder response, boolean showMetadata, BigPotatoShell shell) {
        response.append(".\n");
        printTree(baseDir, response, "", true, showMetadata, shell);
    }

    private void printTree(fsDir dir, StringBuilder response, String prefix, boolean isTail, boolean showMetadata, BigPotatoShell shell) {
        Set<String> elements = dir.getContents();
        int elementCount = elements.size();
        int currentIndex = 0;
        
        for (String element : elements) {
            if (element.equals(".") || element.equals("..") || element.equals("")) continue;
            
            fsIElement e = dir.getElement(element);
            currentIndex++;
            if(!showMetadata){
                response.append(prefix).append(isTail ? "└── " : "├── ").append(e.getName()).append("\n");
            } else {
                response.append(prefix).append(isTail ? "└── " : "├── ");
                response.append((e instanceof fsDir) ? "d" : (e instanceof fsLink) ? "l" : "-").append("  ");
                response.append(shell.fsManager.getUser(dir.getOwnerID()).getName()).append("  ");
                response.append(shell.fsManager.getGroup(dir.getGUID()).getName()).append("  ");
                response.append(e.getModifiedDate()).append("  ");
                response.append(e.getSize()).append("  ");
                response.append(e.getName()).append("\n");
            }
            if (e instanceof fsDir) {
                printTree((fsDir) e, response, prefix + (isTail ? "    " : "│   "), currentIndex == elementCount, showMetadata, shell);
            }
        }
    }
    

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: tree [argumentos] [ruta]\n");
        help.append("Muestra la estructura de directorios y archivos a partir de la ruta especificada o del directorio actual.\n");
        help.append("Argumentos:\n");
        help.append(" -l: Muestra información detallada de los elementos.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}