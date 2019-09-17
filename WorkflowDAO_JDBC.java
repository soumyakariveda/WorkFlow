import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class WorkflowDAO_JDBC implements WorkflowDAO {
  Connection dbConnection;
	public WorkflowDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

	@Override
  public boolean addWorkflow(String name){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "Insert into Workflow values(0,\""+name+"\");";
      stmt.executeUpdate(sql);
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

  @Override
  public void displayWFs(){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "Select name from Workflow;";
      ResultSet rs = stmt.executeQuery(sql);
      int i = 1;
      System.out.println("..............................................................");
      while(rs.next()){
        String name = rs.getString("name");
        System.out.println(i+"."+name);
        i++;
      }
      System.out.println("..............................................................");
      rs.close();
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
  }
}
