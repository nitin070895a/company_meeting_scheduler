package com.example.companymeetingscheduler.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Contains all the data representing a single meeting
 * Created by Nitin on 01/09/18.
 */
public class Meeting {

    /**
     * The start time of the meeting in format
     * {@link com.example.companymeetingscheduler.Helper.TimeAndDateUtils#DEFAULT_TIME_FORMAT}
     */
    @SerializedName("startTime")
    private String startTime;

    /**
     * The end time of the meeting in format
     * {@link com.example.companymeetingscheduler.Helper.TimeAndDateUtils#DEFAULT_TIME_FORMAT}
     */
    @SerializedName("endTime")
    private String endTime;

    /**
     * The description about the meeting
     */
    @SerializedName("description")
    private String description;

    /**
     * The names of the participants that will be taking part in the meeting
     */
    @SerializedName("participants")
    private String[] participants;

    public String getDescription() {
        return description;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String[] getParticipants() {
        return participants;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setParticipants(String[] participants) {
        this.participants = participants;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
