import java.lang.*;
import java.util.ArrayList;

public interface ReportsDAO{
  public void pending_wf_insts();
  public void completed_wf_insts();
  public void overdue_task_insts();
  public void view_reports(int choice);
}
