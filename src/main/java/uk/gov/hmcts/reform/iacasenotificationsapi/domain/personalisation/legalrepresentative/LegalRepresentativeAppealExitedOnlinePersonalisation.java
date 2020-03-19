package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.legalrepresentative;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.*;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.HearingCentre;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.EmailNotificationPersonalisation;

@Service
public class LegalRepresentativeAppealExitedOnlinePersonalisation implements EmailNotificationPersonalisation {

    private final String appealExitedOnlineBeforeListingLegalRepresentativeTemplateId;
    private final String appealExitedOnlineAfterListingLegalRepresentativeTemplateId;

    public LegalRepresentativeAppealExitedOnlinePersonalisation(
        @NotNull(message = "appealOutcomeAllowedLegalRepresentativeTemplateId cannot be null") @Value("${govnotify.template.removeAppealFromOnlineBeforeListingLegalRep.email}") String appealExitedOnlineBeforeListingLegalRepresentativeTemplateId,
        @NotNull(message = "appealOutcomeAllowedLegalRepresentativeTemplateId cannot be null") @Value("${govnotify.template.removeAppealFromOnlineAfterListingLegalRep.email}") String appealExitedOnlineAfterListingLegalRepresentativeTemplateId) {

        this.appealExitedOnlineBeforeListingLegalRepresentativeTemplateId = appealExitedOnlineBeforeListingLegalRepresentativeTemplateId;
        this.appealExitedOnlineAfterListingLegalRepresentativeTemplateId = appealExitedOnlineAfterListingLegalRepresentativeTemplateId;
    }

    @Override
    public String getTemplateId(AsylumCase asylumCase) {
        return isAppealListed(asylumCase)
            ? appealExitedOnlineAfterListingLegalRepresentativeTemplateId : appealExitedOnlineBeforeListingLegalRepresentativeTemplateId;
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        return Collections.singleton(asylumCase
            .read(LEGAL_REPRESENTATIVE_EMAIL_ADDRESS, String.class)
            .orElseThrow(() -> new IllegalStateException("legalRepresentativeEmailAddress is not present")));
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_APPEAL_EXITED_ONLINE_LEGAL_REPRESENTATIVE";
    }

    @Override
    public Map<String, String> getPersonalisation(AsylumCase asylumCase) {
        requireNonNull(asylumCase, "asylumCase must not be null");

        return ImmutableMap
            .<String, String>builder()
            .put("appealReferenceNumber", asylumCase.read(AsylumCaseDefinition.APPEAL_REFERENCE_NUMBER, String.class).orElse(""))
            .put("legalRepReferenceNumber", asylumCase.read(AsylumCaseDefinition.LEGAL_REP_REFERENCE_NUMBER, String.class).orElse(""))
            .put("ariaListingReference", asylumCase.read(ARIA_LISTING_REFERENCE, String.class).orElse(""))
            .put("appellantGivenNames", asylumCase.read(AsylumCaseDefinition.APPELLANT_GIVEN_NAMES, String.class).orElse(""))
            .put("appellantFamilyName", asylumCase.read(AsylumCaseDefinition.APPELLANT_FAMILY_NAME, String.class).orElse(""))
            .build();
    }

    public boolean isAppealListed(AsylumCase asylumCase) {

        final Optional<HearingCentre> appealListed = asylumCase
            .read(AsylumCaseDefinition.LIST_CASE_HEARING_CENTRE, HearingCentre.class);
        if (appealListed.isPresent()) {
            return true;
        }
        return false;
    }
}
