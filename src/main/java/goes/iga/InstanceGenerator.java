package goes.iga;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class InstanceGenerator {

	private int numberOfCustomers;
	private int numberOfRequirements;
	private int importanceUpperBound;
	private int costUpperBound;

	private Random random;

	public InstanceGenerator(int numberOfCustomers, int numberOfRequirements, int importanceUpperBound, int costUpperBound){
		this.numberOfCustomers = numberOfCustomers;
		this.numberOfRequirements = numberOfRequirements;
		this.importanceUpperBound = importanceUpperBound;
		this.costUpperBound = costUpperBound;

		this.random = new Random();
	}

	public void generateInstance(){
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("../../instances/I_S_" + numberOfRequirements + ".txt")));

			writer.write(numberOfCustomers + " " + numberOfRequirements + "\n");
			writer.write("\n");

			writer.write(getCustomersImportance(numberOfCustomers) + "\n");
			writer.write("\n");

			for(int i = 0; i <= numberOfCustomers - 1; i++){
				writer.write(getRequirementsImportances(numberOfRequirements) + "\n");
			}
			writer.write("\n");

			writer.write(getRequirementsCosts(numberOfRequirements));

			writer.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	private String getCustomersImportance(int numberOfCustomers){
		String customersImportance = null;
		double[] randomNumbers = new double[numberOfCustomers];
		double randomNumbersSum = 0;

		for(int i = 0; i <= randomNumbers.length - 1; i++){
			randomNumbers[i] = random.nextDouble();
			randomNumbersSum += randomNumbers[i];
		}

		for(int i = 0; i <= randomNumbers.length - 1; i++){
			if(customersImportance == null){
				customersImportance = randomNumbers[i] / randomNumbersSum + "";
			}
			else{
				customersImportance += " " + randomNumbers[i] / randomNumbersSum;
			}
		}

		return customersImportance;
	}

	private String getRequirementsImportances(int numberOfRequirements){
		String requirementsImportances = null;

		for(int i = 0; i <= numberOfRequirements - 1; i++){
			if(i == 0){
				requirementsImportances = (random.nextInt(importanceUpperBound) + 1) + "";
			}
			else{
				requirementsImportances += " " + (random.nextInt(importanceUpperBound) + 1);
			}
		}

		return requirementsImportances;
	}

	private String getRequirementsCosts(int numberOfRequirements){
		String requirementsCosts = null;

		for(int i = 0; i <= numberOfRequirements - 1; i++){
			if(i == 0){
				requirementsCosts = (random.nextInt(costUpperBound) + 1) + "";
			}
			else{
				requirementsCosts += " " + (random.nextInt(costUpperBound) + 1);
			}
		}

		return requirementsCosts;
	}
}
