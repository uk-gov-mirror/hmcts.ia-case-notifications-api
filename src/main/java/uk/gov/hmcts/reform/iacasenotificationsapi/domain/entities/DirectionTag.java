package uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum DirectionTag {

    BUILD_CASE("buildCase"),
    CASE_EDIT("caseEdit"),
    LEGAL_REPRESENTATIVE_REVIEW("legalRepresentativeReview"),
    LEGAL_REPRESENTATIVE_HEARING_REQUIREMENTS("legalRepresentativeHearingRequirements"),
    RESPONDENT_EVIDENCE("respondentEvidence"),
    RESPONDENT_REVIEW("respondentReview"),
    REQUEST_CASE_BUILDING("requestCaseBuilding"),
    REQUEST_RESPONSE_REVIEW("requestResponseReview"),
    REQUEST_RESPONSE_AMEND("requestResponseAmend"),
    REQUEST_REASONS_FOR_APPEAL("requestReasonsForAppeal"),
    REQUEST_CLARIFYING_QUESTIONS("requestClarifyingQuestions"),

    @JsonEnumDefaultValue
    NONE("");

    @JsonValue
    private final String id;

    DirectionTag(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
}
