package fsSim;

import java.util.ArrayList;
import java.util.Date;

public interface fsIElement {
    public void setName(String new_name);

    public String getName();

    public String getOwnerID();

    public String getGUID();

    public ArrayList<fsLink> getReferenced_by();

    public void setReferenced_by(ArrayList<fsLink> referenced_by);

    public int getSize();

    public Date getCreationDate();

    public Date getAccessDate();

    public Date getModifiedDate();

}
