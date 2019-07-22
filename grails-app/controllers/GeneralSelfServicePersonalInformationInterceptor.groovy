/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

import net.hedtech.banner.general.GeneralSsbConfigService

class GeneralSelfServicePersonalInformationInterceptor {
    def generalSsbConfigService

    GeneralSelfServicePersonalInformationInterceptor() {
        match controller: ~/(personalInformation|personalInformationQA)/
    }
    boolean before()
    {
        if (generalSsbConfigService.getParamFromSession(GeneralSsbConfigService.ENABLE_PERSONAL_INFORMATION, 'Y') != 'Y') {
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
