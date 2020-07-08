package ahmedt.buruhidmitra.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.ncorti.slidetoact.SlideToActView;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.modelactivities.Data;
import ahmedt.buruhidmitra.home.modelhistory.DataItem;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class LastActivitiesActivity extends AppCompatActivity {
    private SlideToActView btnSlide;
    private Button btnConfirm;
    LinearLayout ln_btnCall, ln_btnCancel, include_lay;
    private TextView txtStartDate, txtEndDate, txtStartHour, txtTotalDays, txtStatus,
            txtJobdesk, txtAlamat, txtNama, txtHarga, txtPotongan, txtRealHarga, txtCodeOrder, txtTitleRow, txtFinishDate;
    private NestedScrollView nest;
    private ProgressBar progressBar;
    private TableRow rowTitle, rowDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_notif);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.order_details);
        findView();
    }

    private void findView() {
        include_lay = findViewById(R.id.include_lay);
        nest = findViewById(R.id.nest_detail_notif);
        progressBar = findViewById(R.id.progress_bar);

        txtStartDate = findViewById(R.id.txt_start_date_notif);
        txtStartHour = findViewById(R.id.txt_start_hour_notif);
        txtEndDate = findViewById(R.id.txt_end_date_notif);
        txtTotalDays = findViewById(R.id.txt_total_date_notif);
        txtStatus = findViewById(R.id.txt_status_notif);
        txtAlamat = findViewById(R.id.txt_address_notif);
        txtJobdesk = findViewById(R.id.txt_jobdesk_notif);
        txtNama = findViewById(R.id.txt_nama_notif);
        txtHarga = findViewById(R.id.txt_harga_orderan_notif);
        txtPotongan = findViewById(R.id.txt_potongan_orderan_notif);
        txtRealHarga = findViewById(R.id.txt_total_harga_orderan_notif);
        txtCodeOrder = findViewById(R.id.txt_code_order_notif);
        txtTitleRow = findViewById(R.id.txt_tit_finish_date_notif);
        txtFinishDate = findViewById(R.id.txt_finish_date_notif);
        rowTitle = findViewById(R.id.row_fin_title);
        rowDesc = findViewById(R.id.row_fin_desc);
        btnSlide = findViewById(R.id.btn_slide_notif);
        btnConfirm = findViewById(R.id.btn_confirm_notif);
        ln_btnCancel = findViewById(R.id.ln_btn_cancel_help_notif);
        ln_btnCall = findViewById(R.id.ln_btn_call_customer_notif);

        progressBar.setVisibility(View.GONE);
        include_lay.setVisibility(View.GONE);
        nest.setVisibility(View.VISIBLE);
        ln_btnCall.setVisibility(View.GONE);
        ln_btnCancel.setVisibility(View.GONE);
        btnSlide.setVisibility(View.GONE);
        btnConfirm.setVisibility(View.GONE);

        Intent intent = getIntent();
        final DataItem item = intent.getParcelableExtra("data_item");
        String nama = item.getNama();
        String startDate = item.getStartDate();
        String endDate = item.getEndDate();
        String codeOrder = item.getCodeOrder();
        String harga = item.getHarga();
        String jobDesk = item.getJobdesk();
        String status = item.getStatusOrder();
        String alamat = item.getAlamat();
        String tStatus = "";
        String finDate = "";
        String titleFin = "";
        double dHarga = Double.parseDouble(harga);
        double potongan = dHarga * 0.1;
        double realHarga = dHarga - potongan;

        Log.d("status", "findView: " + status);
        if (item.getFinishDate() != null) {
            finDate = item.getFinishDate();
            parseDate("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy", finDate, txtFinishDate);
        } else {
            rowDesc.setVisibility(View.GONE);
            rowTitle.setVisibility(View.GONE);
        }
        Locale locale = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);

        parseDate("yyyy-MM-dd HH:mm:ss", "HH:mm", startDate, txtStartHour);
        parseDate("yyyy-MM-dd HH:mm:ss", "dd MMM yyyy", startDate, txtStartDate);
        parseDate("yyyy-MM-dd", "dd MMM yyyy", endDate, txtEndDate);
        getTotaldays(startDate, endDate);

        txtAlamat.setText(alamat);

        txtCodeOrder.setText(codeOrder);
        txtJobdesk.setText(jobDesk);
        txtNama.setText(nama);

        txtHarga.setText(format.format(dHarga));
        txtPotongan.setText(format.format(potongan));
        txtRealHarga.setText(format.format(realHarga));

        if (status.matches("0")) {
            btnSlide.setVisibility(View.GONE);
            ln_btnCall.setVisibility(View.GONE);
            ln_btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            tStatus = getString(R.string.canceled);
            titleFin = "Cancel date";
            txtTitleRow.setText(titleFin);
            txtStatus.setText(tStatus);
        } else if (status.matches("1")) {
            tStatus = getString(R.string.wait_confirm);
            btnSlide.setVisibility(View.GONE);
            ln_btnCall.setVisibility(View.VISIBLE);
            ln_btnCancel.setVisibility(View.VISIBLE);
            btnConfirm.setVisibility(View.VISIBLE);
            txtStatus.setText(tStatus);
        } else if (status.matches("2")) {
            tStatus = getString(R.string.order_acc);
            btnSlide.setVisibility(View.VISIBLE);
            ln_btnCall.setVisibility(View.VISIBLE);
            ln_btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            txtStatus.setText(tStatus);
        } else if (status.matches("3")) {
            tStatus = getString(R.string.workinggg);
            btnSlide.setVisibility(View.GONE);
            ln_btnCall.setVisibility(View.VISIBLE);
            ln_btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            txtStatus.setText(tStatus);
        } else if (status.matches("4")) {
            btnSlide.setVisibility(View.GONE);
            ln_btnCall.setVisibility(View.GONE);
            ln_btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            titleFin = "Finish date";
            tStatus = getString(R.string.fin);
            txtTitleRow.setText(titleFin);
            txtStatus.setText(tStatus);
        }
//        txtStatus.setText(tStatus);
    }

    private void parseDate(String pattern, String pattern2, String date, TextView edt) {
        SimpleDateFormat time = new SimpleDateFormat(pattern);
        try {
            Date waktu = time.parse(date);
            SimpleDateFormat format1 = new SimpleDateFormat(pattern2);
            String realTime = format1.format(waktu);
            edt.setText(realTime);
        } catch (Exception e) {
            Log.d("ASD", "onBindViewHolder: " + e.getMessage());
        }
    }

    private void getTotaldays(String date, String date2) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat time2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            Date waktu = time.parse(date);
            Date waktu2 = time2.parse(date2);
            long end, start;
            end = waktu2.getTime();
            start = waktu.getTime();
            long totalDays = end - start;
            long total = (totalDays / (24 * 60 * 60 * 1000) + 1);
            String day;
            String realTime = String.valueOf(total);
            if (totalDays > 1) {
                day = getString(R.string.days);
            } else {
                day = getString(R.string.day);
            }
            txtTotalDays.setText(realTime + day);
        } catch (Exception e) {
            Log.d("ASD", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
