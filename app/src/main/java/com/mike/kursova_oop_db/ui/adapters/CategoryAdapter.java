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
import com.mike.kursova_oop_db.data.models.Category;
import com.mike.kursova_oop_db.ui.fragments.ShopFragmentMain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {

    public CategoryAdapter(Context ctx) {
        mContext = ctx;
    }

    Context mContext;

    private List<Category> list = new ArrayList<>();

    public void setItems(Collection<Category> tempList) {
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
    public CatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category, parent, false);
        return new CatViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(CatViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    class CatViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private LinearLayout wrap;

        public CatViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.realImage);
            name = itemView.findViewById(R.id.name);
            wrap = itemView.findViewById(R.id.wrap);
        }

        public void bind(Category category, int position) {
            if (!category.getPhotoURI().equals("")) {
                Glide.with(mContext).load(category.getPhotoURI()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(image);
            }

            name.setText(category.getName());
            if(category.getActiveInUi())
                wrap.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            else
                wrap.setBackgroundColor(mContext.getResources().getColor(R.color.white));


            wrap.setOnClickListener(v -> {
                onClickListener.onClickCategory(category);
                for(Category item: list) item.setActiveInUi(false);
                list.get(position).setActiveInUi(true);
                notifyDataSetChanged();

            });
        }
    }

    private ShopFragmentMain.OnAdapterClickListener onClickListener;

    public void setAboutDataListener(ShopFragmentMain.OnAdapterClickListener listener) {
        this.onClickListener = listener;
    }

}