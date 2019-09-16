package uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.*;

import com.google.common.collect.ImmutableMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.Direction;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.HearingCentre;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.StringProvider;
import uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.DateTimeExtractor;

@Service
public class LegalRepresentativePersonalisationFactory {

    private final String iaCcdFrontendUrl;
    private final StringProvider stringProvider;
    private final DateTimeExtractor dateTimeExtractor;

    public LegalRepresentativePersonalisationFactory(
        @Value("${iaCcdFrontendUrl}") String iaCcdFrontendUrl,
        StringProvider stringProvider,
        DateTimeExtractor dateTimeExtractor
    ) {
        requireNonNull(iaCcdFrontendUrl, "iaCcdFrontendUrl must not be null");

        this.iaCcdFrontendUrl = iaCcdFrontendUrl;
        this.stringProvider = stringProvider;
        this.dateTimeExtractor = dateTimeExtractor;
    }

    public Map<String, String> create(
        AsylumCase asylumCase,
        Direction direction
    ) {
        requireNonNull(asylumCase, "asylumCase must not be null");
        requireNonNull(direction, "direction must not be null");

        final String directionDueDate =
            LocalDate
                .parse(direction.getDateDue())
                .format(DateTimeFormatter.ofPattern("d MMM yyyy"));

        return
            ImmutableMap
                .<String, String>builder()
                .put("Appeal Ref Number", asylumCase.read(APPEAL_REFERENCE_NUMBER, String.class).orElse(""))
                .put("LR reference", asylumCase.read(LEGAL_REP_REFERENCE_NUMBER, String.class).orElse(""))
                .put("Given names", asylumCase.read(APPELLANT_GIVEN_NAMES, String.class).orElse(""))
                .put("Family name", asylumCase.read(APPELLANT_FAMILY_NAME, String.class).orElse(""))
                .put("Hyperlink to user’s case list", iaCcdFrontendUrl)
                .put("Explanation", direction.getExplanation())
                .put("due date", directionDueDate)
                .build();
    }

    public Map<String, String> createListedCase(
        AsylumCase asylumCase
    ) {
        requireNonNull(asylumCase, "asylumCase must not be null");

        final HearingCentre listedHearingCentre =
            asylumCase
                .read(LIST_CASE_HEARING_CENTRE, HearingCentre.class)
                .orElseThrow(() -> new IllegalStateException("listCaseHearingCentre is not present"));

        final String hearingCentreAddress =
            stringProvider
                .get("hearingCentreAddress", listedHearingCentre.toString())
                .orElseThrow(() -> new IllegalStateException("hearingCentreAddress is not present"));

        final String hearingDateTime =
            asylumCase
                .read(AsylumCaseDefinition.LIST_CASE_HEARING_DATE, String.class)
                .orElseThrow(() -> new IllegalStateException("hearingDateTime is not present"));

        return
            ImmutableMap
                .<String, String>builder()
                .put("Appeal Ref Number", asylumCase.read(AsylumCaseDefinition.APPEAL_REFERENCE_NUMBER, String.class).orElse(""))
                .put("Listing Ref Number", asylumCase.read(AsylumCaseDefinition.ARIA_LISTING_REFERENCE, String.class).orElse(""))
                .put("Legal Rep Ref Number", asylumCase.read(AsylumCaseDefinition.LEGAL_REP_REFERENCE_NUMBER, String.class).orElse(""))
                .put("Appellant Given Names", asylumCase.read(APPELLANT_GIVEN_NAMES, String.class).orElse(""))
                .put("Appellant Family Name", asylumCase.read(APPELLANT_FAMILY_NAME, String.class).orElse(""))
                .put("Hearing Date", dateTimeExtractor.extractHearingDate(hearingDateTime))
                .put("Hearing Time", dateTimeExtractor.extractHearingTime(hearingDateTime))
                .put("Hearing Centre Address", hearingCentreAddress)
                .put("Hearing Requirement Vulnerabilities", asylumCase.read(AsylumCaseDefinition.LIST_CASE_REQUIREMENTS_VULNERABILITIES, String.class)
                    .orElse("No special adjustments are being made to accommodate vulnerabilities"))
                .put("Hearing Requirement Multimedia", asylumCase.read(AsylumCaseDefinition.LIST_CASE_REQUIREMENTS_MULTIMEDIA, String.class)
                    .orElse("No multimedia equipment is being provided"))
                .put("Hearing Requirement Single Sex Court", asylumCase.read(AsylumCaseDefinition.LIST_CASE_REQUIREMENTS_SINGLE_SEX_COURT, String.class)
                    .orElse("The court will not be single sex"))
                .put("Hearing Requirement In Camera Court", asylumCase.read(AsylumCaseDefinition.LIST_CASE_REQUIREMENTS_IN_CAMERA_COURT, String.class)
                    .orElse("The hearing will be held in public court"))
                .put("Hearing Requirement Other", asylumCase.read(AsylumCaseDefinition.LIST_CASE_REQUIREMENTS_OTHER, String.class)
                    .orElse("No other adjustments are being made"))
                .build();
    }
}
