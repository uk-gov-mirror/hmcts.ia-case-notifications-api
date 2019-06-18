package uk.gov.hmcts.reform.iacasenotificationsapi.domain.handlers.presubmit;

import static java.util.Objects.requireNonNull;
import static uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCaseDefinition.NOTIFICATIONS_SENT;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.NotificationSender;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.Direction;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.DirectionTag;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.Event;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.Callback;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.PreSubmitCallbackResponse;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.PreSubmitCallbackStage;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.field.IdValue;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.handlers.PreSubmitCallbackHandler;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.personalisation.RespondentDirectionPersonalisationFactory;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.DirectionFinder;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.NotificationIdAppender;

@Component
public class RespondentReviewDirectionNotifier implements PreSubmitCallbackHandler<AsylumCase> {

    private final String respondentReviewDirectionTemplateId;
    private final String respondentReviewDirectionEmailAddress;
    private final RespondentDirectionPersonalisationFactory respondentDirectionPersonalisationFactory;
    private final DirectionFinder directionFinder;
    private final NotificationSender notificationSender;
    private final NotificationIdAppender notificationIdAppender;

    public RespondentReviewDirectionNotifier(
        @Value("${govnotify.template.respondentReviewDirection}") String respondentReviewDirectionTemplateId,
        @Value("${respondentEmailAddresses.respondentReviewDirection}") String respondentReviewDirectionEmailAddress,
        RespondentDirectionPersonalisationFactory respondentDirectionPersonalisationFactory,
        DirectionFinder directionFinder,
        NotificationSender notificationSender,
        NotificationIdAppender notificationIdAppender
    ) {
        requireNonNull(respondentReviewDirectionTemplateId, "respondentReviewDirectionTemplateId must not be null");
        requireNonNull(respondentReviewDirectionEmailAddress, "respondentReviewDirectionEmailAddress must not be null");

        this.respondentReviewDirectionTemplateId = respondentReviewDirectionTemplateId;
        this.respondentReviewDirectionEmailAddress = respondentReviewDirectionEmailAddress;
        this.respondentDirectionPersonalisationFactory = respondentDirectionPersonalisationFactory;
        this.directionFinder = directionFinder;
        this.notificationSender = notificationSender;
        this.notificationIdAppender = notificationIdAppender;
    }

    public boolean canHandle(
        PreSubmitCallbackStage callbackStage,
        Callback<AsylumCase> callback
    ) {
        requireNonNull(callbackStage, "callbackStage must not be null");
        requireNonNull(callback, "callback must not be null");

        return callbackStage == PreSubmitCallbackStage.ABOUT_TO_SUBMIT
               && callback.getEvent() == Event.REQUEST_RESPONDENT_REVIEW;
    }

    public PreSubmitCallbackResponse<AsylumCase> handle(
        PreSubmitCallbackStage callbackStage,
        Callback<AsylumCase> callback
    ) {
        if (!canHandle(callbackStage, callback)) {
            throw new IllegalStateException("Cannot handle callback");
        }

        AsylumCase asylumCase =
            callback
                .getCaseDetails()
                .getCaseData();

        Direction respondentReviewDirection =
            directionFinder
                .findFirst(asylumCase, DirectionTag.RESPONDENT_REVIEW)
                .orElseThrow(() -> new IllegalStateException("direction '" + DirectionTag.RESPONDENT_REVIEW + "' is not present"));

        Map<String, String> personalisation =
            respondentDirectionPersonalisationFactory
                .create(asylumCase, respondentReviewDirection);

        String reference =
            callback.getCaseDetails().getId()
            + "_RESPONDENT_REVIEW_DIRECTION";

        String notificationId =
            notificationSender.sendEmail(
                respondentReviewDirectionTemplateId,
                respondentReviewDirectionEmailAddress,
                personalisation,
                reference
            );

        Optional<List<IdValue<String>>> maybeNotificationSent =
                asylumCase.read(NOTIFICATIONS_SENT);

        List<IdValue<String>> notificationsSent =
                maybeNotificationSent
                        .orElseGet(ArrayList::new);

        asylumCase.write(NOTIFICATIONS_SENT,
                notificationIdAppender.append(
                        notificationsSent,
                        reference,
                        notificationId
                )
        );

        return new PreSubmitCallbackResponse<>(asylumCase);
    }
}
