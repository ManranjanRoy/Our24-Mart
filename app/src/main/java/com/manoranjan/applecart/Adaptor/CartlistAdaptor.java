package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.CartMedicineModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class CartlistAdaptor extends RecyclerView.Adapter<CartlistAdaptor.ImageViewHolder> {
private Context mContext;
private List<CartMedicineModel> mUploads;
    private  OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void OnItemCLiCK(int position);
        void OnREmovecart(int position);
        void Onplus(int position);
        void Onminus(int position);
    }

    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public CartlistAdaptor(Context mContext, List<CartMedicineModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }

@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_cart, parent, false);
    return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final CartMedicineModel uploadCurrent = mUploads.get(position);
         String imgurl= ApiLinks.baseimgurl+uploadCurrent.getCode();
         Picasso.with(mContext)
            .load(imgurl)
            .placeholder(R.drawable.ic_launcher_background)
            .fit()
            .into(holder.imgca);
            holder.pname.setText(uploadCurrent.getTitle());
            holder.pcat.setText("( "+uploadCurrent.getCategory_name()+" )");
            holder.pprice.setText(uploadCurrent.getPrice());

    DecimalFormat df2 = new DecimalFormat("#.###");
    DecimalFormat df1 = new DecimalFormat("#.##");
    Double p= Double.parseDouble(uploadCurrent.getQuantity());
    String q=df2.format(p * 1.00);
            holder.pquantity.setText(q);
            Double d=Double.parseDouble(uploadCurrent.getTotalprice());
            holder.ptotalprice.setText(String.format("%.2f",d));
    }

@Override
public int getItemCount() {
        return mUploads.size();
        }
        public void filterdata(List<CartMedicineModel> updatedatalist){
        this.mUploads=updatedatalist;
        notifyDataSetChanged();

        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView pname,pcat,pprice,ptotalprice;
ImageView imgca;
TextView plus,minus,pquantity,remove;
    RelativeLayout ll;
    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        pname = itemView.findViewById(R.id.pname);
        pcat = itemView.findViewById(R.id.pcat);
        pprice = itemView.findViewById(R.id.pprice);
        imgca=itemView.findViewById(R.id.pimg);
        ptotalprice=itemView.findViewById(R.id.ptotalprice);
        pquantity=itemView.findViewById(R.id.pquantity);
        remove=itemView.findViewById(R.id.remove);
        plus=itemView.findViewById(R.id.minus);
        minus=itemView.findViewById(R.id.plus);


       remove.setOnClickListener(new View.OnClickListener() {
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
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.Onplus(position);
                    }
                }
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.Onminus(position);
                    }
                }
            }
        });

    }


}


}