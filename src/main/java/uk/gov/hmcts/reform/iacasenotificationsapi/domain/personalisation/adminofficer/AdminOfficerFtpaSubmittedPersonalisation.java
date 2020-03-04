package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.adminofficer;

import static java.util.Objects.requireNonNull;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.EmailNotificationPersonalisation;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.PersonalisationProvider;

@Service
public class AdminOfficerFtpaSubmittedPersonalisation implements EmailNotificationPersonalisation {

    private final String applyForFtpaTemplateId;
    private final PersonalisationProvider personalisationProvider;
    private final String adminOfficerEmailAddress;

    public AdminOfficerFtpaSubmittedPersonalisation(
        @Value("${govnotify.template.applyForFtpa.adminOfficer.email}") String applyForFtpaTemplateId,
        PersonalisationProvider personalisationProvider,
        @Value("${ftpaSubmitted.ctscAdminEmailAddress}")String adminOfficerEmailAddress
    ) {
        this.applyForFtpaTemplateId = applyForFtpaTemplateId;
        this.personalisationProvider = personalisationProvider;
        this.adminOfficerEmailAddress = adminOfficerEmailAddress;
    }

    @Override
    public Map<String, String> getPersonalisation(Callback<AsylumCase> callback) {
        requireNonNull(callback, "callback must not be null");

        return personalisationProvider.getPersonalisation(callback);
    }

    @Override
    public Set<String> getRecipientsList(AsylumCase asylumCase) {
        return Collections.singleton(adminOfficerEmailAddress);

    }

    @Override
    public String getReferenceId(Long caseId) {
        return caseId + "_FTPA_SUBMITTED_ADMIN_OFFICER";
    }

    @Override
    public String getTemplateId() {
        return applyForFtpaTemplateId;
    }
}
