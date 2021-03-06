package com.example.companymeetingscheduler.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.companymeetingscheduler.Activity.HomeScreen;
import com.example.companymeetingscheduler.Helper.TimeAndDateUtils;
import com.example.companymeetingscheduler.Holders.MeetingItemViewHolder;
import com.example.companymeetingscheduler.Model.Meeting;
import com.example.companymeetingscheduler.R;

import java.util.ArrayList;

/**
 * Adapter for {@link android.support.v7.widget.RecyclerView} at {@link HomeScreen}
 * that list all the meetings of the selected date
 *
 * Created by Nitin on 01/09/18.
 */
public class MeetingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private ArrayList<Meeting> meetings = new ArrayList<>();

    /**
     * Constructor
     * @param context the context of the caller
     * @param meetings meetings to be shown in the list
     */
    public MeetingsAdapter(Context context, ArrayList<Meeting> meetings){
        this.context = context;
        this.meetings = new ArrayList<>(meetings);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_meeting, parent, false);
        return new MeetingItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder h1, int position) {

        MeetingItemViewHolder holder = (MeetingItemViewHolder) h1;
        Meeting meeting = meetings.get(position);

        holder.description.setText(meeting.getDescription());
        holder.startTime.setText(TimeAndDateUtils.convertStringDate(meeting.getStartTime(), "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT));
        holder.endTime.setText(TimeAndDateUtils.convertStringDate(meeting.getEndTime(), "HH:mm", TimeAndDateUtils.DEFAULT_TIME_FORMAT));

        String people = "";
        String seperator = ", ";
        for(String person : meeting.getParticipants()) {
            people += person + seperator;
        }

        // remove the last comma
        if(people.endsWith(seperator)) {
            people = people.substring(0, people.lastIndexOf(seperator));
        }

        holder.participants.setText(people);
    }

    @Override
    public int getItemCount() {
        return meetings.size();
    }
}
