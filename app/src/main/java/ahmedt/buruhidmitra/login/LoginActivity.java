package ahmedt.buruhidmitra.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.HomeActivity;
import ahmedt.buruhidmitra.login.modellogin.LoginModel;
import ahmedt.buruhidmitra.register.RegisterActivity;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextView txtToRegist, txtToForgot;
    private Button btnLogin;
    private TextInputLayout edtEmail, edtPassword;
    private TextInputEditText txtDone;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this;
        findView();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtToRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void findView() {
        txtToForgot = findViewById(R.id.txt_forgot_password);
        txtToRegist = findViewById(R.id.txt_register_login);
        btnLogin = findViewById(R.id.btn_login_login);
        edtEmail = findViewById(R.id.txt_email_login);
        edtPassword = findViewById(R.id.txt_password_login);
        txtDone = findViewById(R.id.edt_pswd_login);

        txtDone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true;
                }
                return false;
            }
        });


    }

    private void login() {
        String email = edtEmail.getEditText().getText().toString().trim().toLowerCase();
        String password = edtPassword.getEditText().getText().toString().trim();

        if (!email.isEmpty() && !password.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginUser(email, password);
        }

        if (email.isEmpty()) {
            edtEmail.setError(getString(R.string.cant_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError(getString(R.string.valid_email));
        } else {
            edtEmail.setErrorEnabled(false);
            edtEmail.setError(null);
        }

        if (password.isEmpty()) {
            edtPassword.setError(getString(R.string.cant_empty));
        } else {
            edtPassword.setErrorEnabled(false);
        }
    }

    private void loginUser(String email, String password) {
        final KProgressHUD hud = new KProgressHUD(context);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_LOGIN)
                .addBodyParameter("email", email)
                .addBodyParameter("password", password)
                .build()
                .getAsOkHttpResponseAndObject(LoginModel.class, new OkHttpResponseAndParsedRequestListener<LoginModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, LoginModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                Prefs.putString(SessionPrefs.U_ID, response.getData().getTukangId());
                                Prefs.putString(SessionPrefs.NAMA, response.getData().getNama());
                                Prefs.putString(SessionPrefs.EMAIL, response.getData().getEmail());
                                Prefs.putString(SessionPrefs.TELEPON, response.getData().getTelepon());
                                Prefs.putString(SessionPrefs.FOTO, response.getData().getFoto());
                                Prefs.putString(SessionPrefs.TOKEN_LOGIN, response.getData().getTokenLogin());
                                Prefs.putString(SessionPrefs.isActive, response.getData().getAktif());
                                Prefs.putString(SessionPrefs.ALAMAT, response.getData().getAlamat());
                                Prefs.putString(SessionPrefs.ANGGOTA, response.getData().getAnggota());
                                Prefs.putString(SessionPrefs.RATING, response.getData().getRating());
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toasty.warning(context, response.getMsg(), Toast.LENGTH_SHORT, true).show();
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(LoginActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(LoginActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }
}
