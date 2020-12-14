package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.wishlistmodel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WishlistAdaptor extends RecyclerView.Adapter<WishlistAdaptor.ImageViewHolder> {
private Context mContext;
private List<wishlistmodel> mUploads;
    private OnitemClickListner mlistner;

    public interface  OnitemClickListner{


        void OnREmovecart(int position);
    }

    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public WishlistAdaptor(Context mContext, List<wishlistmodel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }

@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_wishlist, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final wishlistmodel uploadCurrent = mUploads.get(position);
            String imgurl= ApiLinks.baseimgurl+uploadCurrent.getImage();
            Picasso.with(mContext)
                    .load(imgurl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgca);
            // holder.imgca.setImageDrawable(mContext.getResources().getDrawable(uploadCurrent.getCode()));
            holder.pname.setText(uploadCurrent.getName());
            holder.pprice.setText(uploadCurrent.getPrice());
          //  holder.txtdesc.setText(uploadCurrent.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i=new Intent(mContext, ProductDetailsActivity.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.putExtra("url",uploadCurrent.getId());
                    StaticData.p_id=uploadCurrent.getProduct_id();
                    StaticData.detailstype="wishlist";
                    mContext.startActivity(i);*/
                }
            });

        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView pname,pcat,pprice;
    ImageView imgca;
    RelativeLayout ll;
    CardView addtocart,delete;
    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        pname = itemView.findViewById(R.id.pname);
        pcat=itemView.findViewById(R.id.pcat);
        pprice=itemView.findViewById(R.id.pprice);
        imgca=itemView.findViewById(R.id.pimg);
        addtocart=itemView.findViewById(R.id.addtocart);
        delete=itemView.findViewById(R.id.delete);
        //ll=itemView.findViewById(R.id.rr);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.OnREmovecart(position);
                    }
                }
            }
        });

    }


}


}