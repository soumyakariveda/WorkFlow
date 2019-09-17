import java.lang.*;
import java.util.ArrayList;

public interface Task_InstDAO{
    public boolean performingTask(int action,String data,String login_id_taskPerformedby,String wf_inst_name,String task_name);
    public int displayTaskInsts(String wf_inst_name,String login_id);
    public boolean cancelOverdueTasks();
}
