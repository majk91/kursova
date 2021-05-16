package com.mike.kursova_oop_db.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.ui.fragments.ShopFragmentMain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GoodsAdapter extends  RecyclerView.Adapter<GoodsAdapter.GoodViewHolder> {

    public GoodsAdapter(Context ctx) {
        mContext = ctx;
    }

    Context mContext;

    private List<Good> list = new ArrayList<>();

    public void setItems(Collection<Good> tempList) {
        list.clear();
        list.addAll(tempList);
        notifyDataSetChanged();
    }

    public void clearItems() {
        list.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int item) {
        list.remove(item);
        notifyDataSetChanged();
    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_shop_item, parent, false);
        return new GoodViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(GoodViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    class GoodViewHolder extends RecyclerView.ViewHolder {
        private ImageView realImage, emptyImage;
        private TextView textTitle, category, info;
        private LinearLayout wrap;

        public GoodViewHolder(View itemView) {
            super(itemView);
            realImage = itemView.findViewById(R.id.realImage);
            emptyImage = itemView.findViewById(R.id.emptyImage);
            textTitle = itemView.findViewById(R.id.textTitle);
            category = itemView.findViewById(R.id.category);
            info = itemView.findViewById(R.id.info);
            wrap = itemView.findViewById(R.id.wrap);
        }

        public void bind(Good item, int position) {
            //emptyImage.setVisibility(View.VISIBLE);
            //realImage.setVisibility(View.GONE);
            if(item.getCount()<=0)
                info.setVisibility(View.VISIBLE);
            else
                info.setVisibility(View.GONE);

            textTitle.setText(item.getName()+" | "+item.getPrice()+"$");
            category.setText(item.getCatName());
            if (!"".equals(item.getPhotoURI())) {
                Glide.with(mContext).load(item.getPhotoURI()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(realImage);
                realImage.setVisibility(View.VISIBLE);
                emptyImage.setVisibility(View.GONE);
            }
            wrap.setOnClickListener(l->{
                onClickListener.onClickGood(item);
            });
        }
    }

    private ShopFragmentMain.OnAdapterClickListener onClickListener;

    public void setAboutDataListener(ShopFragmentMain.OnAdapterClickListener listener) {
        this.onClickListener = listener;
    }
}