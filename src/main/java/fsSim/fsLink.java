package fsSim;

import java.util.ArrayList;
import java.util.Date;

public class fsLink implements fsIElement {
    private fsIElement reference;

	private String name;
    private String ownerID;
    private String groupID;
    private int size;

    private ArrayList<fsLink> referenced_by;

	// Metadata
    private Date created_d;
    private Date last_access_d;
    private Date last_modified_d;

    public fsLink(fsIElement original, String new_name, String uid, String guid) {
        this.reference = original;
        this.name = new_name;
        this.ownerID = uid;
        this.groupID = guid;
        this.size = 10;
        this.referenced_by = null;
        this.last_access_d = this.last_modified_d = this.created_d = new Date();
    }

    public void setName(String new_name) {
        this.name = new_name;
    }

    public void setReference(fsIElement reference) {
        this.reference = reference;
    }

    public void setOwnerID(String ownerID) {
		this.ownerID = ownerID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

    /* Getters */
    public fsIElement getReference() {
        return reference;
    }

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

}
