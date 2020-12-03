/*******************************************************************************
 Copyright 2013-2020 Ellucian Company L.P. and its affiliates.
 *******************************************************************************/

//import net.hedtech.banner.configuration.ApplicationConfigurationUtils as ConfigFinder
//import grails.plugin.springsecurity.SecurityConfigType

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

grails.resources.adhoc.excludes = ['/**/*-custom.css', '**/plugins/ckeditor-4.4.1.0/**']
grails.resources.mappers.bundle.excludes = ['**/plugins/ckeditor-4.4.1.0/**']
grails.resources.mappers.hashandcache.excludes = ['**/plugins/ckeditor-4.4.1.0/**']
//grails.config.locations = [] // leave this initialized to an empty list, and add your locations in the map below.
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '*.png', '/plugins/*']


grails.config.locations = [
        BANNER_APP_CONFIG           : "banner_configuration.groovy",
        BANNER_GENERAL_SSB_CONFIG   : "BannerGeneralSsb_configuration.groovy"
        //WEB_APP_EXTENSIBILITY_CONFIG: "WebAppExtensibilityConfig.class"
]

grails.databinding.useSpringBinder=true
/// ******************************************************************************
//
//                       +++ BUILD NUMBER SEQUENCE UUID +++
//
// ******************************************************************************
//
// A UUID corresponding to this project, which is used by the build number generator.
// Since the build number generator web service provides build number sequences to
// multiple projects, and each project uses a unique UUID to identify which number
// sequence it is using.
//
// This number should NOT be changed.
// FYI: When a new UUID is needed (e.g., for a new project), use this URI:
//      http://maldevl2.sungardhe.com:8080/BuildNumberServer/newUUID
//
// DO NOT EDIT THIS UUID UNLESS YOU ARE AUTHORIZED TO DO SO AND KNOW WHAT YOU ARE DOING
//
build.number.uuid = "7f8235d8-2a51-4f2f-8516-47d913caf346" // specific UUID for general ssb solution
build.number.base.url = "http://m037169:8081/BuildNumberServer/buildNumber?method=getNextBuildNumber&uuid="
app.name="GeneralSelfService"
app.appId="GENERAL_SS"
app.platform.version="9.34.1"
contextSecurityEnabled = true

defaultResponseHeadersMap = [
    "X-Content-Type-Options": "nosniff",
    "X-XSS-Protection": "1; mode=block"
]

endpoints.enabled= false
management.contextPath= '/actuator'
management.security.enabled=false
endpoints.health.enabled=true
management.endpoint.health.'show-details'= 'always'
management.health.diskspace.threshold=1073741824  //1GB

server.'contextPath' = '/BannerGeneralSsb'
spring.jmx.enabled=false


grails.project.groupId = "net.hedtech" // used when deploying to a maven repo
grails.databinding.useSpringBinder = true
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [
        html         : ['text/html', 'application/xhtml+xml'],
        xml          : ['text/xml', 'application/xml', 'application/vnd.sungardhe.student.v0.01+xml'],
        text         : 'text/plain',
        js           : 'text/javascript',
        rss          : 'application/rss+xml',
        atom         : 'application/atom+xml',
        css          : 'text/css',
        csv          : 'text/csv',
        all          : '*/*',
        json         : ['application/json', 'text/json'],
        form         : 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data',
        jpg: 'image/jpeg',
        png: 'image/png',
        gif: 'image/gif',
        bmp: 'image/bmp',
        svg:'image/svg+xml',
        svgz:'image/svg+xml'
]

// The default codec used to encode data with ${}
grails.views.default.codec = "html" // none, html, base64  **** note: Setting this to html will ensure html is escaped, to prevent XSS attack ****
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
grails.plugin.springsecurity.logout.afterLogoutUrl = "/"
grails.plugin.springsecurity.logout.mepErrorLogoutUrl='/logout/logoutPage'
grails.converters.domain.include.version = true
//grails.converters.json.date = "default"

grails.converters.json.pretty.print = true
grails.converters.json.default.deep = true

// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = false

// enable GSP preprocessing: replace head -> g:captureHead, title -> g:captureTitle, meta -> g:captureMeta, body -> g:captureBody
grails.views.gsp.sitemesh.preprocess = true
//grails.resources.rewrite.css = false
grails.resources.adhoc.excludes = ['/**/*-custom.css']

grails.controllers.upload.maxFileSize=10000000000000
grails.controllers.upload.maxRequestSize=10000000000000

environments {
    development {
        grails.resources.debug = true
    }
}


// ******************************************************************************
//
//                       +++ DATA ORIGIN CONFIGURATION +++
//
// ******************************************************************************
// This field is a Banner standard, along with 'lastModifiedBy' and lastModified.
// These properties are populated automatically before an entity is inserted or updated
// within the database. The lastModifiedBy uses the username of the logged in user,
// the lastModified uses the current timestamp, and the dataOrigin uses the value
// specified here:
dataOrigin = "Banner"

// ******************************************************************************
//
//                       +++ FORM-CONTROLLER MAP +++
//
// ******************************************************************************
// This map relates controllers to the Banner forms that it replaces.  This map
// supports 1:1 and 1:M (where a controller supports the functionality of more than
// one Banner form.  This map is critical, as it is used by the security framework to
// set appropriate Banner security role(s) on a database connection. For example, if a
// logged in user navigates to the 'medicalInformation' controller, when a database
// connection is attained and the user has the necessary role, the role is enabled
// for that user and Banner object.

formControllerMap = [
        'selfservicemenu'           : ['SELFSERVICE', 'GUAGMNU'],
        'survey'                    : ['SELFSERVICE'],
        'uploadproperties'          : ['SELFSERVICE'],
        'useragreement'             : ['SELFSERVICE'],
        'securityqa'                : ['SELFSERVICE'],
        'general'                   : ['SELFSERVICE'],
        'proxy'                     : ['SELFSERVICE'],
        'proxymanagement'           : ['SELFSERVICE-STUDENT', 'SELFSERVICE-EMPLOYEE'],
        'globalproxy'               : ['SELFSERVICE'],
        'theme'                     : ['SELFSERVICE'],
        'themeeditor'               : ['SELFSERVICE'],
        'directdeposit'             : ['SELFSERVICE-STUDENT', 'SELFSERVICE-EMPLOYEE'],
        'personalinformation'       : ['SELFSERVICE'],
        'updateaccount'             : ['SELFSERVICE-STUDENT', 'SELFSERVICE-EMPLOYEE'],
        'accountlisting'            : ['SELFSERVICE-STUDENT', 'SELFSERVICE-EMPLOYEE'],
        'directdepositconfiguration': ['SELFSERVICE-STUDENT', 'SELFSERVICE-EMPLOYEE'],
        'personalinformationdetails': ['SELFSERVICE'],
        'personalinformationpicture': ['SELFSERVICE'],
        'jobsub-pending-print'      : ['API-JOBSUB-PRINT'],
        'personalinformationqa'     : ['SELFSERVICE'],
        //AIP//
        'aip'                       : ['SELFSERVICE'],
        'aipdocumentmanagement'     : ['SELFSERVICE'],
        'aipadmin'                  : ['SELFSERVICE-ACTIONITEMADMIN'],
        'aipreview'                 : ['SELFSERVICE-ACTIONITEMREVIEWER'],
        'aipactionitemposting'      : ['SELFSERVICE-ACTIONITEMADMIN'],
        'aippagebuilder'            : ['SELFSERVICE'],
        'bcm'                       : ['SELFSERVICE-ACTIONITEMADMIN',
                                       'SELFSERVICE-COMMUNICATIONUSER',
                                       'SELFSERVICE-COMMUNICATIONCONTENTADMIN',
                                       'SELFSERVICE-COMMUNICATIONADMIN' ],
        'about'                     : ['GUAGMNU'],
        'restfulapi'                : ['SELFSERVICE', 'GPBADMN'],
        'keepalive'                 : ['SELFSERVICE'],
        'dateconverter'             : ['SELFSERVICE', 'GUAGMNU'],
        'menu'                      : ['SELFSERVICE', 'GUAGMNU'],
        'answersurvey'              : ['SELFSERVICE'],


        //PAGEBUILDER///////
        'virtualdomaincomposer'     : ['GPBADMN'],
        'cssmanager'                : ['GPBADMN'],
        'visualpagemodelcomposer'   : ['GPBADMN'],
        'cssrender'                 : ['SELFSERVICE', 'GUAGMNU'],
        'custompage'                : ['SELFSERVICE', 'GPBADMN'],
        'userpreference'            : ['SELFSERVICE'],
        'shortcut'                  : ['SELFSERVICE'],
        'login'                     : ['SELFSERVICE'],
        'logout'                    : ['SELFSERVICE']

]



