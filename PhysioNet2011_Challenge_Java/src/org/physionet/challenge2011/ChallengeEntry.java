package org.physionet.challenge2011;
import static org.math.array.LinearAlgebra.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import Jama.EigenvalueDecomposition;


public class ChallengeEntry {

	public static final String DEBUGTAG = ChallengeEntry.class.toString();
	final static int FS= 500;					//Sampling Frequency
	final static int CH= 12;
	final static int MAX_RT= 220;				//Max expected beats in minutes
	final static int WIN=FS*10;
	final static double PKS_MIN=40/6;         		// Minimum number of expected peaks in 10s    
	final static int FRGT=(int) (FS*0.04);   		// Window for ignoring consecutive peaks 
	final static short [] W={1,1,1,-1,1,1,-1,-1,1,1,1,1}; // Weights for each channel
	final static double PK_TH=500; 		// Peak value hardcoded based on sample data
	static short [] sum = new short[WIN]; //Array for storing summed channel data
	
	//Define Quality values (could also be defined as enum...)
	final static int INIT=0;
	final static int GOOD = 0;
	final static int BAD =  1;
	short[] data=new short[WIN*CH];
	
	private double[][] ecg_8leads; //Matrix with only independent leads.
	
	synchronized public int get_result(InputStream iFile, final ECG_MetaData m_MetaData) throws IOException {
		ObjectInputStream in = new ObjectInputStream(iFile);
		try {
			data = (short[])in.readObject();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ECG data is in the following format data = [ch1smp1 ch2smp_1 ... ch12smp1 ch1smp2 ch2smp2 ... ch12smp2 ...]
		
		//##### PCA on ECG #####
		double[] eigen = pca_ecg(data);
		//##### Classification rule #####
		int result = Ripper(eigen);   
		
		//clean-up
		iFile.close();	
		return result;

	}

	public double[] pca_ecg(short[] data){
		
		double[] eigen = new double[8];
		//Compute the pca on the ecg data
		int index = 0;
		
		//convert ECG in double[][] using only 8 leads
		ecg_8leads = new double[WIN][8];
		int[] lead = {0,1,6,7,8,9,10,11};
		while ( index < WIN) {
			//Load summed data into array 
			//System.out.println(index + "Win = "+WIN);
			for(int ch=0;ch<lead.length;ch++){
				ecg_8leads[index][ch]=data[index*12 + lead[ch]];
			}		
			index = index + 1;
		}
		
		//Obtain covariance
		//1) obtain the mean of each lead
		double[] meanECG;
		meanECG = mean(ecg_8leads);
		//2) center the matrix
		double[][] Z = center_reduce(ecg_8leads,meanECG);
		//3) Compute the covariance matrix
		double[][] cov = covariance_1(Z);
		//4)eigenvalue decomposition
		
		EigenvalueDecomposition e = eigen(cov);
		eigen = e.getRealEigenvalues();
		return eigen;

		
	} 
	
	public int Ripper(double[] eigen){
		//Method to classify this ecg as accept or unaccept according to the eigenvalues.
		//TO_DO load weka rip classifier instead of doing this with if's.
		
		//thresholds
		double T1 = 1.221698;
		double T2a = 70218.932346;
		double T2b = 11437.593275;
		double T3a = 121880.326428;
		double T3b = 5649.42276;
		
		int ok = 0; 
		//ok == 0 => accept
		//ok == 1 => unaccept
		
		//if (eig_8  <= T1) ==> bad
		//if (eigen[7]  <= T1 ){
		//	ok = 1;
		//}
		if (eigen[0]  <= T1 ){
			ok = 1;
		}
		//if (eig_1 >= T2a and eig_3 >= T2b) ==> bad
		//else if(eigen[0] >= T2a || eigen[2] >= T2b){
		//	ok = 1;
		//}
		else if(eigen[7] >= T2a && eigen[5] >= T2b){
			ok = 1;
		}
		//if (eig_1 >= T3a or eig_3 >= T3b) ==> bad
		//else if(eigen[0] >= T3a || eigen[1] >= T3b){
		//	ok = 1;
		//}
		else if(eigen[7] >= T3a && eigen[6] >= T3b){
			ok = 1;
		}
		
		return ok;
		
	}
	
    public static double[] mean(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[] mean = new double[n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                mean[j] += v[i][j];
        for (int j = 0; j < n; j++)
            mean[j] /= (double) m;
        return mean;
    }
    
	public double[][] center_reduce(double[][] x,double[] meanECG) {
		double[][] y = new double[x.length][x[0].length];
		for (int i = 0; i < y.length; i++)
			for (int j = 0; j < y[i].length; j++)
				//y[i][j] = (x[i][j] - meanX[j]) / stdevX[j];
				y[i][j] = (x[i][j] - meanECG[j]);
		return y;
	}
    
	public static double[][] covariance_1(double[][] v) {
        int m = v.length;
        int n = v[0].length;
        double[][] X = new double[n][n];
        int degrees = m;
        double c;
        double s1;
        double s2;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                c = 0;
                s1 = 0;
                s2 = 0;
                for (int k = 0; k < m; k++) {
                    s1 += v[k][i];
                    s2 += v[k][j];
                }
                s1 = s1 / m;
                s2 = s2 / m;
                for (int k = 0; k < m; k++)
                    c += (v[k][i] - s1) * (v[k][j] - s2);
                X[i][j] = c / degrees;
            }
        }
        return X;
    }
}
