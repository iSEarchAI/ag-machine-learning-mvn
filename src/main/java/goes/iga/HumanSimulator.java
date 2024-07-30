package goes.iga;
import java.util.Random;
/**
 * This Class is used as a simulator of different types of profile for evaluating solutions.
 * This way, depending on the profile chosen, the evaluation of a  given  can  be different.
 * The evaluation works on the following way: First of all one of the available profiles  is
 * chosen. Then according to the chosen profile  the  requirements  will  be  added  in  the
 * target-individual. The number of requirements to be added is set with a given  percentage.
 * Thus, a solution is evaluated according to the  similarity between it and the target-individual.
 *
 * @since 03-31-2015
 * @author --
 *
 */
public class HumanSimulator {
	/**
	 * Maximum score the Human can give to a solution
	 */
	private double maxHumanEvaluation;
	/**
	 * Stores the number of requirements
	 */
	private int numberOfRequirements;
	/**
	 * Stores the requirements Scores of the requirements
	 */
	private double[] requirementsScore;
	/**
	 * Stores requirements Costs
	 */
	private double[] requirementsCost;
	/**
	 * Stores the number of requirements the target solutions has.
	 */
	private int targetSolutionPercentage;
	/**
	 * Stores Target Solution
	 */
	private int[] targetSolution;