grails.plugin.springsecurity.logout.afterLogoutUrl = "/"


grails.plugin.springsecurity.securityConfigType = grails.plugin.springsecurity.SecurityConfigType.Requestmap
grails.plugin.springsecurity.cas.active = false
grails.plugin.springsecurity.saml.active = false

pageBuilder.adminRoles = 'ROLE_GPBADMN_BAN_DEFAULT_PAGEBUILDER_M'

// ******************************************************************************
//
//                       +++ INTERCEPT-URL MAP +++
//
// ******************************************************************************

grails.plugin.springsecurity.interceptUrlMap = [
        [pattern:'/ssb/proxy/**' ,                      access: ['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M', 'ROLE_SELFSERVICE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALUMNI_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', 'IS_AUTHENTICATED_FULLY','ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/proxy/proxyAction/**',          access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/proxy/submitActionPassword/**', access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/proxy/resetPinAction/**',       access: ['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/proxy/**' ,                     access: ['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M', 'ROLE_SELFSERVICE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALUMNI_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', 'IS_AUTHENTICATED_FULLY','ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/proxyManagement/**',            access: ['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M', 'ROLE_SELFSERVICE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALUMNI_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', 'IS_AUTHENTICATED_FULLY','ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/general/**' ,                   access: ['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M', 'ROLE_SELFSERVICE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALUMNI_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', 'IS_AUTHENTICATED_FULLY','ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/directDeposit/**',              access: ['ROLE_SELFSERVICE-EMPLOYEE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/ssb/UpdateAccount/**',              access: ['ROLE_SELFSERVICE-EMPLOYEE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/ssb/accountListing/**',             access: ['ROLE_SELFSERVICE-EMPLOYEE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/ssb/DirectDepositConfiguration/**', access: ['ROLE_SELFSERVICE-EMPLOYEE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/ssb/personalInformation/**',        access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/PersonalInformationDetails/**', access: ['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M', 'ROLE_SELFSERVICE_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALUMNI_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', 'IS_AUTHENTICATED_FULLY','ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/PersonalInformationPicture/**', access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/PersonalInformationQA/**',      access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/answerSurvey/**',               access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],

        [pattern:'/ssb/aipAdmin/**',                   access: ['ROLE_SELFSERVICE-ACTIONITEMADMIN_BAN_DEFAULT_M']],
        [pattern:'/ssb/aipReview/**',                  access: ['ROLE_SELFSERVICE-ACTIONITEMREVIEWER_BAN_DEFAULT_M']],
        [pattern:'/ssb/BCM/**',                        access: ['ROLE_SELFSERVICE-ACTIONITEMADMIN_BAN_DEFAULT_M']],
        [pattern:'/ssb/aipActionItemPosting/**',       access: ['ROLE_SELFSERVICE-ACTIONITEMADMIN_BAN_DEFAULT_M']],
        [pattern:'/ssb/aip/**',                        access : ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/aipDocumentManagement/**',      access : ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/aipPageBuilder/**' ,            access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],

        [pattern: '/internalPb/virtualDomains.*/**' ,access  : ['IS_AUTHENTICATED_ANONYMOUSLY']],

        //Page Builder master template included to allow for users to pass in without needing role applied in requestmap table in extz app.
        [pattern:'/customPage/page/AIPMasterTemplateSystemRequired/**' , access : ['IS_AUTHENTICATED_FULLY']],
        //For now use a page builder dummy page for cas aut
        [pattern:'/customPage/page/pbadm.ssoauth/**' , access: ['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M', pageBuilder.adminRoles]],

        [pattern:'/selfServiceMenu/**',                 access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/selfServiceMenu/**',             access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/menu**',                         access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/about/**',                       access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/generalSsbApp/**',                   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/keepAlive/data**',               access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/dateConverter/**',                   access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/dateConverter/**',               access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/',									access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/login/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/index**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/logout/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/logout/**',						access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/logout/redirect',				access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern: '/assets/**',                         access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/resetPassword/**',					access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern: '/static/js/**',                      access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern: '/static/css/**',                     access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/js/**',								access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/css/**',								access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/images/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/plugins/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/errors/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/help/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/static/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ext/**',								access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/i18n/**',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/api/**',								access:['ROLE_DETERMINED_DYNAMICALLY']],
        [pattern:'/qapi/**',							access:['ROLE_DETERMINED_DYNAMICALLY']],
        [pattern:'/ssb/survey/**',		                access:['ROLE_SELFSERVICE-ALLROLES_BAN_DEFAULT_M']],
        [pattern:'/ssb/userAgreement/**',				access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/securityQA/**',					access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/selfServiceMenu/**',			    access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/common/**',						access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/menu/**',						access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/menu',							access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/classListApp/**',				access:['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-CLASSLISTADMINISTRATOR_BAN_DEFAULT_M']],
        [pattern:'/ssb/classListApp/terms',			    access:['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-CLASSLISTADMINISTRATOR_BAN_DEFAULT_M']],
        [pattern:'/ssb/dropRoster/**',					access:['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-DROPROSTERADMINISTRATOR_BAN_DEFAULT_M']],
        [pattern:'/ssb/theme/**',						access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/shortcut/**',                    access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/themeEditor/**',				    access:['ROLE_SELFSERVICE-WTAILORADMIN_BAN_DEFAULT_M']],
        [pattern:'/ssb/keepAlive/data**',				access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/userPreference/**',				access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern:'/ssb/studentAttendanceTracking/**',	access:['ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/ssb/studentCommonDashboard/**',		access:['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M','ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']],
        [pattern:'/actuator/health/**',access:['IS_AUTHENTICATED_ANONYMOUSLY']],
        [pattern: '/**',								access:['ROLE_SELFSERVICE-FACULTY_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-CLASSLISTADMINISTRATOR_BAN_DEFAULT_M', 'ROLE_SELFSERVICE-STUDENT_BAN_DEFAULT_M']]
]



dataSource {
    // configClass = GrailsAnnotationConfiguration.class
    dialect = "org.hibernate.dialect.Oracle10gDialect"
    loggingSql = false
}

hibernate {
    cache.provider_class = 'net.sf.ehcache.hibernate.EhCacheProvider'
    cache.region_prefix = ''
    //net.sf.ehcache.configurationResourceName = 'financeSSBAppEhCache.xml'
    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
    show_sql = false
    dialect = "org.hibernate.dialect.Oracle10gDialect"
    packagesToScan="net.hedtech"
    config.location = [
            "classpath:hibernate-banner-core.cfg.xml",
            "classpath:hibernate-banner-general-person.cfg.xml",
            "classpath:hibernate-banner-general-validation-common.cfg.xml",
            "classpath:hibernate-banner-general-common.cfg.xml",
            "classpath:hibernate-banner-aip-gate-keeper.cfg.xml",
            "classpath:hibernate-banner-aip.cfg.xml",
            "classpath:hibernate-banner-general-utility.cfg.xml",
            "classpath:hibernate-banner-sspb.cfg.xml",
            "classpath:hibernate-restful-api.cfg.xml"
    ]
}

