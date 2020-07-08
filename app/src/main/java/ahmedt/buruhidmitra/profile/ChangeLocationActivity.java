package ahmedt.buruhidmitra.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.regex.Pattern;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.profile.modelCity.CityModel;
import ahmedt.buruhidmitra.profile.modelCity.DataItem;
import ahmedt.buruhidmitra.profile.modelUnicode.UnicodeModel;
import ahmedt.buruhidmitra.profile.modelVIll.VillModel;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class ChangeLocationActivity extends AppCompatActivity {
    private static final String TAG = "ChangeLocationActivity";
    private EditText edtCit, edtSubs, edtVille, edtPass;
    private TextInputLayout edtCity,  edtSubdis, edtVill, edtAdd, txtNewPass, txtReNewPass;
    private TextView txtTitle;
    private Button btnChange;
    String token, id_kota = "", id_vill = "", id_prov = "31";
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$"
            );
    String idTukang = Prefs.getString(SessionPrefs.U_ID, "");
    String token_login = Prefs.getString(SessionPrefs.TOKEN_LOGIN, "");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);
        findView();
    }

    private void findView(){
        edtCit = findViewById(R.id.edt_mo_city);
        edtSubs = findViewById(R.id.edt_mo_subdis);
        edtSubdis = findViewById(R.id.txt_mo_subdis);
        edtVill = findViewById(R.id.txt_mo_vill);
        edtVille = findViewById(R.id.edt_mo_vill);
        edtCity = findViewById(R.id.txt_mo_city);
        edtPass = findViewById(R.id.edt_password_location);
        btnChange = findViewById(R.id.btn_edit_location);
        txtNewPass = findViewById(R.id.txt_new_password_profile);
        txtReNewPass = findViewById(R.id.txt_repassword_profile);
        edtAdd = findViewById(R.id.txt_mo_address);
        txtTitle = findViewById(R.id.txt_title_profile);
        Intent i = getIntent();
        final String code = i.getStringExtra("code");
        if (code.equals("1")){
            txtTitle.setText(getString(R.string.change_location));
            txtNewPass.setVisibility(View.GONE);
            txtReNewPass.setVisibility(View.GONE);
            edtCity.setVisibility(View.VISIBLE);
            edtSubdis.setVisibility(View.VISIBLE);
            edtAdd.setVisibility(View.VISIBLE);
            edtVill.setVisibility(View.VISIBLE);
        }else{
            txtTitle.setText(getString(R.string.change_password));
            txtNewPass.setVisibility(View.VISIBLE);
            txtReNewPass.setVisibility(View.VISIBLE);
            edtCity.setVisibility(View.GONE);
            edtSubdis.setVisibility(View.GONE);
            edtAdd.setVisibility(View.GONE);
            edtVill.setVisibility(View.GONE);
        }




        edtCit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtSubs.setText("");
                edtVille.setText("");
            }
        });

        edtSubs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtVille.setText("");
            }
        });

        edtVille.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtCit.getText().toString().isEmpty() && !edtSubs.getText().toString().isEmpty()) {
                    showVillDialog(edtVille, UrlClass.URL_GET_VIL, id_vill);
                } else {
                    Toasty.warning(ChangeLocationActivity.this, "Districts/City and sub districts can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        edtCit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCityDialog(edtCit, UrlClass.URL_GET_KAB, id_prov);
            }
        });

        edtSubs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edtCit.getText().toString().isEmpty()) {
                    showSubsDialog(edtSubs, UrlClass.URL_GET_KAC, id_kota);
                } else {
                    Toasty.warning(ChangeLocationActivity.this, "Districts/City can't be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vill = edtVille.getText().toString().trim().toLowerCase();
                String subs = edtSubs.getText().toString().trim().toLowerCase();
                String cit = edtCit.getText().toString().trim().toLowerCase();
                String alamat = edtAdd.getEditText().getText().toString().trim().toLowerCase();
                String password = edtPass.getText().toString().trim();
                String add = alamat + ", " + vill + ", " + subs + ", " + cit;
                if (code.equals("1")) {
                    if (!vill.isEmpty() && !subs.isEmpty() && !cit.isEmpty() && !alamat.isEmpty() && !password.isEmpty()) {
                        changeLocationCondition(cit, subs, vill, alamat, password, add);
                    } else {
                        Toasty.warning(ChangeLocationActivity.this, getString(R.string.fill_all), Toasty.LENGTH_SHORT, true).show();
                    }
                }else{
                    changePass();
                }
            }
        });

    }

    private void changePass() {
        String newPass = txtNewPass.getEditText().getText().toString().trim();
        String rePass = txtReNewPass.getEditText().getText().toString().trim();
        String pass = edtPass.getText().toString().trim();
        if (!newPass.isEmpty() && !pass.isEmpty() && rePass.equals(newPass) && PASSWORD_PATTERN.matcher(newPass).matches()) {
            changePassword(newPass, pass);
        } else if (!PASSWORD_PATTERN.matcher(newPass).matches()) {
            Toasty.warning(ChangeLocationActivity.this, R.string.password_must_contains, Toast.LENGTH_LONG, true).show();
        } else if (!rePass.equals(newPass)) {
            Toasty.warning(ChangeLocationActivity.this, R.string.doesnt_match, Toast.LENGTH_SHORT, true).show();
        } else {
            Toasty.warning(ChangeLocationActivity.this, R.string.fill_all, Toast.LENGTH_SHORT, true).show();
        }
    }

    private void changePassword(String newPass, String pass) {
        final KProgressHUD hud = new KProgressHUD(this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_CHANGE_PASS)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("new_password", newPass)
                .addBodyParameter("password", pass)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toasty.success(ChangeLocationActivity.this, "Change data success!", Toast.LENGTH_SHORT, true).show();
                                finish();
                            } else {
                                Toasty.warning(ChangeLocationActivity.this, response.getMsg(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void changeLocationCondition(String kota, String kec, String kel, String alamat, String pass, final String value) {
        final KProgressHUD hud = new KProgressHUD(this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_CHANGE_LOCATION)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token_login", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .addBodyParameter("kota", kota)
                .addBodyParameter("kec", kec)
                .addBodyParameter("kel", kel)
                .addBodyParameter("alamat", alamat)
                .addBodyParameter("password", pass)
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Toasty.success(ChangeLocationActivity.this, "Change data success!", Toast.LENGTH_SHORT, true).show();
                                Intent i = new Intent();
                                i.putExtra("extra", value);
                                setResult(RESULT_OK, i);
                                finish();
                            } else {
                                Toasty.warning(ChangeLocationActivity.this, response.getMsg(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void showCityDialog(final EditText edt, final String jenis, final String id) {
        final Dialog dialog = new Dialog(ChangeLocationActivity.this);
        dialog.setContentView(R.layout.dialog_address);
        RecyclerView rv_address;
        Button btnCancel;
        final AddressAdapter adapter;
        final ProgressBar progressBar;
        final ArrayList<DataItem> list = new ArrayList<>();
        rv_address = dialog.findViewById(R.id.rv_alamat);
        progressBar = dialog.findViewById(R.id.progress_bar);
        btnCancel = dialog.findViewById(R.id.btn_cancel_address);

        adapter = new AddressAdapter(this, list);
        rv_address.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_address.setLayoutManager(linearLayoutManager);
        rv_address.setAdapter(adapter);

        AndroidNetworking.get(UrlClass.URL_GET_UNICODE)
                .setTag("city")
                .build()
                .getAsOkHttpResponseAndObject(UnicodeModel.class, new OkHttpResponseAndParsedRequestListener<UnicodeModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UnicodeModel response) {
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Prefs.putString(SessionPrefs.UNICODE, "");
                                token = response.getToken();
                                AndroidNetworking.get(UrlClass.URL_GET_ADDRESS + token + jenis + id)
                                        .setTag("city")
                                        .build()
                                        .getAsOkHttpResponseAndObject(CityModel.class, new OkHttpResponseAndParsedRequestListener<CityModel>() {
                                            @Override
                                            public void onResponse(Response okHttpResponse, CityModel response) {
                                                progressBar.setVisibility(View.GONE);
                                                if (okHttpResponse.isSuccessful()) {
                                                    if (response.getCode() == 200) {
                                                        for (int i = 0; i < response.getData().size(); i++) {
                                                            final DataItem item = new DataItem();
                                                            item.setName(response.getData().get(i).getName());
                                                            item.setId(response.getData().get(i).getId());
                                                            list.add(item);
                                                        }
                                                        adapter.updateList(list);
                                                        adapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
                                                            @Override
                                                            public void onItemClick(View view, int position, DataItem model) {
                                                                edt.setText(model.getName());
                                                                id_kota = String.valueOf(model.getId());
                                                                dialog.dismiss();
                                                            }
                                                        });
                                                    } else {
                                                        dialog.dismiss();
                                                        progressBar.setVisibility(View.GONE);
                                                        Toasty.warning(ChangeLocationActivity.this, R.string.something_wrong, Toasty.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                dialog.dismiss();
                                                progressBar.setVisibility(View.GONE);
                                                if (anError.getErrorCode() != 0) {
                                                    Log.d(TAG, "onError: " + anError.getErrorDetail());
                                                    Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                                                } else {
                                                    Log.d(TAG, "onError: " + anError.getErrorCode());
                                                    Log.d(TAG, "onError: " + anError.getErrorBody());
                                                    Log.d(TAG, "onError: " + anError.getErrorDetail());
                                                    Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                                                }
                                            }
                                        });
                            } else {
                                dialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toasty.warning(ChangeLocationActivity.this, R.string.something_wrong, Toasty.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressBar.setVisibility(View.GONE);
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.cancel("city");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showSubsDialog(final EditText edt, final String jenis, final String id) {
        final Dialog dialog = new Dialog(ChangeLocationActivity.this);
        dialog.setContentView(R.layout.dialog_address);
        RecyclerView rv_address;
        Button btnCancel;
        final AddressAdapter adapter;
        final ProgressBar progressBar;
        final ArrayList<DataItem> list = new ArrayList<>();
        rv_address = dialog.findViewById(R.id.rv_alamat);
        progressBar = dialog.findViewById(R.id.progress_bar);
        btnCancel = dialog.findViewById(R.id.btn_cancel_address);

        adapter = new AddressAdapter(this, list);
        rv_address.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_address.setLayoutManager(linearLayoutManager);
        rv_address.setAdapter(adapter);
        AndroidNetworking.get(UrlClass.URL_GET_ADDRESS + token + jenis + id)
                .setTag("city")
                .build()
                .getAsOkHttpResponseAndObject(CityModel.class, new OkHttpResponseAndParsedRequestListener<CityModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, CityModel response) {
                        progressBar.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem item = new DataItem();
                                    item.setName(response.getData().get(i).getName());
                                    item.setId(response.getData().get(i).getId());
                                    list.add(item);
                                }
                                adapter.updateList(list);
                                adapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, DataItem model) {
                                        edt.setText(model.getName());
                                        id_vill = String.valueOf(model.getId());
                                        Log.d(TAG, "onItemClick: " + id_vill);
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toasty.warning(ChangeLocationActivity.this, R.string.something_wrong, Toasty.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.cancel("city");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

    private void showVillDialog(final EditText edt, final String jenis, final String id) {
        final Dialog dialog = new Dialog(ChangeLocationActivity.this);
        dialog.setContentView(R.layout.dialog_address);
        RecyclerView rv_address;
        Button btnCancel;
        final AddressAdapter adapter;
        final ProgressBar progressBar;
        final ArrayList<DataItem> list = new ArrayList<>();
        rv_address = dialog.findViewById(R.id.rv_alamat);
        progressBar = dialog.findViewById(R.id.progress_bar);
        btnCancel = dialog.findViewById(R.id.btn_cancel_address);

        adapter = new AddressAdapter(this, list);
        rv_address.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_address.setLayoutManager(linearLayoutManager);
        rv_address.setAdapter(adapter);

        AndroidNetworking.get(UrlClass.URL_GET_ADDRESS + token + jenis + id)
                .setTag("city")
                .build()
                .getAsOkHttpResponseAndObject(VillModel.class, new OkHttpResponseAndParsedRequestListener<VillModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, VillModel response) {
                        progressBar.setVisibility(View.GONE);
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                for (int i = 0; i < response.getData().size(); i++) {
                                    final DataItem item = new DataItem();
                                    item.setName(response.getData().get(i).getName());
                                    list.add(item);
                                }
                                adapter.updateList(list);
                                adapter.SetOnItemClickListener(new AddressAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position, DataItem model) {
                                        edt.setText(model.getName());
                                        dialog.dismiss();
                                    }
                                });
                            } else {
                                dialog.dismiss();
                                progressBar.setVisibility(View.GONE);
                                Toasty.warning(ChangeLocationActivity.this, R.string.something_wrong, Toasty.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        dialog.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ChangeLocationActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidNetworking.cancel("city");
                dialog.dismiss();
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }
}
