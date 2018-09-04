package com.rizosoft.eatoutapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rizosoft.eatoutapp.R;
import com.rizosoft.eatoutapp.pojos.Attribution;

import java.util.List;

/**
 * Created by oliver on 15/04/2018.
 */

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditViewHolder>  {

    List<Attribution> attributions;
    Context context;
    public class CreditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView tvInfo;

        public CreditViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imAttribution);
            tvInfo = itemView.findViewById(R.id.tvAttribution);
        }

        @Override
        public void onClick(View v) {

        }

        public void bind(int listIndex) {
            Attribution attribution = attributions.get(listIndex);
            imageView.setImageDrawable(context.getResources().getDrawable(attribution.getDrawable()));
            tvInfo.setText(Html.fromHtml(attribution.toString()));
            tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }


    @Override
    public CreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.layout_for_credits;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        CreditViewHolder creditViewHolder = new CreditViewHolder(view);
        return creditViewHolder;
    }

    @Override
    public void onBindViewHolder(CreditViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return attributions.size();
    }


    public CreditsAdapter(List<Attribution> attributions, Context context) {
        this.attributions = attributions;
        this.context = context;
    }
}
