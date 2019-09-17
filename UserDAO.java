import java.lang.*;
import java.util.ArrayList;

public interface UserDAO{
  public boolean is_correct(String user_id,String password);
  public boolean exist_already(String user_id);
  public boolean addUser(String name,String user_id,String password);
  public ArrayList<String> showRoles(String userid);
  public boolean addUser(String userid, String new_role);
}
