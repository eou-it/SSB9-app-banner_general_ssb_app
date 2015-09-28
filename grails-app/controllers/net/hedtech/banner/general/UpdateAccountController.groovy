package net.hedtech.banner.general

import grails.converters.JSON
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.i18n.LocalizeUtil
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.core.context.SecurityContextHolder

class UpdateAccountController {

    def directDepositAccountService
    
    def createAccount() {
        def model = [:]
        def map = request?.JSON ?: params
        map.pidm = SecurityContextHolder?.context?.authentication?.principal?.pidm
        
        // default values for a new Direct Deposit account
        map.status = 'P'
        map.documentType = 'D'
        map.intlAchTransactionIndicator = 'N'
        
        log.debug("trying to create acct: "+ map.bankAccountNum)
        
        try {
            render directDepositAccountService.create(map) as JSON

        } catch (ApplicationException e) {
            render returnFailureMessage(e) as JSON
        }
    }
    
    def getBankInfo() {
        def model = [:]
        def map = request?.JSON ?: params
        
        log.debug("trying to fetch bank: "+ map.bankRoutingNum)
        
        try {
            render directDepositAccountService.validateRoutingNumber(map.bankRoutingNum)[0] as JSON

        } catch (ApplicationException e) {
            render returnFailureMessage(e) as JSON
        }
    }
    
    def validateAccountNum() {
        def model = [:]
        def map = request?.JSON ?: params
        
        log.debug("validating acct num: "+ map.bankAccountNum)
        
        try {
            model.failure = false
            directDepositAccountService.validateAccountNumFormat(map.bankAccountNum)
            render model as JSON

        } catch (ApplicationException e) {
            render returnFailureMessage(e) as JSON
        }
    }
    
    def  returnFailureMessage(ApplicationException  e) {
        def model = [:]
        model.failure = true
        log.error(e)
        try {
            model.message = e.returnMap({ mapToLocalize -> new ValidationTagLib().message(mapToLocalize) }).message
            return model
        } catch (ApplicationException ex) {
            log.error(ex)
            model.message = e.message
            return model
        }
    }

}
