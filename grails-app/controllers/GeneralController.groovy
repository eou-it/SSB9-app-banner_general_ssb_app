/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */


import grails.converters.JSON
import groovy.util.logging.Slf4j
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.UserRoleService

import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Controller for General
 */
@Slf4j
class GeneralController {

    static defaultAction = "landingPage"

    def generalSsbConfigService
    def userRoleService


    def landingPage() {
        try {
            println "general landing page init"
            def p_proxyIDM = SecurityContextHolder?.context?.authentication?.principal?.gidm
            println "p_proxyIDM: ${p_proxyIDM}"
            if(p_proxyIDM){
                forward controller: 'proxy', action: 'landingPage'
            }
            else {
                println " else general"
                render(view: "general")
            }
        } catch (ApplicationException e) {
            println "ApplicationException catch block = ${e}"
            render returnFailureMessage( e ) as JSON
        }
    }

    def getRoles() {
        render userRoleService.getRoles() as JSON
    }

    /**
     * Get General Configuration
     * @return
     */
    def getGeneralConfig() {
        try {
            def model = generalSsbConfigService.getGeneralConfig()
            render model as JSON
        }
        catch (ApplicationException e) {
            render returnFailureMessage( e ) as JSON
        }
    }


    def returnFailureMessage( ApplicationException e ) {
        println "returnFailureMessage method = ${e}"
        def model = [:]
        model.failure = true
        log.error(e)
        try {
            model.message = e.returnMap( {mapToLocalize -> new ValidationTagLib().message( mapToLocalize )} ).message
            return model
        } catch (ApplicationException ex) {
            log.error( ex )
            model.message = e.message
            return model
        }

    }


    def denied403() {
        render( status: 403 )
    }


}
