package fsSim;

import java.util.Date;
import java.util.concurrent.Semaphore;

public class fsFile implements fsIElement {
    private String name;
    private String ownerID;
    private String groupID; // TODO manejar permisos
    //private int permissions;
    private int size;

    // Metadata
    private Date created_d;
    private Date last_access_d;
    private Date last_modified_d;

    // Semáforo para evitar accesos concurrentes
    private Semaphore semi;
    private Thread workingThread;

    private String data;

    public fsFile(String name, String uid, String guid) {
        this.name = name;
        this.ownerID = uid;
        this.groupID = guid;
        //this.permissions = 622;
        this.size = 0;
        this.last_access_d = this.last_modified_d = this.created_d = new Date();
        this.semi = new Semaphore(1);
        this.data = null;
        this.workingThread = null;
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

    /* Primitivas para manejar archivos */
    public void open() {
        try {
            semi.acquire();
            workingThread = Thread.currentThread();
            last_access_d = new Date();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("🥱");
        }
    }

    public void close() {
        workingThread = null;
        semi.release();
    }

    public synchronized boolean write(String new_data, boolean overwrite) {
        if (semi.availablePermits() != 0)
            return false;

        if (workingThread != Thread.currentThread())
            return false;

        try {
            if (overwrite || (data == null)) {
                data = new_data;
            } else {
                data = data.concat(new_data);
            }
            last_modified_d = new Date();
            size = data.length();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("🥱");
            return false;
        }
    }

    // TODO: Hacer validación de semáforo y thread???
    public String read() {
        last_access_d = new Date();
        return data;
    }
}
