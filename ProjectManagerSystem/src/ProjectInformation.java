
//Import a java library
import java.util.Scanner;

/**
 * The ProjectInformation class declaration
 * <p>
 * This class holds methods that handle most of the users inputs and most of the
 * projects information.
 * 
 * @param customerObj   person object that stores info about the customer
 * @param struEngiObj   person object that stores info about the structural
 *                      engineer
 * @param contractorObj person object that stores info about the contractor
 * @param architectObj  person object that stores info about the architect
 * @author Matthew-James Pether
 * @version 1.00 25 June 2024
 */
public class ProjectInformation {

	// initiates variables that can be used anywhere within the class
	static Person customerObj, struEngiObj, contractorObj, architectObj;

	/**
	 * This method uses the persons id number to find them in the database and
	 * stores their information in their corresponding object variable
	 * 
	 * @param scan        initiates the Scanner class to get the users input
	 * @param strucEngiId Stores the structural engineers id number
	 * @param contrId     Stores the contractors id number
	 * @param archiId     Stores the architects id number
	 * @param custoId     Stores the customers id number
	 * @version 1.00
	 */
	public static void insertPersonsInfo(Scanner scan) {
		try {
			int strucEngiId = 0, contrId = 0, archiId = 0, custoId = 0;

			// A method that returns the id number of the person
			custoId = optionPerson(scan, "customer", "Customer");
			customerObj = SqlQuerys.searchPersonQuery(custoId, "customer", "customer_id");

			strucEngiId = optionPerson(scan, "structural_engineer", "Structural Engineer");
			struEngiObj = SqlQuerys.searchPersonQuery(strucEngiId, "structural_engineer", "structural_engineer_id");

			contrId = optionPerson(scan, "contractor", "Contractor");
			contractorObj = SqlQuerys.searchPersonQuery(contrId, "contractor", "contractor_id");

			archiId = optionPerson(scan, "architect", "Architect");
			architectObj = SqlQuerys.searchPersonQuery(archiId, "architect", "architect_id");

		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			scan.nextLine();
			System.out.println("\nPlease enter the valid value.");
			insertPersonsInfo(scan);
		}
	}

	/**
	 * Asks the user if they would like to create a new person or use an existing
	 * person for each category (structural engineer, contractor, architect,
	 * customer) <br>
	 * This method also returns the corresponding persons id number
	 * 
	 * @param scan           initiates the Scanner class to get the users input
	 * @param person         Stores the type of person that will be used
	 * @param personOutput   Stores the type of person that will be displayed
	 * @param assignPerson   Stores what the user would like to do
	 * @param stringPersonId stores the id number the user entered
	 * @param intPersonId    stores the id number as an integer
	 * @param valid          checks to see if the input is valid
	 * @return intPersonId
	 * @version 1.00
	 */
	public static int optionPerson(Scanner scan, String person, String personOutput) {

		String assignPerson = "", stringPersonId;
		int intPersonId = -1;
		boolean validId = false;

		try {
			// displays the corresponding persons table
			SqlQuerys.displayPeoplesQuery(person, person + "_id");
			System.out.println("\nWould you like to assign an existing " + personOutput + " (Y/N):");
			assignPerson = scan.nextLine();

			// Loops code until the id number is bigger than 0 and the validId equals true
			while (intPersonId < 0 || !validId) {
				// Calls a method that creates a person, inserts the person into the database
				// and returns the persons id number
				if (assignPerson.equalsIgnoreCase("N")) {
					intPersonId = SqlQuerys.insertPersonQuery(createPerson(scan), person);
					validId = true;
				}
				// stores the input of the user and converts it to an integer
				else if (assignPerson.equalsIgnoreCase("Y")) {
					System.out.println("Please enter the desired " + personOutput + "s id number for the new project:");
					stringPersonId = scan.nextLine();
					intPersonId = Integer.parseInt(stringPersonId);
					// a method to check if the id number exists
					validId = isIdValid(intPersonId, person);
				}
				// recurse the method if the values don't match
				else {
					System.out.println("\nPlease enter one of the corresponding letters:\n");
					intPersonId = optionPerson(scan, person, personOutput);
				}
			}

		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			scan.nextLine();
			System.out.println("\nPlease enter the valid value.");
			intPersonId = optionPerson(scan, person, personOutput);
		}
		return intPersonId;
	}

