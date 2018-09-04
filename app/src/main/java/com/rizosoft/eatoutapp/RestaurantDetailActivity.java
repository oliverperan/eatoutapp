package com.rizosoft.eatoutapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.Transition;
import android.view.Gravity;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.rizosoft.eatoutapp.fragments.FragmentRestaurantDetail;
import com.rizosoft.eatoutapp.interfaces.OnClickPhoneRestaurantOverview;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;

public class RestaurantDetailActivity extends AppCompatActivity implements OnClickPhoneRestaurantOverview {

    private static final int REQUEST_PHONE_CALL =1;
    private Restaurant restaurant;
    private static  final String TAG_FR_DETAIL="frDetail";
    private String phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!getResources().getBoolean(R.bool.istablet)) {
            Transition slide = new Slide(Gravity.END);
            slide.setDuration(500);
            slide.setInterpolator(new DecelerateInterpolator());
            getWindow().setEnterTransition(slide);
            getWindow().setAllowEnterTransitionOverlap(false);
        }
        setContentView(R.layout.activity_restaurant_detail);

        if (savedInstanceState == null) {
            restaurant = getIntent().getParcelableExtra("restaurant");
            FragmentRestaurantDetail fragmentRestaurantDetail = FragmentRestaurantDetail.newInstance(restaurant, this);
            FrameLayout fr = findViewById(R.id.frameRestaurantDetails);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(fr.getId(),fragmentRestaurantDetail,TAG_FR_DETAIL);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.restaurant),restaurant);
    }

    @Override
    public Intent getParentActivityIntent() {
        if (super.getParentActivityIntent()!= null) {
            return super.getParentActivityIntent().addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        } else
        {
            return super.getParentActivityIntent();
        }
    }

    @Override
    public void onClickPhoneRestaurantOverview(String phoneNumber) {
        try {
            makePhoneCall(phoneNumber);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void makePhoneCall(String phoneNumber){
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED))
        {
            this.phoneNumber = phoneNumber;
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            String uri = "tel:" + phoneNumber.trim() ;
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse(uri));
            startActivity(intent);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Boolean sw=true;
        switch (requestCode) {
            case REQUEST_PHONE_CALL:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    sw = (grantResult == PackageManager.PERMISSION_GRANTED);
                }
                break;
        }

        if (!sw) {
            ConstraintLayout cl = findViewById(R.id.cl);
            NetworkUtilities.showMessage(cl,getResources().getString(R.string.permission_phone_call_rejected));
        } else {
            makePhoneCall(phoneNumber);
        }

    }


    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }
}
