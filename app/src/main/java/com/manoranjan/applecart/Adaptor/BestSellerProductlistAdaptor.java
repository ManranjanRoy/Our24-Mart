package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.ProductListModel;
import com.manoranjan.applecart.model.wishlistmodel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BestSellerProductlistAdaptor extends RecyclerView.Adapter<BestSellerProductlistAdaptor.ImageViewHolder> {
private Context mContext;
private List<ProductListModel> mUploads;
    List<wishlistmodel> wishlistmodels;
    private OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void onwishlistclick(int position);
    }
    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
    public void filterdlist(List<ProductListModel> hospitalListModels){
        mUploads=hospitalListModels;
        notifyDataSetChanged();
    }
public BestSellerProductlistAdaptor(Context mContext, List<ProductListModel> mUploads, List<wishlistmodel> wishlistmodels) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        this.wishlistmodels=wishlistmodels;
        }

@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final ProductListModel uploadCurrent = mUploads.get(position);
            String imgurl= ApiLinks.baseimgurl+uploadCurrent.getImage();
            Picasso.with(mContext)
                    .load(imgurl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgca);
            // holder.imgca.setImageDrawable(mContext.getResources().getDrawable(uploadCurrent.getCode()));
            holder.pname.setText(uploadCurrent.getName());
            holder.pcat.setText("( "+ uploadCurrent.getCatName()+" )");
            holder.pprice.setText( " "+Float.parseFloat(uploadCurrent.getPrice()));
            holder.pstatus.setText(uploadCurrent.getProductStatus());
          //  holder.txtdesc.setText(uploadCurrent.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   /* Intent i=new Intent(mContext, ProductDetailsActivity.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.putExtra("url",uploadCurrent.getId());
                    StaticData.p_id=uploadCurrent.getId();
                    StaticData.detailstype="Bestseller";
                    mContext.startActivity(i);*/
                }
            });


        }



    @Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView pname,pcat,pprice,pstatus;
    ImageView imgca,pwishlist;
    //RelativeLayout ll;
    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        pname = itemView.findViewById(R.id.pname);
        pcat=itemView.findViewById(R.id.pcat);
        pprice=itemView.findViewById(R.id.pprice);
        pstatus=itemView.findViewById(R.id.pstatus);
        imgca=itemView.findViewById(R.id.pimg);
        pwishlist=itemView.findViewById(R.id.pwishlist);
        pwishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.onwishlistclick(position);
                    }
                }
            }
        });


    }


}


}