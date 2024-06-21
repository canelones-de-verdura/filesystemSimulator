package fsSim;

import java.util.ArrayList;

public class fsSimManager {
    private static fsSimManager instance = null;

    private fsUser groot;
    private fsGroup root_group;
    private fsDir filesystem_root;

    private ArrayList<fsUser> users;
    private ArrayList<fsGroup> groups;

    private ArrayList<fsUser> logged_users;

    public static synchronized fsSimManager getInstance() {
        if (instance == null)
            instance = new fsSimManager();

        return instance;
    }

    private fsSimManager() {
        // Creamos el root
        String r_home = "/groot";
        this.root_group = new fsGroup("groot");
        this.groot = new fsUser("groot", "1234", this.root_group.getGUID(), null, r_home, "/bin/bigpotato");

        // Cosas de la sesión
        this.logged_users = null;

        // Cosas del "sistema"
        this.users = new ArrayList<>();
        this.groups = new ArrayList<>();

        // Agregamos a las colecciones
        this.users.add(this.groot);
        this.groups.add(this.root_group);

        // Creamos la estructura de carpetas
        this.filesystem_root = new fsDir("", null, groot.getUID(), root_group.getGUID());
        // Arreglamos la referencia a ".."
        this.filesystem_root.changeParent(filesystem_root);
        generateFileStructure();
        fsUser eze = addUser("eze", groot.getUID());
        fsUser facu = addUser("facu", groot.getUID());

        eze.setPassword("", "2210");
        updatePasswdFile(eze, 1);
        facu.setPassword("", "2509");
        updatePasswdFile(facu, 1);

    }

    public boolean Login(fsUser user, String password, Thread t) {
        if (logged_users == null)
            logged_users = new ArrayList<>();

        if (user != null && user.LogIn(password, t))
            logged_users.add(user);
        else
            return false;

        return true;
    }

    public boolean Logout(String uid) {
        if (logged_users == null)
            return false;

        fsUser user = null;
        for (fsUser usr : logged_users)
            if (usr.getUID().equals(uid)) {
                usr.LogOut();
                user = usr;
                break;
            }

        if (user == null)
            return false;

        logged_users.remove(user);

        return true;
    }

