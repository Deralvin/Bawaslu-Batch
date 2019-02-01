package id.pptik.bawaslubatch.networks.POJO;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class DataItem{

	@SerializedName("TIMESTAMP")
	private long tIMESTAMP;

	@SerializedName("GUID")
	private String gUID;

	@SerializedName("FILENAME")
	private String fILENAME;

	public void setTIMESTAMP(long tIMESTAMP){
		this.tIMESTAMP = tIMESTAMP;
	}

	public long getTIMESTAMP(){
		return tIMESTAMP;
	}

	public void setGUID(String gUID){
		this.gUID = gUID;
	}

	public String getGUID(){
		return gUID;
	}

	public void setFILENAME(String fILENAME){
		this.fILENAME = fILENAME;
	}

	public String getFILENAME(){
		return fILENAME;
	}

	@Override
 	public String toString(){
		return 
			"DataItem{" + 
			"tIMESTAMP = '" + tIMESTAMP + '\'' + 
			",gUID = '" + gUID + '\'' + 
			",fILENAME = '" + fILENAME + '\'' + 
			"}";
		}
}