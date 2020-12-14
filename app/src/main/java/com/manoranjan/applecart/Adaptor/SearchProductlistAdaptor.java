package com.manoranjan.applecart.Adaptor;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.manoranjan.applecart.Api.ApiLinks;
import com.manoranjan.applecart.Api.Configss;
import com.manoranjan.applecart.R;
import com.manoranjan.applecart.model.ProductListModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchProductlistAdaptor extends RecyclerView.Adapter<SearchProductlistAdaptor.ImageViewHolder> {
private Context mContext;
private List<ProductListModel> mUploads;
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
public SearchProductlistAdaptor(Context mContext, List<ProductListModel> mUploads) {
        this.mContext = mContext;
        this.mUploads = mUploads;
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
                    /*Intent i=new Intent(mContext, ProductDetailsActivity.class);
                    i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
                    //i.putExtra("url",uploadCurrent.getId());
                    StaticData.p_id=uploadCurrent.getId();
                    StaticData.detailstype="search";
                    mContext.startActivity(i);*/
                }
            });


        }

    private void addwishlist(final String id) {
        //Getting values from edit texts
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Configss.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        //Fetching the boolean value form sharedpreferences
        final String tokencode = sharedPreferences.getString(Configss.tokencode, "default");


        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLinks.add_wishlist,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("pincheck",response.toString());
                        try {

                            JSONObject object1=new JSONObject(response);
                            if(object1.getString("status").equals("true")){

                                Toast.makeText(mContext,object1.getString("message"),Toast.LENGTH_SHORT).show();

                            }else{
                               // progressDialog.dismiss();
                                Toast.makeText(mContext,object1.getString("message"),Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                          //  progressDialog.dismiss();
                            Toast.makeText(mContext,e.toString(),Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want
                       // progressDialog.dismiss();
                        Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                //Adding parameters to request
                params.put("access_token",tokencode);
                params.put("product_id",id);
                return params;
            }
        };
        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

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