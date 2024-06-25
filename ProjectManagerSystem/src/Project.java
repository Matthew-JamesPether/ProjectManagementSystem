//The class  declaration
public class Project {
	
 // Creates project attributes 
	String name;  
	String typeBuild;
	String  physiAddres;
	String dueDate;
	int num;
	int efr;
	double totalFee;
    double paidToDate;
    boolean finalised;
    
    //Creates a project object
	public Project ( int num, String name, String typeBuild, String  physiAddres, int efr, boolean finalised, String dueDate, double totalFee, double paidToDate) {
		
		this.num = num;
		this.name = name;
		this.typeBuild = typeBuild;
		this.physiAddres = physiAddres;
		this.efr = efr;
		this.dueDate = dueDate;
		this.totalFee = totalFee;
		this.paidToDate = paidToDate;
		this.finalised = finalised;
	}
}
