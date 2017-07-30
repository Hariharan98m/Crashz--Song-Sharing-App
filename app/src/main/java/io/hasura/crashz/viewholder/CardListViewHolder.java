package io.hasura.crashz.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.hasura.crashz.R;

/**
 * Created by HARIHARAN on 09-07-2017.
 */

public class CardListViewHolder extends RecyclerView.ViewHolder{
    public CardView cardView;
    public TextView cardText;

    public CardListViewHolder(View itemView) {
        super(itemView);
        cardText= (TextView) itemView.findViewById(R.id.drawer_text);
        cardView=(CardView) itemView.findViewById(R.id.drawer_card);
    }
}
