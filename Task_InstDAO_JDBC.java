import java.lang.*;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
// import java.text.SimpleDateFormat;

public class Task_InstDAO_JDBC implements Task_InstDAO {
  Connection dbConnection;
	public Task_InstDAO_JDBC(Connection dbconn){
		// JDBC driver name and database URL
 		//  Database credentials
		dbConnection = dbconn;
	}

	@Override
  public boolean performingTask(int action,String data,String login_id_taskPerformedby,String wf_inst_name,String task_name){
    Statement stmt = null;
    String sql,sql_uid,sql_wf_instid,sql_wf_id,sql_task_id,sql_task_instid,sql_pending,sql_reject,sql_nextTask,sql_wf_inst,sql_seq_no,sql_max_seq_no,sql_wf_inst_stat,sql_next_task_id;
    int uid=0,wf_inst_id=0,wf_id=0,task_id=0,task_inst_id=0;
    String[] wfname_loginid=wf_inst_name.split("_");
    String wf_name = wfname_loginid[0];
    String login_id_guest = wfname_loginid[1];

    try{
      stmt = dbConnection.createStatement();

      sql_uid="Select user_id from User where login_id = \""+login_id_taskPerformedby+"\"";
      ResultSet rs_uid = stmt.executeQuery(sql_uid);
      while(rs_uid.next()){
        uid = rs_uid.getInt("user_id");
      }

      sql_wf_instid = "Select wf_inst_id from WF_Inst where wf_inst_name = \""+wf_inst_name+"\"";
      ResultSet rs_wf_instid = stmt.executeQuery(sql_wf_instid);
      while(rs_wf_instid.next()){
        wf_inst_id = rs_wf_instid.getInt("wf_inst_id");
      }

      sql_wf_id = "Select wf_id from Workflow where name = \""+wf_name+"\"";
      ResultSet rs_wf_id = stmt.executeQuery(sql_wf_id);
      while(rs_wf_id.next()){
        wf_id = rs_wf_id.getInt("wf_id");
      }

      sql_task_id = "select task_id from Task where name = \""+task_name+"\" and wf_id = \""+wf_id+"\";";
      ResultSet rs_task_id = stmt.executeQuery(sql_task_id);
      while(rs_task_id.next()){
        task_id = rs_task_id.getInt("task_id");
      }

      sql_task_instid = "select task_inst_id from Task_Inst where user_id = \""+uid+"\" and task_id = \""+task_id+"\" and wf_inst_id = \""+wf_inst_id+"\";";
      ResultSet rs_task_instid = stmt.executeQuery(sql_task_instid);
      while(rs_task_instid.next()){
        task_inst_id = rs_task_instid.getInt("task_inst_id");
      }

      String act=null;
      if(action == 1)
        act = "Approved";
      else if(action == 2)
        act = "Rejected";

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime end_date = LocalDateTime.now();

      sql = "Update Task_Inst Set action = \""+act+"\", data = \""+data+"\", status = 'Completed', end_date = \""+end_date+"\" where task_inst_id = \""+task_inst_id+"\";";
      stmt.executeUpdate(sql);

      sql_pending = "Update Task_Inst set status = 'Cancelled' where wf_inst_id = \""+wf_inst_id+"\" and status = 'Pending';";
      stmt.executeUpdate(sql_pending);

      if(action == 2){
        sql_reject = "Update Task_Inst set status = 'Cancelled' where wf_inst_id = \""+wf_inst_id+"\" and status = 'Not Started';";
        stmt.executeUpdate(sql_reject);
        sql_wf_inst = "Update WF_Inst set status = 'Completed', end_date = \""+end_date+"\" where wf_inst_id = \""+wf_inst_id+"\";";
        stmt.executeUpdate(sql_wf_inst);
      }
      else if(action == 1){
        sql_seq_no = "select sequence_id from (select * from Task_Inst where wf_inst_id = \""+wf_inst_id+"\") t inner join Task on Task.task_id = t.task_id where task_inst_id = \""+task_inst_id+"\";";
        ResultSet rs_seq_no = stmt.executeQuery(sql_seq_no);
        int seq_no = 0;
        while(rs_seq_no.next()){
          seq_no = rs_seq_no.getInt("sequence_id");
        }
        int next_seq_no = seq_no+1;

        sql_max_seq_no = "select max(sequence_id) from Task where wf_id = \""+wf_id+"\";";
        ResultSet rs_max_seq_no = stmt.executeQuery(sql_max_seq_no);
        int max_seq_no = 0;
        while(rs_max_seq_no.next()){
          max_seq_no = rs_max_seq_no.getInt("max(sequence_id)");
        }

        if(seq_no == max_seq_no){
          sql_wf_inst_stat = "Update WF_Inst set status = 'Completed', end_date = \""+end_date+"\" where wf_inst_id = \""+wf_inst_id+"\";";
          stmt.executeUpdate(sql_wf_inst_stat);
        }
        else{
          sql_next_task_id = "select task_inst_id from (select * from Task_Inst where wf_inst_id = \""+wf_inst_id+"\") t inner join Task on Task.task_id = t.task_id where sequence_id = \""+next_seq_no+"\";";
          ResultSet rs_next_task_id = stmt.executeQuery(sql_next_task_id);
          int next_task_inst_id = 0;
          while(rs_next_task_id.next()){
            next_task_inst_id = rs_next_task_id.getInt("task_inst_id");
            stmt = null;
            stmt = dbConnection.createStatement();
            sql_nextTask = "Update Task_Inst set status = 'Pending', start_date = \""+end_date+"\" where task_inst_id = \""+next_task_inst_id+"\";";
            stmt.executeUpdate(sql_nextTask);
          }
        }
      }
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
  public int displayTaskInsts(String wf_inst_name,String login_id){
    Statement stmt = null;
    String sql,sql_task;
    try{
      stmt = dbConnection.createStatement();
      sql = "select wf_inst_id from WF_Inst where wf_inst_name = \""+wf_inst_name+"\";";
      ResultSet rs = stmt.executeQuery(sql);
      int wf_inst_id = 0;
      while(rs.next()){
        wf_inst_id = rs.getInt("wf_inst_id");
      }
      rs.close();
      sql_task = "select Task.name from (Task_Inst inner join Task on Task.task_id = Task_Inst.task_id)  inner join User on Task_Inst.user_id = User.user_id where wf_inst_id = \""+wf_inst_id+"\" and status = 'Pending' and User.login_id = \""+login_id+"\";";
      ResultSet rs_task = stmt.executeQuery(sql_task);
      int i = 0;
      System.out.println("..............................................................");
      while(rs_task.next()){
        i++;
        System.out.println(i+"."+rs_task.getString("name"));
      }
      System.out.println("..............................................................");
      rs_task.close();
      return i;
    }
    catch (SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
      System.out.println("Please enter valid details!");
    }
    return 0;
  }

  @Override
  public boolean cancelOverdueTasks(){
    Statement stmt = null;
    String sql,sql_task_inst,sql_wf_inst;
    try{

      stmt = dbConnection.createStatement();

      DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
      LocalDateTime cur_date = LocalDateTime.now();

      stmt = dbConnection.createStatement();
      sql = "select Task_Inst.task_inst_id,WF_Inst.wf_inst_id from (Task INNER JOIN Task_Inst on Task.task_id = Task_Inst.task_id) INNER JOIN WF_Inst on Task_Inst.wf_inst_id=WF_Inst.wf_inst_id where DATEDIFF(\""+cur_date+"\",Task_Inst.start_date)>duration and Task_Inst.status = 'Pending';";
      ResultSet rs = stmt.executeQuery(sql);

      while(rs.next()){
        stmt = null;
        stmt = dbConnection.createStatement();

        int task_inst_id = rs.getInt("task_inst_id");
        int wf_inst_id = rs.getInt("wf_inst_id");

        sql_task_inst = "Update Task_Inst set status = 'Cancelled' where wf_inst_id = \""+wf_inst_id+"\" and status = 'Not Started' or status = 'Pending';";
        stmt.executeUpdate(sql_task_inst);

        sql_wf_inst = "Update WF_Inst set status = 'Completed', end_date = \""+cur_date+"\" where wf_inst_id = \""+wf_inst_id+"\";";
        stmt.executeUpdate(sql_wf_inst);

      }
      rs.close();
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
