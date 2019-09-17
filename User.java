import java.lang.*;

public class User{
  String name;
  String user_id;
  String password;
  String role;
  public User(){}
  public User(String n,String s,String p){name = n;user_id = s;password = p;}
  public String getName(){return name;}
  public String getUserId(){return user_id;}
  public String getPassword(){return password;}
  public void setName(String n){name = n;}
  public void setUserId(String s){user_id = s;}
  public void setPassword(String s){password = s;}
  public void print(){
    System.out.println("Name-"+name);
    System.out.println("User ID-"+user_id);
    System.out.println("Password-"+password);
  }
};
