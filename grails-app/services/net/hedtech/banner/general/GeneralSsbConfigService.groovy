/*******************************************************************************
 Copyright 2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general

import grails.util.Holders
import net.hedtech.banner.security.BannerAccessDecisionVoter
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder

/**
 * Service Class for General SSB Configuration
 */
class GeneralSsbConfigService extends BasePersonConfigService {

    static final String ENABLE_DIRECT_DEPOSIT = 'ENABLE.DIRECT.DEPOSIT'
    static final String ENABLE_PERSONAL_INFORMATION = 'ENABLE.PERSONAL.INFORMATION'
    static final String ENABLE_ACTION_ITEM = 'ENABLE.ACTION.ITEMS'
    static final String ENABLE_PROXY_MANAGMENT = 'ENABLE.PROXY.MANAGEMENT'
    def actionItemProcessingConfigService
    def selfServiceMenuService

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
         isActionItemEnabled :  getParamFromSession( ENABLE_ACTION_ITEM, 'Y' ) == 'Y',
         isProxyManagementEnabled : getParamFromSession( ENABLE_PROXY_MANAGMENT, 'Y' ) == 'Y',
         proxyManagementUrl : get8xProxyManagementUrl()]
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

    private def get8xProxyManagementUrl() {
        def session = RequestContextHolder.currentRequestAttributes().request.session
        if(session['proxyManagementUrl'] == null) {
            def pidm = PersonalInformationControllerUtility.getPrincipalPidm()
            def menus = selfServiceMenuService.bannerMenu(null, null, pidm)
            def url = findProxyMgmtUrl(menus, pidm)
            // cache -1 to prevent future futile searches
            session['proxyManagementUrl'] = url ? url : -1
        }
        return session['proxyManagementUrl']
    }

    private String findProxyMgmtUrl(menus, pidm) {
        for(int i = 0; i < menus.size(); i++) {
            def it = menus[i]
            if(it.type.equals('FORM') && it.formName.equals('bwgkprxy.P_ManageProxy')) {
                return it.url
            }
            else if(it.type.equals('MENU')) {
                String url = findProxyMgmtUrl(selfServiceMenuService.bannerMenu(it.formName, null, pidm), pidm)
                if(url != null) {
                    return url
                }
            }
        }

        return null
    }
}
