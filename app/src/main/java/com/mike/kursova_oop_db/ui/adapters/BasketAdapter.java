package com.mike.kursova_oop_db.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.BasketItems;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.ui.fragments.BasketFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasketAdapter extends  RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    public BasketAdapter(Context ctx) {
        mContext = ctx;
    }

    Context mContext;

    private List<BasketItems> list = new ArrayList<>();

    public void setItems(HashMap<Long, BasketItems> tempList) {
        list.clear();
        for(Map.Entry<Long, BasketItems> item : tempList.entrySet()){
            list.add(item.getValue());
        }
        notifyDataSetChanged();
    }

    @Override
    public BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_basket, parent, false);
        return new BasketViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(BasketViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    class BasketViewHolder extends RecyclerView.ViewHolder {
        private ImageView realImage, emptyImage;
        private TextView textTitle, prise, allPrise;
        private ImageButton delBtn;
        private EditText count;

        public BasketViewHolder(View itemView) {
            super(itemView);
            realImage = itemView.findViewById(R.id.realImage);
            emptyImage = itemView.findViewById(R.id.emptyImage);
            textTitle = itemView.findViewById(R.id.name);
            //category = itemView.findViewById(R.id.category);

            prise = itemView.findViewById(R.id.prise);
            count = itemView.findViewById(R.id.count);
            allPrise = itemView.findViewById(R.id.resultSum);
            delBtn = itemView.findViewById(R.id.delete);
        }

        public void bind(BasketItems item, int position) {
            emptyImage.setVisibility(View.VISIBLE);
            realImage.setVisibility(View.GONE);
            if(item.getGood() == null)
                return;

            Good g = item.getGood();

            textTitle.setText(g.getName());
            //category.setText(g.getCatName());
            if (!"".equals(g.getPhotoURI())) {
                Glide.with(mContext).load(g.getPhotoURI()).listener(new RequestListener<Drawable>() {
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

            prise.setText(g.getPrice()+" $");
            count.setText(String.valueOf(item.getCount()));
            allPrise.setText(String.valueOf(item.getCount()*g.getPrice()));

            delBtn.setOnClickListener(l->{
                onClickListener.onDelete(item);
            });

            count.setOnEditorActionListener((v, actionId, event) -> {
                int countItem = Integer.parseInt(count.getText().toString());
                if(countItem<1) count.setText("1");
                else            onClickListener.onChangeCount(item, countItem);
                return false;
            });


        }
    }

    private BasketFragment.OnAdapterClickListener onClickListener;

    public void setUpdateDataListener(BasketFragment.OnAdapterClickListener listener) {
        this.onClickListener = listener;
    }

}