package com.tooltruck.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.typeface.IIcon;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.ImageHolder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.tooltruck.R;
import com.tooltruck.utils.SetTypeFace;
import com.tooltruck.utils.UserDetailsPref;
import com.tooltruck.utils.Utils;

public class MainActivity extends AppCompatActivity {
    
    public static int PERMISSION_REQUEST_CODE = 11;
    final int CURRENT_LOCATION_REQUEST_CODE = 1;
    GoogleApiClient client;
    Double currentLatitude = 0.0;
    Double currentLongitude = 0.0;
    
    
    Bundle savedInstanceState;
    Toolbar toolbar;
    RecyclerView rvPropertyList;
    SwipeRefreshLayout swipeRefreshLayout;
    UserDetailsPref buyerDetailsPref;
    CoordinatorLayout clMain;
    ImageView ivFilter;
    ImageView ivMaps;
    ImageView ivOverflow;
    Menu menu2;
    ImageView ivNavigation;
    RelativeLayout rlList;
    RelativeLayout rlInternetConnection;
    RelativeLayout rlNoResultFound;
    TextView tvRetry;
    TextView tvResetFilter;
    private AccountHeader headerResult = null;
    private Drawer result = null;
    
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);
        initView ();
        initData ();
        initListener ();
        initDrawer ();
        isLogin ();
        checkPermissions ();
        this.savedInstanceState = savedInstanceState;
    }
    
    private void isLogin () {
        if (buyerDetailsPref.getIntPref (MainActivity.this, UserDetailsPref.USER_ID) == 0) {
            Intent myIntent = new Intent (this, LoginActivity.class);
            startActivity (myIntent);
        }
        if (buyerDetailsPref.getIntPref (MainActivity.this, UserDetailsPref.USER_ID) == 0)
            finish ();
    }
    
    private void initView () {
        toolbar = (Toolbar) findViewById (R.id.toolbar);
        rvPropertyList = (RecyclerView) findViewById (R.id.rvPropertyList);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById (R.id.swipe_refresh_layout);
        clMain = (CoordinatorLayout) findViewById (R.id.clMain);
        ivFilter = (ImageView) findViewById (R.id.ivFilter);
        ivOverflow = (ImageView) findViewById (R.id.ivOverflow);
        ivNavigation = (ImageView) findViewById (R.id.ivNavigation);
        ivMaps = (ImageView) findViewById (R.id.ivMaps);
        
        rlInternetConnection = (RelativeLayout) findViewById (R.id.rlInternetConnection);
        rlNoResultFound = (RelativeLayout) findViewById (R.id.rlNoResultFound);
        rlList = (RelativeLayout) findViewById (R.id.rlList);
        tvRetry = (TextView) findViewById (R.id.tvRetry);
        tvResetFilter = (TextView) findViewById (R.id.tvResetFilter);
        
    }
    
    private void initData () {
        client = new GoogleApiClient.Builder (this).addApi (AppIndex.API).build ();
        buyerDetailsPref = UserDetailsPref.getInstance ();
        Utils.setTypefaceToAllViews (this, clMain);
    }
    
    private void initListener () {
        ivNavigation.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                result.openDrawer ();
            }
        });
    }
    
    public void checkPermissions () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission (Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission (Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions (new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE},
                        MainActivity.PERMISSION_REQUEST_CODE);
            }
        }
    }
    
    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult (requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    boolean showRationale = shouldShowRequestPermissionRationale (permission);
                    if (! showRationale) {
                        AlertDialog.Builder builder = new AlertDialog.Builder (MainActivity.this);
                        builder.setMessage ("Permission are required please enable them on the App Setting page")
                                .setCancelable (false)
                                .setPositiveButton ("OK", new DialogInterface.OnClickListener () {
                                    public void onClick (DialogInterface dialog, int id) {
                                        dialog.dismiss ();
                                        Intent intent = new Intent (Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                                Uri.fromParts ("package", getPackageName (), null));
                                        startActivity (intent);
                                    }
                                });
                        AlertDialog alert = builder.create ();
                        alert.show ();
                    } else if (Manifest.permission.ACCESS_FINE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.ACCESS_COARSE_LOCATION.equals (permission)) {
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals (permission)) {
                    }
                }
            }
        }
        
        
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        }
    }
    
    private void initDrawer () {
        IProfile profile = new IProfile () {
            @Override
            public Object withName (String name) {
                return null;
            }
            
            @Override
            public StringHolder getName () {
                return null;
            }
            
            @Override
            public Object withEmail (String email) {
                return null;
            }
            
            @Override
            public StringHolder getEmail () {
                return null;
            }
            
            @Override
            public Object withIcon (Drawable icon) {
                return null;
            }
            
            @Override
            public Object withIcon (Bitmap bitmap) {
                return null;
            }
            
            @Override
            public Object withIcon (@DrawableRes int iconRes) {
                return null;
            }
            
            @Override
            public Object withIcon (String url) {
                return null;
            }
            
            @Override
            public Object withIcon (Uri uri) {
                return null;
            }
            
            @Override
            public Object withIcon (IIcon icon) {
                return null;
            }
            
            @Override
            public ImageHolder getIcon () {
                return null;
            }
            
            @Override
            public Object withSelectable (boolean selectable) {
                return null;
            }
            
            @Override
            public boolean isSelectable () {
                return false;
            }
            
            @Override
            public Object withIdentifier (long identifier) {
                return null;
            }
            
            @Override
            public long getIdentifier () {
                return 0;
            }
        };
        
        DrawerImageLoader.init (new AbstractDrawerImageLoader () {
            @Override
            public void set (ImageView imageView, Uri uri, Drawable placeholder) {
                if (uri != null) {
                    Glide.with (imageView.getContext ()).load (uri).placeholder (placeholder).into (imageView);
                }
            }
            
            @Override
            public void cancel (ImageView imageView) {
                Glide.clear (imageView);
            }
            
            @Override
            public Drawable placeholder (Context ctx, String tag) {
                //define different placeholders for different imageView targets
                //default tags are accessible via the DrawerImageLoader.Tags
                //custom ones can be checked via string. see the CustomUrlBasePrimaryDrawerItem LINE 111
                if (DrawerImageLoader.Tags.PROFILE.name ().equals (tag)) {
                    return DrawerUIUtils.getPlaceHolder (ctx);
                } else if (DrawerImageLoader.Tags.ACCOUNT_HEADER.name ().equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (com.mikepenz.materialdrawer.R.color.colorPrimary).sizeDp (56);
                } else if ("customUrlItem".equals (tag)) {
                    return new IconicsDrawable (ctx).iconText (" ").backgroundColorRes (R.color.md_white_1000);
                }
                
                //we use the default one for
                //DrawerImageLoader.Tags.PROFILE_DRAWER_ITEM.name()
                
                return super.placeholder (ctx, tag);
            }
        });
        headerResult = new AccountHeaderBuilder ()
                .withActivity (this)
                .withCompactStyle (false)
                .withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                .withTypeface (SetTypeFace.getTypeface (this))
                .withPaddingBelowHeader (false)
                .withSelectionListEnabled (false)
                .withSelectionListEnabledForSingleProfile (false)
                .withProfileImagesVisible (true)
                .withOnlyMainProfileImageVisible (true)
                .withDividerBelowHeader (true)
                .withSavedInstance (savedInstanceState)
                .build ();


//        if (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.BUYER_IMAGE).length () != 0) {
//            headerResult.addProfiles (new ProfileDrawerItem ()
//                    .withIcon (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.BUYER_IMAGE))
//                    .withName (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_NAME))
//                    .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL)));
//        } else {
        headerResult.addProfiles (new ProfileDrawerItem ()
                .withName (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_NAME))
                .withEmail (buyerDetailsPref.getStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL)));
