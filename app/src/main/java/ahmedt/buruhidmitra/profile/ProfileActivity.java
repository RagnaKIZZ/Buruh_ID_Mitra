package ahmedt.buruhidmitra.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.HomeActivity;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UrlClass;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtName, txtEmail, txtPhone, txtLocation, txtRating;
    private ImageView imgProfile;
    private ProgressBar progressBar;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findView();
    }

    private void findView(){
        txtName = findViewById(R.id.txt_name_account);
        txtEmail = findViewById(R.id.txt_email_account);
        txtPhone = findViewById(R.id.txt_phone_account);
        txtLocation = findViewById(R.id.txt_location_account);
        txtRating = findViewById(R.id.txt_rating_account);
        imgProfile = findViewById(R.id.img_account);
        progressBar = findViewById(R.id.progress_bar);
        ratingBar = findViewById(R.id.rating_sheet);
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

        if (Prefs.getString(SessionPrefs.FOTO, "").isEmpty()){
            Glide.with(this)
                    .load(R.drawable.blank_profile)
                    .into(imgProfile);
        }else {
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
        if (rating >= 4 && rating <= 5){
            msg = "Keep up your good work!";
        }else if (rating < 4 && rating >= 3){
            msg = "You should be more active!";
        }else if (rating < 3){
            msg = "What is wrong with you? tell me your problem";
        }
        ratingBar.setRating((float) rating);
        ratingBar.setIsIndicator(true);
//        ratingBar.setEnabled(false);
        txtRating.setText("("+Prefs.getString(SessionPrefs.RATING, "")+", "+msg+")");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
                default:
                    return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
