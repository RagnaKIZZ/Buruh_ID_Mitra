package ahmedt.buruhidmitra.notification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;

import ahmedt.buruhidmitra.FirebaseMessagingService;
import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.notification.DetailNotifikasi.DetailNotifActivity;
import ahmedt.buruhidmitra.notification.modelnotifikasi.DataItem;
import ahmedt.buruhidmitra.notification.modelnotifikasi.NotifikasiModel;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class NotificationActivity extends AppCompatActivity {
    private static final String TAG = "NotificationActivity";
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private LinearLayout lay_include;
    private ImageView imgMsg;
    private FloatingActionButton fabRefresh;
    private TextView txtMsg;
    private ArrayList<DataItem> list = new ArrayList<>();
    private NotificationAdapter adapter;
    String id = Prefs.getString(SessionPrefs.U_ID, "");
    String token = Prefs.getString(SessionPrefs.TOKEN_LOGIN, "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        findView();
        LocalBroadcastManager.getInstance(this).registerReceiver(updateList, new IntentFilter(FirebaseMessagingService.INFO_UPDATE));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateList);
    }

    private BroadcastReceiver updateList = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String param = FirebaseMessagingService.INFO_UPDATE;
            if (intent.getAction().equals(param)) {
                setAdapter(true);
            }
        }
    };

    private void findView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(R.string.title_notifications);

        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.rc_notif);
        txtMsg = findViewById(R.id.txt_msg);
        imgMsg = findViewById(R.id.img_message);
        fabRefresh = findViewById(R.id.floatingActionButton);
        lay_include = findViewById(R.id.include_lay);
        lay_include.setVisibility(View.GONE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new NotificationAdapter(this, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        setAdapter(false);

        adapter.SetOnItemClickListener(new NotificationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, DataItem model) {
                if (model.getIsRead().matches("0")) {
                    view.findViewById(R.id.v_badge).setVisibility(View.GONE);
//                    list.get(position).setIsRead("1");
                }
                Intent i = new Intent(NotificationActivity.this, DetailNotifActivity.class);
                i.putExtra("data_item", list.get(position));
                startActivityForResult(i, 222);
            }
        });

        fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 222) {
                setAdapter(true);
            }
        }
    }

    private void setAdapter(final boolean isBackground) {
        if (isBackground) {

        } else {
            progressBar.setVisibility(View.VISIBLE);
            lay_include.setVisibility(View.GONE);
        }
        AndroidNetworking.post(UrlClass.URL_NOTIF)
                .addBodyParameter("id", id)
                .addBodyParameter("token", token)
                .build()
                .getAsOkHttpResponseAndObject(NotifikasiModel.class, new OkHttpResponseAndParsedRequestListener<NotifikasiModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, NotifikasiModel response) {
                        progressBar.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                list.clear();
                                lay_include.setVisibility(View.GONE);
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem items = new DataItem();
                                    items.setId(response.getData().get(i).getId());
                                    items.setCreateDate(response.getData().get(i).getCreateDate());
                                    items.setMessage(response.getData().get(i).getMessage());
                                    items.setTitle(response.getData().get(i).getTitle());
                                    items.setOrderId(response.getData().get(i).getOrderId());
                                    items.setAlamat(response.getData().get(i).getAlamat());
                                    items.setEndDate(response.getData().get(i).getEndDate());
                                    items.setCodeOrder(response.getData().get(i).getCodeOrder());
                                    items.setFinishDate(response.getData().get(i).getFinishDate());
                                    items.setStatusOrder(response.getData().get(i).getStatusOrder());
                                    items.setOrderDate(response.getData().get(i).getOrderDate());
                                    items.setHarga(response.getData().get(i).getHarga());
                                    items.setNama(response.getData().get(i).getNama());
                                    items.setTukangId(response.getData().get(i).getTukangId());
                                    items.setJobdesk(response.getData().get(i).getJobdesk());
                                    items.setStartDate(response.getData().get(i).getStartDate());
                                    items.setTelepon(response.getData().get(i).getTelepon());
                                    items.setIsRead(response.getData().get(i).getIsRead());
                                    list.add(items);
                                }
                                setResult(RESULT_OK);
                                adapter.updateList(list);
                                Log.d(TAG, "onResponse: "+response.getMsg());
                            } else {
                                Log.d(TAG, "onResponse: "+response.getMsg());
                                if (isBackground) {
                                    Toasty.warning(NotificationActivity.this, R.string.something_wrong, Toasty.LENGTH_SHORT).show();
                                } else {
                                    HelperClass.emptyError(lay_include, imgMsg, txtMsg, getString(R.string.blmnotif));
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            if (isBackground) {
                                Toasty.error(NotificationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                            } else {
                                HelperClass.serverError(NotificationActivity.this, lay_include, imgMsg, txtMsg);
                            }
                        } else {
                            Log.d("ERR", "onError: " + anError.getErrorCode());
                            Log.d("ERR", "onError: " + anError.getErrorBody());
                            Log.d("ERR", "onError: " + anError.getErrorDetail());
                            if (isBackground) {
                                Toasty.error(NotificationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                            } else {
                                HelperClass.InetError(NotificationActivity.this, lay_include, imgMsg, txtMsg);
                            }
                        }
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
