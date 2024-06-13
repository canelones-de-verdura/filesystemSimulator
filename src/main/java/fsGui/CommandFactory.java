package fsGui;

import java.util.HashMap;
import java.util.Map;

import fsGui.Handlers.*;

public class CommandFactory {
    public Map<String, BaseCommand> commandMap = new HashMap<>();

    public CommandFactory() {
        register(new ManCommand(this));
        register(new EchoCommand(this));
        register(new ENanoCommand(this));
        register(new EasterEggsCommand(this));
        register(new ExitCommand(this));
    }

    public void register(BaseCommand command) {
        commandMap.put(String.join("-",command.getKeywords()), command);
    }

    public BaseCommand getCommand(String commandName) {
        for (String key : commandMap.keySet()) {
            if (key.toLowerCase().contains(commandName.toLowerCase())) {
                return commandMap.get(key);
            }
        }
        return null;
    }

    public void handleCommand(String commandName, String message, StringBuilder response, BigPotatoShell shell) {
        BaseCommand command = getCommand(commandName);
        if (command != null) {
            boolean wasSuccess = command.handle(message, response, shell);
            if (!wasSuccess) {
                response.append("Error al procesar el comando");
            }
        } else {
            response.append("Comando no reconocido");
        }
    }
}
