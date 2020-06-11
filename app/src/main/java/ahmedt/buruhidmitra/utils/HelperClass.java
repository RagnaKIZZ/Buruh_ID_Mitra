package ahmedt.buruhidmitra.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import ahmedt.buruhidmitra.R;

public class HelperClass {
    public static void expand(LinearLayout mLinearLayout){
        mLinearLayout.setVisibility(View.VISIBLE);

        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        mLinearLayout.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, mLinearLayout.getMeasuredHeight(), mLinearLayout);
        mAnimator.start();
    }

    private static ValueAnimator slideAnimator(int start, int end, final LinearLayout mLinearLayout){
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (Integer) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = mLinearLayout.getLayoutParams();
                layoutParams.height = value;
                mLinearLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    public static void collapse(final LinearLayout mLinearLayout){
        int finalHeight = mLinearLayout.getHeight();

        ValueAnimator animator = slideAnimator(finalHeight, 0, mLinearLayout);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLinearLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
    public static void serverError(Context context, LinearLayout linearLayout, ImageView img, TextView textView){
        linearLayout.setVisibility(View.VISIBLE);
        img.setImageResource(R.drawable.error);
        textView.setText(context.getString(R.string.servererrorr));
    }

    public static void convertPirce(TextView textView, double harga){
        Locale locale = new Locale("in", "ID");
        NumberFormat format = NumberFormat.getCurrencyInstance(locale);
        textView.setText(format.format(harga));
    }

    public static void InetError(Context context, LinearLayout linearLayout, ImageView img, TextView textView){
        linearLayout.setVisibility(View.VISIBLE);
        img.setImageResource(R.drawable.nointernet);
        textView.setText(context.getString(R.string.nointernett));
    }

    public static void emptyError(LinearLayout linearLayout, ImageView img, TextView textView, String msg){
        linearLayout.setVisibility(View.VISIBLE);
        img.setImageResource(R.drawable.noworker);
        textView.setText(msg);
    }
    public static void getDate (Date date, String time, TextView txt){
        long now = System.currentTimeMillis();
        if (Math.abs(now - date.getTime()) > TimeUnit.MINUTES.toMillis(1)) {
            time = String.valueOf(DateUtils.getRelativeTimeSpanString(date.getTime(), now,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
                            | DateUtils.FORMAT_NUMERIC_DATE));
        } else {
            time = "just now";
        }
        txt.setText(time);
    }

    public static void loadGambar(Context context, String url, final ProgressBar progressBar, final ImageView img) {
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(context)
                .load(url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        img.setImageResource(R.drawable.blank_profile);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(img);
    }

    public static void loading(KProgressHUD hud, String judul, String deskripsi, boolean cancelable){
        if (judul==null){
            judul="Loading";
        }
        if (deskripsi==null){
            deskripsi="Please Wait";
        }
        hud.setLabel(judul);
        hud.setDimAmount(0.5f);
        hud.setDetailsLabel(deskripsi);
        hud.setCancellable(cancelable);
        hud.setCornerRadius(14);
        hud.show();
    }
}
