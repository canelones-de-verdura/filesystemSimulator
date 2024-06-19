package fsSim;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.google.common.hash.Hashing;

// TODO? Sacar los semáforos
public class fsUser {
    private String name;
    private String uid;
    private String main_guid;
    private String password;
    private List<String> groupID;
    private String home;
    private String shell;

    private Date last_logged_d;

    // Semáforo para evitar accesos concurrentes
    private Semaphore semi;
    private Thread loggedThread;

    private int failed_login_attempts;

    public fsUser(String name, String password, String guid, List<String> group, String home, String shell) {
        this.name = name;
        this.uid = this.main_guid = guid;
        this.password = Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
        this.groupID = group;
        this.home = home;
        this.shell = shell;

        this.loggedThread = null;
        this.semi = new Semaphore(1);

        this.last_logged_d = null;
    }

    public boolean LogIn(String password) {
        if (!this.password.equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())) {
            failed_login_attempts++;
            return false;
        }

        if (semi.availablePermits() == 0 || (loggedThread != null && !Thread.currentThread().equals(loggedThread)))
            return false;

        try {
            semi.acquire();
            loggedThread = Thread.currentThread();
            this.last_logged_d = new Date();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("🥱");
            return false;
        }

        return true;
    }

    public void LogOut() {
        loggedThread = null;
        semi.release();
    }

    /* Getters */
    public String getName() {
        return name;
    }

    public String getUID() {
        return uid;
    }

    public String getGUID() {
        return main_guid;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getGroups() {
        return groupID;
    }

    public String getHome() {
        return home;
    }

    public String getShell() {
        return shell;
    }

    public Date getLoginDate() {
        return last_logged_d;
    }

    /* Setters */
    public boolean setPassword(String password, String new_password) {
        if (!this.password.equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())) {
            return false;
        }

        this.password = Hashing.sha256().hashString(new_password, StandardCharsets.UTF_8).toString();
        return true;
    }

    public boolean manageGroups(String groupGUID, boolean add) {
        if (add)
            if (!groupID.contains(groupGUID))
                groupID.add(groupGUID);
            else
                return false;
        else
            groupID.remove(groupGUID);

        return true;
    }

    public void setMainGroup(String new_main_group) {
        main_guid = new_main_group;
        if (!groupID.contains(new_main_group))
            groupID.add(new_main_group);
    }
}
