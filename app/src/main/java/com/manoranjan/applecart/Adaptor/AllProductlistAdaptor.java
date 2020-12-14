package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manoranjan.applecart.Activity.ProductDetailsActivity;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.StaticData;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.Sqlite.DatabaseHelper;
import com.manoranjan.applecart.model.CartMedicineModel;
import com.manoranjan.applecart.model.ProductListModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AllProductlistAdaptor extends RecyclerView.Adapter<AllProductlistAdaptor.ImageViewHolder> {
private Context mContext;
    double quantityvalue=1;
    double addval=1;
    double finalquantity;
    double startprice;
    double totalprice;
    DatabaseHelper myDb;
private List<ProductListModel> mUploads,productModelList;
    List<CartMedicineModel> cartMedicineModels, updatedatalist;
    private OnitemClickListner mlistner;

    public interface  OnitemClickListner{

        void onwishlistclick(int position);
        void addtocartclick(int position);
    }
    public  void setonItemClickListner(OnitemClickListner listner){
        mlistner=listner;

    }
public AllProductlistAdaptor(Context mContext, List<ProductListModel> mUploads) {
        myDb=new DatabaseHelper(mContext);
        this.mContext = mContext;
         productModelList=mUploads;
        this.mUploads = mUploads;
    cartMedicineModels = new ArrayList<>();
    updatedatalist = new ArrayList<>();
    getalldata();
        }

    private void getalldata() {
        cartMedicineModels.clear();
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            // show message
            Log.d("cartdata","No data");
            //Toast.makeText(mContext, "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {
            Cursor res = myDb.getAllData();
            // //String id,title,details,code,price,category_name,store_name,quantity,totalprice;
            StringBuffer buffer = new StringBuffer();
            cartMedicineModels.clear();
            while (res.moveToNext()) {
                cartMedicineModels.add(new CartMedicineModel(
                        res.getString(0), res.getString(1),
                        res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5),
                        res.getString(6), res.getString(7),
                        res.getString(8)));
                buffer.append("id :" + res.getString(0));
                buffer.append("title :" + res.getString(1));
                buffer.append("details :" + res.getString(2));
                buffer.append("code :" + res.getString(3));
                buffer.append("price :" + res.getString(4));
                buffer.append("category_name :" + res.getString(5));
                buffer.append("store_name :" + res.getString(6));
                buffer.append("quantity :" + res.getString(7));
                buffer.append("totalprice :" + res.getString(8) + "\n\n");
                Log.d("cartdata", buffer.toString());


            }
        }
    }

    @Override
public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_product, parent, false);
        return new ImageViewHolder(v,mlistner);
        }

