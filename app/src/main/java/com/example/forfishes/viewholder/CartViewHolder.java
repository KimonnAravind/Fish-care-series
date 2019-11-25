package com.example.forfishes.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forfishes.Interface.ItemClickListner;
import com.example.forfishes.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductname, txtproductprice, txtproductquantity;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txtProductname=itemView.findViewById(R.id.cardproductnames);
        txtproductprice=itemView.findViewById(R.id.cardproductprice);
        txtproductquantity=itemView.findViewById(R.id.cartproductquantiy);

    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(),false);


    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }
}
