package com.rizosoft.eatoutapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.interfaces.OnClickOnItemRestaurantSearch;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.utilities.NetworkUtilities;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by oliver on 01/04/2018.
 */

public class RestuarantSearchResultAdapter extends RecyclerView.Adapter<RestuarantSearchResultAdapter.RestaurantSearchResultViewHolder>{

    List<Restaurant> restaurants;
    Context context;
    OnClickOnItemRestaurantSearch clickOnItemRestaurantSearch;


    public RestuarantSearchResultAdapter(Context context, List<Restaurant> restaurants, OnClickOnItemRestaurantSearch clickOnItemRestaurantSearch) {
        this.restaurants = restaurants;
        this.context = context;
        this.clickOnItemRestaurantSearch = clickOnItemRestaurantSearch;
    }

    @Override
    public RestaurantSearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.itemfragmentresultsearch;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        RestaurantSearchResultViewHolder restaurantViewHolder = new RestaurantSearchResultViewHolder(view);
        return restaurantViewHolder;
    }

    @Override
    public void onBindViewHolder(RestaurantSearchResultViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }




    class RestaurantSearchResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        ImageView imRestaurantImage;
        TextView tvRestaurantName;
        RatingBar rtBar;

        public RestaurantSearchResultViewHolder(View itemView) {
            super(itemView);

            imRestaurantImage = itemView.findViewById(R.id.imRestaurant);
            tvRestaurantName = itemView.findViewById(R.id.tName);
            rtBar = itemView.findViewById(R.id.ratingBar);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clickOnItemRestaurantSearch.onClickOnItemRestaurantSearch(restaurants.get(clickedPosition));
        }

        public void bind(int listIndex)  {
            Restaurant restaurant = restaurants.get(listIndex);

            tvRestaurantName.setText(restaurant.getName());
            rtBar.setRating(restaurant.getRating());


            if (restaurant.getPhotoReference()!=null) {
                String sURLImage = NetworkUtilities.URL_PHOTO;
                sURLImage = sURLImage.replace(":PHOTO_REFERENCE", restaurant.getPhotoReference());
                sURLImage = sURLImage.replace(":API_KEY", NetworkUtilities.getAPI_Key(context));
                Picasso.with(context)
                            .load(sURLImage)
                            /*.resize(context.getResources().getInteger(R.integer.w185_width),
                                    context.getResources().getInteger(R.integer.w278_height))*/
                            .error(R.drawable.not_found)
                /*.placeholder(R.drawable.searching)*/
                            .into(imRestaurantImage);
                /* else {
                    imageView.setImageURI(Uri.fromFile(new File(movies[position].getPoster_Path())));
                }*/
            }
            else
            {
                Picasso.with(context)
                        .load(R.drawable.not_found)
                        .resize(context.getResources().getInteger(R.integer.w185_width),
                                context.getResources().getInteger(R.integer.w278_height))
                        .error(R.drawable.not_found)
        /*.placeholder(R.drawable.searching)*/
                        .into(imRestaurantImage);
            }



        }




    }







}
