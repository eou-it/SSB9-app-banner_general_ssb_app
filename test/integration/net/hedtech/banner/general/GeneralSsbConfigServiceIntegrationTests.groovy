/*******************************************************************************
 Copyright 2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/
package net.hedtech.banner.general

import grails.util.Holders
import net.hedtech.banner.general.person.PersonUtility
import net.hedtech.banner.testing.BaseIntegrationTestCase
import org.junit.After
import org.junit.Before
import org.junit.Test


class GeneralSsbConfigServiceIntegrationTests extends BaseIntegrationTestCase {

    def generalSsbConfigService

    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        super.setUp()
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
        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_DIRECT_DEPOSIT): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def val = generalSsbConfigService.getParamFromSession(generalSsbConfigService.ENABLE_DIRECT_DEPOSIT, 'dummy_default_value')

        assertEquals 'Y', val
    }

    @Test
    void testGetGeneralConfig() {
        loginSSB 'GDP000005', '111111'

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_ACTION_ITEM): 'Y']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def config = generalSsbConfigService.getGeneralConfig()
        assertTrue config.isActionItemEnabled
        assertTrue config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    @Test
    void testIsDirectDepositAuthorizedForUser() {
        loginSSB 'GDP000005', '111111'

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_ACTION_ITEM): 'N']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        // restrict access to non-employee and non-student role the user does not have
        def origUrlConf = Holders.config.grails.plugin.springsecurity.interceptUrlMap.get('/ssb/directDeposit/**')
        Holders.config.grails.plugin.springsecurity.interceptUrlMap.put('/ssb/directDeposit/**',['ROLE_SELFSERVICE-ACTIONITEMADMIN_BAN_DEFAULT_M'])
        def config = generalSsbConfigService.getGeneralConfig()
        Holders.config.grails.plugin.springsecurity.interceptUrlMap.put('/ssb/directDeposit/**', origUrlConf)

        assertFalse config.isActionItemEnabled
        assertFalse config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertTrue config.isProxyManagementEnabled
        assertEquals(-1, config.proxyManagementUrl)
    }

    @Test
    void testGet8xProxyManagmentUrl() {
        loginSSB 'GDP000002', '111111'

        def personConfigInSession = [(generalSsbConfigService.getCacheName()): [(generalSsbConfigService.ENABLE_PROXY_MANAGMENT): 'N']]
        PersonUtility.setPersonConfigInSession(personConfigInSession)

        def config = generalSsbConfigService.getGeneralConfig()

        assertTrue config.isActionItemEnabled
        assertFalse config.isDirectDepositEnabled
        assertTrue config.isPersonalInformationEnabled
        assertFalse config.isProxyManagementEnabled
        assertEquals 'http://<host_name>:<port_number>/<banner8>/enUS/bwgkprxy.P_ManageProxy', config.proxyManagementUrl
    }
}