package ahmedt.buruhidmitra.home;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.home.modelhistory.DataItem;
import ahmedt.buruhidmitra.utils.HelperClass;
import ahmedt.buruhidmitra.utils.UrlClass;

public class LastActivitiesAdapter extends RecyclerView.Adapter<LastActivitiesAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public LastActivitiesAdapter(Context context, ArrayList<DataItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LastActivitiesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_last_activities, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LastActivitiesAdapter.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            Locale locale = new Locale("in", "ID");
            NumberFormat format = NumberFormat.getCurrencyInstance(locale);
            double price = Double.parseDouble(item.getHarga());
            double realPrice = price - (price*0.1);
            int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            genericViewHolder.txtPrice.setText(format.format(realPrice));
            genericViewHolder.txtName.setText(item.getNama());
            genericViewHolder.txtAdd.setText(item.getAlamat());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            String waktu = "";
            try {
                date = simpleDateFormat.parse(item.getFinishDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            HelperClass.getDate(date, waktu, genericViewHolder.txtTgl);
            genericViewHolder.relativeLayout.setBackgroundColor(randomAndroidColor);

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private DataItem getItem(int position) {
        return list.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, DataItem model);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName, txtTgl, txtAdd, txtPrice;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_activities);
            this.txtName = (TextView) itemView.findViewById(R.id.txt_name_activities);
            this.txtTgl = (TextView) itemView.findViewById(R.id.txt_tgl_activities);
            this.txtAdd = (TextView) itemView.findViewById(R.id.txt_address_activities);
            this.txtPrice = (TextView) itemView.findViewById(R.id.txt_price_activities);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }
}
