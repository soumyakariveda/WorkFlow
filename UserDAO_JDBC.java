import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;

public class UserDAO_JDBC implements UserDAO {
  Connection dbConnection;
	public UserDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

	@Override
  public boolean is_correct(String user_id,String password){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "select * from User where login_id = \""+user_id+"\" and password = \""+password+"\";";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        return true;
      }
      return false;
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
  public boolean exist_already(String user_id){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "select * from User where user_id = \""+user_id+"\";";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        return true;
      }
      return false;
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
    return true;
  }

  @Override
  public boolean addUser(String name,String user_id,String password){
    Statement stmt = null;
    String sql_user,sql_role,sql_role_id,sql_getmaxid;
    try{
      // if(!exist_already(user_id)){
        stmt = dbConnection.createStatement();
        String role = "guest";
        sql_user = "Insert into User values(0,\""+user_id+"\",\""+name+"\",\""+password+"\");";
        stmt.executeUpdate(sql_user);

        sql_getmaxid = "select max(user_id) as max_user_id from User;";
  			ResultSet rs_id = stmt.executeQuery(sql_getmaxid);
  			int max_id = 0;
  			while(rs_id.next()){
  				max_id = rs_id.getInt("max_user_id");
  			}
        rs_id.close();

        sql_role_id = "select role_id from Role where type = \""+role+"\";";
        ResultSet rs_role_id = stmt.executeQuery(sql_role_id);
  			int role_id_guest = 0;
  			while(rs_role_id.next()){
  				role_id_guest = rs_role_id.getInt("role_id");
  			}
        rs_role_id.close();

        sql_role = "Insert into UserRole values(0,\""+max_id+"\",\""+role_id_guest+"\");";
        stmt.executeUpdate(sql_role);

        System.out.println("You have successfully created an account :)");
        System.out.println("Please login");
        return true;
      // }
      // else{
      //   System.out.println("User Id already exists");
      //   return false;
      // }
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
  public ArrayList<String> showRoles(String login_id){
    Statement stmt = null;
    String sql;
    ArrayList<String> roles = new ArrayList<String>();
    try{
      stmt = dbConnection.createStatement();
      sql = "select type from ((User inner join UserRole on User.user_id = UserRole.user_id) inner join Role on UserRole.role_id = Role.role_id) where login_id = \""+login_id+"\";";
      ResultSet rs = stmt.executeQuery(sql);
      while(rs.next()){
        roles.add(rs.getString("type"));
      }
      return roles;
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
    return roles;
  }

  @Override
  public boolean addUser(String login_id, String new_role){
    Statement stmt = null;
    String sql,sql_role_id,sql_user_id;
    try{
      stmt = dbConnection.createStatement();

      sql_user_id = "select user_id from User where login_id = \""+login_id+"\";";
      ResultSet rs_user_id = stmt.executeQuery(sql_user_id);
      int user_id_new = 0;
      while(rs_user_id.next()){
        user_id_new = rs_user_id.getInt("user_id");
      }
      if(user_id_new==0){
        System.out.println("Login id doesn't exits");
        return false;
      }

      sql_role_id = "select role_id from Role where type = \""+new_role+"\";";
      ResultSet rs_role_id = stmt.executeQuery(sql_role_id);
      int role_id_guest = 0;
      while(rs_role_id.next()){
        role_id_guest = rs_role_id.getInt("role_id");
      }
      if(role_id_guest == 0){
        System.out.println("Role doesn't exits");
        return false;
      }

      sql = "Insert into UserRole values(0,\""+user_id_new+"\",\""+role_id_guest+"\");";
      stmt.executeUpdate(sql);
      System.out.println("User role changed successfully");
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
