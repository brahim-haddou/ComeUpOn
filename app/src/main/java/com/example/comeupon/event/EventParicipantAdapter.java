package com.example.comeupon.event;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comeupon.R;

import java.util.ArrayList;

class EventParticipantAdapter extends RecyclerView.Adapter<EventParticipantAdapter.EventParticipantViewHolder> {

    ArrayList<Participant> Participants;

    public EventParticipantAdapter(ArrayList<Participant> participants) {
        Participants = participants;
    }

    @NonNull
    @Override
    public EventParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new EventParticipantViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_participent,
                        parent,
                        false));
    }

    @Override
    public void onBindViewHolder(@NonNull EventParticipantViewHolder holder, int position) {
        Participant participant = Participants.get(position);

        holder.username.setText(participant.username);
        holder.image.setImageBitmap(participant.image);
    }

    @Override
    public int getItemCount() {
        return Participants.size();
    }

    public static class EventParticipantViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public ImageView image;

        public EventParticipantViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.participant_username);
            image = itemView.findViewById(R.id.participant_image);
        }
    }
}
