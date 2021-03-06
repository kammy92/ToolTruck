package com.tooltruck.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tooltruck.R;
import com.tooltruck.activity.MainActivity;
import com.tooltruck.utils.AppConfigTags;
import com.tooltruck.utils.AppConfigURL;
import com.tooltruck.utils.Constants;
import com.tooltruck.utils.NetworkConnection;
import com.tooltruck.utils.TypefaceSpan;
import com.tooltruck.utils.UserDetailsPref;
import com.tooltruck.utils.Utils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import mabbas007.tagsedittext.TagsEditText;

/**
 * Created by l on 20/03/2017.
 */

public class SignUpFragment extends Fragment implements TagsEditText.TagsEditListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    final List<String> list = new ArrayList<String> ();
    EditText etName;
    EditText etEmail;
    EditText etMobile;
    CoordinatorLayout clMain;
    ProgressDialog progressDialog;
    TextView tvTerm;
    TextView tvSignUp;
    UserDetailsPref userDetailsPref;
    Spinner spRole;
    EditText etCompanyAddress;
    EditText etCompanyAddress2;
    EditText etCompanyName;
    EditText etCompanyZipCode;
    EditText etCompanyZipCode2;
    LinearLayout llDriver;
    LinearLayout llTechnician;
    private TagsEditText mTagsEditText;

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate (R.layout.fragment_sign_up, container, false);
        initView (rootView);
        initData ();
        initListener ();
        return rootView;
    }
    
    private void initView (View rootView) {
        etName = (EditText) rootView.findViewById (R.id.etUserName);
        etEmail = (EditText) rootView.findViewById (R.id.etEmail);
        etMobile = (EditText) rootView.findViewById (R.id.etPhone);
        tvSignUp = (TextView) rootView.findViewById (R.id.tvSignUp);
        clMain = (CoordinatorLayout) rootView.findViewById (R.id.clMain);
        tvTerm = (TextView) rootView.findViewById (R.id.tvTermConditions);
        mTagsEditText = (TagsEditText) rootView.findViewById (R.id.tagsEditText);
        Utils.setTypefaceToAllViews (getActivity (), tvSignUp);
        spRole = (Spinner) rootView.findViewById (R.id.spRole);
    
        etCompanyAddress = (EditText) rootView.findViewById (R.id.etCompanyAddress);
        etCompanyAddress2 = (EditText) rootView.findViewById (R.id.etCompanyAddress2);
        etCompanyZipCode = (EditText) rootView.findViewById (R.id.etCompanyZip);
        etCompanyZipCode2 = (EditText) rootView.findViewById (R.id.etCompanyZip2);
        etCompanyName = (EditText) rootView.findViewById (R.id.etCompanyName);
        llDriver = (LinearLayout) rootView.findViewById (R.id.llDriver);
        llTechnician = (LinearLayout) rootView.findViewById (R.id.llTechnician);
    }
    
    private void initData () {
        userDetailsPref = UserDetailsPref.getInstance ();
        progressDialog = new ProgressDialog (getActivity ());
        SpannableString ss = new SpannableString (getResources ().getString (R.string.activity_login_text_i_agree));
        ss.setSpan (new myClickableSpan (1), 17, 35, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan (new myClickableSpan (2), 40, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.md_material_blue_600)), 17, 35, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan (new ForegroundColorSpan (getResources ().getColor (R.color.md_material_blue_600)), 40, 54, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTerm.setText (ss);
        tvTerm.setMovementMethod (LinkMovementMethod.getInstance ());
    
        String[] title = {
                "110077",
                "110075",
                "330022",
                "660099",
                "220088"
        };
    
        mTagsEditText.setHint ("Zip Code");
        mTagsEditText.setTagsListener (this);
        mTagsEditText.setTagsWithSpacesEnabled (true);
        mTagsEditText.setAdapter (new ArrayAdapter<String> (getActivity (),
                android.R.layout.simple_dropdown_item_1line, title));
        mTagsEditText.setThreshold (1);
    
    
        list.add ("Select Role");
        list.add ("Truck Driver");
        list.add ("Technician");
        ArrayAdapter<String> adp1 = new ArrayAdapter<String> (getActivity (), android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource (android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter (adp1);
    
    
        spRole.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected (AdapterView<?> arg0, View arg1, int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {
                    case 0:
                        llDriver.setVisibility (View.GONE);
                        llTechnician.setVisibility (View.GONE);
                        break;
                    case 1:
                        llDriver.setVisibility (View.VISIBLE);
                        llTechnician.setVisibility (View.GONE);
                        break;
                    case 2:
                        llDriver.setVisibility (View.GONE);
                        llTechnician.setVisibility (View.VISIBLE);
                        break;
                }
            }
        
            @Override
            public void onNothingSelected (AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
    
    private void initListener () {
        tvSignUp.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Utils.hideSoftKeyboard (getActivity ());
                SpannableString s = new SpannableString (getResources ().getString (R.string.please_enter_name));
                s.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s2 = new SpannableString (getResources ().getString (R.string.please_enter_email));
                s2.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s2.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s3 = new SpannableString (getResources ().getString (R.string.please_enter_mobile));
                s3.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s3.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s4 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s4.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s4.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s5 = new SpannableString (getResources ().getString (R.string.please_enter_valid_mobile));
                s5.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s5.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                SpannableString s6 = new SpannableString (getResources ().getString (R.string.please_enter_valid_email));
                s6.setSpan (new TypefaceSpan (getActivity (), Constants.font_name), 0, s6.length (), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                if (etName.getText ().toString ().trim ().length () == 0 && etEmail.getText ().toString ().length () == 0 && etMobile.getText ().toString ().length () == 0) {
                    etName.setError (s);
                    etEmail.setError (s2);
                    etMobile.setError (s3);
                } else if (etName.getText ().toString ().trim ().length () == 0) {
                    etName.setError (s);
                } else if (etEmail.getText ().toString ().trim ().length () == 0) {
                    etEmail.setError (s2);
                } else if (! Utils.isValidEmail1 (etEmail.getText ().toString ())) {
                    etEmail.setError (s6);
                } else if (etMobile.getText ().toString ().trim ().length () == 0) {
                    etMobile.setError (s3);
                } else {
                    sendSignUpDetailsToServer (etName.getText ().toString ().trim (), etEmail.getText ().toString ().trim (), etMobile.getText ().toString ().trim ());
                }
            }
            
        });
        etName.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etName.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etEmail.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etEmail.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
        etMobile.addTextChangedListener (new TextWatcher () {
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
                if (count == 0) {
                    etMobile.setError (null);
                }
            }
            
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }
            
            @Override
            public void afterTextChanged (Editable s) {
            }
        });
    }
    
    private void sendSignUpDetailsToServer (final String name, final String email, final String number) {
        if (NetworkConnection.isNetworkAvailable (getActivity ())) {
            Utils.showProgressDialog (progressDialog, getResources ().getString (R.string.progress_dialog_text_please_wait), true);
            Utils.showLog (Log.INFO, "" + AppConfigTags.URL, AppConfigURL.URL_SIGN_UP, true);
            StringRequest strRequest1 = new StringRequest (Request.Method.POST, AppConfigURL.URL_SIGN_UP,
                    new com.android.volley.Response.Listener<String> () {
                        @Override
                        public void onResponse (String response) {
                            Utils.showLog (Log.INFO, AppConfigTags.SERVER_RESPONSE, response, true);
                            if (response != null) {
                                try {
                                    JSONObject jsonObj = new JSONObject (response);
                                    boolean error = jsonObj.getBoolean (AppConfigTags.ERROR);
                                    String message = jsonObj.getString (AppConfigTags.MESSAGE);
                                    if (! error) {
                                        userDetailsPref.putIntPref (getActivity (), UserDetailsPref.USER_ID, jsonObj.getInt (AppConfigTags.BUYER_ID));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_NAME, jsonObj.getString (AppConfigTags.BUYER_NAME));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_EMAIL, jsonObj.getString (AppConfigTags.BUYER_EMAIL));
                                        userDetailsPref.putStringPref (getActivity (), UserDetailsPref.USER_MOBILE, jsonObj.getString (AppConfigTags.BUYER_MOBILE));
                                        
                                        Intent intent = new Intent (getActivity (), MainActivity.class);
                                        intent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity (intent);
                                        getActivity ().overridePendingTransition (R.anim.slide_in_right, R.anim.slide_out_left);
                                    } else {
                                        Utils.showSnackBar (getActivity (), clMain, message, Snackbar.LENGTH_LONG, null, null);
                                    }
                                    progressDialog.dismiss ();
                                } catch (Exception e) {
                                    progressDialog.dismiss ();
                                    Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_exception_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                    e.printStackTrace ();
                                }
                            } else {
                                Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                                Utils.showLog (Log.WARN, AppConfigTags.SERVER_RESPONSE, AppConfigTags.DIDNT_RECEIVE_ANY_DATA_FROM_SERVER, true);
                            }
                            progressDialog.dismiss ();
                        }
                    },
                    new com.android.volley.Response.ErrorListener () {
                        @Override
                        public void onErrorResponse (VolleyError error) {
                            progressDialog.dismiss ();
                            Utils.showLog (Log.ERROR, AppConfigTags.VOLLEY_ERROR, error.toString (), true);
                            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_error_occurred), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_dismiss), null);
                        }
                    }) {
                @Override
                protected Map<String, String> getParams () throws AuthFailureError {
                    Map<String, String> params = new Hashtable<String, String> ();
                    params.put (AppConfigTags.BUYER_NAME, name);
                    params.put (AppConfigTags.BUYER_EMAIL, email);
                    params.put (AppConfigTags.BUYER_MOBILE, number);
                    params.put (AppConfigTags.BUYER_FIREBASE_ID, userDetailsPref.getStringPref (getActivity (), UserDetailsPref.USER_FIREBASE_ID));
                    Utils.showLog (Log.INFO, AppConfigTags.PARAMETERS_SENT_TO_THE_SERVER, "" + params, true);
                    return params;
                }
                
                @Override
                public Map<String, String> getHeaders () throws AuthFailureError {
                    Map<String, String> params = new HashMap<> ();
                    params.put (AppConfigTags.HEADER_API_KEY, Constants.api_key);
                    Utils.showLog (Log.INFO, AppConfigTags.HEADERS_SENT_TO_THE_SERVER, "" + params, false);
                    return params;
                }
            };
            Utils.sendRequest (strRequest1, 60);
        } else {
            Utils.showSnackBar (getActivity (), clMain, getResources ().getString (R.string.snackbar_text_no_internet_connection_available), Snackbar.LENGTH_LONG, getResources ().getString (R.string.snackbar_action_go_to_settings), new View.OnClickListener () {
                @Override
                public void onClick (View v) {
                    Intent dialogIntent = new Intent (Settings.ACTION_SETTINGS);
                    dialogIntent.addFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity (dialogIntent);
                }
            });
        }
    }
    
    
    @Override
    public void onTagsChanged (Collection<String> tags) {
        Log.d (TAG, "Tags changed: ");
        Log.d ("value", Arrays.toString (tags.toArray ()));
    }
    
    @Override
    public void onEditingFinished () {
        Log.d (TAG, "OnEditing finished");
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(mTagsEditText.getWindowToken(), 0);
//        //mTagsEditText.clearFocus();
    }
    
    @Override
    public void onClick (View v) {
    }
    
    public class myClickableSpan extends ClickableSpan {
        int pos;
        
        public myClickableSpan (int position) {
            this.pos = position;
        }
        
        @Override
        public void onClick (View widget) {
            Toast.makeText (getActivity (), "Position " + pos + " clicked!", Toast.LENGTH_LONG).show ();
        }
    }
}