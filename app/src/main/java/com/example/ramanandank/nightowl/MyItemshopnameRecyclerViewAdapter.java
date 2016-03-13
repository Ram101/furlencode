package com.example.ramanandank.nightowl;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ramanandank.nightowl.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemshopnameRecyclerViewAdapter extends RecyclerView.Adapter<MyItemshopnameRecyclerViewAdapter.ViewHolder> {


    private ArrayList<ShopListData> mShopListDatas;

    public MyItemshopnameRecyclerViewAdapter(ArrayList<ShopListData> shopListDatas) {
       mShopListDatas = shopListDatas;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_itemshopname, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mShopNameView.setText(mShopListDatas.get(position).getName());
        holder.mShopOpenTimeView.setText("Open from " + mShopListDatas.get(position).from + " to " + mShopListDatas.get(position).to);
        if(mShopListDatas.get(position).getCategory()=="RESTAURANT")
        holder.mCategoryIconView.setImageResource(R.drawable.ic_restaurant_black_48dp);
        else if (mShopListDatas.get(position).getCategory()=="MEDICINES")
            holder.mCategoryIconView.setImageResource(R.drawable.ic_local_hospital_black_48dp);
        else if (mShopListDatas.get(position).getCategory()=="CLOTHES")
            holder.mCategoryIconView.setImageResource(R.drawable.ic_t_shirt_filled_48);
        else
            holder.mCategoryIconView.setImageResource(R.drawable.ic_hamburger_48);

    }


    @Override
    public int getItemCount() {
        return mShopListDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mShopNameView;
        public final TextView mShopOpenTimeView;
        public final ImageView mCategoryIconView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mCategoryIconView =(ImageView) view .findViewById(R.id.item_list_icon);
            mShopNameView = (TextView) view.findViewById(R.id.shop_name);
            mShopOpenTimeView = (TextView) view.findViewById(R.id.shop_review);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mShopOpenTimeView.getText() + "'";
        }
    }
}
