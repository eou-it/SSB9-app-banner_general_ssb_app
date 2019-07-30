/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package banner.general.ssb.app

import net.hedtech.banner.general.GeneralSsbConfigService

class GeneralSelfServicePersonalInformationDetailsInterceptor {
    def generalSsbConfigService

    GeneralSelfServicePersonalInformationDetailsInterceptor() {
        match (controller: 'personalInformationDetails').excludes(action: 'getPersonalDetails')
                                                        .excludes(action:'getBannerId')
                                                        .excludes(action:'getUserName')
                                                        .excludes(action:'getPiConfig')

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
