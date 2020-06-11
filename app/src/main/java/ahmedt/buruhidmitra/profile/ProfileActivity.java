package ahmedt.buruhidmitra.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.bumptech.glide.Glide;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.HomeActivity;
import ahmedt.buruhidmitra.login.LoginActivity;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtName, txtEmail, txtPhone, txtLocation, txtRating, txtAnggota;
    private ImageView imgProfile;
    private ProgressBar progressBar;
    private RatingBar ratingBar;
    private Button btnLogout;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findView();
    }

    private void findView() {
        txtName = findViewById(R.id.txt_name_account);
        txtEmail = findViewById(R.id.txt_email_account);
        txtPhone = findViewById(R.id.txt_phone_account);
        txtLocation = findViewById(R.id.txt_location_account);
        txtRating = findViewById(R.id.txt_rating_account);
        txtAnggota = findViewById(R.id.txt_anggota_account);
        imgProfile = findViewById(R.id.img_account);
        progressBar = findViewById(R.id.progress_bar);
        ratingBar = findViewById(R.id.rating_sheet);
        btnLogout = findViewById(R.id.btn_logout_account);
        progressBar.setVisibility(View.GONE);
        txtName.setText(Prefs.getString(SessionPrefs.NAMA, ""));
        txtEmail.setText(Prefs.getString(SessionPrefs.EMAIL, ""));
        txtPhone.setText(Prefs.getString(SessionPrefs.TELEPON, ""));
        txtLocation.setText(Prefs.getString(SessionPrefs.ALAMAT, ""));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.profiles));

        if (Prefs.getString(SessionPrefs.FOTO, "").isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.blank_profile)
                    .into(imgProfile);
        } else {
            HelperClass.loadGambar(this, UrlClass.URL_FOTO_TUKANG + Prefs.getString(SessionPrefs.FOTO, ""), progressBar, imgProfile);
        }

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, DetailFotoActivity.class);
                View shareView = imgProfile;
                String transitionName = getString(R.string.img);
                ActivityOptions transOpt = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    transOpt = ActivityOptions.makeSceneTransitionAnimation(ProfileActivity.this, shareView, transitionName);
                    startActivity(i, transOpt.toBundle());
                } else {
                    startActivity(i);
                }
            }
        });
        double rating = Double.parseDouble(Prefs.getString(SessionPrefs.RATING, ""));
        String msg = "";
        if (rating >= 4 && rating <= 5) {
            msg = getString(R.string.keepup);
        } else if (rating < 4 && rating >= 3) {
            msg = getString(R.string.moreactive);
        } else if (rating < 3) {
            msg = getString(R.string.tellmee);
        }

        String anggota = Prefs.getString(SessionPrefs.ANGGOTA, "");
        String msgAnggota = "";
        if (anggota.matches("1")) {
            msgAnggota = getString(R.string.individu_worker);
        } else {
            msgAnggota = getString(R.string.tim_work) + " " + anggota + " " + getString(R.string.people);
        }
        txtAnggota.setText(msgAnggota);
        ratingBar.setRating((float) rating);
        ratingBar.setIsIndicator(true);
//        ratingBar.setEnabled(false);
        txtRating.setText("(" + Prefs.getString(SessionPrefs.RATING, "") + ", " + msg + ")");

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
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

    private void logoutTukang() {
        final KProgressHUD hud = new KProgressHUD(this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_LOGOUT)
                .addBodyParameter("id", Prefs.getString(SessionPrefs.U_ID, ""))
                .addBodyParameter("token", Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""))
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                                Prefs.clear();
                                startActivity(intent);
                                finishAffinity();
                            } else {
                                Toasty.warning(ProfileActivity.this, response.getMsg(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ProfileActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(ProfileActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void logOut() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.logout);
        alert.setMessage(getString(R.string.are_logout))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: " + Prefs.getString(SessionPrefs.TOKEN_LOGIN, ""));
                        logoutTukang();

                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alert.show();
    }
}
