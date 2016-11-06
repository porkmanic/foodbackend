(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('food-joint', {
            parent: 'entity',
            url: '/food-joint',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FoodJoints'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/food-joint/food-joints.html',
                    controller: 'FoodJointController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('food-joint-detail', {
            parent: 'entity',
            url: '/food-joint/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FoodJoint'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/food-joint/food-joint-detail.html',
                    controller: 'FoodJointDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FoodJoint', function($stateParams, FoodJoint) {
                    return FoodJoint.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'food-joint',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('food-joint-detail.edit', {
            parent: 'food-joint-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-joint/food-joint-dialog.html',
                    controller: 'FoodJointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FoodJoint', function(FoodJoint) {
                            return FoodJoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('food-joint.new', {
            parent: 'food-joint',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-joint/food-joint-dialog.html',
                    controller: 'FoodJointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                imageUrl: null,
                                servingNumber: null,
                                lastIssuedTicketNum: null,
                                estimatWaitPerPerson: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('food-joint', null, { reload: 'food-joint' });
                }, function() {
                    $state.go('food-joint');
                });
            }]
        })
        .state('food-joint.edit', {
            parent: 'food-joint',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-joint/food-joint-dialog.html',
                    controller: 'FoodJointDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FoodJoint', function(FoodJoint) {
                            return FoodJoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('food-joint', null, { reload: 'food-joint' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('food-joint.delete', {
            parent: 'food-joint',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-joint/food-joint-delete-dialog.html',
                    controller: 'FoodJointDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FoodJoint', function(FoodJoint) {
                            return FoodJoint.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('food-joint', null, { reload: 'food-joint' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
