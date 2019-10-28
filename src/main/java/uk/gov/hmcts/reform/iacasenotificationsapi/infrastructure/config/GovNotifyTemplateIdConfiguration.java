package uk.gov.hmcts.reform.iacasenotificationsapi.infrastructure.config;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "govnotify.template")
public class GovNotifyTemplateIdConfiguration {

    @NotBlank
    private String endAppealHomeOfficeTemplateId;

    @NotBlank
    private String endAppealLegalRepresentativeTemplateId;

    @NotBlank
    private String hearingBundleReadyLegalRepTemplateId;

    @NotBlank
    private String hearingBundleReadyHomeOfficeTemplateId;

    @NotBlank
    private String legalRepresentativeNonStandardDirectionTemplateId;

    @NotBlank
    private String legalRepNonStandardDirectionOfHomeOfficeTemplateId;
}
