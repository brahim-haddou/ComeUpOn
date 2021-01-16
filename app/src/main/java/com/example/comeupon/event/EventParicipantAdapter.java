package com.example.comeupon.event;

import android.content.Context;
import android.os.Parcelable;
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

import org.json.JSONObject;

import java.util.ArrayList;

class EventParticipantAdapter extends RecyclerView.Adapter<EventParticipantAdapter.EventParticipantViewHolder> {

    ArrayList<Profile> Participants;
    OnParticipantListener mParticipantListener;
    OnParticipantAcceptListener mParticipantAcceptListener;
    Boolean state;
    public EventParticipantAdapter(ArrayList<Profile> participants, Boolean s,OnParticipantListener participantListener,OnParticipantAcceptListener participantAcceptListener) {
        Participants = participants;
        mParticipantListener = participantListener;
        mParticipantAcceptListener = participantAcceptListener;
        state = s;
    }

    @NonNull
    @Override
    public EventParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_participent, parent, false);

        return new EventParticipantViewHolder(parent.getContext(), view,mParticipantListener, mParticipantAcceptListener);
    }

    @Override
    public void onBindViewHolder(@NonNull EventParticipantViewHolder holder, int position) {
        Profile participant = Participants.get(position);

        holder.username.setText(participant.getUser().getUsername());
        Picasso.get().load(participant.getImage()).into(holder.image);
        if(state){
            holder.accept.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return Participants == null ? 0 : Participants.size();
    }

    public interface OnParticipantListener{
        void onParticipantClick(int position);
    }

    public interface OnParticipantAcceptListener{
        void OnParticipantAcceptClick(int position);
    }


    public static class EventParticipantViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView username;
        public ImageView image;
        Button accept;

        private final Context mContext;

        OnParticipantListener mOnParticipantListener;
        OnParticipantAcceptListener mOnParticipantAcceptListener;

        public EventParticipantViewHolder(Context context, View itemView, OnParticipantListener onParticipantListener, OnParticipantAcceptListener onParticipantAcceptListener) {
            super(itemView);

            mContext = context;
            mOnParticipantListener = onParticipantListener;
            mOnParticipantAcceptListener = onParticipantAcceptListener;

            username = itemView.findViewById(R.id.participant_username);
            image = itemView.findViewById(R.id.participant_image);
            accept = itemView.findViewById(R.id.participant_accept_btn);
            accept.setOnClickListener(view -> mOnParticipantAcceptListener.OnParticipantAcceptClick(getAdapterPosition()));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mOnParticipantListener.onParticipantClick(getAdapterPosition());
        }
    }
}
