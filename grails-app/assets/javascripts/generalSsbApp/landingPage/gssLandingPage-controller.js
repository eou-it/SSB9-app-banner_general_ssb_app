/********************************************************************************
  Copyright 2019-2020 Ellucian Company L.P. and its affiliates.
********************************************************************************/
generalSsbAppControllers.controller('gssLandingPageController',['$scope', '$rootScope', '$location', 'generalSsbService', 'piConfigResolve', '$filter', 'generalConfigResolve',
    function ($scope, $rootScope, $location, generalSsbService, piConfigResolve, $filter, generalConfigResolve) {

        // LOCAL VARIABLES
        // ---------------
        var STUDENT = 0,
            EMPLOYEE = 1,
            AIPADMIN =2,
            FACULTY = 3;


        // LOCAL FUNCTIONS
        // ---------------
            getAppTilesForRole = function(tiles) {
                var tilesForRole = [];

                _.each(tiles, function(tile) {
                    // If roles are specified, filter based on role. If not specified, include tile.
                    if (tile.roles) {
                        if (($scope.isStudent && _.contains(tile.roles, STUDENT)) ||
                            ($scope.isEmployee && _.contains(tile.roles, EMPLOYEE))||
                            ($scope.isAipAdmin && _.contains(tile.roles, AIPADMIN)) ||
                            ($scope.isFaculty && _.contains(tile.roles, FACULTY))) {

                            tilesForRole.push(tile);
                        }
                    } else {
                        tilesForRole.push(tile);
                    }
                });

                return tilesForRole;
            },

            init = function() {
                var preferredNameParams = {pageName: 'Home', sectionName: 'My Profile'};

                $scope.piConfig = piConfigResolve;
                if(generalConfigResolve.isPersonalInformationEnabled) {

                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.personalinfo.title',
                            desc: 'banner.generalssb.landingpage.personalinfo.description',
                            url: $scope.applicationContextRoot +'/ssb/personalInformation',
                            icon: '../assets/personal_info.svg'
                        }
                    );
                }

                if(generalConfigResolve.isDirectDepositEnabled) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.directdeposit.title',
                            desc: 'banner.generalssb.landingpage.directdeposit.description',
                            url: $scope.applicationContextRoot +'/ssb/directDeposit',
                            icon: '../assets/direct_deposit.svg',
                            roles: [STUDENT, EMPLOYEE]
                        }
                    );
                }

                if(generalConfigResolve.isActionItemEnabledAndAvailable) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.actionsitem.title',
                            desc: 'banner.generalssb.landingpage.actionsitem.description',
                            url: $scope.applicationContextRoot +'/ssb/aip',
                            icon: '../assets/action_items_icon.svg'
                        }
                    );
                }

                if(generalConfigResolve.isActionItemEnabled){
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.actionitemadmin.title',
                            desc: 'banner.generalssb.landingpage.actionitemadmin.description',
                            url: $scope.applicationContextRoot +'/ssb/aipAdmin',
                            icon: '../assets/action_items_icon.svg',
                            roles: [AIPADMIN]
                        }
                    );
                }

                if(generalConfigResolve.isProxyManagementEnabled && generalConfigResolve.proxyManagementUrl !== -1) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.proxyMgmt.title',
                            desc: 'banner.generalssb.landingpage.proxyMgmt.description',
                            url:  $scope.applicationContextRoot +'/ssb/proxyManagement',
                            icon: '../assets/Proxy_management1.png'
                        }
                    );
                }


                if(generalConfigResolve.globalProxyUrl !== -1) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.globalProxy.title',
                            desc: 'banner.generalssb.landingpage.globalProxy.description',
                            url:  $scope.applicationContextRoot +'/ssb/globalProxy',
                            icon: '../assets/global_proxy_tile.svg'
                        }
                    );
                }

                if(generalConfigResolve.isCanadaYearEndTaxEnabled && generalConfigResolve.canadaYearEndTaxUrl !== -1){
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.canadaYearEndTax.title',
                            desc: 'banner.generalssb.landingpage.canadaYearEndTax.description',
                            url: generalConfigResolve.canadaYearEndTaxUrl,
                            icon: '../assets/canada_tax_non_payroll_year.svg',
                            roles: [FACULTY, STUDENT]
                        }
                    );
                }

                generalSsbService.getRoles().$promise.then(function (response) {
                    $scope.isStudent = response.isStudent;
                    $scope.isEmployee = response.isEmployee;
                    $scope.isAipAdmin = response.isAipAdmin;
                    $scope.isFaculty = response.isFaculty;
                    $scope.appTilesForRole = getAppTilesForRole($scope.appTiles);
                    $scope.isSingleTile = $scope.appTilesForRole.length === 1;
                });

                // Get user name for display
                generalSsbService.getFromPersonalInfo('PreferredName', preferredNameParams).$promise.then(function (response) {
                    if (response.preferredName) {
                        $scope.landingPageGreetingName = response.preferredName;
                    }
                });

                generalSsbService.getFromPersonalInfo('BannerId').$promise.then(function (response) {
                    $scope.bannerId = response.bannerId;
                });

            };


        // CONTROLLER VARIABLES
        // --------------------
        $scope.piConfig = {};
        $scope.appTiles = [];
        $scope.proxyTiles = [];
        $scope.isStudent;
        $scope.isEmployee;
        $scope.isFaculty;
        $scope.appTilesForRole;
        $scope.isSingleTile;
        $scope.landingPageGreetingName = '';
        $scope.bannerId;


        // INITIALIZE
        // ----------
        init();

    }
]);
