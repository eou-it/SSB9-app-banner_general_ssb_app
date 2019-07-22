/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */

import net.hedtech.banner.general.GeneralSsbConfigService

class GeneralSelfServiceProxyInterceptor {
    def generalSsbConfigService

    GeneralSelfServiceProxyInterceptor() {
        match controller: ~/(proxyManagement)/
    }
    boolean before()
    {
        if (generalSsbConfigService.getParamFromSession(GeneralSsbConfigService.ENABLE_PROXY_MANAGMENT, 'Y') != 'Y') {
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