	/**
	 * this checks to see if the id number the user entered appears on the database
	 * in the corresponding table and returns true of false. <br>
	 * Outputs a message if the query method return false
	 * 
	 * @param intPersonId Stores the corresponding persons id number
	 * @param person      Stores the tables name of the corresponding person
	 * @return true/false
	 * @version 1.00
	 */
	private static boolean isIdValid(int intPersonId, String person) {

		if (!SqlQuerys.isIdValidQuery(intPersonId, person)) {
			System.out.println("This id number does not exsit in the database.\n");
			return false;
		}
		return true;
	}

	/**
	 * This asks the user to enter information about the person they would like to
	 * create. Loops through the code until no variables are left blank and are
	 * valid.Then creates a new person object and returns it. <br>
	 * Will output a message if something is left blank or is invalid
	 * 
	 * @param scan        initiates the Scanner class to get the users input
	 * @param name        Stores the input the user has entered
	 * @param surname     Stores the input the user has entered
	 * @param phoneNum    Stores the input the user has entered
	 * @param emailAddres Stores the input the user has entered
	 * @param emailAddres Stores the input the user has entered
	 * @param physiAddres Stores the input the user has entered
	 * @param stringId    Stores the input the user has entered
	 * @param intId       Stores the entered id number as an integer
	 * @param phoneValid  Stores weather the phone number is valid
	 * @param emailValid  Stores weather the email is valid
	 * @param newPerson   Stores the entered input into an object variable
	 * @return newPerson
	 * @version 1.00
	 */
	public static Person createPerson(Scanner scan) {

		String name = "", surname = "", phoneNum = "", emailAddres = "", physiAddres = "", stringId = "";
		int intId = 0;
		boolean phoneValid = false, emailValid = false;

		try {
			System.out.println("\nEnter a new ID number:");
			stringId = scan.nextLine();
			intId = Integer.parseInt(stringId);

			while (name.isBlank() || surname.isBlank() || phoneNum.isBlank() || emailAddres.isBlank()
					|| physiAddres.isBlank() || !emailValid || !phoneValid) {
				System.out.println("Enter thier name:");
				name = scan.nextLine();

				System.out.println("Enter thier surname:");
				surname = scan.nextLine();

				System.out.println("Enter thier phone number:");
				phoneNum = scan.nextLine();
				phoneValid = phoneNotValid(phoneNum);

				System.out.println("Enter thier email address:");
				emailAddres = scan.nextLine();
				emailValid = emailNotValid(emailAddres);

				System.out.println("Enter thier physical address:");
				physiAddres = scan.nextLine();

				// Outputs a message if something is left blank
				if (name.isBlank() || surname.isBlank() || phoneNum.isBlank() || emailAddres.isBlank()
						|| physiAddres.isBlank()) {
					System.out.println("You left someting Blank.");
				}
			}

		} catch (NumberFormatException e) {
			// Catches any invalid variables converting to integer and recurse itself
			System.out.println("\nPlease enter the valid value.");
			createPerson(scan);
		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			System.out.println("\nSomething went wrong. Please enter the valid value.");
			createPerson(scan);
		}

		Person newPerson = new Person(intId, name, surname, phoneNum, emailAddres, physiAddres);

		return newPerson;
	}

	/**
	 * This validates the email address the user has enter. Returns false and
	 * displays a message if the email address does not contain '@' else returns
	 * true
	 * 
	 * @param emailAddres Stores the email address the user entered
	 * @return true/false
	 * @version 1.00
	 */
	private static boolean emailNotValid(String emailAddres) {

		if (!emailAddres.contains("@")) {
			System.out.println("The email address is missing '@'.");
			return false;
		}

		return true;
	}

	/**
	 * This validates the phone number the user has entered. Returns false and
	 * displays a message if the number does not start with a '0', does not have a
	 * length of 10 or is not a number.
	 * 
	 * @param phoneNum Stores the phone number the user entered
	 * @return true/false
	 * @version 1.00
	 */
	private static boolean phoneNotValid(String phoneNum) {

		if (phoneNum.charAt(0) != '0' || phoneNum.length() != 10 || !phoneNum.matches("[0-9]+")) {

			if (phoneNum.charAt(0) != '0') {
				System.out.print("The phone number needs to start with a zero.");
			}

			if (phoneNum.length() != 10) {
				System.out.print("The phone number needs to have 10 digits.");
			}

			if (!phoneNum.matches("[0-9]+")) {
				System.out.print("The phone number must only contain numbers.");
			}

			return false;

		}

		return true;
	}

