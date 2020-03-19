package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.homeoffice;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.*;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.APPELLANT_FAMILY_NAME;

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
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.EmailAddressFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.PersonalisationProvider;

@Service
public class HomeOfficeAppealExitedOnlinePersonalisation implements EmailNotificationPersonalisation {
    private final String appealExitedOnlineBeforeListingTemplateId;
    private final String appealExitedOnlineAfterListingTemplateId;
    private EmailAddressFinder emailAddressFinder;
    private final String homeOfficeEmailAddresses;

    public HomeOfficeAppealExitedOnlinePersonalisation(
        @NotNull(message = "appealExitedOnlineBeforeListingHomeOfficeTemplateId cannot be null") @Value("${govnotify.template.removeAppealFromOnlineBeforeListingHomeOffice.email}") String appealExitedOnlineBeforeListingTemplateId,
        @NotNull(message = "appealExitedOnlineAfterListingHomeOfficeTemplateId cannot be null") @Value("${govnotify.template.removeAppealFromOnlineAfterListingHomeOffice.email}") String appealExitedOnlineAfterListingTemplateId,
        @NotNull(message = "home_office email cannot be null") @Value("${endAppealHomeOfficeEmailAddress}") String homeOfficeEmailAddresses,
        EmailAddressFinder emailAddressFinder) {
        this.appealExitedOnlineBeforeListingTemplateId = appealExitedOnlineBeforeListingTemplateId;
        this.appealExitedOnlineAfterListingTemplateId = appealExitedOnlineAfterListingTemplateId;
        this.homeOfficeEmailAddresses = homeOfficeEmailAddresses;
        this.emailAddressFinder = emailAddressFinder;
    }

    @Override
    public String getTemplateId(AsylumCase asylumCase) {
        return isAppealListed(asylumCase)
            ? appealExitedOnlineAfterListingTemplateId : appealExitedOnlineBeforeListingTemplateId;
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        if (isAppealListed(asylumCase)) {
            return Collections.singleton(emailAddressFinder.getListCaseHearingCentreEmailAddress(asylumCase));
        } else {
            return Collections.singleton(homeOfficeEmailAddresses);
        }
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_APPEAL_EXITED_ONLINE_HOME_OFFICE";
    }

    @Override
    public Map<String, String> getPersonalisation(AsylumCase asylumCase) {
        requireNonNull(asylumCase, "asylumCase must not be null");

        final ImmutableMap.Builder<String, String> listCaseFields = ImmutableMap
            .<String, String>builder()
            .put("appealReferenceNumber", asylumCase.read(APPEAL_REFERENCE_NUMBER, String.class).orElse(""))
            .put("ariaListingReference", asylumCase.read(ARIA_LISTING_REFERENCE, String.class).orElse(""))
            .put("homeOfficeReferenceNumber", asylumCase.read(HOME_OFFICE_REFERENCE_NUMBER, String.class).orElse(""))
            .put("appellantGivenNames", asylumCase.read(APPELLANT_GIVEN_NAMES, String.class).orElse(""))
            .put("appellantFamilyName", asylumCase.read(APPELLANT_FAMILY_NAME, String.class).orElse(""));

        PersonalisationProvider.buildHearingRequirementsFields(asylumCase, listCaseFields);

        return listCaseFields.build();
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

