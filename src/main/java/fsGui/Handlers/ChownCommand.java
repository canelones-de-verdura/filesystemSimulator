package fsGui.Handlers;

import java.util.ArrayList;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsGroup;
import fsSim.fsIElement;
import fsSim.fsUser;

public class ChownCommand extends BaseCommand {
    public ChownCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "chown" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] chownArgs = message.split(" ");

        if (chownArgs.length != 2) {
            response.append(showHelp());
            return;
        }

        // Armamos rutas
        String[] who = chownArgs[0].split(":");
        String path = composePath(chownArgs[1], shell.PWD);

        fsIElement element = shell.fsManager.getElementInFs(path);

        if (element == null) {
            response.append("El elemento no existe.");
            return;
        }

        fsUser new_owner = null;
        for (fsUser user : shell.fsManager.getAllTheUsers()) {
            if (user.getName().equals(who[0])) {
                new_owner = user;
                break;
            }
        }

        if (new_owner == null) {
            response.append("El usuario no existe.\n");
            return;
        }

        element.setOwnerID(new_owner.getGUID());

        fsGroup new_group_owner = null;
        if (who.length > 1) {
            for (fsGroup group : shell.fsManager.getAllTheGroups()) {
                if (group.getName().equals(who[0])) {
                    new_group_owner = group;
                    break;
                }
            }

            if (new_group_owner == null) {
                response.append("El grupo no existe.\n");
                return;

            }
        }

        if (new_group_owner != null)
            element.setGroupID(new_group_owner.getGUID());

        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: chown usuario[:grupo] [ruta del archivo]\n");
        help.append("Cambia el dueño y grupo de un elemento.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
