package ahmedt.buruhidmitra.home;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

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
import ahmedt.buruhidmitra.notification.DetailNotifikasi.DetailNotifActivity;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class ActivitiesActivity extends AppCompatActivity {
    private SlideToActView btnSlide;
    private Button btnCall, btnMessage, btnConfirm;
    LinearLayout ln_btnCall, ln_btnCancel, include_lay;
    private TextView txtStartDate, txtEndDate, txtStartHour, txtTotalDays, txtStatus,
            txtJobdesk, txtAlamat, txtNama, txtHarga, txtPotongan, txtRealHarga, txtCodeOrder;
    private NestedScrollView nest;
    private ProgressBar progressBar;
    private TableRow rowTitle, rowDesc;
    String id = Prefs.getString(SessionPrefs.U_ID, "");
    String token = Prefs.getString(SessionPrefs.TOKEN_LOGIN, "");

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
        rowTitle = findViewById(R.id.row_fin_title);
        rowDesc = findViewById(R.id.row_fin_desc);
        btnSlide = findViewById(R.id.btn_slide_notif);
        btnConfirm = findViewById(R.id.btn_confirm_notif);
        btnCall = findViewById(R.id.btn_call_notif);
        btnMessage = findViewById(R.id.btn_msg_notif);
        ln_btnCancel = findViewById(R.id.ln_btn_cancel_help_notif);
        ln_btnCall = findViewById(R.id.ln_btn_call_customer_notif);

        progressBar.setVisibility(View.GONE);
        include_lay.setVisibility(View.GONE);
        nest.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        final Data item = intent.getParcelableExtra("data_item");

        String nama = item.getNama();
        String startDate = item.getStartDate();
        String endDate = item.getEndDate();
        String codeOrder = item.getCodeOrder();
        String harga = item.getHarga();
        String jobDesk = item.getJobdesk();
        String status = item.getStatusOrder();
        String alamat = item.getAlamat();
        String tStatus = "";
        final String id_order = item.getId();
        final String telepon = item.getTelepon();
        double dHarga = Double.parseDouble(harga);
        double potongan = dHarga * 0.1;
        double realHarga = dHarga - potongan;

        rowDesc.setVisibility(View.GONE);
        rowTitle.setVisibility(View.GONE);

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

        if (status.matches("0") && status.matches("4")) {
            btnSlide.setVisibility(View.GONE);
            ln_btnCall.setVisibility(View.GONE);
            ln_btnCancel.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            if (status.matches("0")){
                tStatus = getString(R.string.canceled);
            }else{
                tStatus = getString(R.string.fin);
            }
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
        }

        btnSlide.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideToActView slideToActView) {
                btnSlide.setVisibility(View.GONE);
                startWoriking(id_order);
            }
        });

        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", telepon, null));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("", "onClick: openmessage" + e.getMessage());
                    Toasty.error(ActivitiesActivity.this, e.getMessage()).show();
                }
            }
        });

        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("smsto:" + telepon));
                    startActivity(intent);
                } catch (Exception e) {
                    Log.d("", "onClick: openmessage" + e.getMessage());
                    Toasty.error(ActivitiesActivity.this, e.getMessage()).show();
                }
            }
        });
    }

    private void startWoriking(String id_order) {
        final KProgressHUD hud = new KProgressHUD(this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_START_WORKING)
                .addBodyParameter("tukang_id", id)
                .addBodyParameter("token_login", token)
                .addBodyParameter("order_id", id_order)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toast.makeText(ActivitiesActivity.this, response.getMsg(), Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toasty.warning(ActivitiesActivity.this, R.string.something_wrong, Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(ActivitiesActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(ActivitiesActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
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
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
