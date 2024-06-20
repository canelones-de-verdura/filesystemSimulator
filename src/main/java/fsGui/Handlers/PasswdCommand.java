package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsUser;

import java.util.ArrayList;

public class PasswdCommand extends BaseCommand {
    public PasswdCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "passwd" };
        super.arguments = new String[] { "f" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] messageParts = message.split(" ");
        if (arguments.contains("f")) {
            if (messageParts.length < 2) {
                response.append("No se especificó un usuario o contraseña.\n");
                response.append(showHelp());
                return;
            }
        } else {
            if (messageParts.length < 3) {
                response.append("No se especificó un usuario o falta una contraseña.\n");
                response.append(showHelp());
                return;
            }
        }
        String username = messageParts[0];
        String oldPassword = (arguments.contains("f") ? "" : messageParts[1]);
        String newPassword = (arguments.contains("f") ? messageParts[1] : messageParts[2]);

        if (shell.user.getName() != "groot") {
            response.append("No tienes permisos para cambiar la contraseña.");
            return;
        }
        ArrayList<fsUser> users = shell.fsManager.getAllTheUsers();
        for (fsUser user : users) {
            if (user.getName().equals(username)) {
                if (user.setPassword(oldPassword, newPassword)) {
                    shell.fsManager.updatePasswdFile(user, 1);
                    response.append("Contraseña cambiada exitosamente.");
                    return;
                } else {
                    response.append("Contraseña incorrecta.");
                    return;
                }
            }
        }
        response.append("Usuario no encontrado.");
        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("--------------------------------------------------------\n");
        help.append("Uso: passwd [usuario] [vieja contraseña] [nueva contraseña]\n");
        help.append("Cambia la contraseña del usuario especificado.\n");
        help.append("Argumentos:\n");
        help.append("  -f: Setea la contraseña por primera vez, no requiere la vieja contraseña.\n");
        help.append("--------------------------------------------------------");
        return help.toString();
    }
}
