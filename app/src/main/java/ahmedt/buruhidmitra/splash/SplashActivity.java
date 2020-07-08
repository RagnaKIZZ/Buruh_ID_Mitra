package ahmedt.buruhidmitra.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.HomeActivity;
import ahmedt.buruhidmitra.login.LoginActivity;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UpdateHelper;

public class SplashActivity extends AppCompatActivity implements UpdateHelper.OnUpdateCheckLinstener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UpdateHelper.with(this)
                .onUpdateCheck(this)
                .check();


        if (UpdateHelper.keyUpdateEnable()){

        }else{
            Toast.makeText(this, R.string.announcebeta, Toast.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Prefs.getString(SessionPrefs.isLogin, "").matches("1")) {
                        startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }
                }
            }, 2000);
        }
    }

    @Override
    public void onUpdateCheckListener(final String urlApp) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.newupdate);
        alert.setMessage(R.string.plzupdate)
                .setCancelable(false)
                .setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlApp)));
                        }
                    }
                })
                .setNegativeButton(R.string.nothx, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finishAffinity();
                    }
                });
        alert.show();
    }
}
