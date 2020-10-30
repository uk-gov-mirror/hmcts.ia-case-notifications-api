package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.adminofficer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;


@ExtendWith(MockitoExtension.class)
public class AdminOfficerWithoutHearingRequirementsPersonalisationTest {

    @Mock
    AsylumCase asylumCase;
    @Mock
    AdminOfficerPersonalisationProvider adminOfficerPersonalisationProvider;
    private Long caseId = 12345L;
    private String templateId = "someTemplateId";
    private String reviewHearingRequirementsAdminOfficerEmailAddress =
        "adminofficer-without-hearing-requirements@example.com";
    private AdminOfficerWithoutHearingRequirementsPersonalisation adminOfficerWithoutHearingRequirementsPersonalisation;

    @BeforeEach
    public void setup() {

        adminOfficerWithoutHearingRequirementsPersonalisation =
            new AdminOfficerWithoutHearingRequirementsPersonalisation(
                templateId,
                reviewHearingRequirementsAdminOfficerEmailAddress,
                adminOfficerPersonalisationProvider
            );
    }

    @Test
    public void should_return_given_template_id() {
        assertEquals(templateId, adminOfficerWithoutHearingRequirementsPersonalisation.getTemplateId());
    }

    @Test
    public void should_return_given_reference_id() {

        assertEquals(caseId + "_WITHOUT_HEARING_REQUIREMENTS_ADMIN_OFFICER",
            adminOfficerWithoutHearingRequirementsPersonalisation.getReferenceId(caseId));
    }

    @Test
    public void should_throw_exception_on_personalisation_when_case_is_null() {

        assertThatThrownBy(
            () -> adminOfficerWithoutHearingRequirementsPersonalisation.getPersonalisation((AsylumCase) null))
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("asylumCase must not be null");
    }

    @Test
    public void should_return_personalisation_when_all_information_given() {

        Map<String, String> personalisation =
            adminOfficerWithoutHearingRequirementsPersonalisation.getPersonalisation(asylumCase);

        assertThat(asylumCase).isEqualToComparingOnlyGivenFields(personalisation);

    }

    @Test
    public void should_return_personalisation_when_all_mandatory_information_given() {

        Map<String, String> personalisation =
            adminOfficerWithoutHearingRequirementsPersonalisation.getPersonalisation(asylumCase);

        assertThat(asylumCase).isEqualToComparingOnlyGivenFields(personalisation);
    }
}
