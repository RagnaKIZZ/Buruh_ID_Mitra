package ahmedt.buruhidmitra.home.modelhistory;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DataItem implements Parcelable {

	@SerializedName("end_date")
	private String endDate;

	@SerializedName("order_date")
	private String orderDate;

	@SerializedName("nama")
	private String nama;

	@SerializedName("jobdesk")
	private String jobdesk;

	@SerializedName("harga")
	private String harga;

	@SerializedName("finish_date")
	private String finishDate;

	@SerializedName("code_order")
	private String codeOrder;

	@SerializedName("telepon")
	private String telepon;

	@SerializedName("status_order")
	private String statusOrder;

	@SerializedName("id")
	private String id;

	@SerializedName("alamat")
	private String alamat;

	@SerializedName("start_date")
	private String startDate;

	public DataItem(Parcel in) {
		endDate = in.readString();
		orderDate = in.readString();
		nama = in.readString();
		jobdesk = in.readString();
		harga = in.readString();
		finishDate = in.readString();
		codeOrder = in.readString();
		telepon = in.readString();
		statusOrder = in.readString();
		id = in.readString();
		alamat = in.readString();
		startDate = in.readString();
	}

	public static final Creator<DataItem> CREATOR = new Creator<DataItem>() {
		@Override
		public DataItem createFromParcel(Parcel in) {
			return new DataItem(in);
		}

		@Override
		public DataItem[] newArray(int size) {
			return new DataItem[size];
		}
	};

	public DataItem() {

	}

	public void setEndDate(String endDate){
		this.endDate = endDate;
	}

	public String getEndDate(){
		return endDate;
	}

	public void setOrderDate(String orderDate){
		this.orderDate = orderDate;
	}

	public String getOrderDate(){
		return orderDate;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setJobdesk(String jobdesk){
		this.jobdesk = jobdesk;
	}

	public String getJobdesk(){
		return jobdesk;
	}

	public void setHarga(String harga){
		this.harga = harga;
	}

	public String getHarga(){
		return harga;
	}

	public void setFinishDate(String finishDate){
		this.finishDate = finishDate;
	}

	public String getFinishDate(){
		return finishDate;
	}

	public void setCodeOrder(String codeOrder){
		this.codeOrder = codeOrder;
	}

	public String getCodeOrder(){
		return codeOrder;
	}

	public void setTelepon(String telepon){
		this.telepon = telepon;
	}

	public String getTelepon(){
		return telepon;
	}

	public void setStatusOrder(String statusOrder){
		this.statusOrder = statusOrder;
	}

	public String getStatusOrder(){
		return statusOrder;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setAlamat(String alamat){
		this.alamat = alamat;
	}

	public String getAlamat(){
		return alamat;
	}

	public void setStartDate(String startDate){
		this.startDate = startDate;
	}

	public String getStartDate(){
		return startDate;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(endDate);
		dest.writeString(orderDate);
		dest.writeString(nama);
		dest.writeString(jobdesk);
		dest.writeString(harga);
		dest.writeString(finishDate);
		dest.writeString(codeOrder);
		dest.writeString(telepon);
		dest.writeString(statusOrder);
		dest.writeString(id);
		dest.writeString(alamat);
		dest.writeString(startDate);
	}
}