package com.university.dms.model.project.enums;

public enum DissertationType {
    ACADEMIC("Academic"),
    CONSULTANCY("Consultancy"),
    PRACTICAL("Practical");

    private String value;

    DissertationType(String enumValue) {
        this.value = enumValue;
    }

    public String getValue() {
        return value;
    }

}
