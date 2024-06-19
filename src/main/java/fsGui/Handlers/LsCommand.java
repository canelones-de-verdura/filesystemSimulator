package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsDir;
import fsSim.fsFile;
import fsSim.fsIElement;
import java.util.ArrayList;

public class LsCommand extends BaseCommand {
    public LsCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "ls" };
        super.arguments = new String[] { "l" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String path = composePath(message, shell.PWD);
        boolean showMetadata = false;

        if (arguments.contains("l")) {
            showMetadata = true;
        }

        fsIElement result = shell.fsManager.getElementInFs(path);
        // Si no se encuentra el archivo o directorio, se muestra un mensaje de error.
        if (result == null) {
            response.append("No existe el archivo o directorio especificado.");
            return;
        }
        // Si el resultado es un directorio, se listan sus elementos.
        if (result instanceof fsDir) {
            fsDir dir = (fsDir) result;
            // Información del . y ..
            if (!showMetadata) {
                response.append(".\n");
                response.append("..\n");
            } else {
                response.append("d").append("  ");
                response.append(shell.fsManager.getUser(dir.getOwnerID()).getName()).append("  ");
                response.append(shell.fsManager.getGroup(dir.getGUID()).getName()).append("  ");
                response.append(dir.getModifiedDate()).append("  ");
                response.append(dir.getSize()).append("  ");
                response.append(".\n");
                response.append("d").append("  ");
                response.append(shell.fsManager.getUser(dir.getOwnerID()).getName()).append("  ");
                response.append(shell.fsManager.getGroup(dir.getGUID()).getName()).append("  ");
                response.append(dir.getModifiedDate()).append("  ");
                response.append(dir.getSize()).append("  ");
                response.append("..\n");
            }
            // Información de los elementos del directorio
            for (String element : dir.getContents()) {
                if (element.equals(".") || element.equals("..")) {
                    continue;
                }
                if (!showMetadata) {
                    response.append(element).append("\n");
                } else {
                    String pathToCurrentElement = path + (path.endsWith("/") ? "" : "/") + element;
                    fsIElement elementObj = shell.fsManager.getElementInFs(pathToCurrentElement);
                    if (elementObj instanceof fsDir) {
                        response.append("d").append("  ");
                    } else {
                        response.append("-").append("  ");
                    }
                    response.append(shell.fsManager.getUser(((fsIElement) elementObj).getOwnerID()).getName())
                            .append("  ");
                    response.append(shell.fsManager.getGroup(((fsIElement) elementObj).getGUID()).getName())
                            .append("  ");
                    response.append(((fsIElement) elementObj).getModifiedDate()).append("  ");
                    // relleno con espacios antes del numero hasta que ocupe 4 caracteres
                    if (((fsIElement) elementObj).getSize() < 10) {
                        response.append("   ");
                    } else if (((fsIElement) elementObj).getSize() < 100) {
                        response.append("  ");
                    } else if (((fsIElement) elementObj).getSize() < 1000) {
                        response.append(" ");
                    }
                    response.append(((fsIElement) elementObj).getSize()).append("  ");
                    response.append(element).append("\n");
                }
            }
            response.deleteCharAt(response.length() - 1);
        } else if (result instanceof fsFile) {
            fsFile file = (fsFile) result;
            if (!showMetadata) {
                response.append(file.getName()).append("\n");
            } else {
                response.append("-").append("  ");
                response.append(shell.fsManager.getUser(file.getOwnerID()).getName()).append("  ");
                response.append(shell.fsManager.getGroup(file.getGUID()).getName()).append("  ");
                response.append(file.getModifiedDate()).append("  ");
                response.append(file.getSize()).append("  ");
                response.append(file.getName()).append("\n");
            }
        }
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: ls\n");
        help.append("Lista los archivos y directorios del directorio actual.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}