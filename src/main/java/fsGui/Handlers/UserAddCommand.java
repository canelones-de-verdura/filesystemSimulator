package fsGui.Handlers;

import fsGui.BigPotatoShell;
import fsGui.CommandFactory;
import fsSim.fsUser;

import java.util.ArrayList;
import java.util.Map;

public class UserAddCommand extends BaseCommand {
    public UserAddCommand(CommandFactory commandFactory) {
        super(commandFactory);
        super.keywords = new String [] { "useradd" };
    }

    @Override
    protected void internalHandle(String message, ArrayList<String> arguments, StringBuilder response, BigPotatoShell shell) {
        if(message.isEmpty()){
            response.append("No se especificó un usuario.\n");
            response.append(showHelp());
            return;
        } 
        String username = message.split(" ")[0].trim();
        Map<String, fsUser> users = shell.fsManager.getAllTheUsers();
        for (String key : users.keySet()) {
            if (users.get(key).getName().equals(username)) {
                response.append("El usuario ya existe.");
                return;
            }
        }
        try{
            shell.fsManager.addUser(username, shell.user.getUID());
            response.append("Usuario creado exitosamente.");
            return;
        }catch (Exception e){
            response.append("No tienes permisos para crear un usuario.");
        }
        return;
    }

    @Override
    public String showHelp() {
        StringBuilder help = new StringBuilder();
        help.append("-----------------------------\n");
        help.append("Uso: useradd [usuario]\n");
        help.append("Crea un usuario con el nombre especificado.\n");
        help.append("-----------------------------");
        return help.toString();
    }
}
