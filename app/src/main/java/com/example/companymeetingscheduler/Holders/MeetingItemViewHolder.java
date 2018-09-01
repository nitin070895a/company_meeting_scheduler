package com.example.companymeetingscheduler.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.companymeetingscheduler.R;

/**
 * Contains all the views represented in the {@link com.example.companymeetingscheduler.Adapter.MeetingsAdapter}
 * Created by Nitin on 01/09/18.
 */
public class MeetingItemViewHolder extends RecyclerView.ViewHolder{

    /**
     * Start time of the meeting
     */
    public TextView startTime;

    /**
     * End time of the meeting
     */
    public TextView endTime;

    /**
     * Description of the meeting
     */
    public TextView description;

    /**
     * Participants in the meeting
     */
    public TextView participants;

    /**
     * ViewHolder for each item in the {@link com.example.companymeetingscheduler.Adapter.MeetingsAdapter}
     * @param itemView the root layout of the item
     */
    public MeetingItemViewHolder(View itemView) {
        super(itemView);

        startTime = itemView.findViewById(R.id.startTime);
        endTime = itemView.findViewById(R.id.endTime);
        description = itemView.findViewById(R.id.description);
        participants = itemView.findViewById(R.id.participants);
    }
}
