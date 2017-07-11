/*******************************************************************************
 Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package net.hedtech.banner.general

import grails.converters.JSON
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

import net.hedtech.banner.testing.BaseIntegrationTestCase

import net.hedtech.banner.general.AccountListingController

class AccountListingControllerTests extends BaseIntegrationTestCase {

    /**
     * The setup method will run before all test case method executions start.
     */
    @Before
    public void setUp() {
        formContext = ['GUAGMNU']
        controller = new AccountListingController()
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
    void testGetApAccountsForCurrentUser(){
        loginSSB 'MYE000001', '111111'
        
        controller.request.contentType = "text/json"
        controller.getApAccountsForCurrentUser()
        def dataForNullCheck = controller.response.contentAsString
        def data = JSON.parse( dataForNullCheck )
        println data
        assertNotNull data
    }

    @Test
    void testGetApAccountsForCurrentUserWithInactive(){
        loginSSB 'GDP000001', '111111'

        controller.request.contentType = "text/json"
        controller.getApAccountsForCurrentUser()
        def dataForNullCheck = controller.response.contentAsString
        def data = JSON.parse( dataForNullCheck )
        println data
        assertNotNull data
        assertEquals 3, data.size()
        assertTrue data.bankAccountNum.contains('xxxx4850')
        assertTrue data.bankAccountNum.contains('xxxx4852')
        assertTrue data.bankAccountNum.contains('xxxxx7244')
    }

}
