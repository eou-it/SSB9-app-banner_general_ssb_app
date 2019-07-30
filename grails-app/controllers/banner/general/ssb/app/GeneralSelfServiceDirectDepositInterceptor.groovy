/*******************************************************************************
 Copyright 2019 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */
package banner.general.ssb.app

import net.hedtech.banner.general.GeneralSsbConfigService

class GeneralSelfServiceDirectDepositInterceptor {
    def generalSsbConfigService

    GeneralSelfServiceDirectDepositInterceptor() {
        match controller: ~/(directDeposit|accountListing|updateAccount)/
    }
    boolean before()
    {
        if (generalSsbConfigService.getParamFromSession(GeneralSsbConfigService.ENABLE_DIRECT_DEPOSIT, 'Y') != 'Y') {
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