    public fsIElement getElementInFs(String path) {
        // No rompemos si no hay user porque precisamos el método para la configuración
        // inicial.
        String[] dirs = path.split("/");
        fsIElement current_dir = filesystem_root;

        if (dirs.length == 0)
            return current_dir;

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = ((fsDir) current_dir).getElement(jumpto);

                if (current_dir instanceof fsLink)
                    current_dir = ((fsLink) current_dir).getReference();

                if (current_dir == null)
                    return null;
            }
        }

        return ((fsDir) current_dir).getElement(dirs[dirs.length - 1]);
    }

    public fsUser addUser(String name, String caller_uid) {
        if (!groot.getUID().equals(caller_uid))
            throw new RuntimeException();

        String new_home = String.format("/home/%s", name);
        fsGroup new_group = new fsGroup(name);
        fsUser new_user = new fsUser(name, "", new_group.getGUID(), null, new_home, "/bin/bigpotato");

        users.add(new_user);
        groups.add(new_group);

        updatePasswdFile(new_user, 0);
        updateGroupFile(new_group);

        // Creamos el home
        fsDir aux = (fsDir) getElementInFs("/home");
        fsDir user_home = new fsDir(new_user.getName(), aux, new_user.getUID(), new_user.getGUID());
        aux.move(user_home);

        return new_user;
    }

    public fsUser getUser(String uid) {
        if (logged_users == null)
            throw new RuntimeException();

        for (fsUser user : users)
            if (user.getGUID().equals(uid))
                return user;

        return null;
    }

    public void removeUser(String user, String caller_uid) {
        if (!groot.getUID().equals(caller_uid))
            throw new RuntimeException();

        fsUser usr = null;
        for (fsUser usr2 : users) {
            if (usr2.getUID().equals(user)) {
                usr = usr2;
                break;
            }
        }

        updatePasswdFile(usr, -1);
        users.remove(usr);
    }

    public void addGroup(String name, String caller_uid) {
        if (!groot.getUID().equals(caller_uid))
            throw new RuntimeException();

        fsGroup new_group = new fsGroup(name);
        groups.add(new_group);
        updateGroupFile(new_group);
    }

    public fsGroup getGroup(String guid) {
        if (logged_users == null)
            throw new RuntimeException();

        for (fsGroup group : groups)
            if (group.getGUID().equals(guid))
                return group;

        return null;
    }

    public ArrayList<fsUser> getAllTheUsers() {
        return users;
    }

    public ArrayList<fsGroup> getAllTheGroups() {
        return groups;
    }

    public boolean createFile(String path, fsUser creator) {
        if (!logged_users.contains(creator))
            throw new RuntimeException();

        // Obtenemos la referencia de la carpeta padre
        String[] dirs = path.split("/");
        fsIElement current_dir = filesystem_root;
        String name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 2] : dirs[dirs.length - 1];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                if (current_dir instanceof fsLink)
                    current_dir = ((fsLink) current_dir).getReference();

                current_dir = ((fsDir) current_dir).getElement(jumpto);
                // Chequeamos que el directorio exista
                if (current_dir == null)
                    return false;
            }
        }

        // Chequeamos que el nombre del nuevo elemento ya no esté en uso
        if (((fsDir) current_dir).getElement(name) != null)
            return false;

        // Creamos y colocamos el archivo
        fsFile new_elem = new fsFile(name, creator.getUID(), creator.getGUID());
        ((fsDir) current_dir).move(new_elem);
        return true;
    }

    public boolean createDir(String path, fsUser creator) {
        if (!logged_users.contains(creator))
            throw new RuntimeException();

        // Obtenemos la referencia de la carpeta padre
        String[] dirs = path.split("/");
        fsIElement current_dir = filesystem_root;
        String name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 2] : dirs[dirs.length - 1];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = ((fsDir) current_dir).getElement(jumpto);
                if (current_dir instanceof fsLink)
                    current_dir = ((fsLink) current_dir).getReference();

                // Chequeamos que el directorio exista
                if (current_dir == null)
                    return false;
            }
        }

        // Chequeamos que el nombre del nuevo elemento ya no esté en uso
        if (((fsDir) current_dir).getElement(name) != null)
            return false;

        // Creamos y colocamos el directorio
        fsDir new_elem = new fsDir(name, (fsDir) current_dir, creator.getUID(), creator.getGUID());
        ((fsDir) current_dir).move(new_elem);
        return true;
    }

    public boolean createLink(String reference_path, String new_path, fsUser creator) {
        if (!logged_users.contains(creator))
            throw new RuntimeException();

        // Obtenemos la referencia al elemento original
        String[] dirs = reference_path.split("/");
        fsIElement current_dir = filesystem_root;
        String r_name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 2] : dirs[dirs.length - 1];
        fsIElement reference;

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                if (current_dir instanceof fsLink)
                    current_dir = ((fsLink) current_dir).getReference();

                current_dir = ((fsDir) current_dir).getElement(jumpto);
                if (current_dir == null)
                    return false;
            }
        }

        if (current_dir instanceof fsLink)
            current_dir = ((fsLink) current_dir).getReference();

        reference = ((fsDir) current_dir).getElement(r_name);
        if (reference == null)
            return false;

        // Obtenemos la referencia al directorio donde va el link
        current_dir = filesystem_root;
        dirs = new_path.split("/");
        String n_name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 2] : dirs[dirs.length - 1];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                if (current_dir instanceof fsLink)
                    current_dir = ((fsLink) current_dir).getReference();

                current_dir = ((fsDir) current_dir).getElement(jumpto);
                if (current_dir == null)
                    return false;
            }
        }

        // Si la ruta de destino no incluye el nombre nuevo, entonces usa el mismo que
        // el archivo original
        if (!current_dir.getName().equals(n_name)) {
            // Chequeamos que el nombre del nuevo elemento ya no esté en uso
            if (((fsDir) current_dir).getElement(n_name) != null)
                return false;
        } else {
            n_name = r_name;
        }

        // Creamos y colocamos el archivo
        fsIElement new_elem = new fsLink(reference, n_name, creator.getUID(), creator.getGUID());

        if (reference.getReferenced_by() == null)
            reference.setReferenced_by(new ArrayList<>());

        reference.getReferenced_by().add((fsLink) new_elem);

        ((fsDir) current_dir).move(new_elem);
        return true;
    }

    /* Auxiliares */
    // -1 borra el user
    // 0 lo agrega
    // 1 lo reescribe
    public void updatePasswdFile(fsUser user, int opt) {
        // user:passwd:uid:guid:home:shell
        String line = String.format("%s:%s:%s:%s:%s:%s",
                user.getName(), user.getPassword(), user.getUID(),
                user.getGUID(), user.getHome(), user.getShell());

        fsFile passwd = (fsFile) getElementInFs("/etc/passwd");
        passwd.open();
        if (opt == 0) {
            if (passwd.read() != null)
                line = "\n" + line;

            passwd.write(line, false);
        }

        if (opt == -1) {
            String texto_editado = passwd.read().replace("\n" + line, "");
            passwd.write(texto_editado, true);
        }

        if (opt == 1) {
            String file = passwd.read();
            String[] lineas_en_archivo = file.split("\n");
            int index = 0;
            for (; index < lineas_en_archivo.length; ++index)
                if (lineas_en_archivo[index].contains(user.getName()))
                    break;

            lineas_en_archivo[index] = line;
            String texto_editado = String.join("\n", lineas_en_archivo);
            passwd.write(texto_editado, true);
        }

        passwd.close();
    }

    private void updateGroupFile(fsGroup group) {
        // group:guid
        String line = String.format("%s:%s\n", group.getName(), group.getGUID());

        fsFile passwd = (fsFile) getElementInFs("/etc/group");
        passwd.open();
        passwd.write(line, false);
        passwd.close();
    }

    private void generateFileStructure() {
        filesystem_root.move(new fsDir("groot", filesystem_root, groot.getUID(), root_group.getGUID()));
        filesystem_root.move(new fsDir("home", filesystem_root, groot.getUID(), root_group.getGUID()));
        filesystem_root.move(new fsDir("etc", filesystem_root, groot.getUID(), root_group.getGUID()));

        fsDir etc = (fsDir) filesystem_root.getElement("etc");

        // Agregamos el /etc/passwd
        fsFile file = new fsFile("passwd", groot.getUID(), root_group.getGUID());
        etc.move(file);
        updatePasswdFile(groot, 0);

        // Agregamos el /etc/group
        file = new fsFile("group", groot.getUID(), root_group.getGUID());
        etc.move(file);
        updateGroupFile(root_group);
    }

}
