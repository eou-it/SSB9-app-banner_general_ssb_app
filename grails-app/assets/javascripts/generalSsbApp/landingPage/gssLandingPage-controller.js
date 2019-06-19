/********************************************************************************
  Copyright 2018 Ellucian Company L.P. and its affiliates.
********************************************************************************/
generalSsbAppControllers.controller('gssLandingPageController',['$scope', '$rootScope', '$location', 'generalSsbService', 'piConfigResolve', '$filter', 'generalConfigResolve',
    function ($scope, $rootScope, $location, generalSsbService, piConfigResolve, $filter, generalConfigResolve) {

        // LOCAL VARIABLES
        // ---------------
        var STUDENT = 0,
            EMPLOYEE = 1,
            AIPADMIN =2,


        // LOCAL FUNCTIONS
        // ---------------
            getAppTilesForRole = function(tiles) {
                var tilesForRole = [];

                _.each(tiles, function(tile) {
                    // If roles are specified, filter based on role. If not specified, include tile.
                    if (tile.roles) {
                        if (($scope.isStudent && _.contains(tile.roles, STUDENT)) ||
                            ($scope.isEmployee && _.contains(tile.roles, EMPLOYEE))||
                            ($scope.isAipAdmin && _.contains(tile.roles, AIPADMIN))) {

                            tilesForRole.push(tile);
                        }
                    } else {
                        tilesForRole.push(tile);
                    }
                });

                return tilesForRole;
            },

            init = function() {

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
                            url: $scope.applicationContextRoot +'/ssb/aip/#/list',
                            icon: '../images/action_items_icon.svg'
                        }
                    );
                }

                if(generalConfigResolve.isActionItemEnabled){
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.actionitemadmin.title',
                            desc: 'banner.generalssb.landingpage.actionitemadmin.description',
                            url: $scope.applicationContextRoot +'/ssb/aipAdmin/#/landing',
                            icon: '../images/action_items_icon.svg',
                            roles: [AIPADMIN]
                        }
                    );
                }

                // TODO: remove this temporary shim to set URL once configuration has been updated to deliver the 9.x URL
                generalConfigResolve.proxyManagementUrl = $scope.applicationContextRoot +'/ssb/proxyManagement';
                // END temporary shim
                if(generalConfigResolve.isProxyManagementEnabled && generalConfigResolve.proxyManagementUrl !== -1) {
                    $scope.appTiles.push(
                        {
                            title: 'banner.generalssb.landingpage.proxyMgmt.title',
                            desc: 'banner.generalssb.landingpage.proxyMgmt.description',
                            url: generalConfigResolve.proxyManagementUrl,
                            icon: '../assets/Proxy_management1.png'
                        }
                    );
                }

                generalSsbService.getRoles().$promise.then(function (response) {
                    $scope.isStudent = response.isStudent;
                    $scope.isEmployee = response.isEmployee;
                    $scope.isAipAdmin = response.isAipAdmin;
                    $scope.appTilesForRole = getAppTilesForRole($scope.appTiles);
                    $scope.isSingleTile = $scope.appTilesForRole.length === 1;
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
        $scope.appTilesForRole;
        $scope.isSingleTile;
        $scope.firstName = '';
        $scope.bannerId;


        // INITIALIZE
        // ----------
        init();

    }
]);
