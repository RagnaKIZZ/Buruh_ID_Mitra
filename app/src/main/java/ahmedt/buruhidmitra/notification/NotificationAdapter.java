package ahmedt.buruhidmitra.notification;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import ahmedt.buruhidmitra.R;
import ahmedt.buruhidmitra.notification.modelnotifikasi.DataItem;
import ahmedt.buruhidmitra.utils.HelperClass;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private Context context;
    private ArrayList<DataItem> list = new ArrayList<>();

    private OnItemClickListener mItemClickListener;

    public NotificationAdapter(Context context, ArrayList<DataItem> list) {
        this.context = context;
        this.list = list;
    }

    public void updateList(ArrayList<DataItem> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notif, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final DataItem item = getItem(position);
            ViewHolder genericViewHolder = (ViewHolder) holder;
            genericViewHolder.txtTitle.setText(item.getTitle());
            genericViewHolder.txtDesc.setText(item.getMessage());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            String waktu = "";
            if (item.getIsRead().matches("1")){
                genericViewHolder.txtTitle.setTextColor(Color.GRAY);
                genericViewHolder.txtDesc.setTextColor(Color.GRAY);
                genericViewHolder.txtTgl.setTextColor(Color.GRAY);
                genericViewHolder.vBadge.setVisibility(View.GONE);
            }else{
                genericViewHolder.vBadge.setVisibility(View.VISIBLE);
                genericViewHolder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                genericViewHolder.txtDesc.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                genericViewHolder.txtTgl.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            }
            try {
                date = simpleDateFormat.parse(item.getCreateDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            HelperClass.getDate(date, waktu, genericViewHolder.txtTgl);
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
        private TextView txtTitle, txtTgl, txtDesc;
        private View vBadge;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            this.txtTitle = (TextView) itemView.findViewById(R.id.txt_title_notif);
            this.txtTgl = (TextView) itemView.findViewById(R.id.txt_date_notif);
            this.txtDesc = (TextView) itemView.findViewById(R.id.txt_detail_notif);
            this.vBadge = (View) itemView.findViewById(R.id.v_badge);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), list.get(getAdapterPosition()));
                }
            });
        }
    }
}
