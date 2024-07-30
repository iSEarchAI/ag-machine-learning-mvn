package goes.iga;
import java.util.ArrayList;
import java.util.HashMap;

public class ResultsWriter {
	/**
	 * Stores a set Of results given by the evaluations
	 */
	ArrayList <HashMap<String, String>> listOfResults;
	/**
	 *
	 */
	HumanSimulator simulator;
	/**
	 *
	 * @param listOfResults
	 * @param simulator
	 */
	public void setResults(ArrayList <HashMap<String, String>> listOfResults, HumanSimulator simulator) {
		this.listOfResults = listOfResults;
		this.simulator = simulator;
	}
	/**
	 * Write results
	 */
	public void writeResults(){
		System.out.println("Number of executions: " + listOfResults.size());
		System.out.println("Average of Similarities: " + getSimilaritiesStatistics());
		System.out.println("Score: " + getIndividualsAverageScore() + " +/- " + getStandardDeviation(true));
		System.out.println("Mean Relative Absolute Error: "+ getAverageMeanRelativeAbsoluteError()+"%");
	}
	/**
	 *
	 * @return Average Mean Relative Absolute Error
	 */
	private double getAverageMeanRelativeAbsoluteError() {
		double averageError = 0;
		for (int i = 0; i < listOfResults.size(); i++) {
			averageError += Double.parseDouble(listOfResults.get(i).get("meanRelativeAbsoluteError"));
		}
		return averageError/listOfResults.size();
	}


	/**
	 *
	 * @return Mean Number Of Similarities of All solutions in List of Results
	 */
	public double getSimilaritiesStatistics(){
		HashMap<String, String> results = null;
		double meanNumberOfSimilaritiesInIndividual = 0;

		for(int i = 0; i <= listOfResults.size() - 1; i++){
			results = listOfResults.get(i);
			meanNumberOfSimilaritiesInIndividual += simulator.getNumberOfSimilaritiesInIndividual(getIndividual(results.get("bestIndividual")));
		}
		meanNumberOfSimilaritiesInIndividual = (meanNumberOfSimilaritiesInIndividual/listOfResults.size())/simulator.getNumberOfRequirements();

		return meanNumberOfSimilaritiesInIndividual;
	}
	/**
	 *
	 * @return The respective similarity of each solution in list of results
	 */
	public double[] getIndividualsSimilarities(){
		HashMap<String, String> results = null;
		double[] individualSimilarities = new double[listOfResults.size()];

		for(int i = 0; i <= listOfResults.size() - 1; i++){
			results = listOfResults.get(i);
			individualSimilarities[i] =	(double) simulator.getNumberOfSimilaritiesInIndividual(getIndividual(results.get("bestIndividual")))/simulator.getNumberOfRequirements();
		}

		return individualSimilarities;
	}
	/**
	 * Convert a string individual in a vector of integers
	 * @param stringIndividual
	 * @return
	 */
	private int[] getIndividual(String stringIndividual){
		String[] values = stringIndividual.split(" ");
		int[] individual = new int[values.length];

		for(int i = 0; i <= individual.length - 1; i++){
			individual[i] = Integer.parseInt(values[i]);
		}

		return individual;
	}
	/**
	 *
	 * @return The Individual Average Score in list of results
	 */
	public double getIndividualsAverageScore(){
		double[] individualsScores = getIndividualsScores();
		double scoresSum = 0;

		for(int i = 0; i <= individualsScores.length - 1; i++){
			scoresSum += individualsScores[i];
		}
		scoresSum = scoresSum / individualsScores.length;

		return scoresSum;
	}
	/**
	 *
	 * @return A vector containing scores of all evaluations
	 */
	private double[] getIndividualsScores(){
		double[] individualsScores = new double[listOfResults.size()];

		for(int i = 0; i <= individualsScores.length - 1; i++){
			individualsScores[i] = Double.parseDouble(listOfResults.get(i).get("bestIndividualScore"));
		}

		return individualsScores;
	}


	/**
	 *
	 * @param measure (If true return the standard deviation of the scores, else return the one of the Similarities)
	 * @return The standard Deviation
	 */
	public double getStandardDeviation(boolean measure) {
		double standardDeviation;

		if(measure) { // Score
			standardDeviation = getStandardDeviation(getIndividualsAverageScore(), getIndividualsScores());
		}else { // Similarity
			standardDeviation = getStandardDeviation(getSimilaritiesStatistics(), getIndividualsSimilarities());
		}

		return standardDeviation;
	}
	/**
	 * Given a set of results this function return the standard Deviation.
	 * @param average
	 * @param values
	 * @return
	 */
	private double getStandardDeviation(double average, double[] values){
	      double[] deviances = getDeviances(average, values);
	      double variance = getVariance(deviances);

	      return Math.sqrt(variance);
	}

	/**
	 *
	 * @param mean
	 * @param values
	 * @return deviances of the fiven numerical vector
	 */
	private double[] getDeviances(double mean, double[] values){
		double[] deviances = new double[values.length];

		for(int i = 0; i <= deviances.length - 1; i++){
			deviances[i] = mean - values[i];
		}

		return deviances;
	}
	/**
	 *
	 * @param deviances
	 * @return Variances
	 */
	private double getVariance(double[] deviances){
		double quadraticDeviancesSum = 0;

		for(int i = 0; i <= deviances.length - 1; i++){
			quadraticDeviancesSum += Math.pow(deviances[i], 2);
		}

		return quadraticDeviancesSum / deviances.length;
	}
}
