//STEP 1. Import required packages
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DAO_Demo{
	public static void main(String[] args) {
		try{
			DAO_Factory daoFactory = new DAO_Factory();
			daoFactory.activateConnection();
			Scanner sc = new Scanner(System.in);
			while(true){
				System.out.println("..............................................................");
				System.out.println("1.Login                                                      |");
				System.out.println("2.Signup                                                     |");
				System.out.println("3.Exit                                                       |");
				System.out.println("..............................................................");
				System.out.print("\nEnter Choice:");
				int choice = sc.nextInt();
				sc.nextLine();
				if(choice == 1){
					///////////////////////////////////
					//////////LOGIN UP/////////////////
					System.out.print("\nLogin id:");
					String id = sc.nextLine();
					System.out.print("\nPassword:");
					String pass = sc.nextLine();
					UserDAO udao = daoFactory.getUserDAO();
					ArrayList<String> roles = new ArrayList<String>();
					int role_type;
					if(udao.is_correct(id,pass)){
						System.out.println("\nLogin succesful\n");
						roles = udao.showRoles(id);
						while(true){
							System.out.println("\nYour Roles are\n");
							System.out.println("..............................................................");
							int i;
							for(i=0;i<roles.size();i++){
								System.out.println((i+1)+"."+roles.get(i));
							}
							System.out.println((i+1)+".Exit");
							System.out.println("..............................................................");

							System.out.print("Select which role you want to take:");
							int r = sc.nextInt();
							r = r-1;
							sc.nextLine();

							if(i==r)
								break;
							else if(roles.get(r).equals("Manager")){
								while(true){
									System.out.println("..............................................................");
									System.out.println("1.Create Workflow                                            |");
									System.out.println("2.Add new role                                               |");
									System.out.println("3.Add user role                                              |");
									System.out.println("4.View Reports                                               |");
									System.out.println("5.Cancel all overdue tasks                                   |");
									System.out.println("6.Exit                                                       |");
									System.out.println("..............................................................");
									System.out.print("\nEnter choice:");
									int work = sc.nextInt();
									sc.nextLine();
									if(work == 1){
										///////////////////////////////////
										//////////CREATE WORKFLOW//////////
										WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
										System.out.print("\nEnter workflow name:");
										String wf_name = sc.nextLine();
										wfdao.addWorkflow(wf_name);
										int seq_no = 1;
										String task,perf_by;
										int is_role;
										while(true){
											TaskDAO tdao = daoFactory.getTaskDAO();
											System.out.print("\nEnter task name:");
											task = sc.nextLine();
											if(task.equals("End"))
												break;
											System.out.println("\nEnter 1 for task assosiated with role and 0 for specific user\n");
											is_role = sc.nextInt();
											sc.nextLine();
											if(is_role==1)
												System.out.print("\nEnter role name:");
											else
												System.out.print("\nEnter login id:");
											perf_by = sc.nextLine();
											System.out.print("\nEnter duration of task:");
											int dur = sc.nextInt();
											sc.nextLine();
											tdao.addTask(task,seq_no,wf_name,(is_role==1),perf_by,dur);
											seq_no+=1;
										}
										//////////CREATE WORKFLOW END//////////
										///////////////////////////////////////
									}
									else if(work == 2){
										///////////////////////////////////
										//////////ADD NEW ROLE/////////////
										System.out.print("\nEnter role name:");
										String role = sc.nextLine();
										RoleDAO roledao = daoFactory.getRoleDAO();
										roledao.addRole(role);
										//////////ADD NEW ROLE END/////////
										///////////////////////////////////
									}
									else if(work == 3){
										///////////////////////////////////
										//////////CHANGE USER ROLE/////////
										System.out.print("\nEnter login id of user:");
										String log_id = sc.nextLine();
										System.out.print("\nEnter role type:");
										String role = sc.nextLine();
										udao.addUser(log_id,role);
										//////////CHANGE USER ROLE END/////
										///////////////////////////////////
									}
									else if(work == 4){
										///////////////////////////////////
										//////////VIEW REPORTS/////////////
										System.out.println("..............................................................");
										System.out.println("1.Pending Workflow Reports                                   |");
										System.out.println("2.Completed Workflow Reports                                 |");
										System.out.println("3.Overdue Tasks                                              |");
										System.out.println("..............................................................");
										System.out.print("\nEnter Choice:");
										int sel = sc.nextInt();
										sc.nextLine();
										ReportsDAO reportsdao = daoFactory.getReportsDAO();
										reportsdao.view_reports(sel);
										//////////VIEW REPORTS END/////////
										///////////////////////////////////
									}
									else if(work == 5){
										///////////////////////////////////
										//////////CANCEL OVERDUE///////////
										Task_InstDAO task_instdao = daoFactory.getTask_InstDAO();
										if(task_instdao.cancelOverdueTasks())
											System.out.println("\nOverdued tasks are cancelled\n");
										else
											System.out.println("\nCancellation failed\n");
										//////////CANCEL OVERDUE END///////
										///////////////////////////////////
									}
									else{
										break;
									}
								}
							}
							else if(roles.get(r).equals("Guest")){
								while(true){
									System.out.println("..............................................................");
									System.out.println("1.Instantiate Workflow                                       |");
									System.out.println("2.Exit                                                       |");
									System.out.println("..............................................................");
									System.out.print("\nEnter Choice:");
									int work = sc.nextInt();
									sc.nextLine();
									if(work==1){
										///////////////////////////////////
										//////////INSTANTIATE WF///////////
										WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
										wfdao.displayWFs();
										System.out.print("\nEnter the workflow name:");
										String wf_name = sc.nextLine();
										WF_InstDAO wf_instdao = daoFactory.getWF_InstDAO();
										wf_instdao.instantiateWorkflow(wf_name,id);
										//////////INSTANTIATE WF END///////
										///////////////////////////////////
									}
									else{
										break;
									}
								}
							}
							else{
								while(true){
									System.out.println("..............................................................");
									System.out.println("1.Perform Task                                               |");
									System.out.println("2.Exit                                                       |");
									System.out.println("..............................................................");
									System.out.print("\nEnter Choice:");
									int work = sc.nextInt();
									sc.nextLine();
									if(work==1){
										///////////////////////////////////
										//////////PERFORM TASK/////////////
										WorkflowDAO wfdao = daoFactory.getWorkflowDAO();
										wfdao.displayWFs();
										System.out.print("\nEnter the workflow name:");
										String wf_name = sc.nextLine();
										WF_InstDAO wf_instdao = daoFactory.getWF_InstDAO();
										System.out.println("\nWF Inst Name = WF Name + Login Id of instantiator\n");
										int no_wfs = wf_instdao.displayWF_insts(wf_name);
										//System.out.println("num-"+no_wfs);
										if(no_wfs>0){
											System.out.print("\nEnter the workflow instance name:");
											String wf_inst_name = sc.nextLine();
											Task_InstDAO task_instdao = daoFactory.getTask_InstDAO();
											int no_pending_task = task_instdao.displayTaskInsts(wf_inst_name,id);
											if (no_pending_task > 0){
												System.out.print("Enter Task Name:");
												String task_name = sc.nextLine();
												System.out.println("..............................................................");
												System.out.println("1.Approve                                                    |");
												System.out.println("2.Reject                                                     |");
												System.out.println("..............................................................");
												System.out.print("\nEnter action you want to perform:");
												int act = sc.nextInt();
												sc.nextLine();
												System.out.print("\nEnter comments:");
												String comment = sc.nextLine();
												task_instdao.performingTask(act,comment,id,wf_inst_name,task_name);
										  }
											else{
												System.out.println("\nNo pending tasks in this workflow instance\n");
											}
										}
										else{
											System.out.println("\nNo instances of the workflow to perform any task\n");
										}
										//////////PERFORM TASK END/////////
										///////////////////////////////////
									}
									else{
										break;
									}
								}
							}
						}
					}
					else{
						System.out.println("\nEnter Valid credentials\n");
					}
					//////////LOGIN IN END/////////////
					///////////////////////////////////
				}
				else if(choice == 2){
					///////////////////////////////////
					//////////SIGN UP//////////////////
					UserDAO udao = daoFactory.getUserDAO();
					System.out.print("\nName:");
					String name = sc.nextLine();
					System.out.print("\nLogin id:");
					String id = sc.nextLine();
					while(udao.exist_already(id)){
						System.out.println("\nUser id already exits, Enter new id\n");
						id = sc.nextLine();
					}
					System.out.print("\nPassword:");
					String pass = sc.nextLine();
					udao.addUser(name,id,pass);
					//////////SIGN UP END//////////////
					///////////////////////////////////
				}
				else if(choice == 3){
					break;
				}
				else
					break;
			}
			daoFactory.deactivateConnection();
		}
		catch(Exception e){
				e.printStackTrace();
		}
	}
}