	/**
	 * This asks the user to enter information about the new project. Stores the
	 * information and create a project object which gets used to be inserted into
	 * the different tables in the database. Display a message when done
	 * successfully. <br>
	 * Will loop through the code and display a appropriate message until the
	 * necessary variables are not left blank
	 * 
	 * @param scan             initiates the Scanner class to get the users input
	 * @param projectName      stores the name of the project
	 * @param typeBuild        stores the building type of the project
	 * @param physiAddres      stores the physical address of the project
	 * @param dueDate          stores the due date of the project
	 * @param stringEfrNum     stores the EFR of the project
	 * @param stringTotalFee   stores the total fee of the project
	 * @param stringPaidToDate stores the amount paid to date
	 * @param intEfrNum        stores the converted EFR number as a integer
	 * @param projectNum       stores the project number
	 * @param doubleTotalFee   stores the convert total fee as a double
	 * @param doublePaidToDate stores the convert paid to date as a double
	 * @version 1.00
	 */
	static void insertProjectInfo(Scanner scan) {
		try {
			String projectName = "", typeBuild = "", physiAddres = "", dueDate = "", stringEfrNum = "",
					stringTotalFee = "", stringPaidToDate = "";
			int intEfrNum = 0, projectNum = -1;
			double doubleTotalFee = 0, doublePaidToDate = 0;

			while (typeBuild.isBlank() || physiAddres.isBlank()) {

				System.out.println("Enter a New Project name:");
				projectName = scan.nextLine();

				System.out.println("Enter Building type:");
				typeBuild = scan.nextLine();

				System.out.println("Enter Physical address:");
				physiAddres = scan.nextLine();

				System.out.println("Enter EFR number:");
				stringEfrNum = scan.nextLine();
				intEfrNum = Integer.parseInt(stringEfrNum);

				System.out.println("Enter Due date (yyyy/mm/dd):");
				dueDate = scan.nextLine();

				System.out.println("Enter the total fee being charged for the project (R):");
				stringTotalFee = scan.nextLine();
				doubleTotalFee = Double.parseDouble(stringTotalFee);

				System.out.println("The total amount paid to date (R):");
				stringPaidToDate = scan.nextLine();
				doublePaidToDate = Double.parseDouble(stringPaidToDate);

				if (typeBuild.isBlank()) {
					System.out.println("\nPlease enter a Building type.");
				}

				if (physiAddres.isBlank()) {
					System.out.println("\nPlease enter a physical Address.");
				}
			}

			// If the project name is left empty then make project name equal to
			// the building type plus the customers surname
			if (projectName.isBlank()) {
				projectName = typeBuild + " " + customerObj.surname;
			}

			// Uses the inputed info to create a project object
			Project newProject = new Project(projectNum, projectName, typeBuild, physiAddres, intEfrNum, false, dueDate,
					doubleTotalFee, doublePaidToDate);

			// Calls an SQL insert query method
			projectNum = SqlQuerys.insertProjectQuery(scan, newProject);

			// Calls a SQL query method that inserts the corresponding id's with the project
			// number
			SqlQuerys.insertIdsQuery(projectNum, struEngiObj.id, contractorObj.id, architectObj.id, customerObj.id);

			System.out.println("\nA new Project has been Entered!");

		} catch (NumberFormatException e) {
			// Catches any invalid variables converting to integer and recurse itself
			System.out.println("\nPlease enter the valid number values.");
			insertProjectInfo(scan);
		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			System.out.println("\nPlease enter the corresponding values.");
			System.out.print("something went wrong. " + e);
			insertProjectInfo(scan);

		}
	}

