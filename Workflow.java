import java.lang.*;
import java.util.ArrayList;

public class Workflow{
  String name;
  ArrayList<Task> tasks;
  public Workflow(){}
  public Workflow(String n,ArrayList<Task> t){name = n;tasks=t;}
  public String getName(){return name;}
  public ArrayList<Task> getTasks(){return tasks;}
  public void setName(String n){name = n;}
  public void setTasks(ArrayList<Task> t){tasks = t;}
  public void print(){
    System.out.println("Name-"+name);
    for (int counter = 0; counter < tasks.size(); counter++){
      System.out.println("Task:"+(counter+1));
      System.out.println(tasks.get(counter));
    }
  }
};
