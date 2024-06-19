package fsSim;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fsDir implements fsIElement {
    private String name;
    private String ownerID;
    private String groupID; // TODO manejar permisos
    //private int permissions;
    private int size;

    // Metadata
    private Date created_d;
    private Date last_access_d;
    private Date last_modified_d;

    private Map<String, fsIElement> contents;

    // TODO? Sacar parent de los parámetros
    public fsDir(String name, fsDir parent, String uid, String guid) {
        this.name = name;
        this.ownerID = uid;
        this.groupID = guid;
        //this.permissions = 622;
        this.size = 4096;
        this.contents = new HashMap<>();
        this.contents.put("..", parent);
        this.contents.put(".", this);
        this.last_access_d = this.last_modified_d = this.created_d = new Date();
    }

    /* Getters */
    public String getName() {
        return name;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public String getGUID() {
        return groupID;
    }

    /*
    public int getPermissions() {
        return permissions;
    }
     */

    public int getSize() {
        return size;
    }

    public Date getCreationDate() {
        return created_d;
    }

    public Date getAccessDate() {
        return last_access_d;
    }

    public Date getModifiedDate() {
        return last_modified_d;
    }

    /* Primitivas para manejar directorios */
    public fsIElement getElement(String name) {
        last_access_d = new Date();
        return contents.get(name);
    }

    public Set<String> getContents() {
        last_access_d = new Date();
        return this.contents.keySet();
    }

    public void move(fsIElement element) {
        last_access_d = last_modified_d = new Date();
        contents.put(element.getName(), element);
        if (element instanceof fsDir) {
            ((fsDir) element).changeParent(this);
        }
    }

    public void remove(String name) {
        last_access_d = last_modified_d = new Date();
        contents.remove(name);
    }

    public void changeParent(fsIElement new_parent) {
        contents.replace("..", new_parent);
    }

}
