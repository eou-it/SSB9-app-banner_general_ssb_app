/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

import net.hedtech.banner.general.GeneralSsbConfigService

class GeneralSelfServiceAipInterceptor {
    def generalSsbConfigService

    GeneralSelfServiceAipInterceptor() {
        match controller: ~/(aip|aipAdmin|aipReview|aipActionItemPosting|aipDocumentManagement|BCM|aipPageBuilder)/
    }
    boolean before()
    {
        if (generalSsbConfigService.getParamFromSession(GeneralSsbConfigService.ENABLE_ACTION_ITEM, 'Y') != 'Y') {
            redirect(controller: "error", action: "accessForbidden")
            return false
        }
        true

    }

    boolean after() { true }

    void afterView() {
        // no-op
    }
}
