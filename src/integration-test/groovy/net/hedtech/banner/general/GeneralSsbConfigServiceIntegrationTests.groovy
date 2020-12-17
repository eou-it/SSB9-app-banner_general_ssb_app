/*******************************************************************************
 Copyright 2017-2021 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.Holders
import grails.web.servlet.context.GrailsWebApplicationContext
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.junit.After
import org.junit.Before
import org.junit.Test

@Integration
@Rollback
class GeneralSsbConfigServiceIntegrationTests extends BaseIntegrationTestCase {

    def generalSsbConfigService

    @Before
    public void setUp() {
        formContext = ['SELFSERVICE']
        super.setUp()

        webAppCtx = new GrailsWebApplicationContext()
    }

    @After
    public void tearDown() {
        super.tearDown()
        super.logout()
    }


    @Test
    void testGetParamFromSessionWithNoPreexistingConfig() {
        def val = generalSsbConfigService.getParamFromSession(generalSsbConfigService.ENABLE_DIRECT_DEPOSIT, 'dummy_default_value')

        assertEquals 'Y', val
    }

    @Test
    void testGetParamFromSessionWithPreexistingConfig() {

        mockRequest()
        SSBSetUp('GDP000005', '111111')
        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_DIRECT_DEPOSIT): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def val = generalSsbConfigService.getParamFromSession(generalSsbConfigService.ENABLE_DIRECT_DEPOSIT, 'dummy_default_value')

        assertEquals 'Y', val
    }

    @Test
    void testGetGeneralConfig() {

        mockRequest()
        SSBSetUp('GDP000005', '111111')
        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_ACTION_ITEM): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)
        def config = generalSsbConfigService.getGeneralConfig()
        assertTrue config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertFalse config.isCanadaYearEndTaxEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    //@Test
    void testIsDirectDepositAuthorizedForUser() {
        mockRequest()
        SSBSetUp('GDP000005', '111111')

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_ACTION_ITEM): 'N']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        // restrict access to non-employee and non-student role the user does not have
        def origUrlConf = Holders.config.grails.plugin.springsecurity.interceptUrlMap.get('/ssb/directDeposit/**')
        Holders.config.grails.plugin.springsecurity.interceptUrlMap.put('/ssb/directDeposit/**',['ROLE_SELFSERVICE-ACTIONITEMADMIN_BAN_DEFAULT_M'])
        def config = generalSsbConfigService.getGeneralConfig()
        Holders.config.grails.plugin.springsecurity.interceptUrlMap.put('/ssb/directDeposit/**', origUrlConf)

        assertFalse config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    @Test
    void testGet8xUrl() {
        mockRequest()
        SSBSetUp('GDP000005', '111111')

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_PROXY_MANAGMENT): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def config = generalSsbConfigService.getGeneralConfig()

        assertTrue config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertFalse config.isCanadaYearEndTaxEnabled
        //assertEquals 'http://<host_name>:<port_number>/<banner8>/enUS/bwgkprxy.P_ManageProxy', config.proxyManagementUrl
    }

    @Test
    void testGet8xUrlDisabled() {
        mockRequest()
        SSBSetUp('GDP000005', '111111')

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_PROXY_MANAGMENT): 'N']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def config = generalSsbConfigService.getGeneralConfig()

        assertTrue config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertFalse config.isProxyManagementEnabled
        assertFalse config.isCanadaYearEndTaxEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    @Test
    void testGet8xUrlInfiniteLoop() {
        // Alumni user, has access to self-referential Mailing List menu
        SSBSetUp('HOSP0001', '111111')

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_PROXY_MANAGMENT): 'Y', (generalSsbConfigService.ENABLE_DIRECT_DEPOSIT): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def config = generalSsbConfigService.getGeneralConfig()

        assertTrue config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    public GrailsWebRequest mockRequest() {
        GrailsMockHttpServletRequest mockRequest = new GrailsMockHttpServletRequest();
        GrailsMockHttpServletResponse mockResponse = new GrailsMockHttpServletResponse();
        GrailsWebMockUtil.bindMockWebRequest(webAppCtx, mockRequest, mockResponse)
    }
}
