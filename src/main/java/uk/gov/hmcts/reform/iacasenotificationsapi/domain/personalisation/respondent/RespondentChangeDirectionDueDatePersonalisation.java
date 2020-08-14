package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.respondent;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.CURRENT_CASE_STATE_VISIBLE_TO_HOME_OFFICE_ALL;

import com.google.common.collect.ImmutableMap;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.State;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.EmailNotificationPersonalisation;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.CustomerServicesProvider;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.EmailAddressFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.PersonalisationProvider;

@Service
public class RespondentChangeDirectionDueDatePersonalisation implements EmailNotificationPersonalisation {

    private final String respondentChangeDirectionDueDateBeforeListingTemplateId;
    private final String respondentChangeDirectionDueDateAfterListingTemplateId;
    private final String iaExUiFrontendUrl;
    private final PersonalisationProvider personalisationProvider;
    private final String respondentEmailAddressUntilRespondentReview;
    private final String respondentEmailAddressAtRespondentReview;
    private final EmailAddressFinder respondentEmailAddressAfterRespondentReview;
    private final CustomerServicesProvider customerServicesProvider;

    public RespondentChangeDirectionDueDatePersonalisation(
        @Value("${govnotify.template.changeDirectionDueDate.respondent.afterListing.email}") String respondentChangeDirectionDueDateAfterListingTemplateId,
        @Value("${govnotify.template.changeDirectionDueDate.respondent.beforeListing.email}") String respondentChangeDirectionDueDateBeforeListingTemplateId,
        @Value("${iaExUiFrontendUrl}") String iaExUiFrontendUrl,
        PersonalisationProvider personalisationProvider,
        @Value("${respondentEmailAddresses.nonStandardDirectionUntilListing}") String respondentEmailAddressUntilRespondentReview,
        @Value("${respondentEmailAddresses.respondentReviewDirection}") String respondentEmailAddressAtRespondentReview,
        EmailAddressFinder respondentEmailAddressAfterRespondentReview,
        CustomerServicesProvider customerServicesProvider
    ) {
        requireNonNull(iaExUiFrontendUrl, "iaExUiFrontendUrl must not be null");
        this.respondentChangeDirectionDueDateAfterListingTemplateId = respondentChangeDirectionDueDateAfterListingTemplateId;
        this.respondentChangeDirectionDueDateBeforeListingTemplateId = respondentChangeDirectionDueDateBeforeListingTemplateId;
        this.iaExUiFrontendUrl = iaExUiFrontendUrl;
        this.personalisationProvider = personalisationProvider;
        this.respondentEmailAddressUntilRespondentReview = respondentEmailAddressUntilRespondentReview;
        this.respondentEmailAddressAtRespondentReview = respondentEmailAddressAtRespondentReview;
        this.respondentEmailAddressAfterRespondentReview = respondentEmailAddressAfterRespondentReview;
        this.customerServicesProvider = customerServicesProvider;
    }

    @Override
    public String getTemplateId(AsylumCase asylumCase) {

        return asylumCase.read(CURRENT_CASE_STATE_VISIBLE_TO_HOME_OFFICE_ALL, State.class)
            .map(s -> {
                if (Arrays.asList(
                    State.APPEAL_SUBMITTED,
                    State.APPEAL_SUBMITTED_OUT_OF_TIME,
                    State.AWAITING_RESPONDENT_EVIDENCE,
                    State.CASE_BUILDING,
                    State.CASE_UNDER_REVIEW,
                    State.RESPONDENT_REVIEW,
                    State.SUBMIT_HEARING_REQUIREMENTS
                ).contains(s)) {
                    return respondentChangeDirectionDueDateBeforeListingTemplateId;
                }

                return respondentChangeDirectionDueDateAfterListingTemplateId;
            })
            .orElseThrow(() -> new IllegalStateException("currentCaseStateVisibleToHomeOfficeAll flag is not present"));
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        return Collections.singleton(getRespondentEmailAddress(asylumCase));
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_RESPONDENT_CHANGE_DIRECTION_DUE_DATE";
    }

    private String getRespondentEmailAddress(AsylumCase asylumCase) {

        return asylumCase.read(CURRENT_CASE_STATE_VISIBLE_TO_HOME_OFFICE_ALL, State.class)
            .map(s -> {
                if (Arrays.asList(
                    State.APPEAL_SUBMITTED,
                    State.APPEAL_SUBMITTED_OUT_OF_TIME,
                    State.AWAITING_RESPONDENT_EVIDENCE,
                    State.CASE_BUILDING,
                    State.CASE_UNDER_REVIEW
                ).contains(s)) {
                    return respondentEmailAddressUntilRespondentReview;
                } else if (Arrays.asList(
                    State.RESPONDENT_REVIEW,
                    State.SUBMIT_HEARING_REQUIREMENTS
                ).contains(s)) {
                    return respondentEmailAddressAtRespondentReview;
                }

                return respondentEmailAddressAfterRespondentReview.getHomeOfficeEmailAddress(asylumCase);
            })
            .orElseThrow(() -> new IllegalStateException("currentCaseStateVisibleToHomeOfficeAll flag is not present"));
    }

    @Override
    public Map<String, String> getPersonalisation(Callback<AsylumCase> callback) {
        requireNonNull(callback, "callback must not be null");

        final ImmutableMap.Builder<String, String> listCaseFields = ImmutableMap
            .<String, String>builder()
            .putAll(customerServicesProvider.getCustomerServicesPersonalisation())
            .put("linkToOnlineService", iaExUiFrontendUrl)
            .putAll(personalisationProvider.getPersonalisation(callback));

        return listCaseFields.build();
    }
}
