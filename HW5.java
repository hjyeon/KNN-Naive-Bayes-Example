import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used to run KNN and NB. 
 * @author Hyejin Jenny Yeon
 *
 */
public class HW5 {
	public static void main (String[] args) throws FileNotFoundException, IOException {
		//List<ArrayList<Double>> test = DataParser.parseTestRecords("titanic_data.csv");
		//List<ArrayList<Double>> test2 = DataParser.parseTrainingRecords("titanic_data.csv");
		//List<ArrayList<Double>> test3 = DataParser.parseTestRecordsforNB("titanic_data.csv");
		//System.out.println(test.get(0));
		//System.out.println(test2.get(0));
		//System.out.println(test3.get(8));
		ArrayList<Double> myData = new ArrayList<>(List.of(3.0/3.0,1.0,28.0/80.0,0.0,0.0,8.05/51.2329));
		ArrayList<Double> myData2 = new ArrayList<>(List.of(3.0,1.0,28.0,0.0,0.0,8.05));
		/*
		KNN kNN = new KNN("titanic_data.csv");
		System.out.println("==============KNN=================");
		for (int k = 0 ; k < 100; k++) {
			kNN.changeK(k);
			System.out.print(k+ " ");
			kNN.testSingleData(myData);
		}
		for (int k = 0 ; k < 100; k++) {
			kNN.changeK(k);
			System.out.print(k+ ", ");
			kNN.printCVAccuracy(10, "titanic_data.csv");
		}
		*/

		System.out.println("==============NB=================");
		NaiveBayes NB = new NaiveBayes("titanic_data.csv");
		NB.testSingleData(myData2);
		NB.printCVAccuracy(11, "titanic_data.csv");
	}

}
