package ahmedt.buruhidmitra.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.OkHttpResponseAndParsedRequestListener;
import com.google.android.material.textfield.TextInputLayout;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.regex.Pattern;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.UniversalModel;
import ahmedt.buruhidmitra.utils.UrlClass;
import es.dmoral.toasty.Toasty;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    //    private Spinner spinner;
//    int counter_team = 3;
//    int counter_param;
//    int spin_param;
    private RadioGroup radioGroup;
    private static final String TAG = "RegisterActivity";
    private TextView txtToLogin;
    private TextView txtCounter;
    private TextInputLayout txtName, txtEmail, txtPhone, txtPass, txtRePass;
    private CheckBox checkBox;
    private Button btnRegister;
    private Button btnPlus, btnMinus;
    private RelativeLayout lnCounter;
    int counter = 1;
    private Context ctx;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[a-z])" +
                    "(?=.*[A-Z])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$"
            );

    private static final Pattern PHONE_NUMB
            = Pattern.compile(
            "^[+]?[08][0-9]{10,13}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ctx = this;
        findView();
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRegister();
            }
        });

        txtToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void validateRegister() {
        String name, email, phone, password, rePassword;
        name = txtName.getEditText().getText().toString().trim();
        email = txtEmail.getEditText().getText().toString().trim().toLowerCase();
        phone = txtPhone.getEditText().getText().toString().trim();
        password = txtPass.getEditText().getText().toString().trim();
        rePassword = txtRePass.getEditText().getText().toString().trim();

        if (!name.isEmpty() && !password.isEmpty() && !email.isEmpty() && !phone.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                PHONE_NUMB.matcher(phone).matches() && PASSWORD_PATTERN.matcher(password).matches() && !rePassword.isEmpty() &&
                rePassword.matches(password) && checkBox.isChecked()) {
            registerUser(name, email, phone, password);
        }

        if (!checkBox.isChecked()) {
            checkBox.setTextColor(Color.RED);
        } else {
            checkBox.setTextColor(Color.BLACK);
        }

        if (name.isEmpty()) {
            txtName.setError(getString(R.string.cant_empty));
        } else {
            txtName.setErrorEnabled(false);
        }

        if (email.isEmpty()) {
            txtEmail.setError(getString(R.string.cant_empty));
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            txtEmail.setError(getString(R.string.valid_email));
        } else {
            txtEmail.setErrorEnabled(false);
        }

        if (phone.isEmpty()) {
            txtPhone.setError(getString(R.string.cant_empty));
        } else if (!PHONE_NUMB.matcher(phone).matches()) {
            txtPhone.setError(getString(R.string.valid_number));
        } else {
            txtPhone.setErrorEnabled(false);
        }

        if (password.isEmpty()) {
            txtPass.setError(getString(R.string.cant_empty));
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            txtPass.setError(getString(R.string.password_must_contains));
        } else {
            txtPass.setErrorEnabled(false);
        }

        if (rePassword.isEmpty()) {
            txtRePass.setError(getString(R.string.cant_empty));
        } else if (!rePassword.matches(password)) {
            txtRePass.setError(getString(R.string.doesnt_match));
        } else {
            txtRePass.setErrorEnabled(false);
        }
    }

    private void registerUser(String name, String email, String phone, String password) {
        final KProgressHUD hud = new KProgressHUD(RegisterActivity.this);
        HelperClass.loading(hud, null, null, false);
        AndroidNetworking.post(UrlClass.URL_REGISTER)
                .addBodyParameter("nama", name)
                .addBodyParameter("email", email)
                .addBodyParameter("telepon", phone)
                .addBodyParameter("password", password)
                .addBodyParameter("anggota", String.valueOf(counter))
                .build()
                .getAsOkHttpResponseAndObject(UniversalModel.class, new OkHttpResponseAndParsedRequestListener<UniversalModel>() {
                    @Override
                    public void onResponse(Response okHttpResponse, UniversalModel response) {
                        hud.dismiss();
                        if (okHttpResponse.isSuccessful()) {
                            if (response.getCode() == 200) {
                                startActivity(new Intent(RegisterActivity.this, SuccActivity.class));
                                finish();
                            } else {
                                Toasty.warning(ctx, response.getMsg(), Toast.LENGTH_SHORT, true).show();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        hud.dismiss();
                        if (anError.getErrorCode() != 0) {
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(RegisterActivity.this, R.string.server_error, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Log.d(TAG, "onError: " + anError.getErrorCode());
                            Log.d(TAG, "onError: " + anError.getErrorBody());
                            Log.d(TAG, "onError: " + anError.getErrorDetail());
                            Toasty.error(RegisterActivity.this, R.string.cek_internet, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    private void findView() {
//        spinner = findViewById(R.id.spinner_type_worker);
//        addItemSpinner();
        lnCounter = findViewById(R.id.rv_counter);
        txtCounter = findViewById(R.id.txt_counter);
        btnMinus = findViewById(R.id.btn_minus);
        btnPlus = findViewById(R.id.btn_plus);
        radioGroup = findViewById(R.id.radio_group);
        txtToLogin = findViewById(R.id.txt_login_register);
        btnRegister = findViewById(R.id.btn_regist_register);
        txtEmail = findViewById(R.id.txt_email_register);
        txtPhone = findViewById(R.id.txt_hp_register);
        txtName = findViewById(R.id.txt_name_register);
        txtPass = findViewById(R.id.txt_password_register);
        txtRePass = findViewById(R.id.txt_repassword_register);
        checkBox = findViewById(R.id.checkbox_register);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_1:
                        lnCounter.setVisibility(View.GONE);
                        counter = 1;
                        break;
                    case 1:
                        lnCounter.setVisibility(View.VISIBLE);
                        counter = 3;
                        txtCounter.setText(String.valueOf(counter));
                        break;
                }
            }
        });

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter >= 3 && counter < 7) {
                    counter++;
                    txtCounter.setText(String.valueOf(counter));
                } else if (counter >= 7) {
                    Toasty.warning(RegisterActivity.this, "Reach number of maximum worker", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > 3 && counter <= 7) {
                    counter--;
                    txtCounter.setText(String.valueOf(counter));
                } else if (counter <= 3) {
                    Toasty.warning(RegisterActivity.this, "Reach number of minimum worker", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void addItemSpinner(){
//        String select = "-- Select --";
//        String individu = getString(R.string.individu_worker);
//        String team = getString(R.string.team_worker);
//        List<String> listSpinner = new ArrayList<>();
//        listSpinner.add(select);
//        listSpinner.add(individu);
//        listSpinner.add(team);
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout, listSpinner);
//        spinner.setAdapter(dataAdapter);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                switch (position){
//                    case 0:
//                        spin_param = 0;
//                        break;
//                    case 1:
//                        spin_param = 1;
//                        counter_param = 1;
//                        break;
//                    case 2:
//                        counter_param = counter_team;
//                        spin_param = 2;
//                        break;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
}
