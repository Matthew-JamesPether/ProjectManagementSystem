
///Imports java libraries
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * The main class declaration
 * <p>
 * This class executes a set of instructions that asks the user how they would
 * like to manipulate the 'poisepms' database.
 * 
 * @author Matthew-James Pether
 * @version 1.00 25 June 2024
 */
public class Main {

	/**
	 * Loops through a menu that the console outputs which requires the user to
	 * enter a value. The input value determines the next set of instructions this
	 * class will be executing. <br>
	 * if user selects: '1' = calls a method that inserts a new project, '2' = calls
	 * a method that searches a project, '3' = calls a method that displays
	 * unfinished projects, '4' = calls a method that displays overdue projects, '5'
	 * = calls a method that displays all projects, '0' = breaks the loop and
	 * outputs a message
	 * 
	 * @param queryNum    int variable that stores the menu option the user entered
	 * @param isLooping   boolean variable that loops through the code until user
	 *                    selects '0'
	 * @param scan        initiates the Scanner class to get the users input
	 * @param projectNum  int variable that stores the project number to search the
	 *                    project details
	 * @param currentDate initiates the Date class to get the current date
	 * @param dateForma   initiates the SimpleDateFormat class which helps format
	 *                    the currentDate variable
	 * @version 1.00
	 */
	public static void main(String[] args) {

		int queryNum;
		boolean isLooping = true;
		Scanner scan = new Scanner(System.in);

		// loops through the code
		while (isLooping) {

			// Calls a method and returns the users input
			queryNum = selectedQuery(scan);

			// Using the switch statement to execute the appropriate query the user selected
			switch (queryNum) {

			// Executes a insert method
			case 1:
				// Calls a method that requires the user to assign people to the project
				ProjectInformation.insertPersonsInfo(scan);
				// Calls a method that requires the user to assign a project
				ProjectInformation.insertProjectInfo(scan);
				break;

			// Executes a search method
			case 2:
				// Calls a method to search a project and returns the project number
				int projectNum = ProjectInformation.searchProjectInfo(scan);
				// Calls a method which asks the use what to do with the project
				searchOptions(scan, projectNum);
				break;

			// Displays all unfinalised projects
			case 3:
				System.out.println("\nThese are the unfinalised projects:");
				SqlQuerys.searchProjectQuery("Finalised=0");
				break;

			// Displays all projects that are overdue
			case 4:
				System.out.println("\nThese are the projects that are overdue:");
				Date currentDate = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
				SqlQuerys.searchProjectQuery("deadline<'" + dateFormat.format(currentDate) + "'");
				break;

			// Displays all projects
			case 5:
				System.out.println("\nThese are all projects:");
				SqlQuerys.displayAllProjects();
				break;

			// Displays a message and breaks the loop
			case 0:
				System.out.println("\nGoodbye.");
				isLooping = false;
				break;

			// Displays a message and loops again if nothing was selected
			default:
				System.out.println("\nPlease enter the corresponding number:");
			}
		}
		// closes the scanner object;
		scan.close();
	}

	/**
	 * Displays a menu which gives the user options on what they can do with the
	 * database. Stores the option the user inputs and returns it
	 * 
	 * @param optionNum in variable that stores the users input
	 * @param scan      initiates the Scanner class to get the users input
	 * @return optionNum
	 * @version 1.00
	 */
	private static int selectedQuery(Scanner scan) {

		int optionNum = 0;
		try {
			System.out
					.println("\nWhat would you like to find in the PoisePMS Database (Select the corresponding number):"
							+ "\n1. Enter New Project" + "\n2. Search a Project" + "\n3. Display unfinished Projects"
							+ "\n4. Display Overdue Projects" + "\n5. Display all Projects" + "\n0. Exit");

			optionNum = scan.nextInt();
			scan.nextLine();

		} catch (Exception e) {
			// Catches any invalid inputs and recurse the method
			scan.nextLine();
			System.out.println("\nPlease enter the valid number.");
			optionNum = selectedQuery(scan);
		}
		return optionNum;
	}

