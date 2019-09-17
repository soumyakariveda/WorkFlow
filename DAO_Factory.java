import java.lang.*;
import java.sql.*;
/*
	Methods to be called in the following order:

	1. activateConnection
	2. 	Any number getDAO calls with any number of database transactions
	3. deactivateConnection
*/
public class DAO_Factory{
	static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	// Modify the DB_URL string, userid and password depending upon the database you want to connect to
	// In the following string, you are connecting a adatabase named "daoproject"
	// static final String DB_URL = "jdbc:mysql://localhost/hospitaldb";
	static final String DB_URL = "jdbc:mysql://localhost:3306/workflowdb?verifyServerCertificate=false&useSSL=false";

	static final String USER = "root";
	static final String PASS = "";

	Connection dbconnection = null;

	// You can add additional DAOs here as needed. Generally one DAO per class
	UserDAO userDAO = null;
	WorkflowDAO workflowDAO = null;
	TaskDAO taskDAO = null;
	RoleDAO roleDAO = null;
	Task_InstDAO task_instDAO = null;
	WF_InstDAO wf_instDAO = null;
	ReportsDAO reportsDAO = null;

	boolean activeConnection = false;

	public DAO_Factory()
	{
		dbconnection = null;
		activeConnection = false;
	}

	public void activateConnection() throws Exception
	{
		if( activeConnection == true )
			throw new Exception("Connection already active");

		System.out.println("Connecting to database...");
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			dbconnection = DriverManager.getConnection(DB_URL,USER,PASS);
			activeConnection = true;
		} catch(ClassNotFoundException ex) {
			System.out.println("Error: unable to load driver class!");
			System.exit(1);
		} catch (SQLException ex) {
		    // handle any errors
		    System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	public UserDAO getUserDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( userDAO == null )
			userDAO = new UserDAO_JDBC( dbconnection );

		return userDAO;
	}
	public WorkflowDAO getWorkflowDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( workflowDAO == null )
			workflowDAO = new WorkflowDAO_JDBC( dbconnection );

		return workflowDAO;
	}
	public TaskDAO getTaskDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( taskDAO == null )
			taskDAO = new TaskDAO_JDBC( dbconnection );

		return taskDAO;
	}
	public RoleDAO getRoleDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( roleDAO == null )
			roleDAO = new RoleDAO_JDBC( dbconnection );

		return roleDAO;
	}
	public Task_InstDAO getTask_InstDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( task_instDAO == null )
			task_instDAO = new Task_InstDAO_JDBC( dbconnection );

		return task_instDAO;
	}
	public WF_InstDAO getWF_InstDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( wf_instDAO == null )
			wf_instDAO = new WF_InstDAO_JDBC( dbconnection );

		return wf_instDAO;
	}
	public ReportsDAO getReportsDAO() throws Exception
	{
		if( activeConnection == false )
			throw new Exception("Connection not activated...");

		if( reportsDAO == null )
			reportsDAO = new ReportsDAO_JDBC( dbconnection );

		return reportsDAO;
	}

	public void deactivateConnection()
	{
		// Okay to keep deactivating an already deactivated connection
		activeConnection = false;
		if( dbconnection != null ){
			try{
				dbconnection.close();
				dbconnection = null;
				userDAO = null;
				workflowDAO = null;
				taskDAO = null;
				roleDAO = null;
				task_instDAO = null;
				wf_instDAO = null;
				reportsDAO = null;
			}
			catch (SQLException ex) {
			    // handle any errors
			    System.out.println("SQLException: " + ex.getMessage());
			    System.out.println("SQLState: " + ex.getSQLState());
			    System.out.println("VendorError: " + ex.getErrorCode());
			}
		}
	}
};
