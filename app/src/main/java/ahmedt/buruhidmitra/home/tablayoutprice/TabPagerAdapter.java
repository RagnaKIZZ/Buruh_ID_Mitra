package ahmedt.buruhidmitra.home.tablayoutprice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ahmedt.buruhidmitra.R;

public class TabPagerAdapter extends PagerAdapter {
    Context context;
    List<ScreenItem> list;

    public TabPagerAdapter(Context context, List<ScreenItem> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_tab, null);
        TextView txtTitle = view.findViewById(R.id.txt_price);

        txtTitle.setText(list.get(position).getIncome());

        container.addView(view);

        return view;
    }

    @StringRes
    private final int[] TAB_TITLES = new int[]{
            R.string.semuawaktu, R.string.bulanini, R.string.harini
    };

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
