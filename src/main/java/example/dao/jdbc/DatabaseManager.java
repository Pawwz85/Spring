package example.dao.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static DatabaseManager instace = null;
    private final String url;
    private final String user;
    private final String password;
    public static DatabaseManager getInstance() {
        if(DatabaseManager.instace==null){
                DatabaseManager.instace = new DatabaseManager();
        }
        return DatabaseManager.instace;
    }
    private DatabaseManager(){
        this.url = "postgres://fgkaidtw:XcFSgEcmbtukhjs45V4_JFPEHz7uIygu@cornelius.db.elephantsql.com/fgkaidtw";
        this.user = "fgkaidtw";
        this.password = "XcFSgEcmbtukhjs45V4_JFPEHz7uIygu";
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}