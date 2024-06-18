package fsSim;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private Map<String, fsSimManager> managers_by_usr_id;
    private Map<String, fsUser> users_by_uid;
    private Map<String, fsGroup> groups_by_guid;

    private void generateFileStructure() {
        //filesystem_root.move(new fsDir("groot", filesystem_root, groot.getUID(), root_group.getGUID()));
        //filesystem_root.move(new fsDir("home", filesystem_root, groot.getUID(), root_group.getGUID()));
        //filesystem_root.move(new fsDir("etc", filesystem_root, groot.getUID(), root_group.getGUID()));
        //
        //fsDir etc = (fsDir) filesystem_root.getElement("etc");
        //
        //// Agregamos el /etc/passwd
        //fsFile file = new fsFile("passwd", groot.getUID(), root_group.getGUID());
        //etc.move(file);
        //updatePasswdFile(groot, true);
        //
        //// Agregamos el /etc/group
        //file = new fsFile("group", groot.getUID(), root_group.getGUID());
        //etc.move(file);
        //updateGroupFile(root_group);
    }

    private void squareUp() {
        users_by_uid = new HashMap<>();
        groups_by_guid = new HashMap<>();
    }

    public static void main(String[] args) {

    }
}
