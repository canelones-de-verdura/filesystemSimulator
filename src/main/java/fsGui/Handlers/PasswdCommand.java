package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsUser;

import java.util.ArrayList;
import java.util.Map;

public class PasswdCommand extends BaseCommand {
    public PasswdCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "passwd" };
        super.arguments = new String[] { "f" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
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
        Map<String, fsUser> users = shell.fsManager.getAllTheUsers();
        for (String key : users.keySet()) {
            if (users.get(key).getName().equals(username)) {
                fsUser user = users.get(key);
                if(user.setPassword(oldPassword, newPassword)){
                    response.append("Contraseña cambiada exitosamente.");
                    return;
                }else{
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
        help.append("-----------------------------\n");
        help.append("Uso: passwd [usuario] [vieja contraseña] [nueva contraseña]\n");
        help.append("Uso: passwd -f [usuario] [primera contraseña]\n");
        help.append("Cambia la contraseña del usuario especificado.\n");
        help.append("En caso de usar -f, se asigna la primera contraseña como nueva contraseña.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}