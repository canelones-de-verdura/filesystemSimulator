package fsSim;

import java.util.Date;

public interface fsIElement {
    public String getName();

    public String getOwnerID();

    public String getGUID();

    //public int getPermissions();

    public int getSize();

    public Date getCreationDate();

    public Date getAccessDate();

    public Date getModifiedDate();

}
