package org.physionet.challenge2011;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class Challenge_Runner {

	public static void main(String[] args) throws XmlPullParserException, FileNotFoundException {
		// TODO Auto-generated method stub

		//Change root_dir to your project path, for example:"/home/joe/workspace/PhysioNet2011_Challenge_Java/";
		String root_dir="/Users/obarquero/Documents/workspace/PhysioNet2011_Challenge_Java/";
		String data_dir= root_dir + "/res/raw/";
		String xml_dir= root_dir + "/res/xml/";
		int result = -1;
		final int N=50;
		double elapsed_time=0;
		long start_time=0, end_time=0, trial_time=0;
		int i=1;
		ChallengeEntry code = new ChallengeEntry();	
		XmlPullParser mparser = new KXmlParser();


		InputStream HiFile = new FileInputStream(data_dir + "ecg_header.txt");
		InputStreamReader inread= new InputStreamReader(HiFile);
		BufferedReader buf = new BufferedReader(inread,500);
		String strLine=null;
		ECG_MetaData m_MetaData= null;
		BufferedWriter out_log =null;

		try {
			strLine = buf.readLine();
		} catch (IOException e1) {
			System.err.print("Could not read ecg header file.");
		}

		//First line in the header file is the header file name to be used in the log
		String file_name="ECG_LOG_" + strLine;
		File log_file = new File(root_dir, file_name);
		try {
			//FileWriter out_log_writer=new FileWriter(log_file);
			out_log= new BufferedWriter(new FileWriter(log_file));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.print("Could not generate log file:" + log_file);
		}

		try {
			strLine = buf.readLine();
		} catch (IOException e4) {
			// TODO Auto-generated catch block
			e4.printStackTrace();
		} //Move on to the data part

		while (strLine != null){

			//data_int = r.getIdentifier(strLine,"raw", "org.physionet.challenge2011");

			//Get Meta Data for the file
			InputStream xml_int= new FileInputStream(xml_dir + strLine + ".xml");
			//InputStream iFile = getResources().openRawResource(data_int);
			InputStream iFile = new FileInputStream(data_dir + strLine + ".raw");
			InputStreamReader xFile = new InputStreamReader(xml_int);
			//XmlPullParser mparser = getResources().getXml(xml_int);
			mparser.setInput(xFile);

			try {
				System.out.print("Getting metadata from ( " + i + "/" + N + " ) ");
				m_MetaData= ECGXmlParser.Parse(mparser,new ECG_MetaData());
			} catch (XmlPullParserException e2) {
				System.err.print("Could not parse file: " + strLine);
				System.err.print(e2);
				e2.printStackTrace();
			} catch (IOException e2) {
				System.err.print("Could not open xml file: " + strLine);
			}


			try {
				start_time=System.currentTimeMillis();
				result=code.get_result(iFile,m_MetaData);
				end_time=System.currentTimeMillis();
			} catch (IOException e) {
				e.printStackTrace();
			}
			trial_time=end_time-start_time;
			elapsed_time +=  ((double) trial_time/1000);
			String strLog= strLine.substring(3) + " " + result + " Time (ms): " + trial_time + " Total Time (s)= " + elapsed_time + "\n";

			try {
				out_log.write(strLog);
				System.out.print(strLog);
				out_log.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			i++;
			if(i>N){
				break;
			}
			try {
				strLine = buf.readLine();
			} catch (IOException e1) {
				System.err.print("Could not read ecg header file");
			}
		} //end of while loop
		try {
			out_log.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.err.print("Could not close log file.");
			e.printStackTrace();
		}
	}

}
