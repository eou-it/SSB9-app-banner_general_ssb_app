/*******************************************************************************
 Copyright 2013-2018 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

package banner.general.ssb.app

/**
 * Specifies all of the URL mappings supported by the application.
 */
class UrlMappings {

    static mappings = {

        "/ssb/menu" {
            controller = "selfServiceMenu"
            action = [GET: "data", POST: "create"]
        }

        "/ssb/i18n/$name*.properties"(controller: "i18n", action: "index" )


        "/ssb/resource/$controller" {
            action = [ GET: "list", POST: "create" ]
        }

        "/ssb/resource/$controller/batch" {
            action = [ POST: "processBatch" ]
        }


        "/ssb/resource/$controller/$id?" {
            action = [ GET: "show", PUT: "update", DELETE: "destroy" ]
            constraints {
                id(matches:/[0-9]+/)
            }
        }

        "/ssb/resource/$controller/$type" {
            action = "list"
            constraints {
                type(matches:/[^0-9]+/)
            }
        }

        "/ssb/resource/$controller/$type/batch" {
            action = [ POST: "processBatch" ]
            constraints {
                type(matches:/[^0-9]+/)
            }
        }

        "/ssb/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/login/auth" {
            controller = "login"
            action = "auth"
        }

        "/login/denied" {
            controller = "login"
            action = "denied"
        }

        "/login/ajaxDenied" {
            controller = "hrDashboard"
            action = "denied403"
        }

        "/login/authAjax" {
            controller = "login"
            action = "authAjax"
        }

        "/login/authfail" {
            controller = "login"
            action = "authfail"
        }

        "/login/error" {
            controller = "login"
            action = "error"
        }

        "/logout" {
            controller = "logout"
            action = "index"
        }

        "/logout/customLogout" {
            controller = "logout"
            action = "customLogout"
        }

        "/logout/timeout" {
            controller = "logout"
            action = "timeout"
        }
        
        "/resetPassword/changeExpiredPassword" {
           controller = "resetPassword"
           action = "changeExpiredPassword"
        }        

        "/ssb/$controller/logout" {
            controller = "logout"
            action = "index"
        }

        "/ssb/$controller/logout/timeout" {
            controller = "logout"
            action = "timeout"
        }

        "/"(view:"/index")
        "/index.gsp"(view:"/index")
        "500"(controller: "error", action: "internalServerError")
        "403"(controller: "error", action: "accessForbidden")
        "404"(controller: "error", action: "pageNotFoundError")

        "/login/resetPassword" {
            controller = "login"
            action = "forgotpassword"
        }


        "/resetPassword/validateans" {
            controller = "resetPassword"
            action = "validateAnswer"
        }


        "/resetPassword/resetpin" {
            controller = "resetPassword"
            action = "resetPin"
        }


        "/resetPassword/auth" {
            controller = "login"
            action = "auth"
        }


        "/ssb/resetPassword/auth" {
            controller = "login"
            action = "auth"
        }


        "/resetPassword/recovery" {
            controller = "resetPassword"
            action = "recovery"
        }


        "/resetPassword/validateCode" {
            controller = "resetPassword"
            action = "validateCode"
        }


        "/resetPassword/login/auth" {
            controller = "login"
            action = "auth"
        }


        "/resetPassword/logout/timeout" {
            controller = "logout"
            action = "timeout"
        }

        "/resetPinAction" {
            controller = "general"
            action = "resetPinAction"
        }

        // ------------------- RESTful API end points - BEGIN --------------------
        "/api/configuration/$pluralizedResourceName/$id"(controller:'restfulApi') {
            action = [GET: "show"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }

        "/api/qapi/$pluralizedResourceName"(controller:'restfulApi') {
            action = [GET: "list", POST: "list"]
            parseRequest = false
        }

        "/$mepCode/api/qapi/$pluralizedResourceName"(controller:'restfulApi') {
            action = [GET: "list", POST: "list"]
            parseRequest = false
        }

        "/api/$pluralizedResourceName/$id"(controller:'restfulApi') {
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }

        "/api/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName/$id"( controller: 'restfulApi' ) {
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }

        "/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/qapi/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "list"]
            parseRequest = false
        }

        "/$mepCode/api/$pluralizedResourceName/$id"( controller: 'restfulApi' ) {
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }


        "/$mepCode/api/$pluralizedResourceName"(controller:'restfulApi') {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/$mepCode/api/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/$mepCode/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName/$id"( controller: 'restfulApi' ) {
            action = [GET: "show", PUT: "update", DELETE: "delete"]
            parseRequest = false
            constraints {
                // to constrain the id to numeric, uncomment the following:
                // id matches: /\d+/
            }
        }

        "/$mepCode/api/$parentPluralizedResourceName/$parentId/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "create"]
            parseRequest = false
        }

        "/$mepCode/qapi/$pluralizedResourceName"( controller: 'restfulApi' ) {
            action = [GET: "list", POST: "list"]
            parseRequest = false
        }

        // ------------------- RESTful API end points - END ----------------------
    }
}
