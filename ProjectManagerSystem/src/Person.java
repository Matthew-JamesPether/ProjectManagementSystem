//The class  declaration
public class Person {

	//Creates person attributes
	String name;
	String surname;
	String phoneNum;
	String emailAddres;
	String physicalAddres;
	int id;
	
	//Creates a person object
	public Person (int id, String name, String surname, String phoneNum, String emailAddres, String physicalAddres) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		this.phoneNum = phoneNum;
		this.emailAddres = emailAddres;
		this.physicalAddres = physicalAddres;
	}
}
