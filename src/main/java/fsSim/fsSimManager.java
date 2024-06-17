package fsSim;

import java.util.HashMap;
import java.util.Map;

// TODO: Cambiar bools por ints para los valores de retorno?
public class fsSimManager {
    private fsUser groot;
    private fsGroup root_group;
    private fsDir filesystem_root;

    private Map<String, fsUser> users_by_uid;
    private Map<String, fsGroup> groups_by_guid;

    public fsSimManager() {
        // Creamos el root
        String r_home = "/groot";
        this.root_group = new fsGroup("groot");
        this.groot = new fsUser("groot", "1234", this.root_group.getGUID(), null, r_home, "/bin/bigpotato");

        this.users_by_uid = new HashMap<>();
        this.groups_by_guid = new HashMap<>();

        // Agregamos a las colecciones
        this.users_by_uid.put(this.groot.getUID(), this.groot);
        this.groups_by_guid.put(this.root_group.getGUID(), this.root_group);

        // Creamos la estructura de carpetas
        this.filesystem_root = new fsDir("", null, groot.getUID(), root_group.getGUID());
        // Arreglamos la referencia a ".."
        this.filesystem_root.changeParent(filesystem_root);
        generateFileStructure();
    }

    public fsIElement getElementInFs(String path) {
        String[] dirs = path.split("/");
        fsDir current_dir = filesystem_root;

        if (dirs.length == 0)
            return current_dir;

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = (fsDir) current_dir.getElement(jumpto);
                if (current_dir == null)
                    return null;
            }
        }

        return current_dir.getElement(dirs[dirs.length - 1]);
    }

    public void addUser(String name) {
        String new_home = String.format("/home/%s", name);
        fsGroup new_group = new fsGroup(name);
        fsUser new_user = new fsUser(name, "", new_group.getGUID(), null, new_home, "/bin/bigpotato");

        users_by_uid.put(new_user.getUID(), new_user);
        groups_by_guid.put(new_group.getGUID(), new_group);

        updatePasswdFile(new_user, true);
    }

    public fsUser getUser(String uid) {
        return users_by_uid.get(uid);
    }

    public void removeUser(String uid) {
        // TODO: Ver que hacemos con los grupos
        updatePasswdFile(users_by_uid.get(uid), false);
        users_by_uid.remove(uid);
    }

    public void addGroup(String name) {
        fsGroup new_group = new fsGroup(name);
        groups_by_guid.put(new_group.getGUID(), new_group);
        updateGroupFile(new_group);
    }

    public fsGroup getGroup(String guid) {
        return groups_by_guid.get(guid);
    }

    public Map<String, fsUser> getAllTheUsers() {
        return users_by_uid;
    }

    public Map<String, fsGroup> getAllTheGroups() {
        return groups_by_guid;
    }

    public boolean createFile(String path, fsUser creator) {
        // Obtenemos la referencia de la carpeta padre
        String[] dirs = path.split("/");
        fsDir current_dir = filesystem_root;
        String name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 1] : dirs[dirs.length - 2];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = (fsDir) current_dir.getElement(jumpto);
                // Chequeamos que el directorio exista
                if (current_dir == null)
                    return false;
            }
        }

        // Chequeamos que el nombre del nuevo elemento ya no esté en uso
        if (current_dir.getElement(name) != null)
            return false;

        // Creamos y colocamos el archivo
        fsFile new_elem = new fsFile(name, creator.getUID(), creator.getGUID());
        current_dir.move(new_elem);
        return true;
    }

    public boolean createDir(String path, fsUser creator) {
        // Obtenemos la referencia de la carpeta padre
        String[] dirs = path.split("/");
        fsDir current_dir = filesystem_root;
        String name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 1] : dirs[dirs.length - 2];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = (fsDir) current_dir.getElement(jumpto);
                // Chequeamos que el directorio exista
                if (current_dir == null)
                    return false;
            }
        }

        // Chequeamos que el nombre del nuevo elemento ya no esté en uso
        if (current_dir.getElement(name) != null)
            return false;

        // Creamos y colocamos el directorio
        fsDir new_elem = new fsDir(name, current_dir, creator.getUID(), creator.getGUID());
        current_dir.move(new_elem);
        return true;
    }

    public boolean createLink(String reference_path, String new_path, fsUser creator) {
        // Obtenemos la referencia al elemento original
        String[] dirs = reference_path.split("/");
        fsDir current_dir = filesystem_root;
        String r_name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 1] : dirs[dirs.length - 2];
        fsIElement reference;

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = (fsDir) current_dir.getElement(jumpto);
                if (current_dir == null)
                    return false;
            }
        }

        reference = current_dir.getElement(r_name);
        if (reference == null)
            return false;

        // Obtenemos la referencia al directorio donde va el link
        current_dir = filesystem_root;
        dirs = new_path.split("/");
        String n_name = dirs[dirs.length - 1] == null ? dirs[dirs.length - 1] : dirs[dirs.length - 2];

        for (int i = 1; i < dirs.length - 1; ++i) {
            String jumpto = dirs[i];
            if (jumpto != null) {
                current_dir = (fsDir) current_dir.getElement(jumpto);
                if (current_dir == null)
                    return false;
            }
        }

        // Si la ruta de destino no incluye el nombre nuevo, entonces usa el mismo que el archivo original
        if (!current_dir.getName().equals(n_name)) {
            // Chequeamos que el nombre del nuevo elemento ya no esté en uso
            if (current_dir.getElement(n_name) != null)
                return false;
        } else {
            n_name = r_name;
        }

        // Creamos y colocamos el archivo
        fsIElement new_elem = new fsLink(reference, n_name, creator.getUID(), creator.getGUID());
        current_dir.move(new_elem);
        return true;
    }

    /* Auxiliares */
    private void updatePasswdFile(fsUser user, boolean add) {
        // user:passwd:uid:guid:comentarios:home:shell
        String line = String.format("%s:%s:%s:%s:%s:%s\n",
                user.getName(), user.getPassword(), user.getUID(),
                user.getGUID(), user.getHome(), user.getShell());

        fsFile passwd = (fsFile) getElementInFs("/etc/passwd");
        passwd.open();
        if (add) {
            passwd.write(line, false);
        } else {
            String texto_editado = passwd.read().replace(line, "");
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
        updatePasswdFile(groot, true);

        // Agregamos el /etc/group
        file = new fsFile("group", groot.getUID(), root_group.getGUID());
        etc.move(file);
        updateGroupFile(root_group);
    }

}
