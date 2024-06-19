//TODO: AYUDAME POR FAVOR / hay que arreglar las flags
package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.*;

public class CpCommand extends BaseCommand {
    public CpCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "cp" };
        super.arguments = new String[] { "rename" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] cpArgs = message.split(" ");
        boolean rename = false;

        // Verificamos que las dos rutas son correctas
        if (cpArgs.length != 2) {
            response.append(showHelp());
            return;
        }

        // Verificamos argumentos
        if (arguments.size() > 2 || (arguments.size() == 1 && !arguments.contains("rename"))) {
            response.append("Argumentos inválidos.\n");
            return;
        }

        if (arguments.size() == 1 && arguments.get(0).equals("rename"))
            rename = true;

        // Armamos rutas
        String absoluteOrigen = composePath(cpArgs[0], shell.PWD);
        String absoluteDestino = composePath(cpArgs[1], shell.PWD);
        // Recuperamos referencias
        fsIElement origen = shell.fsManager.getElementInFs(absoluteOrigen);
        fsIElement destino = shell.fsManager.getElementInFs(absoluteDestino);

        // Chequeamos que sean correctas
        if (origen == null) {
            response.append("El archivo origen no existe.\n");
            return;
        }

        if (rename) {
            if (destino == null) {
                // String[] aux = mvArgs[1].split("/");
                // String nombre_destino = aux[aux.length] == null ? aux[aux.length - 1] :
                // aux[aux.length];

                // No hay un elemento con ese nombre en el destino, por lo que podemos usarlo.
                String nombre_destino = cpArgs[1].substring(cpArgs[1].lastIndexOf("/"), cpArgs[1].length());
                origen.setName(nombre_destino);

                // Recuperamos la referencia al verdero destino
                destino = shell.fsManager
                        .getElementInFs(absoluteDestino.substring(0, absoluteDestino.lastIndexOf("/")));
            } else {
                // Não não
                response.append("El archivo ya existe.\n");
                return;
            }
        }

        if (destino == null) {
            response.append("La ruta destino es incorrecta.\n");
            return;
        }

        if (destino instanceof fsLink)
            destino = ((fsLink) destino).getReference();

        if (destino instanceof fsFile) {
            response.append("No se puede mover elementos a un archivo.\n");
            return;
        }

        // Finalmente, movemos el archivo
        ((fsDir) destino).move(origen);
        if (origen instanceof fsDir)
            ((fsDir) origen).changeParent(destino);

    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: cp [opción] [origen] [destino]\n");
        help.append("Mueve el archivo origen al destino.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