	/**
	 * This asks the user how they would like to search for a project. If they
	 * select '1' they will be able to search using the project number. If they
	 * select '2' they will be to search using the project name. if they select '0'
	 * it will exit to the previous menu. <br>
	 * If the project number or name does not exist in the data base a message will
	 * be displayed and the method will be recursed
	 * 
	 * @param scan              initiates the Scanner class to get the users input
	 * @param projectNameString stores the project name
	 * @param optionNum         Stores which option the user selected
	 * @param projectNumInt     Stores the product number
	 * @return projectNumInt
	 * @version 1.00
	 */
	static int searchProjectInfo(Scanner scan) {

		String projectNameString = "", optionNum = "";
		int projectNumInt = 0;

		try {
			System.out.println("\nWhich field would you Like to search from (Select the corresponding number)?"
					+ "\n1. Project no." + "\n2. Project Name" + "\n0. Exit");
			optionNum = scan.nextLine();

			// Using the switch statement to execute the appropriate query
			switch (optionNum) {

			// Searches the data base using project number
			case "1":
				System.out.println("\nPlease enter the Project Number:");
				projectNumInt = scan.nextInt();
				scan.nextLine();

				if (!SqlQuerys.isIdValidQuery(projectNumInt)) {
					System.out.println("This project number does not exist in the DataBase");
					projectNumInt = searchProjectInfo(scan);
				}

				return projectNumInt;

			// Searches the data base using project name
			case "2":
				System.out.println("\nPlease enter the Project Name:");
				projectNameString = scan.nextLine();
				// calls a method to find the project number that corresponds to the project
				// name and stores it
				projectNumInt = SqlQuerys.searchProjNumQuery(projectNameString);

				if (!SqlQuerys.isIdValidQuery(projectNumInt)) {
					System.out.println("This project name does not exist in the DataBase");
					projectNumInt = searchProjectInfo(scan);
				}

				return projectNumInt;

			// returns to previous method
			case "0":
				break;

			// If none of the corresponding values were selected outputs a message
			default:
				System.out.println("\nPlease enter the corresponding number:");
				return searchProjectInfo(scan);
			}

		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			scan.nextLine();
			System.out.println("\nPlease enter the valid value.");
			return searchProjectInfo(scan);
		}

		return projectNumInt;
	}

	/**
	 * This updates the corresponding project based on the project number. Loops
	 * through code and asks the user what field of the project they would like to
	 * update. Lists and numbers all the fields of the projects table except
	 * Project_num (1 to 8). Uses '0' to exit the loop. <br>
	 * Loops through each field the user selected if they left the input variable
	 * blank
	 * 
	 * @param scan       initiates the Scanner class to get the users input
	 * @param projectNum Stores the project number
	 * @param isLooping  Determines if the code will be looping
	 * @param optionNum  Stores the field the user wants to update
	 * @param newInfo    Stores the new information
	 * @param stringQues Stores a Question that gets used repetitively
	 * @version 1.00
	 */
	static void updateProjectInfo(Scanner scan, int projectNum) {

		boolean isLooping = true;

		try {
			while (isLooping) {

				String optionNum = "", newInfo = "", stringQues = "\nWhat would you Like to change the info to?";

				System.out.println("\nSelect which field you wish to Update:" + "\n1. Project Name"
						+ "\n2. Building Type" + "\n3. Physical Address" + "\n4. EFR no." + "\n5. Finalised"
						+ "\n6. Deadline" + "\n7. Total Fee(R)" + "\n8. Total to Date(R)" + "\n0. Exit");
				optionNum = scan.nextLine();

				// Using the switch statement to execute the appropriate query the user selected
				switch (optionNum) {

				// Updates project name
				case "1":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "project_name");
						}
					}
					break;
				// Updates building type
				case "2":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "building_type");
						}
					}
					break;
				// Updates physical address
				case "3":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "physical_address");
						}
					}
					break;
				// Updates ERF number
				case "4":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "ERF_num");
						}
					}
					break;
				// Updates finalised
				case "5":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "finalised");
						}
					}
					break;
				// Updates deadline
				case "6":
					while (newInfo.isBlank()) {
						System.out.println(stringQues + "(yyyy/mm/dd)");
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "deadline");
						}
					}
					break;
				// Updates total fee
				case "7":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "total_fee");
						}
					}
					break;
				// Updates total to date
				case "8":
					while (newInfo.isBlank()) {
						System.out.println(stringQues);
						newInfo = scan.nextLine();
						if (newInfo.isBlank()) {
							System.out.println("\nPlease enter the input.");
						} else {
							// Updates the database
							SqlQuerys.updateProjectQuery(projectNum, newInfo, "total_to_date");
						}
					}
					break;
				// exits out the method
				case "0":
					isLooping = false;
					break;

				// If none of the corresponding values were selected outputs a message
				// and exits out the method
				default:
					System.out.println("\nPlease enter the corresponding number:");
				}
			}

		} catch (NumberFormatException e) {
			// Catches any invalid variables converting to integer and recurse itself
			System.out.println("\nPlease enter the valid number values.");
			updateProjectInfo(scan, projectNum);

		} catch (Exception e) {
			// Catches any invalid inputs and recurse itself
			System.out.println("Something want wrong." + e);
			updateProjectInfo(scan, projectNum);
		}
	}
}
