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
import com.rizosoft.eatoutapp.interfaces.OnClickPhoneRestaurantOverview;
import com.rizosoft.eatoutapp.pojos.Restaurant;
import com.rizosoft.eatoutapp.pojos.Review;

/**
 * Created by oliver on 13/04/2018.
 */

public class RestaurantReviewsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Restaurant restaurant;
    Context context;

    public static int HEADER_ELEMENT = 0;
    public static int NORMAL_ELEMENT = 1;


    public class RestaurantLocationViewHolder extends RecyclerView.ViewHolder  {


        TextView tvAddress;
        TextView tvPhoneNumber;
        ImageView imPhoneButton;

        public RestaurantLocationViewHolder(View itemView) {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            imPhoneButton = itemView.findViewById(R.id.imPhoneButton);
        }

        public void bind(int listIndex) {
            tvAddress.setText(restaurant.getStreetAddress());
            tvPhoneNumber.setText(restaurant.getPhoneNumber());

            imPhoneButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v)  {
                    ((OnClickPhoneRestaurantOverview) context).onClickPhoneRestaurantOverview(restaurant.getPhoneNumber());
                }
            });
        }
    }


    public class RestaurantReviewViewHolder extends RecyclerView.ViewHolder  {
        TextView tvNameReviewer;
        RatingBar rtBar;
        TextView tvTextReview;

        public RestaurantReviewViewHolder(View itemView) {
            super(itemView);

            tvNameReviewer = itemView.findViewById(R.id.tvNameReviewer);
            rtBar = itemView.findViewById(R.id.rbReview);
            tvTextReview = itemView.findViewById(R.id.tvTextReview);
        }

        public void bind(int listIndex)  {
            Review review = restaurant.getReviews().get(listIndex-1);
            StringBuilder sb = new StringBuilder();
            sb.append(review.getAuthorName());

            if (!review.getRelativeTimeDescription().equals("")) {
                sb.append(" ("+review.getRelativeTimeDescription()+")");
            }
            tvNameReviewer.setText(sb);


            rtBar.setRating(review.getRating());
            tvTextReview.setText(review.getText());
        }

    }


    public RestaurantReviewsAdapter(Context context, Restaurant restaurant) {
        this.context = context;
        this.restaurant = restaurant;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        if (viewType == NORMAL_ELEMENT) {
            int layoutIdForListItem = R.layout.review_layout;

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(layoutIdForListItem, parent, false);
            RestaurantReviewsAdapter.RestaurantReviewViewHolder restaurantViewHolder = new RestaurantReviewsAdapter.RestaurantReviewViewHolder(view);
            return restaurantViewHolder;
        } else {
            int layoutIdForHeader = R.layout.layout_address_phone;
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(layoutIdForHeader, parent, false);
            RestaurantReviewsAdapter.RestaurantLocationViewHolder restaurantViewHolder = new RestaurantReviewsAdapter.RestaurantLocationViewHolder(view);
            return restaurantViewHolder;

        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof RestaurantReviewViewHolder) {
            ((RestaurantReviewViewHolder) holder).bind(position);
        } else {
            ((RestaurantLocationViewHolder) holder).bind(position);
        }
    }

    @Override
    public int getItemCount() {
        return restaurant.getReviews().size()+1;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_ELEMENT;
        } else {
            return NORMAL_ELEMENT;
        }
    }




}
