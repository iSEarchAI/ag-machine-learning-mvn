package goes.iga;


public class InstanceGeneratorTest {
	public static void main(String[] args){
		int numberOfCustomers = 4;
		int numberOfRequirements = 250;
		int importanceUpperBound = 5;
		int costUpperBound = 5;

		InstanceGenerator ig = new InstanceGenerator(numberOfCustomers, numberOfRequirements, importanceUpperBound, costUpperBound);
		ig.generateInstance();
	}
}
