package fsGui;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import fsGui.Handlers.*;

public class CommandFactory {
    public Map<String, BaseCommand> commandMap = new HashMap<>();

    public CommandFactory() {
        register(new ManCommand(this));
        register(new EchoCommand(this));
        register(new ENanoCommand(this));
        register(new EasterEggsCommand(this));
        register(new ExitCommand(this));
        register(new LsCommand(this));
        register(new CdCommand(this));
        register(new PwdCommand(this));
        register(new TouchCommand(this));
        register(new MkdirCommand(this));
        register(new CatCommand(this));
        register(new LoginCommand(this));
        register(new UserAddCommand(this));
        register(new PasswdCommand(this));
        register(new CleanCommand(this));
        register(new MvCommand(this));
        register(new CpCommand(this));
    }

    public void register(BaseCommand command) {
        commandMap.put(String.join("-",command.getKeywords()), command);
    }

    //TODO no se si la lógica de esto debería de ir en el factory o en el shell
    public BaseCommand getCommand(String commandName) {
        for (String key : commandMap.keySet()) {
            for(String keyword : key.split("-")) {
                if (keyword.toLowerCase().equals(commandName.toLowerCase())) {
                    return commandMap.get(key);
                }
            }
        }
        return null;
    }

    public void handleCommand(String commandName, ArrayList<String> arguments, String message, StringBuilder response, BigPotatoShell shell) {
        if(shell.user == null && !commandName.equals("login")) {
            response.append("No se ha iniciado sesión.");
            return;
        }
        BaseCommand command = getCommand(commandName);
        if (command != null) {
            boolean wasSuccess = command.handle(message, arguments, response, shell);
            if (!wasSuccess) {
                response.setLength(0);
                response.append("Error al ejecutar el comando \"" + commandName + "\"");
            }
        } else {
            response.append("Comando \"" + commandName + "\" no encontrado");
        }
    }
}
