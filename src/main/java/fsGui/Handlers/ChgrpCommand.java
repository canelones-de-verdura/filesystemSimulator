package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsGroup;
import fsSim.fsIElement;

public class ChgrpCommand extends BaseCommand {
    public ChgrpCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "chgrp" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] chgrpArgs = message.split(" ");

        if (chgrpArgs.length != 2) {
            response.append(showHelp());
            return;
        }

        // Armamos rutas
        String path = composePath(chgrpArgs[1], shell.PWD);

        fsIElement element = shell.fsManager.getElementInFs(path);

        if (element == null) {
            response.append("El elemento no existe.");
            return;
        }

        fsGroup new_group_owner = null;
        for (fsGroup group : shell.fsManager.getAllTheGroups()) {
            if (group.getName().equals(chgrpArgs[0])) {
                new_group_owner = group;
                break;
            }
        }

        if (new_group_owner == null) {
            response.append("El grupo no existe.\n");
            return;

        }

        element.setGroupID(new_group_owner.getGUID());
        return;

    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: chown [grupo] [ruta del archivo]\n");
        help.append("Cambia el grupo de un elemento.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
