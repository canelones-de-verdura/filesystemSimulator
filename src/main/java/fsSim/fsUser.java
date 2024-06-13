package fsSim;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

import com.google.common.hash.Hashing;

public class fsUser {
    private String name;
    private String uid;
    private String main_guid;
    private String password;
    private List<String> groupID;
    private String home;
    private String shell;

    private Date creation_d;
    private Date expiring_d;
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

        // Seteamos las fechas
        this.creation_d = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.set(3000, Calendar.JANUARY, 1);
        this.expiring_d = calendar.getTime();

        this.last_logged_d = null;
    }

    public boolean LogIn(String password) {
        if (!this.password.equals(Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString())) {
            failed_login_attempts++;
            return false;
        }

        if (semi.availablePermits() == 0)
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

    public Date getCreationDate() {
        return creation_d;
    }

    public Date getExpirationDate() {
        return expiring_d;
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

    public boolean manageGroups(String groupName, boolean add) {
        if (add)
            if (!groupID.contains(groupName))
                groupID.add(groupName);
            else
                return false;
        else
            groupID.remove(groupName);

        return true;
    }

    public void setExpirationDate(Date new_date) {
        expiring_d = new_date;
    }

    public void setMainGroup(String new_main_group) {
        main_guid = new_main_group;
        if (!groupID.contains(new_main_group))
            groupID.add(new_main_group);
    }
}
