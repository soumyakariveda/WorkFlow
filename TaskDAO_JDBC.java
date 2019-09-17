import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class TaskDAO_JDBC implements TaskDAO {
  Connection dbConnection;
	public TaskDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

	@Override
  public boolean addTask(String name, int seq_id, String wf_name ,boolean isRole, String performed_by,int duration){
    Statement stmt = null;
    String sql,sql_wf;
    try{
      stmt = dbConnection.createStatement();

      int wf_id,user_id,role_id;
      wf_id = 0;
      user_id = 0;
      role_id = 0;

      sql_wf = "select wf_id from Workflow where name = \""+wf_name+"\";";
      ResultSet rs_wf = stmt.executeQuery(sql_wf);
      while(rs_wf.next()){
        wf_id = rs_wf.getInt("wf_id");
      }
      if(wf_id == 0){
        System.out.println("Please enter valid workflow name");
        return false;
      }

      if(isRole){
        //Task performed by role
        String sql_role = "select role_id from Role where type = \""+performed_by+"\";";
        ResultSet rs_role = stmt.executeQuery(sql_role);
        while(rs_role.next()){
          role_id = rs_role.getInt("role_id");
        }
        if(role_id == 0){
          System.out.println("Please enter valid role");
          return false;
        }
        sql = "Insert into Task values(0,\""+duration+"\",\""+name+"\",\""+seq_id+"\",\""+wf_id+"\",NULL,\""+role_id+"\");";
        stmt.executeUpdate(sql);
        return true;
      }
      else{
        //Task by particular user
        String sql_user = "select user_id from User where login_id = \""+performed_by+"\";";
        ResultSet rs_user = stmt.executeQuery(sql_user);
        while(rs_user.next()){
          user_id = rs_user.getInt("user_id");
        }
        if(user_id == 0){
          System.out.println("Please enter valid user");
          return false;
        }
        sql = "Insert into Task values(0,\""+duration+"\",\""+name+"\",\""+seq_id+"\",\""+wf_id+"\",\""+user_id+"\",NULL);";
        stmt.executeUpdate(sql);
        return true;
      }
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
