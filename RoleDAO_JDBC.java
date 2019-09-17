import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class RoleDAO_JDBC implements RoleDAO {
  Connection dbConnection;
	public RoleDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

  @Override
  public boolean addRole(String type){
    Statement stmt = null;
    String sql,sql_find;
    try{
        stmt = dbConnection.createStatement();
        sql_find = "select type from Role where type = \""+type+"\";";
        ResultSet rs = stmt.executeQuery(sql_find);
        while(rs.next()){
          System.out.println("Role already exists");
          return false;
        }
        sql = "Insert into Role values(0,\""+type+"\");";
        stmt.executeUpdate(sql);
        System.out.println("Added role sucessfully");
        return true;
      }
      catch (SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
        System.out.println("Please enter valid details!");
      }
    return false;
  }
}
