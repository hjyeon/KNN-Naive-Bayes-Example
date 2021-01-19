import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * This class implements KNN. 
 * @author Hyejin Jenny Yeon
 *s
 */
public class KNN {
	
	private int k;
	private List<ArrayList<Double>> trainingData;
	private List<ArrayList<Double>> testData;
	private int numfeatures;
	
	public KNN(String filePath) throws FileNotFoundException, IOException {
		k=1;
		this.trainingData = DataParser.parseTrainingRecords(filePath);	
		this.numfeatures = this.trainingData.get(0).size()-1;
	}
	
	/**
	 * Setter method for value of k. 
	 * @param k
	 */
	public void changeK (int k) {
		this.k = k;
	}
	
	/**
	 * Finds label according to KNN
	 * @param testData
	 * @return
	 */
	private int findLabel(ArrayList<Double> testData) {
		//System.out.println(this.trainingData.size());
		ArrayList<Double[]> distances = new ArrayList<Double[]>();
		for (ArrayList<Double> list : this.trainingData) {
			double distance = 0;
			for (int i = 0 ; i < this.numfeatures ; i++) {
				distance += Math.abs(list.get(i) - testData.get(i));
			}
			distances.add(new Double[] {distance,list.get(numfeatures)});
		}
		
		Collections.sort(distances, new sortDistance());
		double average = 0.0;
		for (int i = 0 ; i < k ; i ++) {
			average += distances.get(i)[1];
		}
		average = average/k;

		if (average <= 0.5) return 0;		
		return 1;
	}
	
	private ArrayList<Integer> findLabel(List<ArrayList<Double>> testData) {
		ArrayList<Integer> labels = new ArrayList<Integer>();
		for (ArrayList<Double> data : testData) {
			labels.add(this.findLabel(data));
		}
		return labels;		
	}
	
	/**
	 * This method can perform cross validation.
	 * @param fold
	 * @param filePath
	 * @return list of labels
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public ArrayList<Integer> crossValidation(int fold, String filePath) throws FileNotFoundException, IOException {
		this.trainingData = DataParser.parseTrainingRecords(filePath);
		this.testData = DataParser.parseTestRecords(filePath);
		//System.out.println("Training Data Changed to the given file");
		int sizeOfSet = this.trainingData.size()/fold;
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (int i = 0 ; i < fold ; i ++) {
			List<ArrayList<Double>> trainingSubset = new ArrayList<ArrayList<Double>>();
			List<ArrayList<Double>> testSubset = new ArrayList<ArrayList<Double>>();
			if (i == (fold - 1)) {
				trainingSubset = this.trainingData.subList(0, sizeOfSet*i);
				this.trainingData = trainingSubset;
				testSubset = this.testData.subList(sizeOfSet*i, this.testData.size());
			}
			else {			
				for (int j = 0 ; j < sizeOfSet ; j++) {
					trainingData.remove(sizeOfSet*i);
				}
				testSubset = this.testData.subList(sizeOfSet*i, sizeOfSet*(i+1));
			}
			ArrayList<Integer> partialResults = this.findLabel(testSubset);
			for (Integer r : partialResults) {
				results.add(r);
			}
			this.trainingData = DataParser.parseTrainingRecords(filePath);

		}

		return results;		
	}
	
	/**
	 * This both performs and prints results of cross-validation 
	 * @param fold
	 * @param originalFilePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void printCVAccuracy(int fold, String originalFilePath) throws FileNotFoundException, IOException {
		ArrayList<Integer> result  = this.crossValidation(fold, originalFilePath);
		int count = 0;
		int dataSize = this.trainingData.size();
		for (int i = 0 ; i < dataSize ; i++) {
			if (this.trainingData.get(i).get(this.numfeatures) == (double) (result.get(i))) count++;
		}
		System.out.println(count + " correct out of " + dataSize);
	}
	
	public void testSingleData(ArrayList<Double> testData) {
		System.out.println("The result is " + this.findLabel(testData));		
	}
	
	
	/**
	 * This is a comparator for sorting the distance
	 * @author yun91
	 *
	 */
	class sortDistance implements Comparator<Double[]>{
		@Override
		public int compare(Double[] o1, Double[] o2) {
			return Double.compare(o1[0], o2[0]);
		}
	}
	
}
