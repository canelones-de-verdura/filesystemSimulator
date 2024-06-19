package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsUser;

import java.util.ArrayList;
import java.util.Map;

public class LoginCommand extends BaseCommand {
    public LoginCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String[] { "login" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response,
            BigPotatoShell shell) {
        String[] loginArgs = message.split(" ");
        if (loginArgs.length != 2) {
            response.append("Esperados dos argumentos: [usuario] [contraseña].\n");
            response.append(showHelp());
            return;
        }
        String username = loginArgs[0].trim();
        String password = loginArgs[1].trim();

        Map<String, fsUser> users = shell.fsManager.getAllTheUsers();
        for (String key : users.keySet()) {
            if (users.get(key).getName().equals(username)) {
                fsUser user = users.get(key);
                if (shell.fsManager.Login(user.getUID(), password)) {
                    if (!shell.addUserToStack(user))
                        response.append("No se pudo iniciar sesión.");
                    else
                        response.append("Último inicio de sesión " + user.getLoginDate() + ".");
                    return;
                } else {
                    response.append("Usuario o contraseña incorrectos.");
                    int fails = user.getFailedLoginAttempts();
                    response.append("\nIntentos fallidos: " + fails + ".");
                    if (fails > 5)
                        response.append(" Yo me tomaría un rato antes de intentar de nuevo.");
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
        help.append("Uso: login [usuario] [contraseña]\n");
        help.append("Inicia sesión con el usuario especificado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
