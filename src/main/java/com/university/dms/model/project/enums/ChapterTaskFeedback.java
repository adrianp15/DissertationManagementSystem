package com.university.dms.model.project.enums;

public enum ChapterTaskFeedback {
    GOOD("Good"),
    INCOMPLETE("Incomplete"),
    MISSING("Missing");

    private String value;

    ChapterTaskFeedback(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
