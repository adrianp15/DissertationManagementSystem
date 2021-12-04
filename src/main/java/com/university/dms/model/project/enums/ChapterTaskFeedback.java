package com.university.dms.model.project.enums;

public enum ChapterTaskFeedback {
    GOOD("Good"),
    NEED_MORE_WORK("Needs more work"),
    MISSING("Missing");

    private String value;

    ChapterTaskFeedback(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
