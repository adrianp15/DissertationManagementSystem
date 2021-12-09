package com.university.dms.model.meetings;

public enum MeetingPlatform {
    ZOOM("Zoom"),
    SKYPE("Skype"),
    WEBEX("Webex"),
    MICROSOFT_TEAMS("Microsoft Teams"),
    GOOGLE_MEET("Google Meet"),
    OTHER("Other");

    private String value;

    MeetingPlatform(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
