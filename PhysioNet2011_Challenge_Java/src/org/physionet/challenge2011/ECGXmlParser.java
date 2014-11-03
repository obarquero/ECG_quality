package org.physionet.challenge2011;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;


public class ECGXmlParser
{
	public static final String TAG = ECGXmlParser.class.toString();
	// names of the XML tags
	static final String HEADER="ecg_challenge";
	static final String SEX = "sex";
	static final String AGE = "age";
	static final String ELECTRODE="electrode";
	static final String SHIRTSIZE="shirtsize";
	static final String WEIGHT="weight";
	static final String HEIGHT="height";
	static final String MEDS="meds";
	static final String RISKS="risks";
	static final String SYMPTOMS="symptoms";
	static final String TCHOL="total_cholesterol";
	static final String HDLCHOL="hdl_cholesterol";
	static final String SBP="systolic_blood_pressure";
	static final String CONDITIONS="conditions";
	static final String FS="samplingfrequency";
	static final String CH="numberofleads";
	static final String CH_LABELS="leadlabels";


	public static ECG_MetaData Parse (XmlPullParser xpp, ECG_MetaData m_MetaData) throws XmlPullParserException, IOException {

		int eventType = xpp.getEventType();
		String name=null;
		while (eventType != XmlPullParser.END_DOCUMENT) {
			name = xpp.getName();
			if(eventType == XmlPullParser.START_TAG && !name.equalsIgnoreCase(HEADER)) {
				eventType = xpp.next();
				String tmp_str=xpp.getText();
				if(tmp_str != null && eventType != XmlPullParser.END_TAG){
					if (name.equalsIgnoreCase(SEX)){
						m_MetaData.sex=tmp_str.charAt(0);
					} else if (name.equalsIgnoreCase(AGE)){
						m_MetaData.age=Short.parseShort(tmp_str);
					} else if (name.equalsIgnoreCase(ELECTRODE)){
						m_MetaData.electrode=tmp_str;
					}else if (name.equalsIgnoreCase(FS)){
						m_MetaData.sampling_frequency=Float.parseFloat(tmp_str);
					}else if (name.equalsIgnoreCase(CH)){
						m_MetaData.number_of_leads=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(CH_LABELS)){
						m_MetaData.ch_labels=tmp_str;
					}else{
						//System.out.print(TAG,"INVALID metadata of: " + name + " of value = "+ tmp_str);
					}
					/*
					} else if (name.equalsIgnoreCase(SHIRTSIZE)){
						m_MetaData.shirt_size=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(WEIGHT)){
						m_MetaData.weight=Float.parseFloat(tmp_str);
					}else if (name.equalsIgnoreCase(HEIGHT)){
						m_MetaData.height=Float.parseFloat(tmp_str);
					}else if (name.equalsIgnoreCase(MEDS)){
						m_MetaData.meds=tmp_str;
					}else if (name.equalsIgnoreCase(RISKS)){
						m_MetaData.risks=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(SYMPTOMS)){
						m_MetaData.symptoms=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(TCHOL)){
						m_MetaData.total_cholesterol=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(HDLCHOL)){
						m_MetaData.hdl_cholesterol=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(SBP)){
						m_MetaData.systolic_blood_pressure=Short.parseShort(tmp_str);
					}else if (name.equalsIgnoreCase(CONDITIONS)){
						m_MetaData.conditions=Short.parseShort(tmp_str);
					*/
					//Log.d(TAG,"Got metadata of: " + name + " of value = "+ tmp_str);
				} //!=  end_tag && value != null
			}
			eventType = xpp.next();
		}
		//End of xml document
		return m_MetaData;
	}
}
