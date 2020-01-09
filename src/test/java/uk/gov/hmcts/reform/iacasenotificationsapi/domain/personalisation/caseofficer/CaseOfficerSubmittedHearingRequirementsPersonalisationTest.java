package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.caseofficer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.ImmutableMap;

import java.util.Collections;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.EmailAddressFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.PersonalisationProvider;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.config.GovNotifyTemplateIdConfiguration;

@RunWith(MockitoJUnitRunner.class)
@SuppressWarnings("unchecked")
public class CaseOfficerSubmittedHearingRequirementsPersonalisationTest {

    @Mock AsylumCase asylumCase;

    @Mock EmailAddressFinder emailAddressFinder;
    @Mock GovNotifyTemplateIdConfiguration govNotifyTemplateIdConfiguration;
    @Mock PersonalisationProvider personalisationProvider;

    private Long caseId = 12345L;
    private String templateId = "someTemplateId";

    private String hearingCentreEmailAddress = "hearingCentre@example.com";

    private String appealReferenceNumber = "someReferenceNumber";
    private String appellantGivenNames = "someAppellantGivenNames";
    private String appellantFamilyName = "someAppellantFamilyName";

    private CaseOfficerSubmittedHearingRequirementsPersonalisation caseOfficerSubmittedHearingRequirementsPersonalisation;

    @Before
    public void setUp() {
        when(emailAddressFinder.getEmailAddress(asylumCase)).thenReturn(hearingCentreEmailAddress);
        when(govNotifyTemplateIdConfiguration.getSubmittedHearingRequirementsCaseOfficerTemplateId()).thenReturn(templateId);

        caseOfficerSubmittedHearingRequirementsPersonalisation =
            new CaseOfficerSubmittedHearingRequirementsPersonalisation(
                govNotifyTemplateIdConfiguration,
                personalisationProvider,
                emailAddressFinder
            );
    }

    @Test
    public void should_return_given_template_id() {
        assertEquals(templateId, caseOfficerSubmittedHearingRequirementsPersonalisation.getTemplateId());
    }

    @Test
    public void should_return_given_email_address_from_asylum_case() {
        assertEquals(Collections.singleton(hearingCentreEmailAddress), caseOfficerSubmittedHearingRequirementsPersonalisation.getRecipientsList(asylumCase));
    }

    @Test
    public void should_return_given_reference_id() {
        assertEquals(caseId + "_CASE_OFFICER_OF_SUBMITTED_HEARING_REQUIREMENTS", caseOfficerSubmittedHearingRequirementsPersonalisation.getReferenceId(caseId));
    }

    @Test
    public void should_return_personalisation_when_all_information_given() {
        Map<String, String> personalisation = caseOfficerSubmittedHearingRequirementsPersonalisation.getPersonalisation(asylumCase);
        Map<String, String> expectedPersonalisation = getPersonalisation();

        Assertions.assertThat(personalisation).isEqualToComparingOnlyGivenFields(expectedPersonalisation);
    }

    @Test
    public void should_throw_exception_on_personalisation_when_case_is_null() {

        assertThatThrownBy(() -> caseOfficerSubmittedHearingRequirementsPersonalisation.getPersonalisation((Callback<AsylumCase>) null))
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("callback must not be null");
    }

    private Map<String, String> getPersonalisation() {
        return ImmutableMap
            .<String, String>builder()
            .put("appealReferenceNumber", appealReferenceNumber)
            .put("appellantGivenNames", appellantGivenNames)
            .put("appellantFamilyName", appellantFamilyName)
            .build();
    }
}
