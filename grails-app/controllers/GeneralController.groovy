/*******************************************************************************
 Copyright 2015-2018 Ellucian Company L.P. and its affiliates.
 ****************************************************************************** */


import grails.converters.JSON
import net.hedtech.banner.exceptions.ApplicationException
import net.hedtech.banner.general.overall.UserRoleService
import org.apache.log4j.Logger
import org.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.security.core.context.SecurityContextHolder

/**
 * Controller for General
 */
class GeneralController {

    private static final logfile = Logger.getLogger( this.getClass() )
    static defaultAction = "landingPage"

    def generalSsbConfigService
    def userRoleService


    def landingPage() {
        try {

            def p_proxyIDM = SecurityContextHolder?.context?.authentication?.principal?.gidm

            if(p_proxyIDM){
                forward controller: 'proxy', action: 'landingPage'
            }
            else {
                render view: "general"
            }
        } catch (ApplicationException e) {
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
        def model = [:]
        model.failure = true
        logfile.error( e )
        try {
            model.message = e.returnMap( {mapToLocalize -> new ValidationTagLib().message( mapToLocalize )} ).message
            return model
        } catch (ApplicationException ex) {
            logfile.error( ex )
            model.message = e.message
            return model
        }
    }


    def denied403() {
        render( status: 403 )
    }


}
