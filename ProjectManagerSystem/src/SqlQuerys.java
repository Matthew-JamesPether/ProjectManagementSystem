
//Imports java libraries
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.Scanner;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;

/**
 * The SqlQuerys class declaration
 * <p>
 * This class executes a set of instructions that interacts with the mySQL
 * database 'poisepms'.
 * 
 * @author Matthew-James Pether
 * @version 1.00 25 June 2024
 */
public class SqlQuerys {

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. inserts the appropriate persons details in
	 * the appropriate persons table using mySQL statements. <br>
	 * returns personObj.id if inserted correctly else returns -1 if something went
	 * wrong.
	 * 
	 * @param personObj    Stores the person object
	 * @param personString Stores the name of the table as a string
	 * @param connection   Initiates a connection to the database via the
	 *                     jdbc:mysql: channel on localhost
	 * @param statement    initiates a direct line to the database to execute mySQL
	 *                     statements
	 * @return personObj.id
	 * @return -1
	 * @version 1.00
	 */
	public static int insertPersonQuery(Person personObj, String personString) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();

			// inserts the new person into their corresponding table
			statement.executeUpdate("INSERT INTO " + personString + " VALUES (" + personObj.id + ", '" + personObj.name
					+ "', '" + personObj.surname + "', '" + personObj.phoneNum + "', '" + personObj.emailAddres + "', '"
					+ personObj.physicalAddres + "')");

