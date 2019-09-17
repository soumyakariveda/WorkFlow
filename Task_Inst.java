import java.lang.*;
import java.util.*;

public class Task_Inst{
  int action;
  String data;
  String status;
  int user_id;
  int task_id;
  int wf_inst_id;
  Date start_date,end_date;

  public Task_Inst(){}
  public Task_Inst(int a,String d,String s,int uid,int tid,int wid,Date sd,Date ed)
  {
    action=a;
    data=d;
    status=s;
    user_id=uid;
    task_id=tid;
    wf_inst_id=wid;
    start_date=sd;
    end_date=ed;
  }

  public int getAction(){return action;}
  public void setAction(int a){action=a;}

  public String getData(){return data;}
  public void setData(String d){data=d;}

  public String getStatus(){return status;}
  public void setStatus(String s){status=s;}

  public int getUserId(){return user_id;}
  public void setUserId(int u){user_id=u;}

  public int getTaskId(){return task_id;}
  public void setTaskId(int t){task_id=t;}

  public int getWfInstID(){return wf_inst_id;}
  public void setWfInstID(int w){wf_inst_id=w;}

  public Date getStartDate(){return start_date;}
  public void setStartDate(Date d){start_date = d;}

  public Date getEndDate(){return end_date;}
  public void setEndDate(Date d){end_date = d;}


  public void print(){
    System.out.println("action-"+action);
    System.out.println("data-"+data);
    System.out.println("status-"+status);
    System.out.println("userid-"+user_id);
    System.out.println("task_id-"+task_id);
    System.out.println("wf_inst_id-"+wf_inst_id);
    System.out.println("start_date-"+start_date);
    System.out.println("end_date"+end_date);

  }
};
