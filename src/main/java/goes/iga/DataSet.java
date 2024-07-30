package goes.iga;

import weka.core.*;

/**
 *
 * This class is a platform that stores the individuals and their respective human evaluations.
 * Thus, a dataSet (Type Instances) is available for training the classifiers in WEKA API.
 *
 * @since 03-31-2015
 * @author --
 *
 */
public class DataSet {

	public Instances dataSet;
	FastVector featureVector;
	int numberOfRequirements;
	/**
	 *
	 * @param maxOfEvaluations
	 * @param numberOfRequirements
	 */
	public DataSet(int maxOfEvaluations, int numberOfRequirements) {

		this.numberOfRequirements = numberOfRequirements;
		Attribute[] labelRequirements = new Attribute[numberOfRequirements];

		for (int i = 0; i < numberOfRequirements; i++) {
			labelRequirements[i] = new Attribute("r"+(i+1));
		}

		Attribute classifier = new Attribute("evaluation");

		featureVector = new FastVector(numberOfRequirements+1);
		for (int i = 0; i < numberOfRequirements; i++) {
			featureVector.addElement(labelRequirements[i]);
		}

		featureVector.addElement(classifier);

		dataSet = new Instances ("trainingSet", featureVector, maxOfEvaluations);
		dataSet.setClassIndex(numberOfRequirements);
	}
	/**
	 * Method used to store a solution with its respective subjective evaluation
	 * @param individual
	 * @param she
	 */
	public void insert(int[] individual, int she) {
		Instance aux = new DenseInstance(numberOfRequirements+1);
		for (int i = 0; i < individual.length; i++) {
			aux.setValue((Attribute)featureVector.elementAt(i), individual[i]);
		}
		aux.setValue((Attribute)featureVector.elementAt(numberOfRequirements), she);
		dataSet.add(aux);
	}
	/**
	 *
	 * @return A dataset used by Classifiers in WEKA
	 */
	public Instances getDataSet() {
		return dataSet;
	}
	/**
	 *
	 * @param individual
	 * @return Convert a given solution in instance
	 */
	public Instance getInstance(int [] individual) {
		Instance instance = new DenseInstance(numberOfRequirements+1);
		for (int i = 0; i < individual.length; i++) {
			instance.setValue((Attribute)featureVector.elementAt(i), individual[i]);
		}
		return instance;
	}
}
