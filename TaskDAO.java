import java.lang.*;
import java.util.ArrayList;

public interface TaskDAO{
  public boolean addTask(String name, int seq_id, String wf_name ,boolean isRole, String performed_by,int duration);
}
