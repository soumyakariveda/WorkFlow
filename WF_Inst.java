import java.lang.*;
import java.util.*;

public class WF_Inst{
  String wf_inst_name;
  String status;
  Date start_date,end_date;
  ArrayList<Task_Inst> tasks;
  public WF_Inst(){}
  public WF_Inst(String name,String stat,Date s,Date e,ArrayList<Task_Inst> t){
    wf_inst_name = name;
    status = stat;
    start_date = s;
    end_date = e;
    tasks = t;
  }
  public String getWFInstName(){return wf_inst_name;}
  public void setWFInstName(String n){wf_inst_name = n;}

  public String getStatus(){return status;}
  public void setStatus(String stat){status = stat;}

  public Date getStartDate(){return start_date;}
  public void setStartDate(Date d){start_date = d;}

  public Date getEndDate(){return end_date;}
  public void setEndDate(Date d){end_date = d;}

  public ArrayList<Task_Inst> getTasks(){return tasks;}
  public void setTasks(ArrayList<Task_Inst> t){tasks = t;}

  public void print(){
    System.out.println("name-"+wf_inst_name);
    System.out.println("status-"+status);
    System.out.println("start_date-"+start_date);
    System.out.println("end_date-"+end_date);
    for (int counter = 0; counter < tasks.size(); counter++){
      System.out.println("Task:"+(counter+1));
      System.out.println(tasks.get(counter));
    }
  }
}
