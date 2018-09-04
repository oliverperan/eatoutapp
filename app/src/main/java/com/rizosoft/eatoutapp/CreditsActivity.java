package com.rizosoft.eatoutapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rizosoft.eatoutapp.adapters.CreditsAdapter;
import com.rizosoft.eatoutapp.pojos.Attribution;

import java.util.ArrayList;
import java.util.List;

public class CreditsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        List<Attribution> attributions = getAttribtutions();
        RecyclerView rcv = findViewById(R.id.rcvCredits);

        GridLayoutManager manager;
        if (getResources().getBoolean(R.bool.istablet)) {
            manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        } else {
            manager = new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false);
        }

        rcv.setLayoutManager(manager);
        //rcv.addItemDecoration(new SimpleDividerItemDecoration(context));
        CreditsAdapter adapter = new CreditsAdapter(attributions, this);
        rcv.setItemAnimator(new DefaultItemAnimator());
        rcv.setAdapter(adapter);
    }

    public List<Attribution> getAttribtutions() {
        List<Attribution> attributions = new ArrayList<Attribution>();
        Attribution attribution = new Attribution();

        attribution.setAuthorName("Pixel Buddha");
        attribution.setUrlName("https://www.flaticon.com/authors/pixel-buddha");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/");
        attribution.setDrawable(R.drawable.save);
        attributions.add(attribution);

        attribution = new Attribution();

        attribution.setAuthorName("Pixel Buddha");
        attribution.setUrlName("https://www.flaticon.com/authors/pixel-buddha");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/");
        attribution.setDrawable(R.drawable.calendar);
        attributions.add(attribution);

        attribution = new Attribution();

        attribution.setAuthorName("SimpleIcon");
        attribution.setUrlName("http://www.simpleicon.com");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/authors/simpleicon");
        attribution.setDrawable(R.drawable.phone);
        attributions.add(attribution);


        attribution = new Attribution();

        attribution.setAuthorName("Freepik");
        attribution.setUrlName("https://www.freepik.com/");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/authors/freepik");
        attribution.setDrawable(R.drawable.eye);
        attributions.add(attribution);


        attribution = new Attribution();

        attribution.setAuthorName("Freepik");
        attribution.setUrlName("https://www.freepik.com/");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/authors/freepik");
        attribution.setDrawable(R.drawable.iconapp);
        attributions.add(attribution);


        attribution = new Attribution();

        attribution.setAuthorName("Designerz Base");
        attribution.setUrlName("http://www.finest.graphics/");
        attribution.setNameSiteSource("Flat icons");
        attribution.setWebSiteSource("https://www.flaticon.com/authors/designerz-base");
        attribution.setDrawable(R.drawable.cancel);
        attributions.add(attribution);

        attribution = new Attribution();

        attribution.setAuthorName("This icon was taken from");
        attribution.setUrlName("https://www.materialui.co/icon/near-me");
        attribution.setNameSiteSource("here");
        attribution.setWebSiteSource("https://icons8.com/icon/2924/near-me");
        attribution.setDrawable(R.drawable.near);
        attributions.add(attribution);







        return attributions;
    }
}
