package fsSim;

import java.util.Date;
import java.util.UUID;

public class fsGroup {
    private String name;
    private String guid;


    public fsGroup(String name) {
        this.name = name;
        this.guid = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public String getGUID() {
        return guid;
    }

}
