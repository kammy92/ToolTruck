package com.tooltruck.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tooltruck.R;
import com.tooltruck.model.Wishlist;
import com.tooltruck.utils.SetTypeFace;

import java.util.ArrayList;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    OnItemClickListener mItemClickListener;
    private Activity activity;
    private ArrayList<Wishlist> wishlistArrayList = new ArrayList<Wishlist> ();
    
    public WishlistAdapter (Activity activity, ArrayList<Wishlist> wishlistArrayList) {
        this.activity = activity;
        this.wishlistArrayList = wishlistArrayList;
    }
    
    @Override
    public ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        final LayoutInflater mInflater = LayoutInflater.from (parent.getContext ());
        final View sView = mInflater.inflate (R.layout.listview_item_wishlist, parent, false);
        return new ViewHolder (sView);
    }
    
    @Override
    public void onBindViewHolder (ViewHolder holder, final int position) {
        final Wishlist wishlist = wishlistArrayList.get (position);
        holder.tvItem.setTypeface (SetTypeFace.getTypeface (activity));
        holder.tvItem.setText (wishlist.getText ());
        holder.ivDelete.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                MaterialDialog dialog = new MaterialDialog.Builder (activity)
                        .limitIconToDefaultSize ()
                        .content ("Do you wish to remove " + wishlist.getText () + " from your wishlist?")
                        .positiveText ("Yes")
                        .negativeText ("No")
                        .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                wishlistArrayList.remove (position);
                                notifyDataSetChanged ();
                            }
                        }).build ();
                dialog.show ();
            }
        });
        holder.ivEdit.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                MaterialDialog dialog = new MaterialDialog.Builder (activity)
                        .limitIconToDefaultSize ()
                        .content ("Do you wish to edit " + wishlist.getText () + " in your wishlist?")
                        .positiveText ("Yes")
                        .negativeText ("No")
                        .inputType (
                                InputType.TYPE_CLASS_TEXT
                                        | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                        | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                        .alwaysCallInputCallback () // this forces the callback to be invoked with every input change
                        .typeface (SetTypeFace.getTypeface (activity), SetTypeFace.getTypeface (activity))
                        .onPositive (new MaterialDialog.SingleButtonCallback () {
                            @Override
                            public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                wishlist.setText ("");
//                                notifyDataSetChanged ();
                            }
                        }).build ();
                dialog.show ();
            }
        });
    }
    
    @Override
    public int getItemCount () {
        return wishlistArrayList.size ();
    }
    
    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }
    
    public interface OnItemClickListener {
        public void onItemClick (View view, int position);
    }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvItem;
        ImageView ivEdit;
        ImageView ivDelete;
        
        public ViewHolder (View view) {
            super (view);
            tvItem = (TextView) view.findViewById (R.id.tvItem);
            ivEdit = (ImageView) view.findViewById (R.id.ivEdit);
            ivDelete = (ImageView) view.findViewById (R.id.ivDelete);
            view.setOnClickListener (this);
        }
        
        @Override
        public void onClick (View v) {
//            FAQ FAQ = FAQList.get (getLayoutPosition ());
        }
    }
}