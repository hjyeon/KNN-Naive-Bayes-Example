import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.common.math.Stats;

/**
 * This class implements Naive Bayes. 
 * @author Hyejin Jenny Yeon
 *
 */
public class NaiveBayes {
	private List<ArrayList<Double>> trainingData;
	private List<ArrayList<Double>> survivedData;
	int sizeOfSD; 
	private List<ArrayList<Double>> diedData;
	int sizeOfDD;
	private List<ArrayList<Double>> testData;
	private double psurvived;
	private double pdied;
	// conditionals, index 0 for died, index 1 for survived 
	private List<double[]> mfProb; 
	private List<double[]> classProb;
	private List<double[]> ssaProb;
	private List<double[]> pcaProb;
	// Mean and variance for age and price 
	// Index 0 for died, index 1 for survived 
	private List<double[]> ageStat;
	private List<double[]> fareStat;
	private int numfeatures;
	
	/**
	 * Constructor
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public NaiveBayes(String filePath) throws FileNotFoundException, IOException {
		this.trainingData = DataParser.parseTrainingRecordsforNB(filePath);
		this.testData = DataParser.parseTestRecordsforNB(filePath);
		this.numfeatures = this.trainingData.get(0).size()-1;		
		this.updateTrainingData(this.trainingData);
	}
	
	/**
	 * Calculates various probabilities and statistical information of
	 * the given training data set. 
	 * @param newData
	 */
	private void updateTrainingData(List<ArrayList<Double>> newData) {
		this.trainingData = newData;
		survivedData = new ArrayList<ArrayList<Double>>();
		diedData = new ArrayList<ArrayList<Double>>();
		mfProb = new ArrayList<double[]>(); 
		classProb = new ArrayList<double[]>(); 
		ssaProb = new ArrayList<double[]>(); 
		pcaProb = new ArrayList<double[]>(); 
		ageStat = new ArrayList<double[]>(); 
		fareStat = new ArrayList<double[]>(); 
		for (ArrayList<Double> data : this.trainingData) {
			if (data.get(numfeatures) == 0.0) {
				this.diedData.add(data);
			}
			else this.survivedData.add(data);
		}
		this.sizeOfSD = this.survivedData.size();
		this.sizeOfDD = this.diedData.size();

		ArrayList<Double> labels = new ArrayList<Double>();
		for(ArrayList<Double> list : this.trainingData) {
			labels.add(list.get(this.numfeatures));
		}
		this.psurvived = Stats.meanOf(labels);
		this.pdied = 1 - this.psurvived;
		// Conditional Probabilities for female/male
		ArrayList<Double> mf = new ArrayList<Double>();
		for(ArrayList<Double> list : this.survivedData) {
			mf.add(list.get(1));
		}
		this.mfProb.add(new double[] {Stats.meanOf(mf),1-Stats.meanOf(mf)});
		mf = new ArrayList<Double>();
		for(ArrayList<Double> list : this.diedData) {
			mf.add(list.get(1));
		}
		double mfmean = Stats.meanOf(mf);
		this.mfProb.add(new double[] {mfmean,1-mfmean});
				
		// Conditional Prob for Pclass
		double[] pclassCounts = new double[3];
		for(ArrayList<Double> list : this.survivedData) {
			if (list.get(0)==1) pclassCounts[0]++;
			else if (list.get(0)==2) pclassCounts[1]++;
			else pclassCounts[2]++;
		}
		pclassCounts[0] = pclassCounts[0]/this.sizeOfSD;
		pclassCounts[1] = pclassCounts[1]/this.sizeOfSD;
		pclassCounts[2] = pclassCounts[2]/this.sizeOfSD;
		this.classProb.add(pclassCounts);
		double[] pclassCounts2 = new double[3];
		for(ArrayList<Double> list : this.diedData) {
			if (list.get(0)==1) pclassCounts2[0]++;
			else if (list.get(0)==2) pclassCounts2[1]++;
			else pclassCounts2[2]++;
		}
		pclassCounts2[0] = pclassCounts2[0]/this.sizeOfDD;
		pclassCounts2[1] = pclassCounts2[1]/this.sizeOfDD;
		pclassCounts2[2] = pclassCounts2[2]/this.sizeOfDD;
		this.classProb.add(pclassCounts2);
		
		// Conditional Prob for Siblings/Spouses Aboard
		double[] ssaCounts = new double[9];
		for(ArrayList<Double> list : this.survivedData) {
				ssaCounts[(int)(list.get(3)*1)]++;
			
		}
		for(int d = 0; d < 9 ; d++) ssaCounts[d] = ssaCounts[d]/this.sizeOfSD;
		this.ssaProb.add(ssaCounts);
		double[] ssaCounts2 = new double[9];
		for(ArrayList<Double> list : this.diedData) {
			ssaCounts2[(int)(list.get(3)*1)]++;

		}
		for(int d = 0; d < 8 ; d++) ssaCounts2[d] = ssaCounts2[d]/this.sizeOfDD;
		this.ssaProb.add(ssaCounts2);
		
		// Conditional Prob for Parents/Children Aboard
		double[] ppaCounts = new double[7];
		for(ArrayList<Double> list : this.survivedData) {
			ppaCounts[(int)(list.get(4)*1)]++;
		}
		for(int d = 0; d < 7 ; d++) ppaCounts[d] = ppaCounts[d]/this.sizeOfSD;
		this.pcaProb.add(ppaCounts);
		double[] ppaCounts2 = new double[9];
		for(ArrayList<Double> list : this.diedData) {
			ppaCounts2[(int)(list.get(4)*1)]++;
		}
		for(int d = 0; d < 7 ; d++) ppaCounts2[d] = ppaCounts2[d]/this.sizeOfDD;
		this.pcaProb.add(ppaCounts2);
		
		// Stat for age
		ArrayList<Double> ages = new ArrayList<Double>();
		for(ArrayList<Double> list : this.survivedData) {
			ages.add(list.get(2));
		}
		Stats age = Stats.of(ages);
		this.ageStat.add(new double[] {Stats.meanOf(ages),age.sampleVariance()});
		
		ArrayList<Double> ages2 = new ArrayList<Double>();
		for(ArrayList<Double> list : this.diedData) {
			ages2.add(list.get(2));
		}
		Stats age2 = Stats.of(ages2);
		this.ageStat.add(new double[] {Stats.meanOf(ages2),age2.sampleVariance()});
		
		// Stat for fare
		ArrayList<Double> fares = new ArrayList<Double>();
		for(ArrayList<Double> list : this.survivedData) {
			fares.add(list.get(5));
		}
		Stats fare = Stats.of(fares);
		this.fareStat.add(new double[] {Stats.meanOf(fares),fare.sampleVariance()});
		
		ArrayList<Double> fares2 = new ArrayList<Double>();
		for(ArrayList<Double> list : this.diedData) {
			fares2.add(list.get(5));
		}
		Stats fare2 = Stats.of(fares2);
		this.fareStat.add(new double[] {Stats.meanOf(fares2),fare2.sampleVariance()});
		
		
	}
	
