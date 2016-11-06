(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('food-order', {
            parent: 'entity',
            url: '/food-order',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FoodOrders'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/food-order/food-orders.html',
                    controller: 'FoodOrderController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('food-order-detail', {
            parent: 'entity',
            url: '/food-order/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'FoodOrder'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/food-order/food-order-detail.html',
                    controller: 'FoodOrderDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'FoodOrder', function($stateParams, FoodOrder) {
                    return FoodOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'food-order',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('food-order-detail.edit', {
            parent: 'food-order-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-order/food-order-dialog.html',
                    controller: 'FoodOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FoodOrder', function(FoodOrder) {
                            return FoodOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('food-order.new', {
            parent: 'food-order',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-order/food-order-dialog.html',
                    controller: 'FoodOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                totalPrice: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('food-order', null, { reload: 'food-order' });
                }, function() {
                    $state.go('food-order');
                });
            }]
        })
        .state('food-order.edit', {
            parent: 'food-order',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-order/food-order-dialog.html',
                    controller: 'FoodOrderDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FoodOrder', function(FoodOrder) {
                            return FoodOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('food-order', null, { reload: 'food-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('food-order.delete', {
            parent: 'food-order',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/food-order/food-order-delete-dialog.html',
                    controller: 'FoodOrderDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FoodOrder', function(FoodOrder) {
                            return FoodOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('food-order', null, { reload: 'food-order' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
