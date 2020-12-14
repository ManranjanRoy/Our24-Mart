package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.AddressModel;

import java.util.List;

public class DeliveryAddressAdaptor extends RecyclerView.Adapter<DeliveryAddressAdaptor.ImageViewHolder> {
private Context mContext;
private List<AddressModel> mUploads;
    private  OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void OnItemCLiCK(int position);
        void Deleteaddress(int position);
    }
    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public DeliveryAddressAdaptor(Context mContext, List<AddressModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
        }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_address_1, parent, false);
            return new ImageViewHolder(v,mlistner);
            }

    @Override
        public void onBindViewHolder(final ImageViewHolder holder, int position) {

            final AddressModel uploadCurrent = mUploads.get(position);
            //Log.d("check",uploadCurrent.get());
            // holder.imgca.setImageDrawable(mContext.getResources().getDrawable(uploadCurrent.getCode()));
        holder.name.setText(uploadCurrent.getFirstname()+" "+uploadCurrent.getLastname());
        holder.address.setText(uploadCurrent.getAddress_1()+" "+uploadCurrent.getAddress_2()+" \n"+uploadCurrent.getTown()+" "+uploadCurrent.getState()+" -"+uploadCurrent.getPincode());
            //  holder.txtdesc.setText(uploadCurrent.getDescription());
        if (uploadCurrent.getStatus().equals("1")){
            holder.addresstype.setText("Home");
        }else if (uploadCurrent.getStatus().equals("2")){
            holder.addresstype.setText("Office");
        }else if (uploadCurrent.getStatus().equals("3")){
            holder.addresstype.setText("Others");
        }



        }

@Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView address,name,addresstype;
    Button editaddress,deleteaddress;

    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        address = itemView.findViewById(R.id.address);
        name=itemView.findViewById(R.id.name);
        editaddress=itemView.findViewById(R.id.editaddress);
        deleteaddress=itemView.findViewById(R.id.deleteaddress);
        addresstype=itemView.findViewById(R.id.addresstype);

        //ll=itemView.findViewById(R.id.rr);
        editaddress.setOnClickListener(new View.OnClickListener() {
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
        deleteaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.Deleteaddress(position);
                    }
                }
            }
        });

    }


}


}