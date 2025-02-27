package goes.iga;
import java.util.HashMap;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.functions.MultilayerPerceptron;
/**
 *
 * This class represent a specific interactive genetic algorithm for The Next Release Problem,
 * responsible for the interactions, the learning models and the optimization process.
 *
 * @since 03-31-2015
 * @author --
 *
 */
public class InteractiveGeneticAlgorithm {
	/**
	 * Parameters alpha (the firs one) and beta (the seconde one) representing,
	 * respectively, weight of score and subjective approach
	 */
	private double[] parameters = {1, 1};
	/**
	 * Stores the number of requirements
	 */
	private int numberOfRequirements;
	/**
	 * Stores score of each requirement
	 */
	public double[] requirementsScore;
	/**
	 * Stores maxIndividualScore provided by the Non-Interactive Approach
	 */
	private double maxIndividualScore;
	/**
	 * Stores cost of each requirement
	 */
	public double[] requirementsCost;
	/**
	 * Stores The Set of solution/individuals
	 */
	private int[][] population;
	/**
	 * Stores the number of solutions/individuals
	 */
	private int numberOfIndividuals;
	/**
	 * Stores fitness values of all solutions in current population
	 */
	private double[] fitnessValues;
	/**
	 * Stores the number of Generations
	 */
	private int numberOfGenerations;
	/**
	 * Stores the number of Subjective Evaluations.
	 */
	private int nSubjectiveEvaluations;
	/**
	 * Crossover Prabability
	 */
	private double crossoverProbability;
	/**
	 * Mutation Probabiblity
	 */
	private double mutationProbability;
	/**
	 * Elitism Rate
	 */
	private double elitismRate;
	/**
	 * Budget used by the next release
	 */
	private double budget;
	/**
	 * Random Object used to generate random numbers
	 */
	private Random random;
	/**
	 * HashMap containing results and solution
	 */
	private HashMap<String, String> results;
	/**
	 * Used to set the max number of subjective evaluations can be done given
	 * the values of the parameters
	 */
	private int maxOfEvaluations;
	/**
	 * Object used to simulated a specific profile
	 */
	private HumanSimulator simulator;
	/**
	 * DatSet used to Stores solutions and their respective human evaluations
	 */
	private DataSet dataSet;
	/**
	 * Learning model used to predict
	 */
	private Classifier model;
	/**
	 *
	 */
	private double meanRelativeAbsoluteError;
	/**
	 * Used to counter the number of fitness evaluations
	 * (Only on first step)
	 */
	private int evaluationsCounter;
	/**
	 * Constructor
	 * @param reader
	 * @param model
	 * @param simulator
	 * @throws Exception
	 */
	public InteractiveGeneticAlgorithm(
			InstanceReader reader, String model, HumanSimulator simulator) throws Exception{

		this.simulator = simulator;
		requirementsScore = getRequirementsScore(reader.getCustomersImportance(), reader.getRequirementsImportances());
		this.maxIndividualScore = 1;
		requirementsCost = reader.getRequirementsCosts();
		this.numberOfRequirements = requirementsCost.length;
		this.crossoverProbability = 90;
		this.mutationProbability = 1;
		this.elitismRate = 20;
		this.numberOfGenerations = 200;
		this.numberOfIndividuals = 2 * numberOfRequirements;
		this.nSubjectiveEvaluations = 100;
		double budgetPercentage = 60;
		this.budget = calculateBudget(budgetPercentage);
		this.random = new Random();
		this.model = getClassifier(model);
		this.maxOfEvaluations = numberOfGenerations * numberOfIndividuals;
		simulator.setCostValues(requirementsCost);
		simulator.setScoreValues(requirementsScore);
	}
	/**
	 *
	 * @return the human Simulator
	 */
	public HumanSimulator getSimulator(){
		return this.simulator;
	}
	/**
	 * A Vector containing the parameters alpha and beta
	 * @param parameters
	 */
	public void setParameters(double[] parameters) {
		this.parameters = parameters;
	}
	/**
	 *
	 * @return A Vector containing the parameter alpha and beta
	 */
	public double[] getParameters() {
		return this.parameters;
	}
	/**
	 * Requirement Scores Vector
	 * @param customersImportance
	 * @param requirementsImportance
	 * @return
	 */
	private double[] getRequirementsScore(double[] customersImportance, double[][] requirementsImportance){
		double[] requirementsScore = new double[requirementsImportance[0].length];

		for (int i = 0; i <= customersImportance.length - 1; i++) {
			for (int j = 0; j <= requirementsImportance[0].length - 1; j++) {
				requirementsScore[j] += customersImportance[i] * requirementsImportance[i][j];
			}
		}

		return requirementsScore;
	}
	/**
	 *
	 * @param classifier
	 * @return Classifier Model
	 */
	private Classifier getClassifier(String classifier) {
		Classifier model = null;

		if(classifier == "MLP"){
			model = new MultilayerPerceptron();
		}

		return model;
	}
	/**
	 *
	 * @throws Exception
	 */
	private void buildResults() throws Exception {
		DataSet dataTest = new DataSet(maxOfEvaluations, numberOfRequirements);

		for (int i = 0; i < 50; i++) {
			int[] individual = getRandomIndividual(i);
			int she = simulator.getHumanEvaluation(individual);
			dataTest.insert(individual, she);
		}
	}
	/**
	 *
	 * @return
	 */
	public double getMeanRelativeAbsoluteError() {
		return meanRelativeAbsoluteError;
	}
	/**
	 * Calculate the bugdet given a percentage of the max possible budget,
	 * which is the sum of all requirements
	 * @param budgetPercentage
	 * @return
	 */
	private double calculateBudget(double budgetPercentage){
		double maxCost = 0;

		for(int i = 0; i <= requirementsCost.length - 1; i++){
			maxCost += requirementsCost[i];
		}

		return (budgetPercentage / 100) * maxCost;
	}
	/**
	 * Public Solve Method
	 * @return
	 * @throws Exception
	 */
	public HashMap<String, String> solve() throws Exception {
		return solve(true);
	}
	/**
	 * The private solve method can be used to get the max possible score returned by the Non-Interactive
	 * Approach. That's done because the need of normalizing the score and subjective evaluation in
	 * the fitness calculating
	 * @param getMaxScore
	 * @return
	 * @throws Exception
	 */
	private HashMap<String, String> solve(boolean getMaxScore) throws Exception{

		this.dataSet = new DataSet(maxOfEvaluations, numberOfRequirements);
		results = new HashMap<String, String>();
		Long p = System.currentTimeMillis();

		if(getMaxScore) {
			double[] backupParameters = getParameters();
			double[] temporaryParameters = {1,0}; //Only Score
			setParameters(temporaryParameters);
			maxIndividualScore = 1;
			HashMap<String, String> temporaryResults = solve(false);
			setParameters(backupParameters);
			maxIndividualScore = Double.parseDouble(temporaryResults.get("bestIndividualScore"));
		}

		int parent1 = 0;
		int parent2 = 0;
		int[] bestIndividual = null;
		int numberOfEliteIndividuals = 0;

		createInitialPopulation();
		repair(population);

		fitnessValues = new double[population.length];
		int[][] children = new int[population.length][population[0].length];

		double aux = elitismRate; //Backup the elitismRate
		elitismRate = 0; // Set elitismRate 0


		//Interactive Approach
		for(int j = 0; j < numberOfGenerations; j++){
			calculateFitnessPopulation();

			numberOfEliteIndividuals = getNumberOfEliteIndividuals();
			cloneEliteIndividuals(children, numberOfEliteIndividuals);

			for(int k = numberOfEliteIndividuals; k <= population.length - 1; k = k + 2){
				parent1 = tournamentSelection();
				parent2 = tournamentSelection();

				crossover(parent1, parent2, children, k);

				mutate(children[k]);
				mutate(children[k + 1]);
			}

			repair(children);
			cloneChildrenToPopulation(children);
		}
		bestIndividual = getBestIndividual();
		buildResults();
		results.put("bestIndividual", getStringIndividual(bestIndividual));
		results.put("bestIndividualScore", getIndividualScore(bestIndividual) + "");
		results.put("meanRelativeAbsoluteError", getMeanRelativeAbsoluteError()+"");
		p = System.currentTimeMillis() - p;
		System.out.println(" Time: "+p);
		return results;
	}

