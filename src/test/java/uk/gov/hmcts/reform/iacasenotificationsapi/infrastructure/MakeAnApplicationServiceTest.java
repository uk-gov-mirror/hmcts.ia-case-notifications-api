package uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.DECIDE_AN_APPLICATION_ID;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.MAKE_AN_APPLICATIONS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.MakeAnApplication;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.State;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.field.IdValue;

@RunWith(MockitoJUnitRunner.class)
public class MakeAnApplicationServiceTest {
    @Mock private AsylumCase asylumCase;
    @Mock private List<IdValue<MakeAnApplication>> applications;

    private MakeAnApplicationService makeAnApplicationService;
    private String decideAnApplicationId = "1";

    @Before
    public void setup() {
        when(asylumCase.read(MAKE_AN_APPLICATIONS)).thenReturn(Optional.of(applications));
        when(asylumCase.read(DECIDE_AN_APPLICATION_ID)).thenReturn(Optional.of(decideAnApplicationId));
        makeAnApplicationService = new MakeAnApplicationService();
    }

    @Test
    public void should_return_empty_application_type() {
        when(asylumCase.read(MAKE_AN_APPLICATIONS)).thenReturn(Optional.empty());
        verify(asylumCase,never()).read(MAKE_AN_APPLICATIONS);

        assertEquals("", makeAnApplicationService.getMakeAnApplicationTypeName(asylumCase));

        when(asylumCase.read(DECIDE_AN_APPLICATION_ID)).thenReturn(Optional.empty());
        when(makeAnApplicationService.getMakeAnApplication(asylumCase)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), makeAnApplicationService.getMakeAnApplication(asylumCase));
        assertEquals(Optional.empty(), asylumCase.read(DECIDE_AN_APPLICATION_ID));
    }

    @Test
    public void should_return_other_application_type() {
        assertEquals("", makeAnApplicationService.getMakeAnApplicationTypeName(asylumCase));
        assertEquals(Optional.empty(), makeAnApplicationService.getMakeAnApplication(asylumCase));

        List<IdValue<MakeAnApplication>> makeAnApplications = new ArrayList<>();
        MakeAnApplication makeAnApplication = new MakeAnApplication(
                "",
                "Other",
                "",
                new ArrayList<>(),
                "",
                "",
                "",
                "");

        makeAnApplications.add(new IdValue<>("1",makeAnApplication));
        when(asylumCase.read(MAKE_AN_APPLICATIONS)).thenReturn(Optional.of(makeAnApplications));

        String retrievedMakeAnApplicationType = makeAnApplicationService.getMakeAnApplicationTypeName(asylumCase);

        assertEquals("Other",retrievedMakeAnApplicationType);



        assertTrue(asylumCase.read(DECIDE_AN_APPLICATION_ID).isPresent());

        when(asylumCase.read(MAKE_AN_APPLICATIONS)).thenReturn(Optional.of(makeAnApplications));

        Optional<MakeAnApplication> retrievedMakeAnApplication = makeAnApplicationService.getMakeAnApplication(asylumCase);
        assertTrue(retrievedMakeAnApplication.isPresent());
        assertEquals("Other",retrievedMakeAnApplication.get().getType());
    }

    @Test
    public void isAppealListed() {
        State state = State.APPEAL_SUBMITTED;
        assertFalse(makeAnApplicationService.isApplicationListed(state));

        state = State.ADJOURNED;
        assertTrue(makeAnApplicationService.isApplicationListed(state));
    }
}
