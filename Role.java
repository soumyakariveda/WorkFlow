import java.lang.*;

public class Role{
  String type;
  public Role(){}
  public Role(String ty){type = ty;}
  public String getType(){return type;}
  public void setType(String ty){type = ty;}
  public void print(){
    System.out.println("Type-"+type);
  }
};
