/*******************************************************************************
Copyright 2015-2017 Ellucian Company L.P. and its affiliates.
*******************************************************************************/

modules = {
    /* Override UI Bootstrap 0.10.0 to use version 0.13.3
     * Override AngularUI Router 0.2.10 to use version 0.2.15 */
    overrides {
        'angularApp' {
            resource id:[plugin: 'banner-ui-ss',file: 'js/angular/ui-bootstrap-tpls-0.10.0.min.js'], url: [file: 'js/angular/ui-bootstrap-tpls-0.13.3.min.js']
            resource id:[plugin: 'banner-ui-ss',file: 'js/angular/angular-ui-router.min.js'], url: [file: 'js/angular/angular-ui-router.min.js']
        }
    }

    'angular' {
        resource url:[file: 'js/angular/angular-route.min.js']
    }

    'bootstrapLTR' {
        dependsOn "jquery"
        defaultBundle environment == "development" ? false : "bootstrap"

        resource url:[plugin: 'banner-ui-ss', file: 'bootstrap/css/bootstrap.css'], attrs: [media: 'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/bootstrap-fixes.css'], attrs: [media: 'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'bootstrap/js/bootstrap.js']
    }

    'bootstrapRTL' {
        dependsOn "jquery"
        defaultBundle environment == "development" ? false : "bootstrap"

        resource url:[plugin: 'banner-ui-ss', file: 'bootstrap/css/bootstrap-rtl.css'], attrs: [media: 'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'css/bootstrap-fixes-rtl.css'], attrs: [media: 'screen, projection']
        resource url:[plugin: 'banner-ui-ss', file: 'bootstrap/js/bootstrap.js']
    }

   'generalSsbApp' {
       dependsOn "angular,glyphicons"

       defaultBundle environment == "development" ? false : "generalSsbApp"

       //Main configuration file
       resource url: [file: 'generalSsbApp/app.js']

       // Services
       resource url:[file: 'generalSsbApp/ddListing/ddListing-service.js']
       resource url:[file: 'generalSsbApp/common/services/breadcrumb-service.js']
       resource url:[file: 'generalSsbApp/common/services/notificationcenter-service.js']
       resource url:[file: 'generalSsbApp/common/services/directDeposit-service.js']
       resource url:[file: 'generalSsbApp/ddEditAccount/ddEditAccount-service.js']
       resource url:[file: 'generalSsbApp/ddEditAccount/ddAccountDirty-service.js']


       // Controllers
       resource url:[file: 'generalSsbApp/ddListing/ddListing-controller.js']
       resource url:[file: 'generalSsbApp/ddEditAccount/ddEditAccount-controller.js']

       // Filters
       resource url:[file: 'generalSsbApp/common/filters/i18n-filter.js']

       // Directives
       resource url:[file: 'generalSsbApp/ddListing/ddListing-directive.js']
       resource url:[file: 'generalSsbApp/ddEditAccount/ddEditAccount-directive.js']
       resource url:[file: 'generalSsbApp/common/directives/enterKey-directive.js']
       resource url:[file: 'generalSsbApp/common/directives/ddPopover-directive.js']

   }
   
   'generalSsbAppLTR' {
      dependsOn "bannerWebLTR, generalSsbApp, bootstrapLTR"
      
       // CSS
       resource url:[file: 'css/main.css'],   attrs: [media: 'screen, projection']
       resource url:[file: 'css/responsive.css'],   attrs: [media: 'screen, projection']
       resource url:[file: 'css/banner-icon-font.css'],   attrs: [media: 'screen, projection']
   }
   
   'generalSsbAppRTL' {
      dependsOn "bannerWebRTL, generalSsbApp, bootstrapRTL"
      
       // CSS
       resource url:[file: 'css/main-rtl.css'],   attrs: [media: 'screen, projection']
       resource url:[file: 'css/responsive-rtl.css'],   attrs: [media: 'screen, projection']
       resource url:[file: 'css/banner-icon-font-rtl.css'],   attrs: [media: 'screen, projection']
   }

}
