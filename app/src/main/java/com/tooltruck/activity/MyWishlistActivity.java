package com.tooltruck.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tooltruck.R;
import com.tooltruck.adapter.WishlistAdapter;
import com.tooltruck.model.Wishlist;
import com.tooltruck.utils.SetTypeFace;
import com.tooltruck.utils.Utils;

import java.util.ArrayList;


public class MyWishlistActivity extends AppCompatActivity {
    RecyclerView rvWishlist;
    CoordinatorLayout clMain;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Wishlist> wishlistArrayList = new ArrayList<> ();
    WishlistAdapter wishlistAdapter;
    
    FloatingActionButton fabAddWishlist;
    RelativeLayout rlBack;
    TextView tvRetry;
    RelativeLayout rlList;
    RelativeLayout rlInternetConnection;
    RelativeLayout rlNoResultFound;
    TextView tvTryAgain;
    ImageView ivAdd;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_my_wishlist);
        initView ();
        initData ();
        initListener ();
        getAllWishlist ();
    }
    
    private void initView () {
        rlBack = (RelativeLayout) findViewById (R.id.rlBack);
        rvWishlist = (RecyclerView) findViewById (R.id.rvWishlist);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        rlList = (RelativeLayout) findViewById (R.id.rlList);
        rlInternetConnection = (RelativeLayout) findViewById (R.id.rlInternetConnection);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        tvRetry = (TextView) findViewById (R.id.tvRetry);
        tvTryAgain = (TextView) findViewById (R.id.tvTryAgain);
        fabAddWishlist = (FloatingActionButton) findViewById (R.id.fabAddWishlist);
        ivAdd = (ImageView) findViewById (R.id.ivAdd);
    }
    
    private void initData () {
        swipeRefreshLayout.setRefreshing (true);
        wishlistArrayList.clear ();
        wishlistAdapter = new WishlistAdapter (MyWishlistActivity.this, wishlistArrayList);
        rvWishlist.setAdapter (wishlistAdapter);
        rvWishlist.setHasFixedSize (true);
        rvWishlist.setLayoutManager (new LinearLayoutManager (this, LinearLayoutManager.VERTICAL, false));
        rvWishlist.setItemAnimator (new DefaultItemAnimator ());
        Utils.setTypefaceToAllViews (this, rlBack);
    }
    
    private void initListener () {
        tvRetry.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                rlList.setVisibility (View.VISIBLE);
                rlInternetConnection.setVisibility (View.GONE);
                rlNoResultFound.setVisibility (View.GONE);
                swipeRefreshLayout.setRefreshing (true);
                getAllWishlist ();
            }
        });
        tvTryAgain.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                rlList.setVisibility (View.VISIBLE);
                rlInternetConnection.setVisibility (View.GONE);
                rlNoResultFound.setVisibility (View.GONE);
                swipeRefreshLayout.setRefreshing (true);
                getAllWishlist ();
            }
        });
        
        swipeRefreshLayout.setOnRefreshListener (new SwipeRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh () {
                swipeRefreshLayout.setRefreshing (true);
                getAllWishlist ();
            }
        });
        rlBack.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                finish ();
                overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        ivAdd.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                final MaterialDialog.Builder mBuilder = new MaterialDialog.Builder (MyWishlistActivity.this)
                        .content ("Enter the item name")
                        .typeface (SetTypeFace.getTypeface (MyWishlistActivity.this), SetTypeFace.getTypeface (MyWishlistActivity.this))
//                        .inputType (InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS)
                        .alwaysCallInputCallback ()
                        .canceledOnTouchOutside (true)
                        .cancelable (true)
                        .positiveText ("ADD")
                        .negativeText ("CANCEL");
                mBuilder.input (null, null, new MaterialDialog.InputCallback () {
                    @Override
                    public void onInput (MaterialDialog dialog, CharSequence input) {
                    }
                });
                
                mBuilder.onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        wishlistArrayList.add (new Wishlist (1, dialog.getInputEditText ().getText ().toString ()));
                        wishlistAdapter.notifyDataSetChanged ();
                    }
                });
                
                MaterialDialog dialog = mBuilder.build ();
                dialog.show ();
            }
        });
    }
    
    private void getAllWishlist () {
        wishlistArrayList.clear ();
        wishlistAdapter.notifyDataSetChanged ();
        wishlistArrayList.add (new Wishlist (1, "Hammer 1"));
        wishlistArrayList.add (new Wishlist (2, "Hammer 2"));
        wishlistArrayList.add (new Wishlist (3, "Hammer 3"));
        wishlistArrayList.add (new Wishlist (4, "Hammer 4"));
        wishlistArrayList.add (new Wishlist (5, "Hammer 5"));
        wishlistArrayList.add (new Wishlist (6, "Hammer 6"));
        swipeRefreshLayout.setRefreshing (false);
    }

