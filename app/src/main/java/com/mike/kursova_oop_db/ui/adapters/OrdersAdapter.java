package com.mike.kursova_oop_db.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.mike.kursova_oop_db.R;
import com.mike.kursova_oop_db.data.engines.PreferenceEngine;
import com.mike.kursova_oop_db.data.models.Good;
import com.mike.kursova_oop_db.data.models.Order;
import com.mike.kursova_oop_db.data.models.OrderItem;
import com.mike.kursova_oop_db.data.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.mike.kursova_oop_db.data.models.User.Status.ADMIN;

public class OrdersAdapter extends  RecyclerView.Adapter<OrdersAdapter.BasketViewHolder> {

    public OrdersAdapter(Context ctx) {
        mContext = ctx;
    }

    Context mContext;

    private List<Order> list = new ArrayList<>();

    public void setItems(List<Order> tempList) {
        list = tempList;
        notifyDataSetChanged();
    }

    @Override
    public BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_orders, parent, false);
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
        private TextView statusText, name, sum, userEmail, userName;
        private Spinner statusMenu;
        private LinearLayout orderItemWrap;

        public BasketViewHolder(View itemView) {
            super(itemView);
            statusText = itemView.findViewById(R.id.statusInfo);
            statusMenu = itemView.findViewById(R.id.statusMenu);
            userEmail = itemView.findViewById(R.id.userEmail);
            userName = itemView.findViewById(R.id.userName);
            name = itemView.findViewById(R.id.name);
            orderItemWrap = itemView.findViewById(R.id.orderItemWrap);
            //category = itemView.findViewById(R.id.category);
            sum = itemView.findViewById(R.id.resultSum);
        }

        public void bind(Order item, int position) {
            final Boolean[] selectCreated = {false};
            String role = PreferenceEngine.getInstance().getUserRole(mContext);
            if(User.Status.valueOf(role) == ADMIN){
                statusText.setVisibility(View.GONE);
                statusMenu.setVisibility(View.VISIBLE);
                userEmail.setVisibility(View.VISIBLE);
                userName.setVisibility(View.VISIBLE);

                String[] paths = {Order.Status.NEW.name(), Order.Status.CANCELED.name(), Order.Status.END.name(), Order.Status.IN_DELIVERY.name(), Order.Status.IN_PROGRESS.name(), Order.Status.PAID.name()};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                        android.R.layout.simple_spinner_item,paths);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                statusMenu.setAdapter(adapter);
                statusMenu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if(selectCreated[0])
                            onClickListener.onChangeStatus(item, Order.Status.valueOf(paths[position]));
                        selectCreated[0] =true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {}
                });

                int spinnerPosition = adapter.getPosition(item.getStatus().name());
                selectCreated[0] =false;
                statusMenu.setSelection(spinnerPosition);
            }

            sum.setText(item.getPrise()+" $");

            List<OrderItem> list = item.getItems();
            if(list.size()>0){
                for(OrderItem orderItem: list){
                    Good g = orderItem.getGood();

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,10,0,0);

                    final TextView rowTextView = new TextView(mContext);
                    rowTextView.setText(g.getName() + " [count=" + orderItem.getCount() + ", prise=" +g.getPrice()+"$]");
                    rowTextView.setLayoutParams(params);
                    rowTextView.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_line_bottom));
                    rowTextView.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    rowTextView.setTextColor(mContext.getResources().getColor(R.color.black));
                    orderItemWrap.addView(rowTextView);
                }
            }

        }
    }

    private OnSetStatusListener onClickListener;

    public void setUpdateStatusListener(OnSetStatusListener listener) {
        this.onClickListener = listener;
    }

    public interface OnSetStatusListener {
        void onChangeStatus(Order item, Order.Status status);
    }

}