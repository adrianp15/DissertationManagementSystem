package com.university.dms.model.project;

public enum ProjectStatus {
    SUGGESTION_SUBMITTED("Suggestion submitted"),
    SUGGESTION_VIEWED("Suggestion viewed"),
    SUGGESTION_APPROVED("Suggestion approved"),
    SUGGESTION_REJECTED("Suggestion rejected"),
    PROPOSAL_SUBMITTED("Proposal submitted");

    private String value;

    ProjectStatus(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