//    private void getAllProperties2 () {
//        if (NetworkConnection.isNetworkAvailable (MyWishlistActivity.this)) {
//            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_FAVOURITE_PROPERTY_LIST, true);
//            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_FAVOURITE_PROPERTY_LIST,
//                    new com.android.volley.Response.Listener<String> () {
//                        @Override
//                        public void onResponse (String response) {
//                            favouritePropertyList.clear ();
//                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
//                            if (response != null) {
//                                try {
//                                    rlInternetConnection.setVisibility (View.GONE);
//                                    rlNoFavPropertyFound.setVisibility (View.GONE);
//                                    rlList.setVisibility (View.VISIBLE);
//                                    JSONObject jsonObj = new JSONObject (response);
//                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
//                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
//                                    if (! error) {
//                                        JSONArray jsonArrayProperty = jsonObj.getJSONArray (AppConfigTags.PROPERTIES);
//                                        for (int i = 0; i < jsonArrayProperty.length (); i++) {
//                                            JSONObject jsonObjectProperty = jsonArrayProperty.getJSONObject (i);
//                                            Property property = new Property (
//                                                    jsonObjectProperty.getInt (AppConfigTags.PROPERTY_ID),
//                                                    jsonObjectProperty.getInt (AppConfigTags.PROPERTY_STATUS),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_PRICE),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BEDROOMS),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BATHROOMS),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_AREA),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_BUILT_YEAR),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_ADDRESS),
//                                                    jsonObjectProperty.getString (AppConfigTags.PROPERTY_CITY),
//                                                    jsonObjectProperty.getBoolean (AppConfigTags.PROPERTY_IS_OFFER),
//                                                    jsonObjectProperty.getBoolean (AppConfigTags.PROPERTY_IS_FAVOURITE));
//
//
//                                            JSONArray jsonArrayPropertyImages = jsonObjectProperty.getJSONArray (AppConfigTags.PROPERTY_IMAGES);
//                                            ArrayList<String> propertyImages = new ArrayList<> ();
//
//                                            for (int j = 0; j < jsonArrayPropertyImages.length (); j++) {
//                                                JSONObject jsonObjectImages = jsonArrayPropertyImages.getJSONObject (j);
//                                                propertyImages.add (jsonObjectImages.getString (AppConfigTags.PROPERTY_IMAGE));
//                                                property.setImageList (propertyImages);
//                                            }
//                                            favouritePropertyList.add (i, property);
//                                        }
//
//                                        propertyAdapter.notifyDataSetChanged ();
//                                        if (jsonArrayProperty.length () > 0) {
//                                            swipeRefreshLayout.setRefreshing (false);
//                                            rlNoFavPropertyFound.setVisibility (View.GONE);
//                                        }/*else
//                                        {
//                                            swipeRefreshLayout.setRefreshing (true);
//                                            rlNoFavPropertyFound.setVisibility(View.GONE);
//                                        }*/
//
//                                    } else {
//                                        Utils.showSnackBar (MyFavouriteActivity.this, clMain, message, Snackbar.LENGTH_LONG, null, null);
//                                        if (message.equalsIgnoreCase ("No Property Found")) {
//                                            rlInternetConnection.setVisibility (View.GONE);
//                                            rlNoFavPropertyFound.setVisibility (View.VISIBLE);
//                                            rlList.setVisibility (View.VISIBLE);
//                                        } else {
//                                            rlInternetConnection.setVisibility (View.GONE);
//                                            rlNoFavPropertyFound.setVisibility (View.GONE);
//                                            rlList.setVisibility (View.VISIBLE);
//
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
//                                    e.printStackTrace ();
//                                }
//                            } else {
//                                Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
//                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
//                            }
//                            swipeRefreshLayout.setRefreshing (false);
//                        }
//                    },
//                    new com.android.volley.Response.ErrorListener () {
//                        @Override
//                        public void onErrorResponse (VolleyError error) {
//                            swipeRefreshLayout.setRefreshing (false);
//                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
//                            Utils.showSnackBar (MyFavouriteActivity.this, clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
//                        }
//                    }) {
//                @Override
//                protected Map<String, String> getParams () throws AuthFailureError {
//                    BuyerDetailsPref buyerDetailsPref = BuyerDetailsPref.getInstance ();
//                    Map<String, String> params = new Hashtable<String, String> ();
//                    params.put (AppConfigTags.TYPE, "favourite_property_list");
//                    params.put (AppConfigTags.BUYER_ID, String.valueOf (buyerDetailsPref.getIntPref (MyFavouriteActivity.this, BuyerDetailsPref.BUYER_ID)));
//                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
//                    return params;
//                }
//
//                @Override
//                public Map<String, String> getHeaders () throws AuthFailureError {
//                    Map<String, String> params = new HashMap<> ();
//                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
//                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
//                    return params;
//                }
//            };
//            Utils.sendRequest (strRequest1, 60);
//        } else {
//            swipeRefreshLayout.setRefreshing (false);
//            Utils.showSnackBar (this, clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
//                @Override
//                public void onClick (View v) {
//                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
//                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity (dialogIntent);
//                }
//            });
//            rlInternetConnection.setVisibility (View.VISIBLE);
//            rlList.setVisibility (View.GONE);
//            rlNoFavPropertyFound.setVisibility (View.GONE);
//        }
//    }
    
}



