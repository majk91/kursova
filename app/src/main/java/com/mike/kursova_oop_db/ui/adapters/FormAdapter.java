package com.mike.kursova_oop_db.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.models.FormMsg;
import com.mike.kursova_oop_db.ui.fragments.BasketFragment;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class FormAdapter extends  RecyclerView.Adapter<FormAdapter.FormViewHolder> {

    public FormAdapter(Context ctx) {
        mContext = ctx;
    }

    Context mContext;

    private List<FormMsg> list = new ArrayList<>();

    public void setItems(List<FormMsg> forms) {
        list.clear();
        list = forms;
        notifyDataSetChanged();
    }

    @Override
    public FormViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_form_list, parent, false);
        return new FormViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(FormViewHolder holder, int position) {
        holder.bind(list.get(position), position);
    }

    class FormViewHolder extends RecyclerView.ViewHolder {

        private TextView name, email, phone, msg, time;

        public FormViewHolder(View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            phone = itemView.findViewById(R.id.phone);
            msg = itemView.findViewById(R.id.msg);
        }

        public void bind(FormMsg item, int position) {
            name.setText(item.getName());
            email.setText(item.getEmail());
            phone.setText(item.getPhone());
            msg.setText(item.getMsg());

            Date date = new Date(item.getTime());
            DateFormat formatter = DateFormat.getTimeInstance();//new SimpleDateFormat("dd.MM.YYYY HH:mm");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
            String dateFormatted = formatter.format(date);

            time.setText(dateFormatted);
        }
    }

    private BasketFragment.OnAdapterClickListener onClickListener;

    public void setUpdateDataListener(BasketFragment.OnAdapterClickListener listener) {
        this.onClickListener = listener;
    }

}