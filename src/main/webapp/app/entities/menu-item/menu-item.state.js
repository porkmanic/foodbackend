(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('menu-item', {
            parent: 'entity',
            url: '/menu-item',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MenuItems'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/menu-item/menu-items.html',
                    controller: 'MenuItemController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('menu-item-detail', {
            parent: 'entity',
            url: '/menu-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MenuItem'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/menu-item/menu-item-detail.html',
                    controller: 'MenuItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MenuItem', function($stateParams, MenuItem) {
                    return MenuItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'menu-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('menu-item-detail.edit', {
            parent: 'menu-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-item/menu-item-dialog.html',
                    controller: 'MenuItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MenuItem', function(MenuItem) {
                            return MenuItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('menu-item.new', {
            parent: 'menu-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-item/menu-item-dialog.html',
                    controller: 'MenuItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                price: null,
                                image: null,
                                imageContentType: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('menu-item', null, { reload: 'menu-item' });
                }, function() {
                    $state.go('menu-item');
                });
            }]
        })
        .state('menu-item.edit', {
            parent: 'menu-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-item/menu-item-dialog.html',
                    controller: 'MenuItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MenuItem', function(MenuItem) {
                            return MenuItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('menu-item', null, { reload: 'menu-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('menu-item.delete', {
            parent: 'menu-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/menu-item/menu-item-delete-dialog.html',
                    controller: 'MenuItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MenuItem', function(MenuItem) {
                            return MenuItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('menu-item', null, { reload: 'menu-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