	/**
	 * Method Used to create the initial Population
	 */
	private void createInitialPopulation(){
		population = new int[numberOfIndividuals][numberOfRequirements];

		for(int i = 0; i <= population.length - 1; i++){
			population[i] = getRandomIndividual(i);
		}

		if (numberOfIndividuals >= numberOfRequirements) {
			for (int i = 0; i < numberOfIndividuals; i++) {
				int position = getPositionByIndividualIndex(i);
				population[i][position] = 1;
			}
		} else {
			for (int i = 0; i < numberOfRequirements; i++) {
				int position = getPositionByRequirementIndex(i);
				population[position][i] = 1;
			}
		}
	}
	/**
	 *
	 * @param individualIndex
	 * @return
	 */
	private int getPositionByIndividualIndex(int individualIndex) {
		return individualIndex % numberOfRequirements;
	}

	private int getPositionByRequirementIndex(int requirementIndex) {
		return requirementIndex % numberOfIndividuals;
	}
	/**
	 * Solution Maker Method
	 * @param requirementToBeIncluded
	 * @return
	 */
	private int[] getRandomIndividual(int requirementToBeIncluded){
		int numberOfRequirements = requirementsScore.length;
		int[] randomIndividual = new int[numberOfRequirements];
		int numberOfRequirementsToBeIncluded = random.nextInt(numberOfRequirements) + 1;
		int randomRequirement = 0;

		for(int i = 0; i <= numberOfRequirementsToBeIncluded - 1; i++){
			randomRequirement = random.nextInt(numberOfRequirements);

			while(randomIndividual[randomRequirement] == 1){
				randomRequirement = random.nextInt(numberOfRequirements);
			}

			randomIndividual[randomRequirement] = 1;
		}

		return randomIndividual;
	}
	/**
	 * Calculates Fitness Of all solutions in population vector
	 * @throws Exception
	 */
	private  void calculateFitnessPopulation() throws Exception{
		int she;
		//Non-Interactive Evaluation
		if (parameters[1] == 0) {

			for(int i = 0; i <= population.length - 1; i++){
				she = 0;
				fitnessValues[i] = calculateFitness(population[i], she);
			}

		}
		else if (evaluationsCounter < nSubjectiveEvaluations) { //For Feedback Generation
			int[] populationIndices = suffleIndices(numberOfIndividuals);
			int i = 0;
			for (; evaluationsCounter < nSubjectiveEvaluations && i < numberOfIndividuals; i++) {
				evaluationsCounter++;
				int index = populationIndices[i];
				she = simulator.getHumanEvaluation(population[index]);
				dataSet.insert(population[index], she);
				fitnessValues[index] = calculateFitness(population[index], she);
			}

			if(evaluationsCounter == nSubjectiveEvaluations) {
				model.buildClassifier(dataSet.getDataSet());
				dataSet = new DataSet(maxOfEvaluations, numberOfRequirements);
			}

			for(; i < population.length; i++){
				int index = populationIndices[i];
				she = (int) model.classifyInstance(dataSet.getInstance(population[index]));
				fitnessValues[index] = calculateFitness(population[index], she);
			}

		} else {

			for(int j = 0; j <= population.length - 1; j++){
				she = (int) model.classifyInstance(dataSet.getInstance(population[j]));
				fitnessValues[j] = calculateFitness(population[j], she);
			}

		}
		//she = simulator.getHumanEvaluation(individual);
	}
	/**
	 * Create a vector with distinct numbers from 0 to parameter given shuffled
	 * @param numberOfIndividuals
	 * @return
	 */
	private int[] suffleIndices(int numberOfIndividuals) {
		int[] indices = new int[numberOfIndividuals];

		for (int i = 0; i < indices.length; i++) {
			indices[i] = i;
		}

		for (int i = 0; i < indices.length; i++) {
			int index2 = random.nextInt(numberOfIndividuals);;
			int aux;

			//swap
			aux = indices[i];
			indices[i] = indices[index2];
			indices[index2] = aux;

		}
		return indices;
	}
	/**
	 * Calculates Individual Fitness
	 * @param individual
	 * @param she
	 * @return
	 * @throws Exception
	 */
	private double calculateFitness(int[] individual, int she) throws Exception{
		return (parameters[0] * getNormalizedIndividualScore(individual)) + (parameters[1] * she);
	}
	/**
	 * Normalizes score
	 * @param individual
	 * @return
	 */
	private double getNormalizedIndividualScore(int[] individual){
		return (getIndividualScore(individual)/maxIndividualScore) * simulator.getMaxHumanEvaluation();
	}
	/**
	 * Calculate Individual Score
	 * @param individual
	 * @return
	 */
	private double getIndividualScore(int[] individual){
		double individualScore = 0;

		for(int i = 0; i <= individual.length - 1; i++){
			individualScore += individual[i] * requirementsScore[i];
		}

		return individualScore;
	}
	/**
	 * Get Number Of Elite individuals/solutions
	 * @return
	 */
	private int getNumberOfEliteIndividuals(){
		int numberOfEliteIndividuals = 0;

		numberOfEliteIndividuals = (int) (population.length * (elitismRate / 100.0));
		if(numberOfEliteIndividuals % 2 != 0){
			numberOfEliteIndividuals += 1;
		}

		return numberOfEliteIndividuals;
	}
	/**
	 * Clone Offspring to current population
	 * @param children
	 * @param numberOfEliteIndividuals
	 */
	private void cloneEliteIndividuals (int[][] children, int numberOfEliteIndividuals) {
		if(numberOfEliteIndividuals > 0){
			int[][] eliteIndividuals = getEliteIndividuals(numberOfEliteIndividuals);

			for(int i = 0; i <= numberOfEliteIndividuals - 1; i++){
				for(int j = 0; j <= population[0].length - 1; j++){
					children[i][j] = eliteIndividuals[i][j];
				}
			}
		}
	}
	/**
	 * Return The Best individual given the parameter
	 * @param numberOfEliteIndividuals
	 * @return
	 */
	private int[][] getEliteIndividuals(int numberOfEliteIndividuals){
		int[][] eliteIndividuals = new int[numberOfEliteIndividuals][population[0].length];
		int[] eliteFlag = new int[population.length];
		double bestFitnessValue;
		int eliteIndividualIndex;

		for(int i = 0; i <= eliteIndividuals.length - 1; i++){
			bestFitnessValue = 0;
			eliteIndividualIndex = 0;

			for(int j = 0; j <= fitnessValues.length - 1; j++){
				if(fitnessValues[j] > bestFitnessValue && eliteFlag[j] == 0){
					bestFitnessValue = fitnessValues[j];
					eliteIndividualIndex = j;
				}
			}

			for(int j = 0; j <= population[0].length - 1; j++){
				eliteIndividuals[i][j] = population[eliteIndividualIndex][j];
			}

			eliteFlag[eliteIndividualIndex] = 1;
		}

		return eliteIndividuals;
	}
	/**
	 * Binary Tournament Selection Method
	 * @return A solution or an individual
	 */
	private int tournamentSelection(){
		int parent = 0;
		int contestant1 = random.nextInt(population.length);
		int contestant2 = random.nextInt(population.length);

		if(fitnessValues[contestant1] >= fitnessValues[contestant2]){
			parent = contestant1;
		}
		else{
			parent = contestant2;
		}

		return parent;
	}
	/**
	 * Single Point Crossover
	 * @param indexParent1
	 * @param indexParent2
	 * @param children
	 * @param childrenPosition
	 */
	private void crossover(int indexParent1, int indexParent2, int[][] children, int childrenPosition){
		int[] parent1 = population[indexParent1];
		int[] parent2 = population[indexParent2];
		int pointCut = 0;

		if (random.nextDouble() <= (crossoverProbability / 100)) {
			pointCut = random.nextInt(parent1.length);

			for(int i = 0; i <= pointCut; i++){
				children[childrenPosition][i] = parent1[i];
				children[childrenPosition + 1][i] = parent2[i];
			}

			for(int i = pointCut + 1; i <= parent1.length - 1; i++){
				children[childrenPosition][i] = parent2[i];
				children[childrenPosition + 1][i] = parent1[i];
			}

		}
		else{
			for(int i = 0; i <= parent1.length - 1; i++){
				children[childrenPosition][i] = parent1[i];
				children[childrenPosition + 1][i] = parent2[i];
			}
		}
	}
	/**
	 * Repair Method
	 * @param population
	 */
	private void repair(int[][] population){
		double[] individualsCosts = getIndividualsCosts(population);

		for(int i = 0; i <= population.length - 1; i++){
			if(individualsCosts[i] > budget){
				repair(population[i], individualsCosts[i]);
			}
		}
	}
	/**
	 *
	 * @param population
	 * @return
	 */
	private double[] getIndividualsCosts(int[][] population){
		double[] individualCosts = new double[population.length];

		for(int i = 0; i <= individualCosts.length - 1; i++){
			individualCosts[i] = calculateCost(population[i]);
		}

		return individualCosts;
	}
	/**
	 *
	 * @param individual
	 * @return
	 */
	private double calculateCost(int[] individual){
		double cost = 0;

		for(int i = 0; i <= individual.length - 1; i++){
			cost += individual[i] * requirementsCost[i];
		}

		return cost;
	}
	/**
	 * Repair Method
	 * @param individual
	 * @param individualCost
	 */
	private void repair(int[] individual, double individualCost){
		int randomRequirement = 0;

		while(individualCost > budget){
			randomRequirement = selectRandomRequirement(individual);
			individual[randomRequirement] = 0;
			individualCost -= requirementsCost[randomRequirement];
		}
	}
	/**
	 * Return a random requirement included in solution
	 * @param individual
	 * @return
	 */
	private int selectRandomRequirement(int[] individual){
		int randomRequirement = random.nextInt(individual.length);

		while(individual[randomRequirement] == 0){
			randomRequirement = random.nextInt(individual.length);
		}

		return randomRequirement;
	}
	/**
	 * Bit wise mutation
	 * @param individual
	 */
	private void mutate(int[] individual){
		double mutationRate = mutationProbability / 100;

		for(int i = 0; i <= individual.length - 1; i++){
			if(random.nextDouble() <= mutationRate){
				if(individual[i] == 0){
					individual[i] = 1;
				}
				else{
					individual[i] = 0;
				}
			}
		}
	}
	/**
	 * Clone Offspring to population
	 * @param children
	 */
	private void cloneChildrenToPopulation(int[][] children){
		for(int i = 0; i <= population.length - 1; i++){
			for(int j = 0; j <= population[0].length - 1; j++){
				population[i][j] = children[i][j];
			}
		}
	}
	/**
	 * Return best solution/individual
	 * @return
	 * @throws Exception
	 */
	private int[] getBestIndividual() throws Exception{
		int[] bestIndividual = null;
		double bestFitness = -Double.MAX_VALUE;
		double individualFitness = 0;

		for(int i = 0; i <= population.length - 1; i++){

			if (parameters[1] == 0) {
				individualFitness = calculateFitness(population[i], 0);
			}
			else {
				individualFitness = calculateFitness(population[i],
					(int) model.classifyInstance(dataSet.getInstance(population[i])));
			}

			if(individualFitness >= bestFitness){
				bestFitness = individualFitness;
				bestIndividual = population[i];
			}
		}
		return bestIndividual;
	}
	/**
	 * Return the solutions as a string
	 * @param individual
	 * @return
	 */
	private String getStringIndividual(int[] individual){
		String stringIndividual = "";

		for(int i = 0; i <= individual.length - 1; i++){
			if(i != individual.length - 1){
				stringIndividual += individual[i] + " ";
			}
			else{
				stringIndividual += individual[i];
			}
		}

		return stringIndividual;
	}
}
