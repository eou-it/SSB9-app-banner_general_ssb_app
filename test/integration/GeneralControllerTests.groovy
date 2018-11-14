/********************************************************************************
  Copyright 2018 Ellucian Company L.P. and its affiliates.
 ********************************************************************************/
import grails.converters.JSON
import net.hedtech.banner.exceptions.ApplicationException
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import net.hedtech.banner.testing.BaseIntegrationTestCase

import net.hedtech.banner.general.AccountListingController

class GeneralControllerTests extends BaseIntegrationTestCase {

    /**
     * The setup method will run before all test case method executions start.
     */
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        controller = new GeneralController()
        super.setUp()
    }

    /**
     * The tear down method will run after all test case method execution.
     */
    @After
    public void tearDown() {
        super.tearDown()
        super.logout()
    }


    @Test
    void testLandingPage(){
        loginSSB 'MYE000001', '111111'
        
        controller.request.contentType = "text/json"
        controller.landingPage()
        def dataForNullCheck = controller.response.contentAsString
        assertNotNull dataForNullCheck
    }

    @Test
    void testGetRoles(){
        loginSSB 'MYE000001', '111111'

        controller.request.contentType = "text/json"
        controller.getRoles()
        def dataForNullCheck = controller.response.contentAsString
        def data = JSON.parse(dataForNullCheck)
        assertNotNull data
        assertFalse(data.isStudent)
        assertFalse(data.isEmployee)
        assertFalse(data.isAipAdmin)
    }

    @Test
    void testGetGeneralConfig(){
        loginSSB 'GDP000005', '111111'

        controller.request.contentType = "text/json"
        controller.getGeneralConfig()
        def dataForNullCheck = controller.response.contentAsString
        def data = JSON.parse(dataForNullCheck)
        assertNotNull data
        assertTrue(data.isDirectDepositEnabled)
        assertTrue(data.isPersonalInformationEnabled)
        assertTrue(data.isProxyManagementEnabled)
        assertEquals(-1, data.proxyManagementUrl)
        assertNotNull data.isActionItemEnabled
    }

    @Test
    void testDenied403(){
        loginSSB 'GDP000005', '111111'

        controller.request.contentType = "text/json"
        controller.denied403()
        def dataForNullCheck = controller.response.contentAsString
        assertNotNull dataForNullCheck
    }

    @Test
    void testReturnFailureMessage(){
        loginSSB 'MYE000001', '111111'

        controller.request.contentType = "text/json"
        def data = controller.returnFailureMessage(new ApplicationException('entityClassOrName', 'some exception message'))
        assertNotNull data
        assertTrue(data.failure)
        assertEquals('some exception message', '' + data.message)
    }


}