	private Random random;
	/**
	 * Constructor
	 */
	public HumanSimulator(){
		random = new Random();
		this.maxHumanEvaluation = 100;
		targetSolutionPercentage = 50;
	}
	/**
	 *
	 * @param requirementsScore
	 */
	public void setScoreValues (double[] requirementsScore) {
		this.requirementsScore = requirementsScore;
		this.numberOfRequirements = requirementsScore.length;
	}
	/**
	 *
	 * @param requirementsCost
	 */
	public void setCostValues (double[] requirementsCost) {
		this.requirementsCost = requirementsCost;
		this.numberOfRequirements = requirementsCost.length;
	}
	/**
	 * Set the Profile used to generate a target individual
	 * @param human
	 */
	public void setHumanSimulatorProfile (String human) {

		int nRequirementsInSolution = getNumberOfRequirementsInIndividual(targetSolutionPercentage);
		/*
		 * Generate individual RandomLy
		 */
		if(human == "RANDOM"){
			this.targetSolution = getRandomlyTargetSolution(nRequirementsInSolution);
		}
		/*
		 * Order the requirements by score and return a solution with the higher score requirements
		 */
		else if (human == "HIGHER_SCORE") {
			this.targetSolution = getHigherScoreTargetSolution(nRequirementsInSolution);
		}
		/*
		 * Order the requirements by score and return a solution with the lowest score requirements
		 */
		else if(human == "LOWER_SCORE") {
			this.targetSolution = getLowerScoreTargetSolution(nRequirementsInSolution);
		}
		/*
		 * Order the requirements by cost and return a solution with the higher cost requirements
		 */
		else if (human == "HIGHER_COST") {
			this.targetSolution = getHigherCostTargetSolution(nRequirementsInSolution);
		}
		/*
		 * Order the requirements by cost and return a solution with the lowest cost requirements
		 */
		else if (human == "LOWER_COST") {
			this.targetSolution = getLowerCostTargetSolution(nRequirementsInSolution);
		}
		/*
		 * This method has a set of solutions generated manually
		 */
		else if (human == "MANUALLY") {
			this.targetSolution = getManuallyTargetSolution();
		}
		/*
		 *
		 */
		else {
			System.out.println("Profile NOT FOUND!");
			System.exit(0);
		}
	}
	/**
	 *
	 * @return
	 */
	public double getMaxHumanEvaluation(){
		return this.maxHumanEvaluation;
	}
	/**
	 * Evaluate a given individual according to the target solution
	 * @param individual
	 * @return "subjective" evaluation
	 */
	public int getHumanEvaluation(int[] individual){
		double humanEvaluation = 0;
		double numberOfSimilaritiesInIndividual = getNumberOfSimilaritiesInIndividual(individual);

		humanEvaluation = (maxHumanEvaluation * numberOfSimilaritiesInIndividual) / targetSolution.length;

		return (int) Math.round(humanEvaluation);
	}
	/**
	 *
	 * @return the target solution
	 */
	public int[] getTargetSolution(){
		return this.targetSolution;
	}
	/**
	 *
	 * @return
	 */
	private int[] getManuallyTargetSolution(){
		int[] targetSolution = null;

		if(50 == requirementsCost.length) {
			targetSolution = new int[] {1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0};
		}

		else if(100 == requirementsCost.length){
		targetSolution = new int[] {0, 0, 1, 0, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1};
		}

		else if(150 == requirementsCost.length){
			targetSolution = new int[] {0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0, 1, 1, 1, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 0};
		}

		else if(200 == requirementsCost.length){
			targetSolution = new int[] {0, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 0, 1, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 0, 0, 1, 0, 1, 0, 0, 1, 0, 1, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 1, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 0, 0, 0, 1, 0, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 1, 0, 0, 0, 0, 1, 1, 0, 1, 1, 1, 1, 0, 1, 1, 0, 1, 0, 1, 0, 0, 1, 0, 0, 0, 0, 1, 1, 0, 0, 1, 0, 1};
		}
		else {
			System.out.println("There is no TARGET for such size!");
			System.exit(0);
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirementsPercentage
	 * @return
	 */
	private int getNumberOfRequirementsInIndividual(int requirementsPercentage){
		return (int) Math.round((requirementsPercentage / 100.0) * numberOfRequirements);
	}
	/**
	 *
	 * @param numberOfRequirementsInSolution
	 * @return
	 */
	private int[] getRandomlyTargetSolution(int numberOfRequirementsInSolution){
		int[] targetSolution = new int[numberOfRequirements];

		for(int i = 0; i <= targetSolution.length - 1; i++){
			targetSolution[i] = -1;
		}

		for(int i = 0; i <= numberOfRequirementsInSolution - 1; i++){
			int randomRequirement = random.nextInt(numberOfRequirements);

			while(isRequirementInTargetSolution(randomRequirement, targetSolution) == true){
				randomRequirement = random.nextInt(numberOfRequirements);
			}

			targetSolution[randomRequirement] = 1;
		}

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[i] != 1){
				targetSolution[i] = 0;
			}
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirement
	 * @param targetSolution
	 * @return
	 */
	private boolean isRequirementInTargetSolution(int requirement, int[] targetSolution){
		boolean result = false;

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[requirement] == 1){
				result = true;
				break;
			}
		}

		return result;
	}
	/**
	 *
	 * @param numberOfRequirementsInSolution
	 * @return
	 */
	private int[] getHigherScoreTargetSolution(int numberOfRequirementsInSolution){
		int[] targetSolution = new int[numberOfRequirements];
		int[] requirementsSelectedFlags = new int[numberOfRequirements];
		int higherScoreRequirement = 0;

		for(int i = 0; i <= numberOfRequirementsInSolution - 1; i++){
			higherScoreRequirement = selectHigherScoreRequirement(requirementsSelectedFlags);
			requirementsSelectedFlags[higherScoreRequirement] = 1;
			targetSolution[higherScoreRequirement] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirementsSelectedFlags
	 * @return
	 */
	private int selectHigherScoreRequirement(int[] requirementsSelectedFlags){
		int higherScoreRequirement = getFirstRequirementNotSelected(requirementsSelectedFlags);

		for(int i = 0; i <= requirementsScore.length - 1; i++){
			if(requirementsScore[i] > requirementsScore[higherScoreRequirement] && requirementsSelectedFlags[i] == 0){
				higherScoreRequirement = i;
			}
		}

		return higherScoreRequirement;
	}
	/**
	 *
	 * @param requirementsSelectedFlags
	 * @return
	 */
	private int getFirstRequirementNotSelected(int[] requirementsSelectedFlags){
		int firstRequirementNotSelected = 0;

		for(int i = 0; i <= requirementsSelectedFlags.length - 1; i++){
			if(requirementsSelectedFlags[i] == 0){
				firstRequirementNotSelected = i;
				break;
			}
		}

		return firstRequirementNotSelected;
	}
/**
 *
 * @param numberOfRequirementsInSolution
 * @return
 */
	private int[] getLowerScoreTargetSolution(int numberOfRequirementsInSolution){
		int[] targetSolution = new int[numberOfRequirements];
		int[] requirementsSelectedFlags = new int[numberOfRequirements];
		int lowerScoreRequirement = 0;

		for(int i = 0; i <= numberOfRequirementsInSolution - 1; i++){
			lowerScoreRequirement = selectLowerScoreRequirement(requirementsSelectedFlags);
			requirementsSelectedFlags[lowerScoreRequirement] = 1;
			targetSolution[lowerScoreRequirement] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirementsSelectedFlags
	 * @return
	 */
	private int selectLowerScoreRequirement(int[] requirementsSelectedFlags){
		int lowerScoreRequirement = getFirstRequirementNotSelected(requirementsSelectedFlags);

		for(int i = 0; i <= requirementsScore.length - 1; i++){
			if(requirementsScore[i] < requirementsScore[lowerScoreRequirement] && requirementsSelectedFlags[i] == 0){
				lowerScoreRequirement = i;
			}
		}

		return lowerScoreRequirement;
	}
	/**
	 *
	 * @param numberOfRequirementsInSolution
	 * @return
	 */
	private int[] getHigherCostTargetSolution(int numberOfRequirementsInSolution){
		int[] targetSolution = new int[numberOfRequirements];

		int[] requirementsSelectedFlags = new int[numberOfRequirements];
		int higherCostRequirement = 0;

		for(int i = 0; i <= numberOfRequirementsInSolution - 1; i++){
			higherCostRequirement = selectHigherCostRequirement(requirementsSelectedFlags);
			requirementsSelectedFlags[higherCostRequirement] = 1;
			targetSolution[higherCostRequirement] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirementsSelectedFlags
	 * @return
	 */
	private int selectHigherCostRequirement(int[] requirementsSelectedFlags){
		int higherCostRequirement = getFirstRequirementNotSelected(requirementsSelectedFlags);

		for(int i = 0; i <= requirementsCost.length - 1; i++){
			if(requirementsCost[i] > requirementsCost[higherCostRequirement] && requirementsSelectedFlags[i] == 0){
				higherCostRequirement = i;
			}
		}

		return higherCostRequirement;
	}
	/**
	 *
	 * @param numberOfRequirementsInSolution
	 * @return
	 */
	private int[] getLowerCostTargetSolution(int numberOfRequirementsInSolution){
		int[] targetSolution = new int[numberOfRequirements];

		int[] requirementsSelectedFlags = new int[numberOfRequirements];
		int lowerCostRequirement = 0;

		for(int i = 0; i <= numberOfRequirementsInSolution - 1; i++){
			lowerCostRequirement = selectLowerCostRequirement(requirementsSelectedFlags);
			requirementsSelectedFlags[lowerCostRequirement] = 1;
			targetSolution[lowerCostRequirement] = 1;
		}

		return targetSolution;
	}
	/**
	 *
	 * @param requirementsSelectedFlags
	 * @return
	 */
	private int selectLowerCostRequirement(int[] requirementsSelectedFlags){
		int lowerCostRequirement = getFirstRequirementNotSelected(requirementsSelectedFlags);

		for(int i = 0; i <= requirementsCost.length - 1; i++){
			if(requirementsSelectedFlags[i] == 0)
			if(requirementsCost[i] < requirementsCost[lowerCostRequirement] && requirementsSelectedFlags[i] == 0){
				lowerCostRequirement = i;
			}
		}

		return lowerCostRequirement;
	}
	/**
	 *
	 * @param individual
	 * @return
	 */
	public int getNumberOfSimilaritiesInIndividual(int[] individual){
		int numberOfSimilaritiesInIndividual = 0;

		for(int i = 0; i <= targetSolution.length - 1; i++){
			if(targetSolution[i] == individual[i]){
				numberOfSimilaritiesInIndividual++;
			}
		}

		return numberOfSimilaritiesInIndividual;
	}
	/**
	 *
	 * @return
	 */
	public int getNumberOfRequirements(){
		return numberOfRequirements;
	}
}