@Override
public void onBindViewHolder(final ImageViewHolder holder, final int position) {

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
            holder.pqty.setText(uploadCurrent.getQty() +" "+ uploadCurrent.getMeasurement());
            holder.pwishlist.performClick();
            holder.pprice.setText( " "+Float.parseFloat(uploadCurrent.getOfferPrice()));
            holder.pprice2.setText("\u20B9 " +Float.parseFloat(uploadCurrent.getPrice()));
            holder.pprice2.setPaintFlags(holder.pprice2.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.pstatus.setText(uploadCurrent.getProductStatus());
            holder.pstatus.performClick();
            holder.addbuttonlayout.setVisibility(View.VISIBLE);
            holder.quantitylayout.setVisibility(View.GONE);
    for (int i=0;i<cartMedicineModels.size();i++){
        if (productModelList.get(position).getProduct_id().equals(cartMedicineModels.get(i).getId())){
            holder.addbuttonlayout.setVisibility(View.GONE);
            holder.quantitylayout.setVisibility(View.VISIBLE);
            int quantityvalue= (int) Double.parseDouble(cartMedicineModels.get(i).getQuantity());
            int a=quantityvalue;
            holder.pquantity.setText(String.valueOf(quantityvalue));
            break;
        }
        else{
            holder.addbuttonlayout.setVisibility(View.VISIBLE);
            holder.quantitylayout.setVisibility(View.GONE);
           // Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
        }
    }




            //TextView someTextView = (TextView) findViewById(R.id.some_text_view);
    //someTextView.setText("$29,500");
    //myTextView.setPaintFlags(myTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
          //  holder.txtdesc.setText(uploadCurrent.getDescription());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(mContext, ProductDetailsActivity.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.putExtra("url",uploadCurrent.getId());
                    StaticData.p_id=uploadCurrent.getProduct_id();
                    mContext.startActivity(i);
                }
            });
            holder.addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.addbuttonlayout.setVisibility(View.GONE);
                    holder.quantitylayout.setVisibility(View.VISIBLE);
                    addtocartfun(position);
                }
            });
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 incementfun(position);
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     decerementfun(position);
                }
            });


        }

    public void filterdlist(List<ProductListModel> productListModels1){
        mUploads=productListModels1;
        productModelList=productListModels1;
        notifyDataSetChanged();
    }



    private void incementfun(int position) {
        String cartid="0";
        DecimalFormat df2 = new DecimalFormat("#.##");
        for (int i=0;i<cartMedicineModels.size();i++){
            if (productModelList.get(position).getProduct_id().equals(cartMedicineModels.get(i).getId())){
                cartid=cartMedicineModels.get(i).getId();
                quantityvalue= Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                startprice= Double.parseDouble(cartMedicineModels.get(i).getPrice());
                break;
            }
            else{
                cartid="0";
                Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
        if (quantityvalue >= 1)
            finalquantity=quantityvalue + addval;
        //quantity.setText(String.valueOf(finalquantity));
        double fprice=startprice;
        double total=fprice*finalquantity;
        totalprice=total;

        boolean isUpdate = myDb.updateData(cartid,
                String.valueOf(finalquantity), String.valueOf(total));
        if(isUpdate == true) {
            addproducts1();
            notifyDataSetChanged();
            //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
        }
        else {
            addproducts1();
            notifyDataSetChanged();
            Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
        }

        //price.setText(String.valueOf(totalprice));
        quantityvalue=finalquantity;
    }
    private void decerementfun(int position) {
        String cartid="0";
        DecimalFormat df2 = new DecimalFormat("#.##");
        for (int i=0;i<cartMedicineModels.size();i++){
            if (productModelList.get(position).getProduct_id().equals(cartMedicineModels.get(i).getId())){
                cartid=cartMedicineModels.get(i).getId();
                quantityvalue= Double.parseDouble(cartMedicineModels.get(i).getQuantity());
                startprice= Double.parseDouble(cartMedicineModels.get(i).getPrice());
                break;
            }
            else{
                cartid="0";
                Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
            }
        }
        if (quantityvalue > 1) {
            finalquantity = quantityvalue - addval;
            //quantity.setText(String.valueOf(finalquantity));
            double fprice = startprice;
            double total = fprice * finalquantity;
            totalprice = total;

            boolean isUpdate = myDb.updateData(cartid,
                    String.valueOf(finalquantity), String.valueOf(total));
            if (isUpdate == true) {
                addproducts1();
                notifyDataSetChanged();
                //Toast.makeText(getApplicationContext(), "Data Update", Toast.LENGTH_LONG).show();
            } else {
                addproducts1();
                notifyDataSetChanged();
                Toast.makeText(mContext, "Data not Updated", Toast.LENGTH_LONG).show();
            }
            //price.setText(String.valueOf(totalprice));
            quantityvalue = finalquantity;
        }else if (quantityvalue==1){
            Integer deletedRows = myDb.deleteData(cartid);
            if (deletedRows > 0) {
                addproducts1();
                notifyDataSetChanged();
                //Toast.makeText(mContext, "Data Deleted", Toast.LENGTH_LONG).show();

            } else {
                notifyDataSetChanged();
               // Toast.makeText(mContext, "Data not Deleted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addtocartfun(int position) {

        //String id,title,details,image,price,0,color,quantity,totalprice;
        String id=productModelList.get(position).getProduct_id();
        String title=productModelList.get(position).getName();
        String details=productModelList.get(position).getDescription();
        String image=productModelList.get(position).getImage();//img
        String price=productModelList.get(position).getPrice();
        String category_name=productModelList.get(position).getCatName();
        String store_name="0";
        String quantity="1.0";//gm
        DecimalFormat df2 = new DecimalFormat("#.##");
        // Double p=Double.parseDouble(quantity ) * Double.parseDouble(productModelList.get(position).getPrice());
        String totalprice=
                String.valueOf(df2.format(Double.parseDouble(quantity ) * Double.parseDouble(productModelList.get(position).getPrice())));

        // Toast.makeText(getApplicationContext(), "mannu", Toast.LENGTH_LONG).show();
        boolean isInserted = myDb.insertData(
                id,title,details,image,price,category_name,store_name,quantity,totalprice);
        if(isInserted == true){
            addproducts1();
            notifyDataSetChanged();
            getcount();
            Toast.makeText(mContext,"Added To Cart",Toast.LENGTH_LONG).show();}
        else {
            addproducts1();
            notifyDataSetChanged();
            Toast.makeText(mContext, "Already Added", Toast.LENGTH_LONG).show();
        }
        //addtocartfun(position);

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0) {
           // notifyDataSetChanged();
            // show message
            Toast.makeText(mContext,"No Data Inserted",Toast.LENGTH_LONG).show();
            return;
        }

        //count.setText(res.getCount());
        // //String id,title,details,image,price,category_name,store_name,quantity,totalprice;
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("id :"+ res.getString(0));
            buffer.append("title :"+ res.getString(1));
            buffer.append("details :"+ res.getString(2));
            buffer.append("image :"+ res.getString(3));
            buffer.append("price :"+ res.getString(4));
            buffer.append("category_name :"+ res.getString(5));
            buffer.append("store_name :"+ res.getString(6));
            buffer.append("quantity :"+ res.getString(7));
            buffer.append("totalprice :"+ res.getString(8)+"\n\n");
        }
        // Show all data
        Log.d("alldata",buffer.toString());
        //Toast.makeText(getApplicationContext(),buffer.toString(),Toast.LENGTH_LONG).show();

    }

    private void getcount() {
        //TextView count=findViewById(R.id.cartcount);
        Cursor ress = myDb.getAllData();
        if (ress.getCount() == 0) {
            // show message
            //count.setText(String.valueOf(ress.getCount()));
        }else{
           // count.setText(String.valueOf(ress.getCount()));
        }

    }

    @Override
public int getItemCount() {
        return mUploads.size();
        }

public class ImageViewHolder extends RecyclerView.ViewHolder {
    public TextView pname,pcat,pprice,pstatus,pprice2,pqty;
    ImageView imgca,pwishlist;
    Button addtocart;
    //RelativeLayout ll;
    LinearLayout addbuttonlayout,quantitylayout;

    TextView plus,minus,pquantity,remove;
    public ImageViewHolder(View itemView, final OnitemClickListner listner) {
        super(itemView);
        pname = itemView.findViewById(R.id.pname);
        pcat=itemView.findViewById(R.id.pcat);
        pqty=itemView.findViewById(R.id.pqty);
        pprice=itemView.findViewById(R.id.pprice);
        pprice2=itemView.findViewById(R.id.pprice2);
        pstatus=itemView.findViewById(R.id.pstatus);
        imgca=itemView.findViewById(R.id.pimg);
        pwishlist=itemView.findViewById(R.id.pwishlist);
        addbuttonlayout=itemView.findViewById(R.id.addbuttonlayout);
        quantitylayout=itemView.findViewById(R.id.quantitylayout);
        pquantity=itemView.findViewById(R.id.pquantity);
        plus=itemView.findViewById(R.id.minus);
        minus=itemView.findViewById(R.id.plus);

        addtocart=itemView.findViewById(R.id.addtocart);
        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listner !=null){
                    int position =getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION){
                        listner.addtocartclick(position);
                    }
                }
            }
        });
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
    private void addproducts1() {
        updatedatalist.clear();
        Cursor res = myDb.getAllData();
       /* if (res.getCount() == 0) {
            Toast.makeText(mContext, "No Data", Toast.LENGTH_LONG).show();
            return;
        } else {*/
            while (res.moveToNext()) {
                updatedatalist.add(new CartMedicineModel(
                        res.getString(0), res.getString(1),
                        res.getString(2), res.getString(3),
                        res.getString(4), res.getString(5),
                        res.getString(6), res.getString(7),
                        res.getString(8)));
            }
            cartMedicineModels=updatedatalist;
        }

}