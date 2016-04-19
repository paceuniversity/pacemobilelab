package com.example.jangerhard.tutor;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.TutorViewHolder> {

    private List<TutorInfo> tutorList;

    public CardAdapter(List<TutorInfo> tutorList) {
        this.tutorList = tutorList;
    }

    public List<TutorInfo> getTutorInfo(){
        return tutorList;
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    @Override
    public void onBindViewHolder(TutorViewHolder contactViewHolder, int i) {
        TutorInfo ti = tutorList.get(i);
        contactViewHolder.vName.setText(ti.name);
        contactViewHolder.vEmail.setText(ti.email);
        contactViewHolder.rb.setRating(ti.rating);
        contactViewHolder.vImage.setImageResource(ti.image_resource);
    }

    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_rating, viewGroup, false);

        return new TutorViewHolder(itemView);
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vEmail;
        protected ImageView vImage;
        protected RatingBar rb;
        protected View v;

        public TutorViewHolder(final View v) {
            super(v);
            this.v = v;
            vName =  (TextView) v.findViewById(R.id.tv_tutor_name_general);
            vEmail = (TextView)  v.findViewById(R.id.tv_tutor_email_general);
            vImage = (ImageView) v.findViewById(R.id.iv_tutor_general);
            rb = (RatingBar) v.findViewById(R.id.ratingbar);
            rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    updateRating(rating);
                }
            });
        }

        public void updateRating(float rating){
            tutorList.get(getAdapterPosition()).rating = rating;
        }
    }
}
