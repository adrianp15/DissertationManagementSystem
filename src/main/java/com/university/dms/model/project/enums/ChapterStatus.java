package com.university.dms.model.project.enums;

public enum ChapterStatus {
    NOT_STARTED("Not started yet"),
    NEEDS_REVISION_FROM_STUDENT("Need revision from student"),
    NEW_WORK_SUBMITTED("New work submitted"),
    DONE("Done");

    private String value;

    ChapterStatus(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
