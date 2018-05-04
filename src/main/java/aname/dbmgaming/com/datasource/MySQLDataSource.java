package aname.dbmgaming.com.datasource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLDataSource extends SQLDataSource {


    private final String NAME = "AntiBot-Ultra";
    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://";
    private String HOST;
    private String PORT;
    private String USER;
    private String PASS;
    private String ARGS;


    public MySQLDataSource(String HOST, String PORT, String USER, String PASS, String ARGS) throws SQLException, ClassNotFoundException {
        this.HOST = HOST;
        this.PORT = PORT;
        this.USER = USER;
        this.PASS = PASS;
        this.ARGS = ARGS;
        connect();
        setup();
    }

    @Override
    public void reload() {

    }

    @Override
    public void save() {

    }

    @Override
    public synchronized void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException ignored) {
        }
    }

    @Override
    protected void connect() throws ClassNotFoundException, SQLException {
        Class.forName(JDBC_DRIVER);
        connection = DriverManager.getConnection(DB_URL + HOST + PORT + "/" + NAME, USER, PASS);
    }

    @Override
    protected void setup() throws SQLException {
        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("create table if not exists `whitelist` (`player_name` varchar(32));");
            st.executeUpdate("create table if not exists `blacklist` (`player_ip` varchar(15));");
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }
}
