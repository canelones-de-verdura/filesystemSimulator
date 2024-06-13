package fsGui;
/**
 * Clase que implementa un shell simple.
 * Es el nexo entre la interfaz gráfica y la lógica de ejecución de comandos.
 */
public class BigPotatoShell {
    
    public String PWD;
    public String USER;
    public String HOST;

    public BigPotatoShell() {
        PWD = "/";
        USER = "user";
        HOST = "localhost";
    }


    private CommandFactory commandFactory = new CommandFactory();

    /**
     * Procesa un comando ingresado por el usuario.
     * @param command Comando ingresado por el usuario.
     * @return Resultado de la ejecución del comando.
     */
    public String proccessCommand(String command) {
        StringBuilder response = new StringBuilder();
        String[] parts = command.split(" ");
        String commandName = parts[0];
        String message = command.substring(commandName.length()).trim();
        commandFactory.handleCommand(commandName, message, response, this);
        return response.toString();
    }

    /*
     * String onlyCommand = command.split(" ")[0];
        switch (onlyCommand) {
            case "help":
                return help();
            case "exit":
                System.exit(0);
            case "cd":
            case "pwd":
            case "cp":
            case "mv":
            case "tree":
            case "ls":
            case "cat":
            case "mkdir":
            case "rm":
            case "touch":
            case "chmod":
            case "chown":
            case "chgrp":
            case "history":
            case "clear":
            case "usermod":
            case "enano":
                new SimpleNano(command.split(" ").length>1 ? command.split(" ")[1] : "Untitled");
                return "";
            case "echo":
                return echo(command);
            default:
                return "Comando \""+command+"\" no reconocido.";
        }
     */
}
