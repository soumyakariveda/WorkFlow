import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class ReportsDAO_JDBC implements ReportsDAO {
  Connection dbConnection;
	public ReportsDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

  @Override
  public void pending_wf_insts(){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "select wf_inst_name from WF_Inst where status = 'Not Completed';";
      ResultSet rs = stmt.executeQuery(sql);
      System.out.println("..............................................................");
      while(rs.next()){
        System.out.println(rs.getString("wf_inst_name"));
      }
      System.out.println("..............................................................");
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
  }

  @Override
  public void completed_wf_insts(){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();
      sql = "select wf_inst_name from WF_Inst where status = 'Completed';";
      ResultSet rs = stmt.executeQuery(sql);
      System.out.println("..............................................................");
      while(rs.next()){
        System.out.println(rs.getString("wf_inst_name"));
      }
      System.out.println("..............................................................");
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
  }

  @Override
  public void overdue_task_insts(){
    Statement stmt = null;
    String sql;
    try{
      stmt = dbConnection.createStatement();

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime cur_date = LocalDateTime.now();

      sql = "select DISTINCT Task.name,WF_Inst.wf_inst_name from (Task INNER JOIN Task_Inst on Task.task_id = Task_Inst.task_id) INNER JOIN WF_Inst on Task_Inst.wf_inst_id=WF_Inst.wf_inst_id where DATEDIFF(\""+cur_date+"\",Task_Inst.start_date)>duration and Task_Inst.status = 'Pending';";
      ResultSet rs = stmt.executeQuery(sql);
      System.out.println("..............................................................");
      while(rs.next()){
        System.out.println(rs.getString("name")+" from "+rs.getString("wf_inst_name"));
      }
      System.out.println("..............................................................");
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
  }

  @Override
  public void view_reports(int choice){
    if(choice == 1)
      pending_wf_insts();
    else if(choice == 2)
      completed_wf_insts();
    else if(choice == 3)
      overdue_task_insts();
    else
      System.out.println("Invalid request");
  }

}
