(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ticket', {
            parent: 'entity',
            url: '/ticket',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Tickets'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ticket/tickets.html',
                    controller: 'TicketController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('ticket-detail', {
            parent: 'entity',
            url: '/ticket/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Ticket'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ticket/ticket-detail.html',
                    controller: 'TicketDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Ticket', function($stateParams, Ticket) {
                    return Ticket.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ticket',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ticket-detail.edit', {
            parent: 'ticket-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ticket/ticket-dialog.html',
                    controller: 'TicketDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ticket', function(Ticket) {
                            return Ticket.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ticket.new', {
            parent: 'ticket',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ticket/ticket-dialog.html',
                    controller: 'TicketDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                qrCode: null,
                                qrCodeContentType: null,
                                status: null,
                                createTime: null,
                                estimateTime: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ticket', null, { reload: 'ticket' });
                }, function() {
                    $state.go('ticket');
                });
            }]
        })
        .state('ticket.edit', {
            parent: 'ticket',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ticket/ticket-dialog.html',
                    controller: 'TicketDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ticket', function(Ticket) {
                            return Ticket.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ticket', null, { reload: 'ticket' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ticket.delete', {
            parent: 'ticket',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ticket/ticket-delete-dialog.html',
                    controller: 'TicketDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ticket', function(Ticket) {
                            return Ticket.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ticket', null, { reload: 'ticket' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
