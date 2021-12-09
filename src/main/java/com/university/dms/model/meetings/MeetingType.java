package com.university.dms.model.meetings;

public enum MeetingType {
    REGULAR_MEETING("Regular meeting"),
    URGENT_ISSUE("Urgent issue"),
    DISCUSSION_ABOUT_PROJECT_DEVELOPMENT("Discussion about project development"),
    FEEDBACK("Feedback");

    private String value;

    MeetingType(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
