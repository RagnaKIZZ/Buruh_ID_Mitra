package ahmedt.buruhidmitra.utils;

import com.google.gson.annotations.SerializedName;

public class UniversalModel{

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	public String getMsg(){
		return msg;
	}

	public int getCode(){
		return code;
	}
}