	/**
	 * Finds label according to NB
	 * @param testData
	 * @return
	 */
	public int findLabel(ArrayList<Double> testData) {
		double sp = this.psurvived; // P(survived)
		double dp = this.pdied; // P(died)

		for (int i = 0 ; i < this.numfeatures ; i ++) {
			if (i==0) {
				sp = sp*this.pcaProb.get(0)[(int) (testData.get(0)-1)];
				dp = dp*this.pcaProb.get(1)[(int) (testData.get(0)-1)];

			}
			else if (i== 1) {
				if (testData.get(1) == 1) {
					sp = sp*this.mfProb.get(0)[0];
					dp = dp*this.mfProb.get(1)[0];
				}
				else {
					sp = sp*this.mfProb.get(0)[1];
					dp = dp*this.mfProb.get(1)[1];
				}

			}
			else if (i==3) { //ssa
				sp = sp*(this.ssaProb.get(0)[(int)(testData.get(3)*1)]);
				dp = dp*this.ssaProb.get(1)[(int)(testData.get(3)*1)];


			}
			else if(i==4) { //pca
				sp = sp*this.pcaProb.get(0)[(int)(testData.get(4)*1)];
				dp = dp*this.pcaProb.get(1)[(int)(testData.get(4)*1)];
				
			}

		}

		sp = sp*this.gussianDistribution(this.ageStat.get(0)[0],this.ageStat.get(0)[1],testData.get(2));
		dp = dp*this.gussianDistribution(this.ageStat.get(1)[0],this.ageStat.get(1)[1],testData.get(2));
		sp = sp*this.exponentialDistribution(this.fareStat.get(0)[0],testData.get(5));
		dp = dp*this.exponentialDistribution(this.fareStat.get(1)[0],testData.get(5));
		if(sp > dp) return 1;
		else return 0;
	}
	
	private ArrayList<Integer> findLabel(List<ArrayList<Double>> testData) {
		ArrayList<Integer> labels = new ArrayList<Integer>();
		for (ArrayList<Double> data : testData) {
			labels.add(this.findLabel(data));
		}
		return labels;		
	}
	
	
	public double gussianDistribution(double mean, double variance, double value) {
		return (1/Math.sqrt(variance*2*Math.PI))
				*Math.exp(-0.5*(value-mean)*(value-mean)*(1/variance));
	}
	
	public double exponentialDistribution(double mean, double value) {
		return (1/mean)*Math.exp(-(1/mean)*value);
	}
	
	public void testSingleData(ArrayList<Double> testData) {
		System.out.println("The result is " + this.findLabel(testData));		
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
		this.trainingData = DataParser.parseTrainingRecordsforNB(filePath);
		this.updateTrainingData(this.trainingData);
		this.testData = DataParser.parseTestRecordsforNB(filePath);
		System.out.println("Training Data Changed to the given file");
		int sizeOfSet = this.trainingData.size()/fold;
		ArrayList<Integer> results = new ArrayList<Integer>();
		for (int i = 0 ; i < fold ; i ++) {
			List<ArrayList<Double>> trainingSubset = new ArrayList<ArrayList<Double>>();
			List<ArrayList<Double>> testSubset = new ArrayList<ArrayList<Double>>();
			if (i == (fold - 1)) {
				trainingSubset = this.trainingData.subList(0, sizeOfSet*i);
				this.trainingData = trainingSubset;
				this.updateTrainingData(this.trainingData);
				testSubset = this.testData.subList(sizeOfSet*i, this.testData.size());
			}
			else {			
				for (int j = 0 ; j < sizeOfSet ; j++) {
					trainingData.remove(sizeOfSet*i);
				}
				this.updateTrainingData(this.trainingData);
				testSubset = this.testData.subList(sizeOfSet*i, sizeOfSet*(i+1));
			}
			ArrayList<Integer> partialResults = this.findLabel(testSubset);
			for (Integer r : partialResults) {
				results.add(r);
			}
			this.trainingData = DataParser.parseTrainingRecordsforNB(filePath);
			this.updateTrainingData(this.trainingData);

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
	

}
