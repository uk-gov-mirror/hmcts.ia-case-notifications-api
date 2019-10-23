package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.LEGAL_REPRESENTATIVE_EMAIL_ADDRESS;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.BasePersonalisationProvider;

@Service
public class LegalRepresentativeEditListingPersonalisation implements NotificationPersonalisation {

    private final String legalRepresentativeCaseEditedTemplateId;
    private final BasePersonalisationProvider basePersonalisationProvider;

    public LegalRepresentativeEditListingPersonalisation(
        @Value("${govnotify.template.legalRepresentativeCaseEditedTemplateId}") String legalRepresentativeCaseEditedTemplateId,
        BasePersonalisationProvider basePersonalisationProvider
    ) {
        this.legalRepresentativeCaseEditedTemplateId = legalRepresentativeCaseEditedTemplateId;
        this.basePersonalisationProvider = basePersonalisationProvider;
    }

    @Override
    public String getTemplateId() {
        return legalRepresentativeCaseEditedTemplateId;
    }

    @Override
    public String getEmailAddress(AsylumCase asylumCase) {
        return asylumCase
            .read(LEGAL_REPRESENTATIVE_EMAIL_ADDRESS, String.class)
            .orElseThrow(() -> new IllegalStateException("legalRepresentativeEmailAddress is not present"));
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_CASE_RE_LISTED_LEGAL_REPRESENTATIVE";
    }

    @Override
    public Map<String, String> getPersonalisation(Callback<AsylumCase> callback) {
        requireNonNull(callback, "callback must not be null");

        return basePersonalisationProvider.getEditCaseListingPersonalisation(callback);
    }
}
