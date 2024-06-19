package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.*;

public class MvCommand extends BaseCommand {
    public MvCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "mv" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        String[] mvArgs = message.split(" ");
        if (mvArgs.length != 2) {
            response.append("Uso: mv [origen] [destino]\n");
            response.append(showHelp());
            return;
        }
        String absoluteOrigen = composePath(mvArgs[0], shell.PWD);
        String absoluteDestino = composePath(mvArgs[1], shell.PWD);
        fsIElement origen = shell.fsManager.getElementInFs(absoluteOrigen);
        fsDir padreDestino = (fsDir) shell.fsManager.getElementInFs(absoluteDestino.substring(0, absoluteDestino.lastIndexOf("/")));
        fsIElement destino = shell.fsManager.getElementInFs(absoluteDestino);
        if(origen == null) {
            response.append("El archivo origen no existe.\n");
            return;
        }
        // Si el origen es un link, se obtiene la referencia y lo trato como un archivo o directorio normal.
        if(origen instanceof fsLink) origen = ((fsLink) origen).getReference();
        if(padreDestino == null) {
            response.append("El directorio destino no existe.\n");
            return;
        }
        if(padreDestino.getElement(absoluteDestino.substring(absoluteDestino.lastIndexOf("/") + 1)) != null) {
            response.append("El archivo destino ya existe.\n");
            return;
        }
        // TODO para mañana cuando me levante, hay que emprolijar esto, es una basura, puro código redundante.
        // Caso DIR - FILE
        if(origen instanceof fsDir && destino instanceof fsFile) {
            response.append("No se puede mover un directorio a un archivo.\n");
            return;
        }
        // Caso FILE - DIR Simple
        if(origen instanceof fsFile) {
            fsFile file = (fsFile) origen;
            padreDestino.move(file);
            fsDir padreOrigen = (fsDir) shell.fsManager.getElementInFs(absoluteOrigen.substring(0, absoluteOrigen.lastIndexOf("/")));
            padreOrigen.remove(absoluteOrigen.substring(absoluteOrigen.lastIndexOf("/") + 1));
            response.append("Archivo movido exitosamente.\n");
            return;
        }
        // Caso DIR - DIR Simple
        if(origen instanceof fsDir) {
            fsDir dir = (fsDir) origen;
            padreDestino.move(dir);
            fsDir padreOrigen = (fsDir) shell.fsManager.getElementInFs(absoluteOrigen.substring(0, absoluteOrigen.lastIndexOf("/")));
            padreOrigen.remove(absoluteOrigen.substring(absoluteOrigen.lastIndexOf("/") + 1));
            response.append("Directorio movido exitosamente.\n");
            return;
        }
        // Caso FILE - FILE
        if(origen instanceof fsFile && destino instanceof fsFile) {
            if((fsFile) origen == (fsFile) destino) {
                response.append("El archivo origen y destino son el mismo.\n");
                return;
            }
            origen.setName(absoluteDestino.substring(absoluteDestino.lastIndexOf("/") + 1));
            padreDestino.move(origen);
            fsDir padreOrigen = (fsDir) shell.fsManager.getElementInFs(absoluteOrigen.substring(0, absoluteOrigen.lastIndexOf("/")));
            padreOrigen.remove(absoluteOrigen.substring(absoluteOrigen.lastIndexOf("/") + 1));
            response.append("Archivo movido exitosamente.\n");
            return;
        } 
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: mv [origen] [destino]\n");
        help.append("Mueve el archivo origen al destino.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}