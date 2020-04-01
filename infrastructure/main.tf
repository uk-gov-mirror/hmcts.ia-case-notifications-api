# Temporary fix for template API version error on deployment
provider "azurerm" {
  version = "1.21.0"
}

locals {

  preview_app_service_plan     = "${var.product}-${var.component}-${var.env}"
  non_preview_app_service_plan = "${var.product}-${var.env}"
  app_service_plan             = "${var.env == "preview" || var.env == "spreview" ? local.preview_app_service_plan : local.non_preview_app_service_plan}"

  preview_vault_name           = "${var.raw_product}-aat"
  non_preview_vault_name       = "${var.raw_product}-${var.env}"
  key_vault_name               = "${var.env == "preview" || var.env == "spreview" ? local.preview_vault_name : local.non_preview_vault_name}"
}

resource "azurerm_resource_group" "rg" {
  name     = "${var.product}-${var.component}-${var.env}"
  location = "${var.location}"
  tags     = "${merge(var.common_tags, map("lastUpdated", "${timestamp()}"))}"
}

data "azurerm_key_vault" "ia_key_vault" {
  name                = "${local.key_vault_name}"
  resource_group_name = "${local.key_vault_name}"
}

data "azurerm_key_vault_secret" "ia_gov_notify_key" {
  name      = "ia-gov-notify-key"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_bradford_email" {
  name      = "hearing-centre-bradford-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_manchester_email" {
  name      = "hearing-centre-manchester-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_newport_email" {
  name      = "hearing-centre-newport-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_taylor_house_email" {
  name      = "hearing-centre-taylor-house-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_north_shields_email" {
  name      = "hearing-centre-north-shields-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_birmingham_email" {
  name      = "hearing-centre-birmingham-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_hatton_cross_email" {
  name      = "hearing-centre-hatton-cross-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_glasgow_email" {
  name      = "hearing-centre-glasgow-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_bradford_email" {
  name      = "home-office-bradford-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_manchester_email" {
  name      = "home-office-manchester-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_newport_email" {
  name      = "home-office-newport-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_taylor_house_email" {
  name      = "home-office-taylor-house-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_north_shields_email" {
  name      = "home-office-north-shields-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_birmingham_email" {
  name      = "home-office-birmingham-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_hatton_cross_email" {
  name      = "home-office-hatton-cross-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_glasgow_email" {
  name      = "home-office-glasgow-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_respondent_evidence_direction_email" {
  name      = "respondent-evidence-direction-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_respondent_review_direction_email" {
  name      = "respondent-review-direction-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_respondent_non_standard_direction_until_listing_email" {
  name      = "respondent-non-standard-direction-until-listing-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_bradford_telephone" {
  name      = "hearing-centre-bradford-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_manchester_telephone" {
  name      = "hearing-centre-manchester-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_newport_telephone" {
  name      = "hearing-centre-newport-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_taylor_house_telephone" {
  name      = "hearing-centre-taylor-house-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_north_shields_telephone" {
  name      = "hearing-centre-north-shields-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_birmingham_telephone" {
  name      = "hearing-centre-birmingham-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_hatton_cross_telephone" {
  name      = "hearing-centre-hatton-cross-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_hearing_centre_glasgow_telephone" {
  name      = "hearing-centre-glasgow-telephone"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_end_appeal_email" {
  name      = "home-office-end-appeal-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_allowed_appeal_email" {
  name      = "home-office-allowed-appeal-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_home_office_dismissed_appeal_email" {
  name      = "home-office-dismissed-appeal-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_admin_officer_review_hearing_requirements_email" {
  name      = "admin-officer-review-hearing-requirements-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_ctsc_admin_ftpa_submitted_email" {
  name      = "ctsc-admin-ftpa-submitted-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "ia_respondent_ftpa_submitted_email" {
  name      = "respondent-ftpa-submitted-email"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "test_caseofficer_username" {
  name      = "test-caseofficer-username"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "test_caseofficer_password" {
  name      = "test-caseofficer-password"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "test_law_firm_a_username" {
  name      = "test-law-firm-a-username"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "test_law_firm_a_password" {
  name      = "test-law-firm-a-password"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "system_username" {
  name      = "system-username"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "system_password" {
  name      = "system-password"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "idam_client_id" {
  name      = "idam-client-id"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "idam_secret" {
  name      = "idam-secret"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "s2s_secret" {
  name      = "s2s-secret"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

data "azurerm_key_vault_secret" "s2s_microservice" {
  name      = "s2s-microservice"
  vault_uri = "${data.azurerm_key_vault.ia_key_vault.vault_uri}"
}

module "ia_case_notifications_api" {
  source                          = "git@github.com:hmcts/cnp-module-webapp?ref=master"
  product                         = "${var.product}-${var.component}"
  location                        = "${var.location}"
  env                             = "${var.env}"
  enable_ase                      = "${var.enable_ase}"
  ilbIp                           = "${var.ilbIp}"
  resource_group_name             = "${azurerm_resource_group.rg.name}"
  subscription                    = "${var.subscription}"
  capacity                        = "${var.capacity}"
  instance_size                   = "${var.instance_size}"
  common_tags                     = "${merge(var.common_tags, map("lastUpdated", "${timestamp()}"))}"
  appinsights_instrumentation_key = "${var.appinsights_instrumentation_key}"
  asp_name                        = "${local.app_service_plan}"
  asp_rg                          = "${local.app_service_plan}"

  app_settings = {
    LOGBACK_REQUIRE_ALERT_LEVEL = false
    LOGBACK_REQUIRE_ERROR_CODE  = false

    IA_GOV_NOTIFY_KEY   = "${data.azurerm_key_vault_secret.ia_gov_notify_key.value}"

    IA_HEARING_CENTRE_BRADFORD_EMAIL                         = "${data.azurerm_key_vault_secret.ia_hearing_centre_bradford_email.value}"
    IA_HEARING_CENTRE_MANCHESTER_EMAIL                       = "${data.azurerm_key_vault_secret.ia_hearing_centre_manchester_email.value}"
    IA_HEARING_CENTRE_NEWPORT_EMAIL                          = "${data.azurerm_key_vault_secret.ia_hearing_centre_newport_email.value}"
    IA_HEARING_CENTRE_TAYLOR_HOUSE_EMAIL                     = "${data.azurerm_key_vault_secret.ia_hearing_centre_taylor_house_email.value}"
    IA_HEARING_CENTRE_NORTH_SHIELDS_EMAIL                    = "${data.azurerm_key_vault_secret.ia_hearing_centre_north_shields_email.value}"
    IA_HEARING_CENTRE_BIRMINGHAM_EMAIL                       = "${data.azurerm_key_vault_secret.ia_hearing_centre_birmingham_email.value}"
    IA_HEARING_CENTRE_HATTON_CROSS_EMAIL                     = "${data.azurerm_key_vault_secret.ia_hearing_centre_hatton_cross_email.value}"
    IA_HEARING_CENTRE_GLASGOW_EMAIL                          = "${data.azurerm_key_vault_secret.ia_hearing_centre_glasgow_email.value}"

    IA_HOME_OFFICE_BRADFORD_EMAIL                            = "${data.azurerm_key_vault_secret.ia_home_office_bradford_email.value}"
    IA_HOME_OFFICE_MANCHESTER_EMAIL                          = "${data.azurerm_key_vault_secret.ia_home_office_manchester_email.value}"
    IA_HOME_OFFICE_NEWPORT_EMAIL                             = "${data.azurerm_key_vault_secret.ia_home_office_newport_email.value}"
    IA_HOME_OFFICE_TAYLOR_HOUSE_EMAIL                        = "${data.azurerm_key_vault_secret.ia_home_office_taylor_house_email.value}"
    IA_HOME_OFFICE_NORTH_SHIELDS_EMAIL                       = "${data.azurerm_key_vault_secret.ia_home_office_north_shields_email.value}"
    IA_HOME_OFFICE_BIRMINGHAM_EMAIL                          = "${data.azurerm_key_vault_secret.ia_home_office_birmingham_email.value}"
    IA_HOME_OFFICE_HATTON_CROSS_EMAIL                        = "${data.azurerm_key_vault_secret.ia_home_office_hatton_cross_email.value}"
    IA_HOME_OFFICE_GLASGOW_EMAIL                             = "${data.azurerm_key_vault_secret.ia_home_office_glasgow_email.value}"

    IA_RESPONDENT_EVIDENCE_DIRECTION_EMAIL                   = "${data.azurerm_key_vault_secret.ia_respondent_evidence_direction_email.value}"
    IA_RESPONDENT_REVIEW_DIRECTION_EMAIL                     = "${data.azurerm_key_vault_secret.ia_respondent_review_direction_email.value}"
    IA_RESPONDENT_NON_STANDARD_DIRECTION_UNTIL_LISTING_EMAIL = "${data.azurerm_key_vault_secret.ia_respondent_non_standard_direction_until_listing_email.value}"

    IA_HOME_OFFICE_END_APPEAL_EMAIL                          = "${data.azurerm_key_vault_secret.ia_home_office_end_appeal_email.value}"
    IA_HOME_OFFICE_ALLOWED_APPEAL_EMAIL                      = "${data.azurerm_key_vault_secret.ia_home_office_allowed_appeal_email.value}"
    IA_HOME_OFFICE_DISMISSED_APPEAL_EMAIL                    = "${data.azurerm_key_vault_secret.ia_home_office_dismissed_appeal_email.value}"

    IA_ADMIN_OFFICER_REVIEW_HEARING_REQUIREMENTS_EMAIL       = "${data.azurerm_key_vault_secret.ia_admin_officer_review_hearing_requirements_email.value}"

    IA_CTSC_ADMIN_FTPA_SUBMITTED                             =  "${data.azurerm_key_vault_secret.ia_ctsc_admin_ftpa_submitted_email.value}"
    IA_RESPONDENT_FTPA_SUBMITTED                             =  "${data.azurerm_key_vault_secret.ia_respondent_ftpa_submitted_email.value}"

    IA_HEARING_CENTRE_BRADFORD_TELEPHONE                     = "${data.azurerm_key_vault_secret.ia_hearing_centre_bradford_telephone.value}"
    IA_HEARING_CENTRE_MANCHESTER_TELEPHONE                   = "${data.azurerm_key_vault_secret.ia_hearing_centre_manchester_telephone.value}"
    IA_HEARING_CENTRE_NEWPORT_TELEPHONE                      = "${data.azurerm_key_vault_secret.ia_hearing_centre_newport_telephone.value}"
    IA_HEARING_CENTRE_TAYLOR_HOUSE_TELEPHONE                 = "${data.azurerm_key_vault_secret.ia_hearing_centre_taylor_house_telephone.value}"
    IA_HEARING_CENTRE_NORTH_SHIELDS_TELEPHONE                = "${data.azurerm_key_vault_secret.ia_hearing_centre_north_shields_telephone.value}"
    IA_HEARING_CENTRE_BIRMINGHAM_TELEPHONE                   = "${data.azurerm_key_vault_secret.ia_hearing_centre_birmingham_telephone.value}"
    IA_HEARING_CENTRE_HATTON_CROSS_TELEPHONE                 = "${data.azurerm_key_vault_secret.ia_hearing_centre_hatton_cross_telephone.value}"
    IA_HEARING_CENTRE_GLASGOW_TELEPHONE                      = "${data.azurerm_key_vault_secret.ia_hearing_centre_glasgow_telephone.value}"

    IA_SYSTEM_USERNAME   = "${data.azurerm_key_vault_secret.system_username.value}"
    IA_SYSTEM_PASSWORD   = "${data.azurerm_key_vault_secret.system_password.value}"
    IA_IDAM_CLIENT_ID    = "${data.azurerm_key_vault_secret.idam_client_id.value}"
    IA_IDAM_SECRET       = "${data.azurerm_key_vault_secret.idam_secret.value}"
    IA_S2S_SECRET        = "${data.azurerm_key_vault_secret.s2s_secret.value}"
    IA_S2S_MICROSERVICE  = "${data.azurerm_key_vault_secret.s2s_microservice.value}"

    ROOT_LOGGING_LEVEL   = "${var.root_logging_level}"
    LOG_LEVEL_SPRING_WEB = "${var.log_level_spring_web}"
    LOG_LEVEL_IA         = "${var.log_level_ia}"
  }
}
