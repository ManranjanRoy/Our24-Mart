package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.manoranjan.applecart.Activity.ProductListActivity;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.CatagoryModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CatagorylistAdaptor extends RecyclerView.Adapter<CatagorylistAdaptor.ImageViewHolder> {
private Context mContext;
private List<CatagoryModel> mUploads;

public CatagorylistAdaptor(Context mContext, List<CatagoryModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }

@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_catagorylist, parent, false);
        return new ImageViewHolder(v);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final CatagoryModel uploadCurrent = mUploads.get(position);
            String imgurl= ApiLinks.baseimgurl+uploadCurrent.getImage();
            Picasso.with(mContext)
                    .load(imgurl)
                    .placeholder(R.drawable.ic_launcher_background)
                    .fit()
                    .into(holder.imgca);
            // holder.imgca.setImageDrawable(mContext.getResources().getDrawable(uploadCurrent.getCode()));
            holder.txtcatname.setText(uploadCurrent.getName());
          //  holder.txtdesc.setText(uploadCurrent.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mContext, ProductListActivity.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.putExtra("url",uploadCurrent.getId());
                    StaticData.cat_id=uploadCurrent.getId();
                    mContext.startActivity(i);
                }
            });

        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView txtcatname,txtdesc;
    ImageView imgca;
    RelativeLayout ll;
    public ImageViewHolder(View itemView) {
        super(itemView);
        txtcatname = itemView.findViewById(R.id.subcate);
        //txtdesc=itemView.findViewById(R.id.subdesc);
        imgca=itemView.findViewById(R.id.profile_image);
        //ll=itemView.findViewById(R.id.rr);


    }


}


}