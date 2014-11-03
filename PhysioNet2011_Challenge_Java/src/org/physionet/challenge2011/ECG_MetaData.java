package org.physionet.challenge2011;

public class ECG_MetaData {
	public char sex;
	public short age;
	public String ch_labels;
	public float sampling_frequency;
	public short number_of_leads;
	public String electrode;
	
	/*
	public short shirt_size;
	public float weight;
	public float height;
	public String meds;
	public short risks;
	public short symptoms;
	public short total_cholesterol;
	public short hdl_cholesterol;
	public short systolic_blood_pressure;
	public short conditions;
	*/
	public ECG_MetaData(){
		this.sex=0;
		this.age= 0;
		this.electrode=null;
		this.sampling_frequency=-1;
		this.number_of_leads=-1;
		this.ch_labels=null;
		
		/*
		this.shirt_size=-1;
		this.weight=-1;
		this.height=-1;
		this.meds=null;
		this.risks=-1;
		this.symptoms=-1;
		this.total_cholesterol=-1;
		this.hdl_cholesterol=-1;
		this.systolic_blood_pressure=-1;
		this.conditions=-1;
		*/
	}

}
