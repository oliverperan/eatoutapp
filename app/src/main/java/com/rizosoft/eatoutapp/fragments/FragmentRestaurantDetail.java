package com.rizosoft.eatoutapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.adapters.TabsPageAdapter;
import com.rizosoft.eatoutapp.interfaces.OnGettingRestaurantDetails;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

/**
 * Created by oliver on 03/04/2018.
 */

public class FragmentRestaurantDetail extends Fragment implements OnGettingRestaurantDetails {

    private Restaurant restaurant;
    private Context context;
    private ViewPager viewPager;
    private ImageView imagePoster;
    private TabLayout tabLayout;
    private Activity activity;
    private CoordinatorLayout mCoordinatorLayout;
    private CollapsingToolbarLayout ctl;
    private  AppBarLayout mAppBarLayout;
    private View fragmentView;
    private Boolean isTablet;
    private int mCurrentItem;
    //private NestedScrollView scrollView;

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public static FragmentRestaurantDetail newInstance(Restaurant restaurant, Context context) {
        FragmentRestaurantDetail fragmentFirst = new FragmentRestaurantDetail();
        fragmentFirst.setRestaurant(restaurant);
        fragmentFirst.setContext(context);
        return fragmentFirst;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.restaurant), restaurant);
        outState.putInt(getString(R.string.active_page_tab), viewPager.getCurrentItem());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_restaurant_detail, container, false);
        fragmentView = view;
        viewPager = view.findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        //scrollView = view.findViewById(R.id.scrollView);
        mCurrentItem = 0;


        imagePoster = view.findViewById(R.id.imPoster);
        tabLayout = view.findViewById(R.id.tabs);

        isTablet = getResources().getBoolean(R.bool.istablet);

        if (!isTablet) {
            Toolbar myToolbar = (Toolbar) view.findViewById(R.id.toolbar);


            ctl = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
            mAppBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);

            if (ctl != null) { /*ctl is null when horizontal orientation, I don't have collapsinglayout in horizontal orientation*/
                if (myToolbar != null) {
                    if (((AppCompatActivity)getActivity()).getSupportActionBar() == null) {
                        ((AppCompatActivity) getActivity()).setSupportActionBar(myToolbar);
                    }
                    ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);

                    }
                    mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);
                    ctl = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
                }

            }
        } else {
            mCoordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinator);

            view.post(new Runnable() {
                @Override
                public void run() {
                    int width = view.getWidth() / 2;

                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)viewPager.getLayoutParams();

                    params.setMargins(5, 0, width , 0);
                    viewPager.setLayoutParams(params);
                }
            });
        }


        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = (Context) getActivity();
        Boolean scrollImage = true;
        if (savedInstanceState != null) {
            restaurant = savedInstanceState.getParcelable(getString(R.string.restaurant));
            mCurrentItem = savedInstanceState.getInt(getString(R.string.active_page_tab));
            scrollImage = false;
        }

        setControls(fragmentView, scrollImage);



    }


    private void setAppBarOffset(int offsetPx){
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();

        if (behavior != null) {
            behavior.onNestedPreScroll(mCoordinatorLayout, mAppBarLayout, null, 0, offsetPx, new int[]{0, 0},1);
        } else {
            Log.i("Oliver", "behaviour null");
        }

    }


    public void setControls(View view, final Boolean scrollImage) {

        if (!restaurant.getDetailsLoaded()) {
            NetworkUtilities.getDetails(restaurant, (AppCompatActivity) getActivity(), this);
        }
        else {
            if (!isTablet) {
                ctl.setTitle(restaurant.getName());
            }
            tabLayout.addTab(tabLayout.newTab().setText("Overview"));
            tabLayout.addTab(tabLayout.newTab().setText("Reservations"));
            tabLayout.addTab(tabLayout.newTab().setText("Add a reservation"));
            tabLayout.addTab(tabLayout.newTab().setText("Map"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            tabLayout.setupWithViewPager(viewPager);
            final TabsPageAdapter tabAdapter = new TabsPageAdapter(getChildFragmentManager(), restaurant, context, this);
            //final TabsPageAdapter tabAdapter = new TabsPageAdapter(getActivity().getSupportFragmentManager(), restaurant);
            viewPager.setAdapter(tabAdapter);
            viewPager.setCurrentItem(mCurrentItem);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setupWithViewPager(viewPager);


            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


            if (restaurant.getPhotoReference() != null) {
                String sURLImage = NetworkUtilities.URL_PHOTO;
                sURLImage = sURLImage.replace(":PHOTO_REFERENCE", restaurant.getPhotoReference());
                sURLImage = sURLImage.replace(":API_KEY", NetworkUtilities.getAPI_Key(context));

                Picasso.with(context)
                        .load(sURLImage)
                        .error(R.drawable.not_found)
                        .into(imagePoster, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (ctl != null) {
                                    mAppBarLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            int heightPx = fragmentView.findViewById(R.id.imPoster).getHeight();

                                            float dp = NetworkUtilities.convertPixelsToDp((float) heightPx, context);

                                            if ((dp > 320) && (!isTablet)) {
                                               if (scrollImage) { setAppBarOffset(heightPx / 2);}
                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });

                TextView tvContributions = fragmentView.findViewById(R.id.tvContributions);
                String urlContributions = restaurant.getPictureAttibutions();
                urlContributions = urlContributions.replace("\">","\">Picture by ");
                tvContributions.setText(Html.fromHtml(urlContributions));
                tvContributions.setMovementMethod(LinkMovementMethod.getInstance());

            } else {
                Picasso.with(context)
                        .load(R.drawable.not_found)
                        .resize(context.getResources().getInteger(R.integer.w185_width),
                                context.getResources().getInteger(R.integer.w278_height))
                        .error(R.drawable.not_found)
                        .into(imagePoster, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (ctl != null) {
                                    mAppBarLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            int heightPx = fragmentView.findViewById(R.id.imPoster).getHeight();

                                            float dp = NetworkUtilities.convertPixelsToDp((float) heightPx, context);

                                               if (scrollImage) { setAppBarOffset(heightPx / 2);}
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }


    }

    @Override
    public void onGettingRestaurantDetails(Restaurant restaurant) {
        setControls(getView(), true);
    }

    public void setActivePageReservations(int activePage)
    {
        viewPager.setCurrentItem(activePage);
    }



}
