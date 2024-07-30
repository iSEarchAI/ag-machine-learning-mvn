package goes.iga;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

public class InstanceReader {

	private BufferedReader reader;
	/**
	 * Constructor
	 * @param instance
	 */
	public InstanceReader(File instance){
		try{
			this.reader = new BufferedReader(new FileReader(instance));
			reader.mark(1);
		}
		catch(Exception e){
			System.out.println("Instance reading error");
			e.printStackTrace();
		}
	}
	/**
	 * Get Number Of Clients
	 * @return
	 */
	private int getNumberOfCustomers(){
		String[] values = null;
		int numberOfCustomers = 0;

		try{
			reader.reset();
			values = reader.readLine().split(" ");
			numberOfCustomers = Integer.parseInt(values[0]);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return numberOfCustomers;
	}
	/**
	 * Get number of requirements from the specified instance
	 * @return
	 */
	private int getNumberOfRequirements(){
		String[] values = null;
		int numberOfRequirements = 0;

		try{
			reader.reset();
			values = reader.readLine().split(" ");
			numberOfRequirements = Integer.parseInt(values[1]);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return numberOfRequirements;
	}
	/**
	 * Get importance of the clients from the instance
	 * @return
	 */
	public double[] getCustomersImportance(){
		double[] customersImportance = new double[getNumberOfCustomers()];
		String[] values = null;

		try{
			reader.reset();
			reader.readLine();
			reader.readLine();
			values = reader.readLine().split(" ");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		for(int i = 0; i <= customersImportance.length - 1; i++){
			customersImportance[i] = Double.parseDouble(values[i]);
		}

		return customersImportance;
	}
	/**
	 * Get Importances of the requirements
	 * @return
	 */
	public double[][] getRequirementsImportances(){
		double[][] requirementsImportances = new double[getNumberOfCustomers()][getNumberOfRequirements()];
		String[] values = null;

		try{
			reader.reset();
			reader.readLine();
			reader.readLine();
			reader.readLine();
			reader.readLine();

			for(int i = 0; i <= requirementsImportances.length - 1; i++){
				values = reader.readLine().split(" ");

				for(int j = 0; j <= requirementsImportances[0].length - 1; j++){
					requirementsImportances[i][j] = Integer.parseInt(values[j]);
				}
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return requirementsImportances;
	}
	/**
	 * Get requirement Cost from the instance
	 * @return
	 */
	public double[] getRequirementsCosts(){
		double[] requirementsCosts = new double[getNumberOfRequirements()];
		int numberOfCustomers = getNumberOfCustomers();
		String[] values = null;

		try{
			reader.reset();
			for(int i = 0; i <= 5 + numberOfCustomers - 1; i++){
				reader.readLine();
			}

			values = reader.readLine().split(" ");
		}
		catch(Exception e){
			e.printStackTrace();
		}

		for(int i = 0; i <= requirementsCosts.length - 1; i++){
			requirementsCosts[i] = Integer.parseInt(values[i]);
		}

		return requirementsCosts;
	}
}
