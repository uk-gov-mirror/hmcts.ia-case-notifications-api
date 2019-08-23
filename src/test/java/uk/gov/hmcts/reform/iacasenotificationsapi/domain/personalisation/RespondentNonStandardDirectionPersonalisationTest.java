package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.*;

import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.Direction;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.DirectionTag;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.HearingCentre;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.DirectionFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.StringProvider;

@RunWith(MockitoJUnitRunner.class)
public class RespondentNonStandardDirectionPersonalisationTest {

    @Mock AsylumCase asylumCase;
    @Mock StringProvider stringProvider;
    @Mock DirectionFinder directionFinder;
    @Mock Direction direction;

    private Long caseId = 12345L;
    private String templateId = "someTemplateId";
    private String respondentReviewEmailAddress = "respondentReview@example.com";

    private String directionDueDate = "2019-08-27";
    private String expectedDirectionDueDate = "27 Aug 2019";
    private String directionExplanation = "someExplanation";

    private HearingCentre hearingCentre = HearingCentre.TAYLOR_HOUSE;
    private String hearingCentreString = "Taylor House";

    private String appealReferenceNumber = "someReferenceNumber";
    private String homeOfficeRefNumber = "someHomeOfficeRefNumber";
    private String appellantGivenNames = "someAppellantGivenNames";
    private String appellantFamilyName = "someAppellantFamilyName";

    private RespondentNonStandardDirectionPersonalisation respondentNonStandardDirectionPersonalisation;

    @Before
    public void setup() {

        when((direction.getDateDue())).thenReturn(directionDueDate);
        when((direction.getExplanation())).thenReturn(directionExplanation);
        when(directionFinder.findFirst(asylumCase, DirectionTag.NONE)).thenReturn(Optional.of(direction));

        when(asylumCase.read(APPEAL_REFERENCE_NUMBER, String.class)).thenReturn(Optional.of(appealReferenceNumber));
        when(asylumCase.read(APPELLANT_GIVEN_NAMES, String.class)).thenReturn(Optional.of(appellantGivenNames));
        when(asylumCase.read(APPELLANT_FAMILY_NAME, String.class)).thenReturn(Optional.of(appellantFamilyName));
        when(asylumCase.read(HOME_OFFICE_REFERENCE_NUMBER, String.class)).thenReturn(Optional.of(homeOfficeRefNumber));
        when(asylumCase.read(HEARING_CENTRE, HearingCentre.class)).thenReturn(Optional.of(hearingCentre));

        when(stringProvider.get("hearingCentre", hearingCentre.toString())).thenReturn(Optional.of(hearingCentreString));

        respondentNonStandardDirectionPersonalisation = new RespondentNonStandardDirectionPersonalisation(
            templateId,
            respondentReviewEmailAddress,
            stringProvider,
            directionFinder
        );
    }

    @Test
    public void should_return_given_template_id() {
        assertEquals(templateId, respondentNonStandardDirectionPersonalisation.getTemplateId());
    }

    @Test
    public void should_return_given_reference_id() {
        assertEquals(caseId + "_RESPONDENT_NON_STANDARD_DIRECTION", respondentNonStandardDirectionPersonalisation.getReferenceId(caseId));
    }

    @Test
    public void should_return_given_email_address_from_asylum_case() {
        assertEquals(respondentReviewEmailAddress, respondentNonStandardDirectionPersonalisation.getEmailAddress(asylumCase));
    }

    @Test
    public void should_throw_exception_on_personalisation_when_case_is_null() {

        assertThatThrownBy(() -> respondentNonStandardDirectionPersonalisation.getPersonalisation((AsylumCase) null))
            .isExactlyInstanceOf(NullPointerException.class)
            .hasMessage("asylumCase must not be null");
    }

    @Test
    public void should_return_personalisation_when_all_information_given() {

        Map<String, String> personalisation = respondentNonStandardDirectionPersonalisation.getPersonalisation(asylumCase);

        assertEquals(hearingCentreString, personalisation.get("HearingCentre"));
        assertEquals(appealReferenceNumber, personalisation.get("Appeal Ref Number"));
        assertEquals(appellantGivenNames, personalisation.get("Given names"));
        assertEquals(appellantFamilyName, personalisation.get("Family name"));
        assertEquals(directionExplanation, personalisation.get("Explanation"));
        assertEquals(expectedDirectionDueDate, personalisation.get("due date"));
        assertEquals(homeOfficeRefNumber, personalisation.get("HORef"));
    }

    @Test
    public void should_return_personalisation_when_all_mandatory_information_given() {

        when(asylumCase.read(APPEAL_REFERENCE_NUMBER, String.class)).thenReturn(Optional.empty());
        when(asylumCase.read(APPELLANT_GIVEN_NAMES, String.class)).thenReturn(Optional.empty());
        when(asylumCase.read(APPELLANT_FAMILY_NAME, String.class)).thenReturn(Optional.empty());
        when(asylumCase.read(HOME_OFFICE_REFERENCE_NUMBER, String.class)).thenReturn(Optional.empty());

        Map<String, String> personalisation = respondentNonStandardDirectionPersonalisation.getPersonalisation(asylumCase);


        assertEquals("", personalisation.get("HORef"));
        assertEquals("", personalisation.get("Appeal Ref Number"));
        assertEquals("", personalisation.get("Given names"));
        assertEquals("", personalisation.get("Family name"));
        assertEquals(directionExplanation, personalisation.get("Explanation"));
        assertEquals(expectedDirectionDueDate, personalisation.get("due date"));
        assertEquals(hearingCentreString, personalisation.get("HearingCentre"));
    }

    @Test
    public void should_throw_exception_on_personalisation_when_direction_is_empty() {

        when(directionFinder.findFirst(asylumCase, DirectionTag.NONE)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> respondentNonStandardDirectionPersonalisation.getPersonalisation(asylumCase))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("non-standard direction is not present");
    }

    @Test
    public void should_throw_exception_on_personalisation_when_hearing_centre_is_empty() {

        when(asylumCase.read(HEARING_CENTRE, HearingCentre.class)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> respondentNonStandardDirectionPersonalisation.getPersonalisation(asylumCase))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("hearingCentre is not present");
    }

    @Test
    public void should_throw_exception_on_personalisation_when_hearing_centre_display_name_is_empty() {

        when(stringProvider.get("hearingCentre", hearingCentre.toString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> respondentNonStandardDirectionPersonalisation.getPersonalisation(asylumCase))
            .isExactlyInstanceOf(IllegalStateException.class)
            .hasMessage("hearingCentre display string is not present");
    }
}