			// Close up our connections
			statement.close();
			connection.close();

		} catch (SQLIntegrityConstraintViolationException e) {
			// Catches any duplicate entry keys and recalls the create person method and
			// insert person query method
			System.out.println("\nYou have entered a duplicate id no., Please enter a new id number.");

			return -1;

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);

			return -1;
		}
		return personObj.id;
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Selects the appropriate persons details in
	 * the appropriate persons table using mySQL statements. Stores the selected
	 * data in a variable and uses a while loop to display all the data in a table
	 * format.
	 * 
	 * @param personType Stores the tables name as a String
	 * @param fieldType  Stores the name of the field that contains the ids of the
	 *                   corresponding table
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @version 1.00
	 */
	public static void displayPeoplesQuery(String personType, String fieldType) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves the selected data
			results = statement.executeQuery(
					"SELECT " + fieldType + ", name, surname, Phone_num, email_addres FROM " + personType);

			System.out.printf("\n%-14s%-20s%-20s%-25s\n", "ID no.", "Name:", "Phone no.", "Email Address:");

			while (results.next()) {
				System.out.printf("%-14s%-20s%-20s%-25s\n", results.getInt(fieldType),
						results.getString("name") + " " + results.getString("surname"), results.getString("Phone_num"),
						results.getString("email_addres"));
			}

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Inserts the appropriate project details in
	 * the appropriate tables using mySQL statements and auto generates a project
	 * number in the database. <br>
	 * Once the new project is inserted Selects the new project and stores the
	 * generated project number to be use in the project_costs table and returns the
	 * project number for later use
	 * 
	 * @param scan       initiates the Scanner class to get the users input
	 * @param newProject Stores the new project object
	 * @param projectNum stores the project number
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @return projectNum
	 * @version 1.00
	 */
	public static int insertProjectQuery(Scanner scan, Project newProject) {

		int projectNum = 0;

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();
			ResultSet results;

			// inserts new Project into the projects table
			statement.executeUpdate(
					"INSERT INTO projects (project_name, building_type, physical_address, ERF_num, finalised, deadline) VALUES ('"
							+ newProject.name + "', '" + newProject.typeBuild + "', '" + newProject.physiAddres + "', "
							+ newProject.efr + ", " + newProject.finalised + ", '" + newProject.dueDate + "')");

			// Selects the project number of the new project
			results = statement.executeQuery("SELECT project_num FROM projects WHERE project_name ='" + newProject.name
					+ "' AND building_type ='" + newProject.typeBuild + "' AND physical_address ='"
					+ newProject.physiAddres + "' AND ERF_num =" + newProject.efr + " AND finalised ="
					+ newProject.finalised + " AND deadline ='" + newProject.dueDate + "'");

			// Loop over the results, outputting the data the was retrieved
			while (results.next()) {

				// Stores the project number for later use
				projectNum = results.getInt("project_num");
			}

			// inserts the projects costs into the prooject_costs table with the retrieved
			// project number
			statement.executeUpdate("INSERT INTO project_costs VALUES (" + projectNum + ", " + newProject.totalFee
					+ ", " + newProject.paidToDate + ")");

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (MysqlDataTruncation e) {
			// Catches a incorrect date format when inputting a value into the table and
			// calls the insertProjectInfo() method
			System.out.println("Please enter the correct date format.\n");
			ProjectInformation.insertProjectInfo(scan);

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
		// returns the project number for later use
		return projectNum;
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Inserts the appropriate project number with
	 * the appropriate persons id into the project_ids table.
	 * 
	 * @param projectNum  Stores the project number
	 * @param strucEngiId Stores the structural engineers id number
	 * @param contrId     Stores the contractors id number
	 * @param archiId     Stores the architect id number
	 * @param custoId     Stores the customer id number
	 * @param connection  Initiates a connection to the database via the jdbc:mysql:
	 *                    channel on localhost
	 * @param statement   initiates a direct line to the database to execute mySQL
	 *                    statements
	 * @version 1.00
	 */
	public static void insertIdsQuery(int projectNum, int strucEngiId, int contrId, int archiId, int custoId) {
		try {
			// Makes a connection to the dataBase
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			// Create a direct line to the database for running our queries
			Statement statement = connection.createStatement();

			// inserts the id's and project number into the database
			statement.executeUpdate("INSERT INTO project_ids VALUES (" + projectNum + ", " + strucEngiId + ", "
					+ contrId + ", " + archiId + ", " + custoId + " )");

			// Close up our connections
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Searches the appropriate person and stores
	 * it into a person object which gets returned.
	 * 
	 * @param id         Stores the persons id number
	 * @param tableType  Stores the table the person will be found in
	 * @param fieldType  Stores the field name that will be compared
	 * @param personObj  Stores the info of the person into an object for later use
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @return personObj
	 * @version 1.00
	 */
	public static Person searchPersonQuery(int id, String tableType, String fieldType) {
		Person personObj = null;
		try {
			// Makes a connection to the dataBase
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			// Create a direct line to the database for running our queries
			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves specific data based on the condition given
			results = statement.executeQuery("SELECT * FROM " + tableType + " WHERE " + fieldType + "=" + id);

			while (results.next()) {
				// Stores the corresponding person into an object
				personObj = new Person(results.getInt(fieldType), results.getString("name"),
						results.getString("surname"), results.getString("Phone_num"), results.getString("email_addres"),
						results.getString("physi_addres"));
			}

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
		// returns the object for later use
		return personObj;
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Searches for projects based on the
	 * appropriate conditions and displays them all using a loop in a table format
	 * <br>
	 * If the finalised field in the projects table is marked as true it will
	 * displays 'finalised' else it will display 'no' if not
	 * 
	 * @param condition  Stores the condition of what the statement must execute
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @version 1.00
	 */
	public static void searchProjectQuery(String condition) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves specific data based on the condition given
			results = statement.executeQuery(
					"SELECT projects.project_num, project_name, building_type, physical_address, ERF_num, finalised, deadline, total_fee, total_to_date"
							+ " FROM project_costs INNER JOIN projects ON project_costs.project_num = projects.project_num WHERE "
							+ condition);

			System.out.printf("%-14s%-20s%-20s%-25s%-10s%-20s%-20s%-20s%-20s\n", "Project no.", "Project Name:",
					"Building Type:", "Physical Address:", "EFR no.", "Is Finalised:", "Deadline", "Total Fee(R)",
					"Total to Date(R)");

			while (results.next()) {

				if (!results.getBoolean("finalised")) {
					System.out.printf("%-14d%-20s%-20s%-25s%-10d%-20s%-20s%-20.2f%-20.2f\n",
							results.getInt("projects.project_num"), results.getString("project_name"),
							results.getString("building_type"), results.getString("physical_address"),
							results.getInt("ERF_num"), "No", results.getString("deadline"),
							results.getDouble("total_fee"), results.getDouble("total_to_date"));
				} else {
					System.out.printf("%-14d%-20s%-20s%-25s%-10d%-20s%-20s%-20.2f%-20.2f\n",
							results.getInt("projects.project_num"), results.getString("project_name"),
							results.getString("building_type"), results.getString("physical_address"),
							results.getInt("ERF_num"), "Finalised", results.getString("deadline"),
							results.getDouble("total_fee"), results.getDouble("total_to_date"));
				}
			}
			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Searches for projects number based on the
	 * projects name and returns the project number for layer use
	 * 
	 * @param projectName Stores the projects name
	 * @param projectNum  stores the projects number
	 * @param connection  Initiates a connection to the database via the jdbc:mysql:
	 *                    channel on localhost
	 * @param statement   initiates a direct line to the database to execute mySQL
	 *                    statements
	 * @param results     Stores the data selected by the mySQL statement
	 * @return projectNum
	 * @version 1.00
	 */
	public static int searchProjNumQuery(String projectName) {

		int projectNum = -1;

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");

			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves specific data based on the condition given
			results = statement
					.executeQuery("SELECT project_num FROM projects WHERE project_name = " + "'" + projectName + "'");

			while (results.next()) {
				projectNum = results.getInt("project_num");
			}

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}

		return projectNum;
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Updates the appropriate project infomation
	 * based on the project number and the field type. Calls a method to displays
	 * the updated project if done successfully. <br>
	 * If a specific field entered the code will convert the information from string
	 * to the approprate data type before updating the database.
	 * 
	 * @param projectNum    Stores the project number
	 * @param newInfo       Stores the new information as a String
	 * @param fieldType     Stores the field name as a string
	 * @param connection    Initiates a connection to the database via the
	 *                      jdbc:mysql: channel on localhost
	 * @param statement     initiates a direct line to the database to execute mySQL
	 *                      statements
	 * @param intNewInfo    Stores the converted newInfo as an Integer
	 * @param doubleNewInfo Stores the converted newInfo as a Double
	 * @version 1.00
	 */
	public static void updateProjectQuery(int projectNum, String newInfo, String fieldType) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();

			if (fieldType.equals("ERF_num")) {
				int intNewInfo = Integer.parseInt(newInfo);
				// Updates the corresponding integer value with the corresponding field type
				statement.executeUpdate(
						"UPDATE projects SET " + fieldType + "=" + intNewInfo + " WHERE project_num =" + projectNum);
			}

			else if (fieldType.equals("total_fee") || fieldType.equals("total_to_date")) {
				double doubleNewInfo = Double.parseDouble(newInfo);
				// Updates the corresponding double value with the corresponding field type
				statement.executeUpdate("UPDATE project_costs SET " + fieldType + "=" + doubleNewInfo
						+ " WHERE project_num =" + projectNum);
			}

			else {
				statement.executeUpdate(
						"UPDATE projects SET " + fieldType + "='" + newInfo + "' WHERE project_num =" + projectNum);
			}

			searchProjectQuery("projects.project_num =" + projectNum);

			// Close up our connections
			statement.close();
			connection.close();

		} catch (MysqlDataTruncation e) {
			// Catches a incorrect date format when inputting a value into the table and
			// calls the insertProjectInfo() method
			System.out.println("Please enter the correct date format.\n");
		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Deletes the appropriate project infomation
	 * based on the project number. Displays a message and calls a method to display
	 * all the projects when deleted successfully. <br>
	 * Starts to delete from the tables that use the project number as a FOREIGN KEY
	 * first. Lastly deletes from the table that uses the project number as the
	 * primary key
	 * 
	 * @param projectNum Stores the project number
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @version 1.00
	 */
	public static void deleteProjectQuery(int projectNum) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();

			statement.executeUpdate("DELETE FROM project_costs WHERE project_num=" + projectNum);
			statement.executeUpdate("DELETE FROM project_ids WHERE project_num=" + projectNum);
			statement.executeUpdate("DELETE FROM projects WHERE project_num=" + projectNum);

			// Close up our connections
			statement.close();
			connection.close();

			System.out.println("\nThe project has been deleted.\n");

			displayAllProjects();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Updates the appropriate project to finalised
	 * by making it true. Also updates the date it was finalised on based on the
	 * project number. Displays a message and the date the project was finalised on
	 * when the project has been updated successfully.
	 * 
	 * @param scan       initiates the Scanner class to get the users input
	 * @param projectNum Stores the project number
	 * @param date       stores the date the project was finalised on
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @version 1.00
	 */
	public static void finaliseProjectQuery(Scanner scan, int projectNum, String date) {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();

			statement.executeUpdate(
					"UPDATE projects SET finalised=" + 1 + ", deadline='" + date + "' WHERE project_num=" + projectNum);

			// Close up our connections
			statement.close();
			connection.close();

			System.out.println("\nThe project has been finalised on the " + date + ".\n");

		} catch (MysqlDataTruncation e) {
			// Catches a incorrect date format when inputting a value into the table and
			// calls the insertProjectInfo() method
			System.out.println("Please enter the correct date format.\n");
			Main.searchOptions(scan, projectNum);

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Displays all the project names and thier
	 * approprate team members in table format.
	 * 
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @version 1.00
	 */
	public static void displayAllProjects() {
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves the selected data types from the database
			results = statement.executeQuery(
					"SELECT projects.project_num, project_name, structural_engineer.name, structural_engineer.surname, "
							+ "contractor.name, contractor.surname, architect.name, architect.surname, customer.name, customer.surname "
							+ "FROM projects INNER JOIN project_ids ON projects.project_num = project_ids.project_num INNER JOIN "
							+ "structural_engineer ON structural_engineer.structural_engineer_id = project_ids.structural_engineer_id "
							+ "INNER JOIN contractor ON contractor.contractor_id = project_ids.contractor_id INNER JOIN architect ON "
							+ "architect.architect_id = project_ids.architect_id INNER JOIN customer ON customer.customer_id = project_ids.customer_id");

			System.out.printf("%-14s%-20s%-20s%-25s%-20s%-20s\n", "Project no.", "Project Names:", "Customers:",
					"Structural Engineers:", "Contractors:", "Architects:");

			// Loop over the results, outputting the data the was retrieved
			while (results.next()) {
				System.out.printf("%-14d%-20s%-20s%-25s%-20s%-20s\n", results.getInt("projects.project_num"),
						results.getString("project_name"),
						results.getString("customer.name") + " " + results.getString("customer.surname"),
						results.getString("structural_engineer.name") + " "
								+ results.getString("structural_engineer.surname"),
						results.getString("contractor.name") + " " + results.getString("contractor.surname"),
						results.getString("architect.name") + " " + results.getString("architect.surname"));
			}

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Selects the apropriate table and id field to
	 * check if the selected id number exsits in the database. Returns false if
	 * nothing is found else returns true.
	 * 
	 * @param personId   Stores the id number the user enter
	 * @param person     stores the name of the table and field
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @return true/false
	 * @version 1.00
	 */
	public static boolean isIdValidQuery(int personId, String person) {

		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves the selected data types from the database
			results = statement.executeQuery("SELECT " + person + "_id FROM " + person);

			// Loop over the results, outputting the data the was retrieved
			while (results.next()) {
				if (results.getInt(person + "_id") == personId) {
					return true;
				}
			}

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
		return false;
	}

	/**
	 * This Connects to the mySQL poisepms database and creates a direct line for
	 * running the appropriate queries. Selects the projects table to check if the
	 * selected project number exsits in the database. Returns false if nothing is
	 * found else returns true.
	 * 
	 * @param num        Stores the project number that the user entered
	 * @param connection Initiates a connection to the database via the jdbc:mysql:
	 *                   channel on localhost
	 * @param statement  initiates a direct line to the database to execute mySQL
	 *                   statements
	 * @param results    Stores the data selected by the mySQL statement
	 * @return true/false
	 * @version 1.00
	 */
	public static boolean isIdValidQuery(int num) {

		try { 
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/poisepms?useSSL=false",
					"otheruser", "swordfish");
			Statement statement = connection.createStatement();
			ResultSet results;

			// retrieves the selected data types from the database
			results = statement.executeQuery("SELECT project_num FROM projects");

			// Loop over the results, outputting the data the was retrieved
			while (results.next()) {
				if (results.getInt("project_num") == num) {
					return true;
				}
			}

			// Close up our connections
			results.close();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			// We only want to catch a SQLException - anything else is off-limits for now.
			System.out.println("Something want wrong." + e);
		}
		return false;
	}
}
