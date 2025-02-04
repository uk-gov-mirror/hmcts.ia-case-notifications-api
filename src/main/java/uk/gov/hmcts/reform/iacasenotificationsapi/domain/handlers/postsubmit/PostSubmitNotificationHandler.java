package uk.gov.hmcts.reform.iacasenotificationsapi.domain.handlers.postsubmit;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.AsylumCase;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.Message;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.entities.ccd.callback.*;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.handlers.ErrorHandler;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.handlers.PostSubmitCallbackHandler;
import uk.gov.hmcts.reform.iacasenotificationsapi.domain.service.NotificationGenerator;

public class PostSubmitNotificationHandler implements PostSubmitCallbackHandler<AsylumCase> {

    private final BiPredicate<PostSubmitCallbackStage, Callback<AsylumCase>> canHandleFunction;
    private final List<? extends NotificationGenerator> notificationGenerators;
    private final Optional<ErrorHandler> errorHandling;

    public PostSubmitNotificationHandler(BiPredicate<PostSubmitCallbackStage, Callback<AsylumCase>> canHandleFunction,
                                         List<? extends NotificationGenerator> notificationGenerator
    ) {
        this.canHandleFunction = canHandleFunction;
        this.notificationGenerators = notificationGenerator;
        this.errorHandling = Optional.empty();
    }

    public PostSubmitNotificationHandler(BiPredicate<PostSubmitCallbackStage, Callback<AsylumCase>> canHandleFunction,
                                         List<? extends NotificationGenerator> notificationGenerator,
                                         ErrorHandler errorHandling
    ) {
        this.canHandleFunction = canHandleFunction;
        this.notificationGenerators = notificationGenerator;
        this.errorHandling = Optional.ofNullable(errorHandling);
    }

    @Override
    public boolean canHandle(PostSubmitCallbackStage callbackStage, Callback<AsylumCase> callback) {
        requireNonNull(callbackStage, "callbackStage must not be null");
        requireNonNull(callback, "callback must not be null");

        return canHandleFunction.test(callbackStage, callback);
    }

    @Override
    public PostSubmitCallbackResponse handle(PostSubmitCallbackStage callbackStage, Callback<AsylumCase> callback) {
        if (!canHandle(callbackStage, callback)) {
            throw new IllegalStateException("Cannot handle callback");
        }

        PostSubmitCallbackResponse postSubmitCallbackResponse = new PostSubmitCallbackResponse("success", "success");
        final int lastNotificationGeneratorIndex = notificationGenerators.size() - 1;

        try {
            notificationGenerators.forEach(notificationGenerator -> notificationGenerator.generate(callback));
            Message message = notificationGenerators.get(lastNotificationGeneratorIndex).getSuccessMessage();
            if (message.getMessageHeader() != null) {
                postSubmitCallbackResponse.setConfirmationHeader(message.getMessageHeader());
            }
            if (message.getMessageBody() != null) {
                postSubmitCallbackResponse.setConfirmationBody(message.getMessageBody());
            }
        } catch (Exception e) {
            if (errorHandling.isPresent()) {
                errorHandling.get().accept(callback, e);
            } else {
                throw e;
            }
        }
        return postSubmitCallbackResponse;
    }
}
