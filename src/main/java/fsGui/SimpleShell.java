package fsGui;
/**
 * Clase que implementa un shell simple.
 * Es el nexo entre la interfaz gráfica y la lógica de ejecución de comandos.
 */
public class SimpleShell {
    
    private String PWD;
    private String USER;
    private String HOST;

    public SimpleShell() {
        PWD = "/";
        USER = "user";
        HOST = "localhost";
    }

    public String getPWD() {
        return PWD;
    }
    public String getUSER() {
        return USER;
    }
    public String getHOST() {
        return HOST;
    }

    /**
     * Procesa un comando ingresado por el usuario.
     * @param command Comando ingresado por el usuario.
     * @return Resultado de la ejecución del comando.
     */
    public String proccessCommand(String command) {
        String onlyCommand = command.split(" ")[0];
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
    }

    /**
     * Muestra información de ayuda sobre los comandos disponibles.
     * @return La lista de comandos disponibles.
     */
    public String help() {
        StringBuilder sb = new StringBuilder();
        sb.append("Comandos disponibles:\n");
        sb.append("help: Muestra la lista de comandos disponibles.\n");
        sb.append("exit: Cierra el shell.\n");
        // TODO Agregar los comandos disponibles cuando estén implementados.
        return sb.toString();
    }
    /**
     * Muestra el comando ingresado.
     * @param command Comando ingresado por el usuario.
     * @return El comando ingresado.
     */
    public String echo(String command) {
        return command.replace("echo ", "").replace("\"", "");
    }
}
