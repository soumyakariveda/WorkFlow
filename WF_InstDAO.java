import java.lang.*;
import java.util.ArrayList;

public interface WF_InstDAO{
  public boolean instantiateWorkflow(String wf_name,String login_id);
  public int displayWF_insts(String wf_name);
}
