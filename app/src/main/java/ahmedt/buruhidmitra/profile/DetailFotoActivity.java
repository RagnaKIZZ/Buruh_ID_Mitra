package ahmedt.buruhidmitra.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.pixplicity.easyprefs.library.Prefs;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.SessionPrefs;
import ahmedt.buruhidmitra.utils.UrlClass;

public class DetailFotoActivity extends AppCompatActivity {
    private static final String TAG = "DetailFotoActivity";
    ProgressBar progressBar;
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_foto);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.pp));

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
        photoView = (PhotoView) findViewById(R.id.photo_view);

        String foto = Prefs.getString(SessionPrefs.FOTO, "");
        if (foto.isEmpty()) {
            Glide.with(this)
                    .load(R.drawable.blank_profile)
                    .into(photoView);
        } else {
            HelperClass.loadGambar(DetailFotoActivity.this, UrlClass.URL_FOTO_TUKANG + foto, progressBar, photoView);

        }
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
