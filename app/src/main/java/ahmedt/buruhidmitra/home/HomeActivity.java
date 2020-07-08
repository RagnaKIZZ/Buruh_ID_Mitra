package ahmedt.buruhidmitra.home;

import android.app.ActivityOptions;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import ahmedt.buruhidmitra.FirebaseMessagingService;
import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.countnotif.CountNotifModel;
import ahmedt.buruhidmitra.home.modelactivities.ActivitiesModel;
import ahmedt.buruhidmitra.home.modelactivities.Data;
import ahmedt.buruhidmitra.home.modelhistory.DataItem;
import ahmedt.buruhidmitra.home.modelhistory.LastActivitiesModel;
import ahmedt.buruhidmitra.home.tablayoutprice.ScreenItem;
import ahmedt.buruhidmitra.home.tablayoutprice.TabPagerAdapter;
import ahmedt.buruhidmitra.home.tablayoutprice.modeprice.PriceModel;
import ahmedt.buruhidmitra.notification.DetailNotifikasi.DetailNotifActivity;
import ahmedt.buruhidmitra.notification.NotificationActivity;
import ahmedt.buruhidmitra.profile.ProfileActivity;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private Switch aSwitch;
    TabPagerAdapter adapter;
    List<ScreenItem> list = new ArrayList<>();
    private ProgressBar progbarIncome, progbarAct, progbarHistory, progbarImg;
    private FloatingActionButton fabRefreshIncome, fabRefreshActivities, fabRefreshHistory;
    private ViewPager viewPager;
    private LastActivitiesAdapter lastAdapter;
    private ArrayList<DataItem> lastList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RelativeLayout rlActivities, rlMsg;
    private LinearLayout lnInclude;
    private ImageView imgTukang, imgMsg;
    private TextView txtMsgAct, txtNamaCustomer, txtTgl, txtLokasi, txtHarga, txtName, txtMsg, txtBadgeNotif;
    private CardView cvActivities;
    Locale locale;
    NumberFormat format;
    double semua = 0, bulan = 0, hari = 0;
    int paramAct = 1, countNotif = 0;
    String nama, telepon, id_order, code_order, alamat, jobdesk, harga, orderdate, startdate, enddate, statusorder, statusPem, id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        FirebaseMessaging.getInstance().subscribeToTopic("buruhID");
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String token = instanceIdResult.getToken();
                if (Prefs.getString(SessionPrefs.TOKEN_FIREBASE, "").isEmpty()) {
                    updateToken(token);
                    Log.d(TAG, "onSuccess: token kosong = " + token);
                } else if (!token.equals(Prefs.getString(SessionPrefs.TOKEN_FIREBASE, ""))) {
                    updateToken(token);
                    Log.d(TAG, "onSuccess: token beda = " + token);
                } else {
                    Log.d(TAG, "onSuccess: token masih sama = " + token);
                }
            }
        });
        findView();
        LocalBroadcastManager.getInstance(HomeActivity.this).registerReceiver(updateBadge, new IntentFilter(FirebaseMessagingService.INFO_UPDATE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(HomeActivity.this).unregisterReceiver(updateBadge);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countNotif = Prefs.getInt(SessionPrefs.NOTIF_COUNT, 0);
        setupBadgeNotif();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        final MenuItem notifItem = menu.findItem(R.id.action_notification);
        View actionNotif = notifItem.getActionView();
        txtBadgeNotif = (TextView) actionNotif.findViewById(R.id.notif_badge);
        actionNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(notifItem);
            }
        });
        setupBadgeNotif();
        return super.onCreateOptionsMenu(menu);
    }

    private void setupBadgeNotif() {
        if (txtBadgeNotif != null) {
            if (countNotif == 0) {
                if (txtBadgeNotif.getVisibility() != View.GONE) {
                    txtBadgeNotif.setVisibility(View.GONE);
                }
            } else {
                txtBadgeNotif.setText(String.valueOf(Math.min(countNotif, 99)));
                if (txtBadgeNotif.getVisibility() != View.VISIBLE) {
                    txtBadgeNotif.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_notification:
                startActivity(new Intent(HomeActivity.this, NotificationActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private BroadcastReceiver updateBadge = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String param = FirebaseMessagingService.INFO_UPDATE;
            if (intent.getAction().equals(param)) {
                getCount(false);
            }
        }
    };

    private void findView() {
//        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        recyclerView = findViewById(R.id.rv_activites);
        lastAdapter = new LastActivitiesAdapter(this, lastList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(lastAdapter);
        imgTukang = findViewById(R.id.img_account);
        rlActivities = findViewById(R.id.rl_is_activities);
        rlActivities.setVisibility(View.GONE);
        rlMsg = findViewById(R.id.rl_no_activities);
        lnInclude = findViewById(R.id.include_lay);
        imgMsg = findViewById(R.id.img_message);
        txtMsg = findViewById(R.id.txt_msg);
        lnInclude.setVisibility(View.GONE);
        adapter = new TabPagerAdapter(this, list);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setVisibility(View.GONE);
        progbarIncome = findViewById(R.id.progress_bar);
        progbarAct = findViewById(R.id.progress_bar_include);
        progbarHistory = findViewById(R.id.progress_bar2);
        progbarImg = findViewById(R.id.progress_bar_img);
        progbarImg.setVisibility(View.GONE);
        cvActivities = findViewById(R.id.cv_select_activities);
        fabRefreshActivities = findViewById(R.id.fab_refresh_include);
        fabRefreshHistory = findViewById(R.id.floatingActionButton);
        fabRefreshIncome = findViewById(R.id.fab_refresh);
        txtMsgAct = findViewById(R.id.txt_no_activities_selected);
        txtNamaCustomer = findViewById(R.id.txt_name_activities);
        txtTgl = findViewById(R.id.txt_tgl_activities);
        txtLokasi = findViewById(R.id.txt_address_activities);
        txtHarga = findViewById(R.id.txt_price_activities);
        txtName = findViewById(R.id.txt_name_tukang);
        aSwitch = findViewById(R.id.switch_tukang);
        txtMsgAct.setVisibility(View.GONE);
        viewPager.setAdapter(adapter);
//        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        fabRefreshIncome.hide();
        fabRefreshHistory.hide();
        fabRefreshActivities.hide();
        progbarHistory.setVisibility(View.GONE);
        progbarAct.setVisibility(View.GONE);
        progbarIncome.setVisibility(View.GONE);
        getPendapatan();
        getActivities();
        getLastActivities();

        locale = new Locale("in", "ID");
        format = NumberFormat.getCurrencyInstance(locale);

        txtName.setText(Prefs.getString(SessionPrefs.NAMA, ""));

        if (Prefs.getString(SessionPrefs.FOTO, "").isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.blank_profile)
                    .into(imgTukang);
        } else {
            HelperClass.loadGambar(this, UrlClass.URL_FOTO_TUKANG + Prefs.getString(SessionPrefs.FOTO, ""), progbarImg, imgTukang);
        }

        imgTukang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this, ProfileActivity.class);
                View shareView = imgTukang;
                String transitionName = getString(R.string.img);
                ActivityOptions transOpt = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transOpt = ActivityOptions.makeSceneTransitionAnimation(HomeActivity.this, shareView, transitionName);
                    startActivity(i, transOpt.toBundle());
                } else {
                    startActivity(i);
                }
            }
        });

        lastAdapter.SetOnItemClickListener(new LastActivitiesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DataItem model) {
                Intent intent = new Intent(HomeActivity.this, LastActivitiesActivity.class);
                intent.putExtra("data_item", lastList.get(position));
                startActivity(intent);
            }
        });


        cvActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paramAct == 1) {
                    Toast.makeText(HomeActivity.this, getString(R.string.no_activities), Toast.LENGTH_SHORT).show();
                } else {
                    Data data = new Data(enddate, orderdate, nama, jobdesk, harga, statusPem, code_order, telepon, statusorder, id, alamat, startdate);;
                    Intent intent = new Intent(HomeActivity.this, ActivitiesActivity.class);
                    intent.putExtra("data_item", data);
                    startActivityForResult(intent, 111);
                }
            }
        });

        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aSwitch.isChecked()) {
                    changeStatus(aSwitch, false, "1");
                } else {
                    changeStatus(aSwitch, true, "0");
                }
            }
        });

        if (Prefs.getString(SessionPrefs.isActive, "").equals("1")) {
            aSwitch.setChecked(true);
            aSwitch.setText(getString(R.string.activee));
        } else {
            aSwitch.setChecked(false);
            aSwitch.setText(getString(R.string.nonactivee));
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    aSwitch.setText(getString(R.string.activee));
                } else {
                    aSwitch.setText(getString(R.string.nonactivee));
                }
            }
        });

        fabRefreshIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPendapatan();
            }
        });

        fabRefreshHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastActivities();
            }
        });

        fabRefreshActivities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivities();
            }
        });

        if (Prefs.getInt(SessionPrefs.NOTIF_COUNT, 0) > 0) {
            countNotif = Prefs.getInt(SessionPrefs.NOTIF_COUNT, 0);
        }

        if (Prefs.getString(SessionPrefs.isLogin, "").isEmpty()) {
            getCount(true);
            Prefs.putString(SessionPrefs.isLogin, "1");
        } else {
            setupBadgeNotif();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getActivities();
        }
    }

    private void getPendapatan() {
        progbarIncome.setVisibility(View.VISIBLE);
        fabRefreshIncome.hide();
        AndroidNetworking.post(UrlClass.URL_PNEDAPATAN)
                .addBodyParameter("tukang_id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .build()
                .getAsOkHttpResponseAndObject(PriceModel.class, new OkHttpResponseAndParsedRequestListener<PriceModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, PriceModel response) {
                        progbarIncome.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                fabRefreshIncome.hide();
                                viewPager.setVisibility(View.VISIBLE);
                                semua = response.getData().getPendapatanSemua();
                                bulan = response.getData().getPendapatanBulanan();
                                hari = response.getData().getPendapatanHarian();
                                list.add(new ScreenItem(format.format(semua)));
                                list.add(new ScreenItem(format.format(bulan)));
                                list.add(new ScreenItem(format.format(hari)));
                                viewPager.setAdapter(adapter);
                            } else {
                                fabRefreshIncome.show();
                                viewPager.setVisibility(View.GONE);
                                Toasty.error(HomeActivity.this, getString(R.string.something_wrong)).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        fabRefreshIncome.show();
                        progbarIncome.setVisibility(View.GONE);
                        viewPager.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void getActivities() {
        progbarAct.setVisibility(View.VISIBLE);
        txtMsgAct.setVisibility(View.GONE);
        fabRefreshActivities.hide();
        AndroidNetworking.post(UrlClass.URL_ACTIVITIES)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .build()
                .getAsOkHttpResponseAndObject(ActivitiesModel.class, new OkHttpResponseAndParsedRequestListener<ActivitiesModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, ActivitiesModel response) {
                        progbarAct.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                paramAct = 0;
                                fabRefreshActivities.hide();
                                rlActivities.setVisibility(View.VISIBLE);
                                rlMsg.setVisibility(View.GONE);
                                txtMsgAct.setVisibility(View.GONE);
                                nama = response.getData().getNama();
                                statusPem = response.getData().getStatusPembayaran();
                                telepon = response.getData().getTelepon();
                                id_order = response.getData().getId();
                                code_order = response.getData().getCodeOrder();
                                alamat = response.getData().getAlamat();
                                statusorder = response.getData().getStatusOrder();
                                jobdesk = response.getData().getJobdesk();
                                harga = response.getData().getHarga();
                                orderdate = response.getData().getOrderDate();
                                startdate = response.getData().getStartDate();
                                enddate = response.getData().getEndDate();
                                id = response.getData().getId();
                                double rHarga = Double.parseDouble(harga);
                                double realHarga = rHarga - (rHarga * 0.1);
                                txtNamaCustomer.setText(nama);
                                txtHarga.setText(format.format(realHarga));
                                txtLokasi.setText(alamat.toLowerCase());
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                try {
                                    Date date = format.parse(startdate);
                                    SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy");
                                    String realTime = format1.format(date);
                                    txtTgl.setText(realTime);
                                } catch (Exception e) {
                                    Log.d("ASD", "onBindViewHolder: " + e.getMessage());
                                }
                                Data item = new Data();
                                item.setNama(nama);
                                item.setAlamat(alamat);
                                item.setCodeOrder(code_order);
                                item.setEndDate(enddate);
                                item.setHarga(harga);
                                item.setId(id_order);
                                item.setJobdesk(jobdesk);
                                item.setOrderDate(orderdate);
                                item.setStartDate(startdate);
                                item.setStatusOrder(statusorder);
                                item.setTelepon(telepon);
                                item.setStatusPembayaran(response.getData().getStatusPembayaran());
                            } else {
                                paramAct = 1;
                                txtMsgAct.setVisibility(View.VISIBLE);
                                rlMsg.setVisibility(View.VISIBLE);
                                rlActivities.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        paramAct = 1;
                        fabRefreshActivities.show();
                        rlMsg.setVisibility(View.VISIBLE);
                        rlActivities.setVisibility(View.GONE);
                        txtMsgAct.setVisibility(View.GONE);
                        progbarAct.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void getLastActivities() {
        lastList.clear();
        lnInclude.setVisibility(View.GONE);
        progbarHistory.setVisibility(View.VISIBLE);
        AndroidNetworking.post(UrlClass.URL_LAST_ACTIVITIES)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .build()
                .getAsOkHttpResponseAndObject(LastActivitiesModel.class, new OkHttpResponseAndParsedRequestListener<LastActivitiesModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, LastActivitiesModel response) {
                        progbarHistory.setVisibility(View.GONE);
                        fabRefreshHistory.hide();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                for (int i = 0; i < response.getData().size(); i++) {
                                    DataItem items = new DataItem();
                                    items.setId(response.getData().get(i).getId());
                                    items.setNama(response.getData().get(i).getNama());
                                    items.setAlamat(response.getData().get(i).getAlamat());
                                    items.setJobdesk(response.getData().get(i).getJobdesk());
                                    items.setStartDate(response.getData().get(i).getStartDate());
                                    items.setEndDate(response.getData().get(i).getEndDate());
                                    items.setHarga(response.getData().get(i).getHarga());
                                    items.setStatusOrder(response.getData().get(i).getStatusOrder());
                                    items.setOrderDate(response.getData().get(i).getOrderDate());
                                    items.setTelepon(response.getData().get(i).getTelepon());
                                    items.setCodeOrder(response.getData().get(i).getCodeOrder());
                                    items.setFinishDate(response.getData().get(i).getFinishDate());
                                    lastList.add(items);
                                }
                                lastAdapter.updateList(lastList);
                                Log.d(TAG, "onResponse: " + response.getMsg());
                                lnInclude.setVisibility(View.GONE);
                            } else {
                                HelperClass.emptyError(lnInclude, imgMsg, txtMsg, "Your activity history is empty");
                                Log.d(TAG, "onResponse: " + response.getMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        fabRefreshHistory.show();
                        progbarHistory.setVisibility(View.GONE);
                        Log.d(TAG, "onError: " + anError.getErrorBody());
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            HelperClass.serverError(HomeActivity.this, lnInclude, imgMsg, txtMsg);
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            HelperClass.InetError(HomeActivity.this, lnInclude, imgMsg, txtMsg);
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void changeStatus(final Switch aSwitch, final boolean checked, final String parameter) {
        final KProgressHUD hud = new KProgressHUD(this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_STATUS)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("aktif", parameter)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Prefs.putString(SessionPrefs.isActive, parameter);
                                Toasty.success(HomeActivity.this, getString(R.string.statuschanged)).show();
                            } else {
                                aSwitch.setChecked(checked);
                                Toasty.warning(HomeActivity.this, response.getMsg()).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        aSwitch.setChecked(checked);
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void updateToken(final String token) {
        AndroidNetworking.post(UrlClass.URL_UPDATE_FIREBASE)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("token_firebase", token)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Prefs.putString(SessionPrefs.TOKEN_FIREBASE, token);
                                Log.d(TAG, "onResponse: " + response.getMsg());
                            } else {
                                Log.d(TAG, "onResponse: " + response.getMsg());
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void getCount(final Boolean notif) {
        AndroidNetworking.post(UrlClass.URL_COUNT)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .build()
                .getAsOkHttpResponseAndObject(CountNotifModel.class, new OkHttpResponseAndParsedRequestListener<CountNotifModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CountNotifModel response) {
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Log.d(TAG, "onResponse: " + response.getCountNotif());
                                countNotif = response.getCountNotif();
                                Prefs.putInt(SessionPrefs.NOTIF_COUNT, response.getCountNotif());
                                setupBadgeNotif();
                                if (notif) {
                                    if (response.getCountNotif() > 0) {
                                        for (int i = 0; i < response.getData().size(); i++) {
                                            showNotification(response.getData().get(i).getTitle(), response.getData().get(i).getMessage());
                                        }
                                    }
                                }

                                Log.d(TAG, "onResponse: suc= " + response.getMsg());
                            } else {
                                Log.d(TAG, "onResponse: badge" + response.getMsg());
                            }

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            Toasty.error(HomeActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void showNotification(String title, String message) {
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_001";
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Random rand = new Random();
        final int notification_ID = rand.nextInt();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("My Channel");
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent i = new Intent(this, NotificationActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intent = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_UPDATE_CURRENT);
//        PendingIntent intent = TaskStackBuilder.create(this).addNextIntentWithParentStack(i).getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.mipmap.ic_launcher_new_new)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(intent)
                .setStyle(new NotificationCompat.InboxStyle().addLine(message));

        manager.notify(notification_ID, builder.build());
    }


}
