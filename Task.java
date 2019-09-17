import java.lang.*;

public class Task{
  int seq_id;
  String name;
  public Task(){}
  public Task(int s_id,String n){seq_id = s_id; name = n;}
  public int getSeqId(){return seq_id;}
  public String getName(){return name;}
  public void setSeqId(int s){seq_id=s;}
  public void setName(String n){name = n;}
  public void print(){
    System.out.println("Name-"+name);
    System.out.println("Seq ID-"+seq_id);
  }
};
