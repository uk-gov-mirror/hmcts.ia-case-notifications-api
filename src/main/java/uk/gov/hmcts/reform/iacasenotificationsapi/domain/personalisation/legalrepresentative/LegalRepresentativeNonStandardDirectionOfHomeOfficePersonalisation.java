package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.legalrepresentative;

import static java.util.Objects.requireNonNull;

import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.HearingCentre;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.EmailNotificationPersonalisation;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.CustomerServicesProvider;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.EmailAddressFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.PersonalisationProvider;

@Service
public class LegalRepresentativeNonStandardDirectionOfHomeOfficePersonalisation implements EmailNotificationPersonalisation {

    private final String legalRepNonStandardDirectionOfHomeOfficeBeforeListingTemplateId;
    private final String legalRepNonStandardDirectionOfHomeOfficeAfterListingTemplateId;
    private final String iaExUiFrontendUrl;
    private final PersonalisationProvider personalisationProvider;
    private final EmailAddressFinder emailAddressFinder;
    private final CustomerServicesProvider customerServicesProvider;

    public LegalRepresentativeNonStandardDirectionOfHomeOfficePersonalisation(
        @Value("${govnotify.template.nonStandardDirectionOfHomeOfficeBeforeListing.legalRep.email}") String legalRepNonStandardDirectionOfHomeOfficeBeforeListingTemplateId,
        @Value("${govnotify.template.nonStandardDirectionOfHomeOfficeAfterListing.legalRep.email}") String legalRepNonStandardDirectionOfHomeOfficeAfterListingTemplateId,
        @Value("${iaExUiFrontendUrl}") String iaExUiFrontendUrl,
        PersonalisationProvider personalisationProvider,
        EmailAddressFinder emailAddressFinder,
        CustomerServicesProvider customerServicesProvider
    ) {
        this.legalRepNonStandardDirectionOfHomeOfficeBeforeListingTemplateId = legalRepNonStandardDirectionOfHomeOfficeBeforeListingTemplateId;
        this.legalRepNonStandardDirectionOfHomeOfficeAfterListingTemplateId = legalRepNonStandardDirectionOfHomeOfficeAfterListingTemplateId;
        this.iaExUiFrontendUrl = iaExUiFrontendUrl;
        this.personalisationProvider = personalisationProvider;
        this.emailAddressFinder = emailAddressFinder;
        this.customerServicesProvider = customerServicesProvider;
    }

    @Override
    public String getTemplateId(AsylumCase asylumCase) {
        return isAppealListed(asylumCase)
            ? legalRepNonStandardDirectionOfHomeOfficeAfterListingTemplateId : legalRepNonStandardDirectionOfHomeOfficeBeforeListingTemplateId;
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        return Collections.singleton(emailAddressFinder.getLegalRepEmailAddress(asylumCase));
    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_LEGAL_REP_NON_STANDARD_DIRECTION_OF_HOME_OFFICE";
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

    protected boolean isAppealListed(AsylumCase asylumCase) {
        final Optional<HearingCentre> appealListed = asylumCase
            .read(AsylumCaseDefinition.LIST_CASE_HEARING_CENTRE, HearingCentre.class);

        return appealListed.isPresent();
    }
}
