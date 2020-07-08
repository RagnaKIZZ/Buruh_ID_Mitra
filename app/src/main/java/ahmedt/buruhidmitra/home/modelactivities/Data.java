package ahmedt.buruhidmitra.home.modelactivities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable {

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

	@SerializedName("status_pembayaran")
	private String statusPembayaran;

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

	public Data() {
	}

	protected Data(Parcel in) {
		endDate = in.readString();
		orderDate = in.readString();
		nama = in.readString();
		jobdesk = in.readString();
		harga = in.readString();
		statusPembayaran = in.readString();
		codeOrder = in.readString();
		telepon = in.readString();
		statusOrder = in.readString();
		id = in.readString();
		alamat = in.readString();
		startDate = in.readString();
	}

	public Data(String endDate, String orderDate, String nama, String jobdesk, String harga, String statusPembayaran, String codeOrder, String telepon, String statusOrder, String id, String alamat, String startDate) {
		this.endDate = endDate;
		this.orderDate = orderDate;
		this.nama = nama;
		this.jobdesk = jobdesk;
		this.harga = harga;
		this.statusPembayaran = statusPembayaran;
		this.codeOrder = codeOrder;
		this.telepon = telepon;
		this.statusOrder = statusOrder;
		this.id = id;
		this.alamat = alamat;
		this.startDate = startDate;
	}

	public static final Creator<Data> CREATOR = new Creator<Data>() {
		@Override
		public Data createFromParcel(Parcel in) {
			return new Data(in);
		}

		@Override
		public Data[] newArray(int size) {
			return new Data[size];
		}
	};

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

	public void setStatusPembayaran(String statusPembayaran){
		this.statusPembayaran = statusPembayaran;
	}

	public String getStatusPembayaran(){
		return statusPembayaran;
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
		dest.writeString(statusPembayaran);
		dest.writeString(codeOrder);
		dest.writeString(telepon);
		dest.writeString(statusOrder);
		dest.writeString(id);
		dest.writeString(alamat);
		dest.writeString(startDate);
	}
}