package fsGui;

import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import fsSim.fsSimManager;
import fsSim.fsUser;

/**
 * Clase que implementa un shell simple.
 * Es el nexo entre la interfaz gráfica y la lógica de ejecución de comandos.
 */
public class BigPotatoShell {

    public String PWD;
    public String USER;
    public String HOST;
    public fsUser user; // current user
    public ArrayList<String> commandHistory;
    private Stack<Map<fsUser, ArrayList<String>>> usersStack;

    public BigPotatoShell() {
        usersStack = new Stack<Map<fsUser, ArrayList<String>>>();
    }

    public boolean addUserToStack(fsUser user) {
        try {
            ArrayList<String> historyCommands = new ArrayList<String>();
            usersStack.push(Map.of(user, historyCommands));
            this.user = user;
            this.HOST = "localhost";
            this.PWD = user.getHome();
            this.USER = user.getName();
            this.commandHistory = historyCommands;
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public fsUser popUserFromStack() {
        fsUser user_return = usersStack.peek().keySet().iterator().next();
        usersStack.pop();
        if (usersStack.isEmpty()) {
            this.user = null;
            this.HOST = "localhost";
            this.PWD = "/";
            this.USER = "";
            this.commandHistory = new ArrayList<String>();
        } else {
            Map<fsUser, ArrayList<String>> registry = usersStack.peek();
            fsUser user = registry.keySet().iterator().next();
            ArrayList<String> historyCommands = registry.get(user);
            this.user = user;
            this.HOST = "localhost";
            this.PWD = user.getHome();
            this.USER = user.getName();
            this.commandHistory = historyCommands;
        }
        return user_return;
    }

    private CommandFactory commandFactory = new CommandFactory();
    public fsSim.fsSimManager fsManager = fsSimManager.getInstance();

    /**
     * Procesa un comando ingresado por el usuario.
     * 
     * @param command Comando ingresado por el usuario.
     * @return Resultado de la ejecución del comando.
     */
    public String proccessCommand(String command) {
        StringBuilder response = new StringBuilder();
        String[] parts = command.split(" ");
        ArrayList<String> arguments = new ArrayList<String>();
        String commandName = parts[0];
        String message = "";
        if (parts.length > 1) {
            if (parts[1].startsWith("-")) {
                for (int i = 1; i < parts[1].length(); i++) {
                    String flag = parts[i].replace("-", "");
                    if (flag != null)
                        arguments.add(flag);
                }
                String restOfMessage = "";
                for (int i = 2; i < parts.length; i++) {
                    restOfMessage += parts[i] + " ";
                }
                message = !restOfMessage.isEmpty() ? restOfMessage.substring(0, restOfMessage.length() - 1) : "";
            } else {
                String restOfMessage = "";
                for (int i = 1; i < parts.length; i++) {
                    restOfMessage += parts[i] + " ";
                }
                message = !restOfMessage.isEmpty() ? restOfMessage.substring(0, restOfMessage.length() - 1) : "";
            }
        }

        commandFactory.handleCommand(commandName, arguments, message, response, this);
        return response.toString();
    }

    /*
     * String onlyCommand = command.split(" ")[0];
     * switch (onlyCommand) {
     * 
     * case "chmod":
     * case "chgrp":
     * }
     */
}
