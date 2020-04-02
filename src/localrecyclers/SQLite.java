package localrecyclers;

// SQLite Imports
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLite {

    // Variables
    private static Connection conn;
    private static boolean hasData = false;
    
    public String getEmpty() {
        return "";
    }
    
    // Gets the connection
    public void getConnection() throws ClassNotFoundException, SQLException {
        // Gets the class and sets conn as the connection
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:busnrecord.db");
        System.out.println("SQLite Database Connection Established\n");
        // Initalizes data if not already initalized
        initalizeConnection();
    }

    // Initalize data
    private void initalizeConnection() throws SQLException {
        if (!hasData) {
            // If no data then set data
            hasData = true;

            // Create table if it doesn't exist
            Statement state = conn.createStatement();
            state.executeUpdate("CREATE TABLE IF NOT EXISTS busninfo("
                    + "id integer,"
                    + "busnname varchar(60),"
                    + "address varchar(120),"
                    + "phone varchar(10),"
                    + "website varchar(60),"
                    + "recycles varchar(200),"
                    + "primary key(id));");
        }
    }

    // Set BusnInfo
    public void setBusnInfo(int index, String busnname, String address, String phone, String website, String recycles) throws ClassNotFoundException, SQLException {
        // if no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Check if exists
        PreparedStatement prep = conn.prepareStatement("SELECT * FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        // If exists UPDATE else INSERT
        if (result.next()) {
            // Update BusnInfo
            PreparedStatement prep2 = conn.prepareStatement("UPDATE busninfo"
                    + " SET busnname=?,address=?,phone=?,website=?,recycles=? WHERE id=?");
            prep2.setString(1, busnname);
            prep2.setString(2, address);
            prep2.setString(3, phone);
            prep2.setString(4, website);
            prep2.setString(5, recycles);
            prep2.setInt(6, index);
            prep2.execute();
        } else {
            // Insert BusnInfo
            PreparedStatement prep2 = conn.prepareStatement("INSERT INTO busninfo(busnname,address,phone,website,recycles)"
                    + " VALUES(?,?,?,?,?)");
            prep2.setString(1, busnname);
            prep2.setString(2, address);
            prep2.setString(3, phone);
            prep2.setString(4, website);
            prep2.setString(5, recycles);
            prep2.execute();
        }
    }

    // Get BusnInfo
    public ResultSet getBusnInfo(int index) throws ClassNotFoundException, SQLException {
        // if no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select everthing but ID
        PreparedStatement prep = conn.prepareStatement("SELECT busnname,address,phone,website,recycles FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get ALL BusnInfo
    public ResultSet getBusnInfo() throws ClassNotFoundException, SQLException {
        // if no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select everthing but ID
        PreparedStatement prep = conn.prepareStatement("SELECT busnname,address,phone,website,recycles FROM busninfo");
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get the BusnName
    public ResultSet getBusnName(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select BusnName
        PreparedStatement prep = conn.prepareStatement("SELECT busnname FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get the Address
    public ResultSet getAddress(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select Address
        PreparedStatement prep = conn.prepareStatement("SELECT address FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get the Phone
    public ResultSet getPhone(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select Phone
        PreparedStatement prep = conn.prepareStatement("SELECT phone FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get the Website
    public ResultSet getWebsite(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select Website
        PreparedStatement prep = conn.prepareStatement("SELECT website FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    // Get the Recycles
    public ResultSet getRecycles(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select Recycles
        PreparedStatement prep = conn.prepareStatement("SELECT recycles FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        ResultSet result = prep.executeQuery();
        return result;
    }

    public void deleteEntry(int index) throws ClassNotFoundException, SQLException {
        // If no connection, get the connection
        if (conn == null) {
            getConnection();
        }

        // Select Recycles
        PreparedStatement prep = conn.prepareStatement("DELETE FROM busninfo WHERE id=?");
        prep.setInt(1, index);
        prep.execute();
    }
}
