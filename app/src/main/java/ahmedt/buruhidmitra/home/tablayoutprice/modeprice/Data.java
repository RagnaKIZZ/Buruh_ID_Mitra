package ahmedt.buruhidmitra.home.tablayoutprice.modeprice;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("pendapatan_semua")
	private int pendapatanSemua;

	@SerializedName("pendapatan_bulanan")
	private int pendapatanBulanan;

	@SerializedName("pendapatan_harian")
	private int pendapatanHarian;

	public void setPendapatanSemua(int pendapatanSemua){
		this.pendapatanSemua = pendapatanSemua;
	}

	public int getPendapatanSemua(){
		return pendapatanSemua;
	}

	public void setPendapatanBulanan(int pendapatanBulanan){
		this.pendapatanBulanan = pendapatanBulanan;
	}

	public int getPendapatanBulanan(){
		return pendapatanBulanan;
	}

	public void setPendapatanHarian(int pendapatanHarian){
		this.pendapatanHarian = pendapatanHarian;
	}

	public int getPendapatanHarian(){
		return pendapatanHarian;
	}
}