//        }
        
        result = new DrawerBuilder ()
                .withActivity (this)
                .withAccountHeader (headerResult)
//                .withToolbar (toolbar)
//                .withItemAnimator (new AlphaCrossFadeAnimator ())
                .addDrawerItems (
                        new PrimaryDrawerItem ().withName ("Home").withIcon (FontAwesome.Icon.faw_home).withIdentifier (1).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Favourites").withIcon (FontAwesome.Icon.faw_heart).withIdentifier (2).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("How It Works").withIcon (FontAwesome.Icon.faw_handshake_o).withIdentifier (3).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("About Us").withIcon (FontAwesome.Icon.faw_info).withIdentifier (4).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Testimonials").withIcon (FontAwesome.Icon.faw_comments).withIdentifier (5).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Contact Us").withIcon (FontAwesome.Icon.faw_phone).withIdentifier (6).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("FAQ").withIcon (FontAwesome.Icon.faw_question).withIdentifier (7).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("My Profile").withIcon (FontAwesome.Icon.faw_user).withIdentifier (8).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Change Password").withIcon (FontAwesome.Icon.faw_key).withIdentifier (9).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this)),
                        new PrimaryDrawerItem ().withName ("Sign Out").withIcon (FontAwesome.Icon.faw_sign_out).withIdentifier (10).withSelectable (false).withTypeface (SetTypeFace.getTypeface (MainActivity.this))
                )
                .withSavedInstance (savedInstanceState)
                .withOnDrawerItemClickListener (new Drawer.OnDrawerItemClickListener () {
                    @Override
                    public boolean onItemClick (View view, int position, IDrawerItem drawerItem) {
                        switch ((int) drawerItem.getIdentifier ()) {
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 8:
                                break;
                            case 9:
                                break;
                            case 10:
                                showLogOutDialog ();
                                break;
                        }
                        return false;
                    }
                })
                .build ();
//        result.getActionBarDrawerToggle ().setDrawerIndicatorEnabled (false);
    }
    
    private void showLogOutDialog () {
        MaterialDialog dialog = new MaterialDialog.Builder (this)
                .limitIconToDefaultSize ()
                .content ("Do you wish to Sign Out?")
                .positiveText ("Yes")
                .negativeText ("No")
                .typeface (SetTypeFace.getTypeface (MainActivity.this), SetTypeFace.getTypeface (MainActivity.this))
                .onPositive (new MaterialDialog.SingleButtonCallback () {
                    @Override
                    public void onClick (@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        buyerDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_NAME, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_EMAIL, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_MOBILE, "");
                        buyerDetailsPref.putStringPref (MainActivity.this, UserDetailsPref.USER_LOGIN_KEY, "");
                        buyerDetailsPref.putIntPref (MainActivity.this, UserDetailsPref.USER_ID, 0);
                        
                        Intent intent = new Intent (MainActivity.this, LoginActivity.class);
                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity (intent);
                        overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
                    }
                }).build ();
        dialog.show ();
    }
    
    @Override
    public void onBackPressed () {
        if (result != null && result.isDrawerOpen ()) {
            result.closeDrawer ();
        } else {
            super.onBackPressed ();
            finish ();
            overridePendingTransition (R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}