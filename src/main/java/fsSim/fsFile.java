package fsSim;

import java.util.Date;
import java.util.concurrent.Semaphore;

public class fsFile implements fsIElement {
    public String name;
    public String owner;
    public String guid;
    public String permissions;
    public int size;

    // Metadata
    public String created_d;
    public String last_modified_d;

    // Semáforo para evitar accesos concurrentes
    private Semaphore semi;

    private String data;

    public fsFile(String name) {
        this.name = name;
        this.last_modified_d = this.created_d = new Date().toString();
        this.semi = new Semaphore(1);

        // ... ?
    }

    private void open() {
        try {
			semi.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
            System.out.println("🥱");
		}
    }

    private void close() {
        semi.release();
    }

    public void write(String new_data, boolean overwrite) {
        // TODO
        // De seguro hay una mejor manera de manejar los opens.
        open();
        if (overwrite) {
            data = new_data;
        } else {
            data.concat(new_data);
        }
        last_modified_d = new Date().toString();
        close();
    }

    public String read() {
        return data;
    }
}
