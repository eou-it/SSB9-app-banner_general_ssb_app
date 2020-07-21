/*******************************************************************************
 Copyright 2018-2020 Ellucian Company L.P. and its affiliates.
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
    static final String ENABLE_CANADA_YEAR_END_TAX = 'ENABLE.CANADA.YEAR.END.TAX'
    static final int URL_NOT_FOUND = -1
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
        [isDirectDepositEnabled         : getParamFromSession(ENABLE_DIRECT_DEPOSIT, 'Y') == 'Y' && isDirectDepositAuthorizedForUser(),
         isPersonalInformationEnabled   : getParamFromSession(ENABLE_PERSONAL_INFORMATION, 'Y') == 'Y',
         isActionItemEnabledAndAvailable: getParamFromSession(ENABLE_ACTION_ITEM, 'Y') == 'Y' && actionItemProcessingConfigService?.isActionItemPresentForUser(),
         isActionItemEnabled            : getParamFromSession(ENABLE_ACTION_ITEM, 'Y') == 'Y',
         isProxyManagementEnabled       : getParamFromSession(ENABLE_PROXY_MANAGMENT, 'Y') == 'Y',
         proxyManagementUrl             : getParamFromSession(ENABLE_PROXY_MANAGMENT, 'Y') == 'Y' ? get8xUrl('proxyManagementUrl', 'bwgkprxy.P_ManageProxy') : URL_NOT_FOUND,
         isCanadaYearEndTaxEnabled      : getParamFromSession(ENABLE_CANADA_YEAR_END_TAX, 'N') == 'Y',
         canadaYearEndTaxUrl            : getParamFromSession(ENABLE_CANADA_YEAR_END_TAX, 'N') == 'Y' ? get8xUrl('canadianYearEndTaxUrl', 'bwvkgtax.P_SelectAdminOption') : URL_NOT_FOUND]
    }

    private boolean isDirectDepositAuthorizedForUser() {
        def urlMap = Holders.config.grails.plugin.springsecurity.interceptUrlMap
        String baseUrl = "/ssb/directDeposit/\\**"
        def baseList = []

        def pageRolesDirDep
        def pageRolesAcct

        pageRolesDirDep = urlMap.find {
            it.pattern =~ baseUrl
        }?.configAttributes

        pageRolesDirDep.each{
            baseList << it
        }

        String viewUrl = "/ssb/accountListing/\\**"
        def viewList = []

        pageRolesAcct = urlMap.find {
            it.pattern =~ viewUrl
        }?.configAttributes

        pageRolesAcct.each{
            viewList << it
        }

        def voter = new BannerAccessDecisionVoter()

        return AccessDecisionVoter.ACCESS_GRANTED == voter.vote(SecurityContextHolder?.context?.authentication, '/ssb/directDeposit/**', baseList) &&
                AccessDecisionVoter.ACCESS_GRANTED == voter.vote(SecurityContextHolder?.context?.authentication, '/ssb/accountListing/**', viewList)
    }

    private def get8xUrl(String urlSessionStorageName, String menuName) {
        def session = RequestContextHolder.currentRequestAttributes().request.session
        if(session[urlSessionStorageName] == null) {
            def pidm = PersonalInformationControllerUtility.getPrincipalPidm()
            def menus = selfServiceMenuService.bannerMenu(null, null, pidm)
            Set menusChecked = new HashSet()
            menusChecked.add("bmenu.P_MainMnu")
            def url = findUrl(menuName, menus, pidm, menusChecked)
            // cache -1 to prevent future futile searches
            session[urlSessionStorageName] = url ? url : URL_NOT_FOUND
        }
        return session[urlSessionStorageName]
    }

    private String findUrl(String menuName, menus, pidm, menusChecked) {
        for(int i = 0; i < menus.size(); i++) {
            def it = menus[i]
            if(it.type.equals('FORM') && it.formName.equals(menuName)) {
                return it.url
            }
            else if(it.type.equals('MENU')) {
                if(menusChecked.add(it.formName)) {
                    String url = findUrl(menuName, selfServiceMenuService.bannerMenu(it.formName, null, pidm), pidm, menusChecked)
                    if(url != null) {
                        return url
                    }
                }
            }
        }

        return null
    }
}
