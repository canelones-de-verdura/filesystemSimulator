package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsUser;

import java.util.ArrayList;

public class UserDelCommand extends BaseCommand {
    public UserDelCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "userdel" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        if (message.isEmpty()) {
            response.append("No se especificó un usuario.\n");
            response.append(showHelp());
            return;
        }
        String username = message.split(" ")[0].trim();

        ArrayList<fsUser> users = shell.fsManager.getAllTheUsers();
        fsUser condenado = null;
        for (fsUser user : users) {
            if (user.getName().equals(username)) {
                condenado = user;
                break;
            }
        }

        if (condenado == null) {
            response.append("El usuario no existe.");
            return;
        }

        try {
            shell.fsManager.removeUser(condenado.getUID(), shell.user.getUID());
            response.append("Usuario eliminado exitosamente.");
            return;
        } catch (Exception e) {
            response.append("No tienes permisos para eliminar un usuario.");
        }

        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: userdel [usuario]\n");
        help.append("Elimina un usuario con el nombre especificado.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}
