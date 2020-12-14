package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.Common;
import com.manoranjan.applecart.model.SUbcatagoryModel;

import java.util.List;

public class ProductBySubCatAdaptor extends RecyclerView.Adapter<ProductBySubCatAdaptor.ImageViewHolder> {
private Context mContext;
private List<SUbcatagoryModel> mUploads;
    public int row_index=0;
    private  OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void OnItemCLiCK(int position);
        void OnCartCLiCK(int position);
    }
    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public ProductBySubCatAdaptor(Context mContext, List<SUbcatagoryModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }

@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_catagoryproduct, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

    @Override
        public void onBindViewHolder(final ImageViewHolder holder, final int position) {

            final SUbcatagoryModel uploadCurrent = mUploads.get(position);
            Log.d("check",uploadCurrent.getName());
            // holder.imgca.setImageDrawable(mContext.getResources().getDrawable(uploadCurrent.getCode()));
            holder.txtcatname.setText(uploadCurrent.getName());
            //  holder.txtdesc.setText(uploadCurrent.getDescription());
       /* holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row_index=position;
                Common.currentitem=mUploads.get(position);
                // Toast.makeText(mContext,"hi",Toast.LENGTH_LONG).show();
                notifyDataSetChanged();

            }
        });*/
        if (row_index==position){
            holder.txtcatname.setTextColor(android.graphics.Color.parseColor("#ffffff"));
            //holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#000000"));
            holder.l1.setBackgroundColor(android.graphics.Color.parseColor("#8cc24a"));

        }
        else{
            holder.txtcatname.setTextColor(android.graphics.Color.parseColor("#000000"));
           // holder.itemView.setBackgroundColor(android.graphics.Color.parseColor("#0CD52E"));
            holder.l1.setBackgroundColor(android.graphics.Color.parseColor("#ffffff"));

        }



        }

        public void  updaterowindex(int position ){
            row_index=position;
            Common.currentitem=mUploads.get(position);
            // Toast.makeText(mContext,"hi",Toast.LENGTH_LONG).show();
            notifyDataSetChanged();
        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView txtcatname;
    LinearLayout l1;

    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        txtcatname = itemView.findViewById(R.id.subcate);
        //txtdesc=itemView.findViewById(R.id.subdesc);
        l1=itemView.findViewById(R.id.l1);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.OnItemCLiCK(position);
                    }
                }
            }
        });

    }


}


}