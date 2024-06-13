package fsSim;

import java.util.Date;
import java.util.UUID;

public class fsGroup {
    private String name;
    private String guid;
    private Date created_d;


    public fsGroup(String name) {
        this.name = name;
        this.guid = UUID.randomUUID().toString();
        this.created_d = new Date();
    }

    public String getName() {
        return name;
    }

    public String getGUID() {
        return guid;
    }

    public Date getDate() {
        return created_d;
    }
    
}
