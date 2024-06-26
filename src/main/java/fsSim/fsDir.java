package fsSim;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class fsDir implements fsIElement {
    private String name;
    private String ownerID;
    private String groupID;
    private int size;

    private ArrayList<fsLink> referenced_by;

    // Metadata
    private Date created_d;
    private Date last_access_d;
    private Date last_modified_d;

    private Map<String, fsIElement> contents;

    public fsDir(String name, fsDir parent, String uid, String guid) {
        this.name = name;
        this.ownerID = uid;
        this.groupID = guid;
        this.size = 4096;
        this.referenced_by = null;
        this.contents = new HashMap<>();
        this.contents.put("..", parent);
        this.contents.put(".", this);
        this.last_access_d = this.last_modified_d = this.created_d = new Date();
    }

    public void setName(String new_name) {
        this.name = new_name;
    }

    public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
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

    public int getSize() {
        return size;
    }

    public ArrayList<fsLink> getReferenced_by() {
        return referenced_by;
    }

    public void setReferenced_by(ArrayList<fsLink> referenced_by) {
        this.referenced_by = referenced_by;
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
        fsIElement elem = contents.get(name);
        if (elem.getReferenced_by() != null)
            for (fsLink link : elem.getReferenced_by())
                link.setReference(null);

        contents.remove(name);
    }

    public void changeParent(fsIElement new_parent) {
        contents.replace("..", new_parent);
    }

}
