package com.pacemobilelab.TutorsAtSeidenberg;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class GeneralCardAdapter extends RecyclerView.Adapter<GeneralCardAdapter.TutorViewHolder> {

    private List<Tutor> tutorList;

    public GeneralCardAdapter(List<Tutor> tutorList) {
        this.tutorList = tutorList;
    }

    public List<Tutor> getTutorInfo(){
        return tutorList;
    }

    @Override
    public int getItemCount() {
        return tutorList.size();
    }

    @Override
    public void onBindViewHolder(TutorViewHolder contactViewHolder, int i) {
        Tutor ti = tutorList.get(i);
        contactViewHolder.vName.setText(ti.name);
        contactViewHolder.vEmail.setText(ti.email);
        contactViewHolder.vImage.setImageResource(ti.image_resource);
        contactViewHolder.bEmail.setTag(ti.email);

        final Context c = contactViewHolder.itemView.getContext();
        contactViewHolder.bEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{v.getTag().toString()});
                email.putExtra(Intent.EXTRA_SUBJECT, "Tutor Question");
                email.setType("message/rfc822");
                c.startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });

        contactViewHolder.bRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(c, RateTutorsActivity.class);
                c.startActivity(i);
            }
        });

    }

    @Override
    public TutorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.card_layout_normal, viewGroup, false);

        return new TutorViewHolder(itemView);
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vEmail;
        protected ImageView vImage;
        protected Button bEmail;
        protected Button bRate;
        protected View v;

        public TutorViewHolder(final View v) {
            super(v);
            this.v = v;
            vName =  (TextView) v.findViewById(R.id.tv_tutor_name_general);
            vEmail = (TextView) v.findViewById(R.id.tv_tutor_email_general);
            vImage = (ImageView) v.findViewById(R.id.iv_tutor_general);
            bEmail = (Button) v.findViewById(R.id.b_email);
            bRate = (Button) v.findViewById(R.id.b_rate);

        }


    }
}
