import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * This class contains a static method to parse csv files and 
 * generates an matrix containing all the training data in the file. 
 * Here, a matrix is implemented as a list of double arrays. 
 * Test data format: (any numer, x1, x2, .. , xn) where xi's are values of the feature.
 * @author Hyejin Jenny Yeon
 */
public class DataParser {
	
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * All the features are converted into the number between 0 and 1. 
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Double>> parseTrainingRecords(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Double[] doubleValues = new Double[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i==1) doubleValues[i-1] = Double.parseDouble(stringValues[i])/3.0; 
           		else if (i == 2) doubleValues[i-1] = Double.parseDouble(stringValues[i]); 
           		else if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			doubleValues[2] = age/80;
           		}
           		else {
           			doubleValues[i-1] = Double.parseDouble(stringValues[i])/8;  
           		}   	
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	//fare = fare/10;
           	fare = fare/512.329;
           	doubleValues[colSize-2] = fare;
           	// Copy Labels : 1 = survived 0 = died
       		doubleValues[colSize-1] = Double.parseDouble(stringValues[0]);
       		
           	ArrayList<Double> asArray = new ArrayList<Double>();
           	for (Double i : doubleValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * Continuous features - age, fare - are converted into the range 0-1
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Double>> parseTrainingRecordsforNB(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Double[] doubleValues = new Double[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			//doubleValues[2] = age/80;
           			doubleValues[2] = age;
           		}
           		else {
           			doubleValues[i-1] = Double.parseDouble(stringValues[i]);  
           		}   	
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	//fare = fare/512.329;
           	doubleValues[colSize-2] = fare;
           	// Copy Labels : 1 = survived 0 = died
       		doubleValues[colSize-1] = Double.parseDouble(stringValues[0]);
       		
           	ArrayList<Double> asArray = new ArrayList<Double>();
           	for (Double i : doubleValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * Continuous features - age, fare - are converted into the range 0-1
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Double>> parseTestRecordsforNB(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Double[] doubleValues = new Double[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			doubleValues[2] = age;
           		}
           		else {
           			doubleValues[i-1] = Double.parseDouble(stringValues[i]);  
           		}   	
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	doubleValues[colSize-2] = fare;
       		
           	ArrayList<Double> asArray = new ArrayList<Double>();
           	for (Double i : doubleValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    
    /**
     * This method parses csv file and returns a matrix as ArrayList<double[]>.
     * All the features are converted into the range 0-1. 
     * @param filePath is the path of the file. 
     * @return ArrayList<double[]> a matrix containing all the training data
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<ArrayList<Double>> parseTestRecords(String filePath) throws FileNotFoundException, IOException {
    	List<ArrayList<Double>> data = new ArrayList<ArrayList<Double>>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line = "";
        reader.readLine();
        while ((line = reader.readLine()) != null) {
            String[] stringValues = line.split(",");
            int colSize = stringValues.length;
            Double[] doubleValues = new Double[colSize];
           	for (int i = 1 ; i < colSize-1; i ++) {
           		if (i == 2) doubleValues[i-1] = Double.parseDouble(stringValues[i]);
           		else if (i == 1) {
           			doubleValues[i-1] = Double.parseDouble(stringValues[i])/3.0;
           		}
           		else if (i == 3) {
           			double age = Double.parseDouble(stringValues[3]);
           			doubleValues[2] = age/80;
           		}
           		else {
           			doubleValues[i-1] = Double.parseDouble(stringValues[i])/8;  
           		}   		
            }
           	double fare = Double.parseDouble(stringValues[colSize-1]);
           	/*if (doubleValues[colSize-3]+doubleValues[colSize-4]!=0) {
               	fare = fare/(doubleValues[colSize-3]+doubleValues[colSize-4]);
           	}*/
           	fare = fare/512.329;
           	doubleValues[colSize-2] = fare;       		
           	ArrayList<Double> asArray = new ArrayList<Double>();
           	for (Double i : doubleValues) {
           		asArray.add(i);
           	}

            data.add(asArray);
        }
        reader.close();
    return data;
    }
    

}
