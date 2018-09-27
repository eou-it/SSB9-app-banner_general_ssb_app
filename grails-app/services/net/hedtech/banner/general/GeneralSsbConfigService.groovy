/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general

import grails.util.Holders
import net.hedtech.banner.security.BannerAccessDecisionVoter
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Service Class for General SSB Configuration
 */
class GeneralSsbConfigService extends BasePersonConfigService {

    static final String ENABLE_DIRECT_DEPOSIT = 'ENABLE.DIRECT.DEPOSIT'
    static final String ENABLE_PERSONAL_INFORMATION = 'ENABLE.PERSONAL.INFORMATION'
    static final String ENABLE_ACTION_ITEM = 'ENABLE.ACTION.ITEMS'
    def actionItemProcessingConfigService

    @Override
    protected String getCacheName() {
        return 'generalSsbConfig'
    }


    @Override
    protected String getProcessCode() {
        return 'GENERAL_SSB'
    }


    @Override
    protected List getExcludedProperties() {
        return []
    }

    /**
     * Get General Configuration
     * @return
     */
    def getGeneralConfig() {
        [isDirectDepositEnabled      : getParamFromSession( ENABLE_DIRECT_DEPOSIT, 'Y' ) == 'Y' && isDirectDepositAuthorizedForUser(),
         isPersonalInformationEnabled: getParamFromSession( ENABLE_PERSONAL_INFORMATION, 'Y' ) == 'Y',
         //isActionItemEnabledAndAvailable         : getParamFromSession( ENABLE_ACTION_ITEM, 'Y' ) == 'Y' && actionItemProcessingConfigService.isActionItemPresentForUser(),
         isActionItemEnabled :  getParamFromSession( ENABLE_ACTION_ITEM, 'Y' ) == 'Y']
    }

    private boolean isDirectDepositAuthorizedForUser() {
        def urlMap = Holders.config.grails.plugin.springsecurity.interceptUrlMap
        String baseUrl = '/ssb/directDeposit/**'
        def baseList = []
        urlMap.get(baseUrl).each {
            // role config items should act like a ConfigAttribute
            baseList << [attribute: it]
        }

        String viewUrl = '/ssb/accountListing/**'
        def viewList = []
        urlMap.get(viewUrl).each {
            // role config items should act like a ConfigAttribute
            viewList << [attribute: it]
        }

        def voter = new BannerAccessDecisionVoter()
        return AccessDecisionVoter.ACCESS_GRANTED == voter.vote(SecurityContextHolder?.context?.authentication, baseUrl, baseList) &&
                AccessDecisionVoter.ACCESS_GRANTED == voter.vote(SecurityContextHolder?.context?.authentication, viewUrl, viewList)
    }
}
