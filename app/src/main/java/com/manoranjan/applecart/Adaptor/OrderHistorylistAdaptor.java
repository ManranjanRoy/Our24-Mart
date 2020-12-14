package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.OrderHistoryModel;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class OrderHistorylistAdaptor extends RecyclerView.Adapter<OrderHistorylistAdaptor.ImageViewHolder> {
private Context mContext;
private List<OrderHistoryModel> mUploads;

    private  OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void OnItemCLiCK(int position);
        void onCancel(int position);
        void onReturn(int position);

    }

    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public OrderHistorylistAdaptor(Context mContext, List<OrderHistoryModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }
@Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_orderhistory, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, int position) {

        final OrderHistoryModel uploadCurrent = mUploads.get(position);

            final String imgurl= ApiLinks.baseimgurl+uploadCurrent.getImage();
                Picasso.with(mContext)
                .load(imgurl)
                .placeholder(R.drawable.ic_launcher_background)
                .fit()
                .into(holder.hotelimg);
                holder.hotelname.setText("Order Id : #"+uploadCurrent.getOrderNo());
                holder.hoteladdress.setText(uploadCurrent.getAddress1()+" "+uploadCurrent.getAddress1()+" \n"+uploadCurrent.getTown()+" "+uploadCurrent.getState()+ "\n Pincode : " +uploadCurrent.getPincode());
                float a= Float.parseFloat(uploadCurrent.getQty());
                int b= (int) a;
                holder.hotelfooditem.setText(String.valueOf(b) +" x "+uploadCurrent.getProductName());
                holder.totralprice.setText("\u20B9 " +uploadCurrent.getAmount());
                   Log.d("datetime",uploadCurrent.getCreatedDate());

                DateFormat f = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d = null;
                try {
                    d = f.parse(uploadCurrent.getCreatedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                 DateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                 DateFormat time = new SimpleDateFormat("  hh:mm");

                holder.orderdate.setText(date.format(d) +" at " + time.format(d));

                if (uploadCurrent.getPaymentType().equals("1")) {
                    holder.paymentstatus.setText(" Paid Online ");
                }else if (uploadCurrent.getPaymentType().equals("0")) {
                    holder.paymentstatus.setText(" Cash On Delivery");
                }
                if (uploadCurrent.getOrderStatus().equals("4")){
                    holder.orderstatus.setText(" Canceled ");
                }
                else  if (uploadCurrent.getOrderStatus().equals("3")){
                    holder.orderstatus.setText(" Returned ");
                }else {
                    if (uploadCurrent.getOrderStatus().equals("0")) {
                        holder.orderstatus.setText(" Accepted ");
                    } else if (uploadCurrent.getOrderStatus().equals("1")) {
                        holder.orderstatus.setText(" On Process ");
                    } else if (uploadCurrent.getOrderStatus().equals("2")) {
                        holder.orderstatus.setText(" delivered ");
                    } else {
                        holder.orderstatus.setText(" Dont Know");
                    }
                }
    }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView hotelname,hoteladdress,hotelfooditem,orderdate,totralprice,orderstatus,paymentstatus;
    TextView cancelordr,returnorder;
    ImageView  hotelimg;
    LinearLayout ll;
    public ImageViewHolder(View itemView, final OnitemClickListner listner) {

        super(itemView);
        hotelimg=itemView.findViewById(R.id.ohotelimg);
        hotelname=itemView.findViewById(R.id.ohotelname);
        hoteladdress=itemView.findViewById(R.id.ohoteladdress);
        hotelfooditem=itemView.findViewById(R.id.ofoodname);
        orderdate=itemView.findViewById(R.id.oorderdate);
        totralprice=itemView.findViewById(R.id.ototalprice);
        orderstatus=itemView.findViewById(R.id.oorderstatus);
        cancelordr=itemView.findViewById(R.id.cancelorder);
        returnorder=itemView.findViewById(R.id.returnorder);
        paymentstatus=itemView.findViewById(R.id.paymentstatus);
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
        cancelordr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.onCancel(position);
                    }
                }
            }
        });
        returnorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.onReturn(position);
                    }
                }
            }
        });



    }


}


}