package com.university.dms.model.project.enums;

public enum ProjectStatus {
    SUGGESTION_SUBMITTED("Suggestion submitted"),
    SUGGESTION_APPROVED("Suggestion approved"),
    SUGGESTION_REJECTED("Suggestion rejected"),
    PROPOSAL_SUBMITTED("Proposal submitted"),
    PROPOSAL_REJECTED("Proposal rejected"),
    PROPOSAL_APPROVED("Proposal approved");

    private String value;

    ProjectStatus(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }
}
