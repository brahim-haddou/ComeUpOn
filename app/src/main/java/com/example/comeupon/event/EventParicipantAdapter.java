package com.example.comeupon.event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.Models.Profile;
import com.example.comeupon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class EventParticipantAdapter extends RecyclerView.Adapter<EventParticipantAdapter.EventParticipantViewHolder> {

    ArrayList<Profile> Participants;
    OnParticipantListener mParticipantListener;
    public EventParticipantAdapter(ArrayList<Profile> participants,OnParticipantListener participantListener) {
        Participants = participants;
        mParticipantListener = participantListener;
    }

    @NonNull
    @Override
    public EventParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_participent, parent, false);

        return new EventParticipantViewHolder(parent.getContext(), view,mParticipantListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventParticipantViewHolder holder, int position) {
        Profile participant = Participants.get(position);

        holder.username.setText(participant.getUser().getUsername());
        Picasso.get().load(participant.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return Participants == null ? 0 : Participants.size();
    }

    public interface OnParticipantListener{
        void onParticipantClick(int position);
    }

    public static class EventParticipantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView username;
        public ImageView image;
        Button accept;

        private final Context mContext;

        OnParticipantListener mOnParticipantListener;

        public EventParticipantViewHolder(Context context, View itemView, OnParticipantListener onParticipantListener) {
            super(itemView);

            mContext = context;
            mOnParticipantListener = onParticipantListener;

            username = itemView.findViewById(R.id.participant_username);
            image = itemView.findViewById(R.id.participant_image);
            accept = itemView.findViewById(R.id.participant_accept_btn);
            accept.setOnClickListener(view -> Toast.makeText(itemView.getContext(), "accept :" + getAdapterPosition(), Toast.LENGTH_SHORT).show());

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnParticipantListener.onParticipantClick(getAdapterPosition());
        }
    }
}
