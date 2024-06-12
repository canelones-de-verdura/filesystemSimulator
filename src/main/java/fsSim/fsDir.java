package fsSim;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class fsDir implements fsIElement {
    public String name;
    public String owner;
    public String guid;
    public String permissions;
    // Metadata
    public String created_d;
    public String last_modified_d;

    public Map<String, fsIElement> contents;

    public fsDir(String name, fsDir parent) {
        this.name = name;
        this.contents = new HashMap<>();
        this.contents.put("..", parent);
        this.contents.put(".", this);
        this.last_modified_d = this.created_d = new Date().toString();
        // ...?

    }

    public fsIElement getContents(String name) {
        return contents.get(name);
    }

    public void create(String name, boolean dir) {
        fsIElement new_elem = dir ? new fsDir(name, this) : new fsFile(name);

        contents.put(name, new_elem);
    }

    public void remove(String name) {
        contents.remove(name);
    }

}