	/**
	 * Displays a menu about what the user would like to do to the project he
	 * searched <br>
	 * if user selects: '1' = calls a method to update the project '2' = deletes the
	 * project '3' = finalises the project '0' = breaks the loop
	 * 
	 * @param scan       initiates the Scanner class to get the users input
	 * @param projectNum Stores the project number
	 * @param isLooping  boolean variable that loops through the code until user
	 *                   selects '0'
	 * @param isDelete   String variable that stores the users input (yes/no)
	 * @param deleteLoop boolean variable that loops through the code until user
	 *                   selects '0'
	 * @param isFinalise String variable that stores the users input (yes/no)
	 * @param finalLoop  boolean variable that loops through the code until user
	 *                   selects '0'
	 * @param date       String variable that stores the date the user inputs
	 * @return null
	 * @version 1.00
	 * 
	 */
	public static String searchOptions(Scanner scan, int projectNum) {

		int optionNum = 0;
		boolean isLooping = true;

		try {
			// loops through the code until the user selects 0 or the project number is
			// invalid
			while (isLooping && projectNum > 0) {

				// Calls the search SQl query method
				SqlQuerys.searchProjectQuery("projects.project_num =" + projectNum);

				System.out.println(
						"\nWhat would you like to do to the Searched Project (Select the corresponding number):"
								+ "\n1. Update the Project" + "\n2. Delete the Project" + "\n3. Finilise the Project"
								+ "\n0. Exit");

				optionNum = scan.nextInt();
				scan.nextLine();

				// Using the switch statement to execute the appropriate query the user selected
				switch (optionNum) {

				// Executes a update method
				case 1:
					ProjectInformation.updateProjectInfo(scan, projectNum);
					break;

				// Executes a delete method
				case 2:

					String isDelete = "";
					boolean deleteLoop = true;

					while (deleteLoop) {
						// Outputs a message to ensure the user wishes to delete the selected project
						System.out.println(
								"Are you sure you want to delete this project (Select the corresponding number)? \n1. Yes \n2. No");

						isDelete = scan.nextLine();

						// Executes the delete query method on the project if '1' is selected
						if (isDelete.equals("1")) {
							SqlQuerys.deleteProjectQuery(projectNum);
							deleteLoop = false;
							// returns to the first menu if project is deleted
							return null;
						}
						// if '2' breaks the loop
						else if (isDelete.equals("2")) {
							deleteLoop = false;
						}
						// else display a message
						else {
							System.out.println("\nPlease enter the corresponding number:\n");
						}
					}
					break;

				// Executes a method to finalise a project
				case 3:

					String isFinalise = "";
					boolean finalLoop = true;

					while (finalLoop) {
						// Outputs a message to ensure the user wishes to finalise the selected project
						System.out.println(
								"Are you sure you want to finalise this project (Select the corresponding number)? \n1. Yes \n2. No");
						isFinalise = scan.nextLine();

						// executes a the following code and breaks the loop
						if (isFinalise.equals("1")) {
							// Outputs a message that asks the date at which the project was finalised
							System.out.println("Enter the date the project was finalised on (yyyy/mm/dd)?");
							String date = scan.nextLine();

							// Calls a SQL method to finalise the project
							SqlQuerys.finaliseProjectQuery(scan, projectNum, date);
							finalLoop = false;
						}
						// breaks the loop
						else if (isFinalise.equals("2")) {
							finalLoop = false;
						}
						// else display a message
						else {
							System.out.println("\nPlease enter the corresponding number:\n");
						}
					}
					break;

				// breaks the loop
				case 0:
					isLooping = false;
					break;

				// If none of the corresponding values were selected outputs a message
				// and loops again
				default:
					System.out.println("\nPlease enter the corresponding number:\n");
				}
			}
		} catch (InputMismatchException e) {
			// Catches any invalid variables converting to integer and recurse itself
			scan.nextLine();
			System.out.println("\nPlease enter the valid value.");
			return searchOptions(scan, projectNum);

		} catch (Exception e) {
			// Catches any invalid inputs and recurse the method
			scan.nextLine();
			System.out.println("Something want wrong." + e);
			return searchOptions(scan, projectNum);
		}

		return null;
	}
}
/*
 * References: https://youtu.be/ALht4W2QxqY?si=ZYue-tYhCzwEKva_
 * https://youtu.be/6cp4P4XZ9hE?si=8Rv2dyIMRwXx0VOL
 * https://youtu.be/_tS2gw5l1TY?si=TteI9ZQItI3q4-9f
 * https://www.mysqltutorial.org/mysql-basics/mysql-decimal/#:~:text=Use%20MySQL
 * %20DECIMAL%20data%20type,DECIMAL(P%2C%200)%20.
 * https://www.w3schools.com/sql/sql_foreignkey.asp
 */