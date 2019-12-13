package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.caseofficer;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.EmailNotificationPersonalisation;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.BasePersonalisationProvider;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.EmailAddressFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.config.GovNotifyTemplateIdConfiguration;

@Service
public class CaseOfficerSubmittedHearingRequirementsPersonalisation implements EmailNotificationPersonalisation {

    private final GovNotifyTemplateIdConfiguration govNotifyTemplateIdConfiguration;
    private final BasePersonalisationProvider basePersonalisationProvider;
    private final EmailAddressFinder emailAddressFinder;

    public CaseOfficerSubmittedHearingRequirementsPersonalisation(
        GovNotifyTemplateIdConfiguration govNotifyTemplateIdConfiguration,
        BasePersonalisationProvider basePersonalisationProvider,
        EmailAddressFinder emailAddressFinder
    ) {
        this.govNotifyTemplateIdConfiguration = govNotifyTemplateIdConfiguration;
        this.basePersonalisationProvider = basePersonalisationProvider;
        this.emailAddressFinder = emailAddressFinder;
    }


    @Override
    public String getTemplateId() {
        return govNotifyTemplateIdConfiguration.getSubmittedHearingRequirementsCaseOfficerTemplateId();
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        return Collections.singleton(emailAddressFinder.getEmailAddress(asylumCase));
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_CASE_OFFICER_OF_SUBMITTED_HEARING_REQUIREMENTS";
    }

    @Override
    public Map<String, String> getPersonalisation(Callback<AsylumCase> callback) {
        requireNonNull(callback, "callback must not be null");

        return basePersonalisationProvider.getSubmittedHearingRequirementsPersonalisation(callback.getCaseDetails().getCaseData());

    }
}
