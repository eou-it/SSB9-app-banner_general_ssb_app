/********************************************************************************
  Copyright 2018-2020 Ellucian Company L.P. and its affiliates.
 ********************************************************************************/
import grails.converters.JSON
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.util.GrailsWebMockUtil
import grails.util.Holders
import grails.web.servlet.context.GrailsWebApplicationContext
import net.hedtech.banner.exceptions.ApplicationException
import org.grails.plugins.testing.GrailsMockHttpServletRequest
import org.grails.plugins.testing.GrailsMockHttpServletResponse
import org.grails.web.servlet.mvc.GrailsWebRequest
import org.junit.After
import org.junit.Before
import org.junit.Test
import net.hedtech.banner.testing.BaseIntegrationTestCase

@Integration
@Rollback
class GeneralControllerTests extends BaseIntegrationTestCase {

    def controller

    public GrailsWebRequest mockRequest() {
        GrailsMockHttpServletRequest mockRequest = new GrailsMockHttpServletRequest();
        GrailsMockHttpServletResponse mockResponse = new GrailsMockHttpServletResponse();
        GrailsWebMockUtil.bindMockWebRequest(webAppCtx, mockRequest, mockResponse)
    }

    /**
     * The setup method will run before all test case method executions start.
     */
    @Before
    public void setUp() {
        formContext = ['SELFSERVICE']
        super.setUp()
        webAppCtx = new GrailsWebApplicationContext()
        controller = Holders.grailsApplication.getMainContext().getBean("banner.general.ssb.app.GeneralController")
        mockRequest()
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
        SSBSetUp ('HOP510001', '111111')

        controller.request.contentType = "text/json"
        controller.landingPage()
        def dataForNullCheck = controller.response.contentAsString
        assertNotNull dataForNullCheck
    }

    @Test
    void testGetRoles(){
        SSBSetUp ('HOF00714', '111111')

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
        SSBSetUp( 'GDP000005', '111111')
        controller.request.contentType = "text/json"
        controller.getGeneralConfig()
        def dataForNullCheck = controller.response.contentAsString
        def data = JSON.parse(dataForNullCheck)
        assertNotNull data
        assertTrue(data.isDirectDepositEnabled)
        assertTrue(data.isPersonalInformationEnabled)
        assertTrue(data.isProxyManagementEnabled)
        assertFalse (data.isCanadaYearEndTaxEnabled)
        assertEquals(-1, data.proxyManagementUrl)
        assertNotNull data.isActionItemEnabled
    }

    @Test
    void testDenied403(){
        SSBSetUp( 'GDP000005', '111111')

        controller.request.contentType = "text/json"
        controller.denied403()
        def dataForNullCheck = controller.response.contentAsString
        assertNotNull dataForNullCheck
    }

    @Test
    void testReturnFailureMessage(){
        SSBSetUp ('HOP510001', '111111')

        controller.request.contentType = "text/json"
        def data = controller.returnFailureMessage(new ApplicationException('entityClassOrName', 'some exception message'))
        assertNotNull data
        assertTrue(data.failure)
        assertEquals('some exception message', '' + data.message)
    }


}
