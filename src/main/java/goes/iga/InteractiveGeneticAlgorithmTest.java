package goes.iga;

import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * This class was used to perform the presented tests.
 *
 * @since 03-31-2014
 * @author --
 *
 */

public class InteractiveGeneticAlgorithmTest {

	public static void main(String[] args) throws Exception {
		/*
		 * Used to write the file with results
		 */
		ResultsWriter resultsWriter = new ResultsWriter();
		/*
		 * Stores the results
		 */
		ArrayList<HashMap<String, String>> listOfResults;
		/*
		 * Number of Evaluations Per Test
		 */
		int numberOfExecutions = 30;
		/*
		 * Instance reader
		 */
		InstanceReader reader;
		/*
		 * Evaluating Profile
		 */
		HumanSimulator simulator = new HumanSimulator();
		/*
		 * Solver
		 */
		InteractiveGeneticAlgorithm iga;
		/*
		 * Decimal Format
		 */
		DecimalFormat myFormatter = new DecimalFormat("###.##");


		double[][] parameters = {{1,0}, {0,1}, {1,1}};
		int[] instances = {50/*, 100, 150, 200*/};
		String[] profiles = {"MANUALLY"/*, "RANDOM", "LOWER_SCORE", "HIGHER_COST"*/};

		for (int i = 0; i < instances.length; i++) { // for each instance
			//A String containing score results
			String scoreResults = "";
			//A String Containing similarity results
			String similarityResults = "";
			//Used to store the results
			FileWriter file = new FileWriter(new File("results.data"), true);
			//Instance Reader
			reader = new InstanceReader(new File("instances/I_"+instances[i]+".txt"));
			System.out.println("instances/I_"+instances[i]+".txt");
			//IGA
			iga = new InteractiveGeneticAlgorithm(reader, "MLP", simulator);

			for (int j = 0; j < profiles.length; j++) { // for each profile
				simulator.setHumanSimulatorProfile(profiles[j]);

				for (int k = 0; k < parameters.length; k++) { // for each parameter combination

					iga.setParameters(parameters[k]);
					listOfResults = new ArrayList<HashMap<String,String>>();

					for(int l = 0; l <= numberOfExecutions - 1; l++){ // execute numberOfExecutions times
						System.out.println(l);
						listOfResults.add(iga.solve());
					}

					resultsWriter.setResults(listOfResults, iga.getSimulator());
					scoreResults += myFormatter.format(resultsWriter.getIndividualsAverageScore())+
							"+/-" +myFormatter.format(resultsWriter.getStandardDeviation(true)) +" ";
					similarityResults += myFormatter.format(resultsWriter.getSimilaritiesStatistics()*100)+
							"+/-" +myFormatter.format(resultsWriter.getStandardDeviation(false)*100) +" ";;
				}
			}

			scoreResults += "\n";
			scoreResults = scoreResults.replace(',', '.');
			similarityResults += "\n";
			similarityResults = similarityResults.replace(',','.');

			file.write(similarityResults);
			file.write(scoreResults);
			file.close();
		}

	}
}
