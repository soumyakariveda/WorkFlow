import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class WF_InstDAO_JDBC implements WF_InstDAO {
  Connection dbConnection;
	public WF_InstDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

  @Override
  public boolean instantiateWorkflow(String wf_name,String login_id){
    Statement stmt = null;
    String sql_wf,sql_wf_inst,sql_task,sql_task_inst,sql_wf_inst1,sql_user_role,sql_task_inst1,sql_task_inst2;
    try{
      stmt = dbConnection.createStatement();
      sql_wf = "select wf_id from Workflow where name = \""+wf_name+"\";";
      ResultSet rs_wf_id = stmt.executeQuery(sql_wf);
      int workflow_id = 0;
      while(rs_wf_id.next()){
        workflow_id = rs_wf_id.getInt("wf_id");
      }
      rs_wf_id.close();
      if(workflow_id == 0){
        System.out.println("Workflow not found");
        return false;
      }

      String workflow_inst_name = wf_name+"_"+login_id;

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime workflow_start_date = LocalDateTime.now();

      sql_wf_inst = "Insert into WF_Inst values(0,\""+workflow_id+"\",\""+workflow_inst_name+"\",'Not Completed',\""+workflow_start_date+"\",NULL);";
      stmt.executeUpdate(sql_wf_inst);

      sql_wf_inst1 = "select max(wf_inst_id) as max_wf_inst_id from WF_Inst;";
      ResultSet rs_wf_inst = stmt.executeQuery(sql_wf_inst1);
      int wf_inst_id = 0;
      while(rs_wf_inst.next()){
        wf_inst_id = rs_wf_inst.getInt("max_wf_inst_id");
      }
      rs_wf_inst.close();

      sql_task = "select * from Task where wf_id = \""+workflow_id+"\";";
      ResultSet rs_task = stmt.executeQuery(sql_task);
      while(rs_task.next()){
        stmt = null;
        stmt = dbConnection.createStatement();

        int task_id = rs_task.getInt("task_id");
        String user_id = rs_task.getString("user_id");
        String role_id = rs_task.getString("role_id");

        if(user_id!=null){ //user specific task
          int user_id_int = Integer.parseInt(user_id);
          sql_task_inst = "Insert into Task_Inst values(0,NULL,NULL,'Not Started',\""+user_id_int+"\",\""+task_id+"\",\""+wf_inst_id+"\",NULL,NULL);";
          stmt.executeUpdate(sql_task_inst);
        }
        else if(role_id!=null){ //role specific task
          int role_id_int = Integer.parseInt(role_id);
          sql_user_role = "select user_id from UserRole where role_id = \""+role_id_int+"\";";
          ResultSet rs_user_role = stmt.executeQuery(sql_user_role);

          while(rs_user_role.next()){
            stmt = null;
            stmt = dbConnection.createStatement();

            int user_id_int = rs_user_role.getInt("user_id");
            sql_task_inst = "Insert into Task_Inst values(0,NULL,NULL,'Not Started',\""+user_id_int+"\",\""+task_id+"\",\""+wf_inst_id+"\",NULL,NULL);";
            stmt.executeUpdate(sql_task_inst);
          }
          rs_user_role.close();
        }
        else{
          System.out.println("A Task can be performed by Role or User");
          return false;
        }
      }
      rs_task.close();

      // update start date as workflow_start_date for 1st task and update status of 1st task to Pending
      sql_task_inst1 = "select task_inst_id from Task inner join (select * from Task_Inst where wf_inst_id = \""+wf_inst_id+"\") as T on Task.task_id = T.task_id where sequence_id = 1;";
      ResultSet rs_task_inst = stmt.executeQuery(sql_task_inst1);
      int task_inst_id = 0;
      while(rs_task_inst.next()){
        stmt = null;
        stmt = dbConnection.createStatement();

        task_inst_id = rs_task_inst.getInt("task_inst_id");
        if(task_inst_id == 0){
          System.out.println("Task_Inst not found");
          return false;
        }
        sql_task_inst2 = "update Task_Inst set start_date = \""+workflow_start_date+"\", status = 'Pending' where task_inst_id = \""+task_inst_id+"\";";
        stmt.executeUpdate(sql_task_inst2);
      }
      rs_task_inst.close();
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
  public int displayWF_insts(String wf_name){
    Statement stmt = null;
    String sql,sql_inst;
    try{
      stmt = dbConnection.createStatement();
      sql = "select wf_id from Workflow where name = \""+wf_name+"\";";
      ResultSet rs = stmt.executeQuery(sql);
      int wf_id = 0;
      while(rs.next()){
        wf_id = rs.getInt("wf_id");
      }
      rs.close();
      sql_inst = "select wf_inst_name from WF_Inst where wf_id = \""+wf_id+"\" and status = 'Not Completed';";
      ResultSet rs_inst = stmt.executeQuery(sql_inst);
      int i = 0;
      System.out.println("..............................................................");
      while(rs_inst.next()){
        i++;
        System.out.println(i+"."+rs_inst.getString("wf_inst_name"));
      }
      System.out.println("..............................................................");
      rs_inst.close();
      return i;
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
    return -1;
  }
}
