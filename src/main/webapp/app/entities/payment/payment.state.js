(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment', {
            parent: 'entity',
            url: '/payment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Payments'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment/payments.html',
                    controller: 'PaymentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('payment-detail', {
            parent: 'entity',
            url: '/payment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Payment'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment/payment-detail.html',
                    controller: 'PaymentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Payment', function($stateParams, Payment) {
                    return Payment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-detail.edit', {
            parent: 'payment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment/payment-dialog.html',
                    controller: 'PaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payment', function(Payment) {
                            return Payment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment.new', {
            parent: 'payment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment/payment-dialog.html',
                    controller: 'PaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                totalPrice: null,
                                status: null,
                                paymentInfo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment', null, { reload: 'payment' });
                }, function() {
                    $state.go('payment');
                });
            }]
        })
        .state('payment.edit', {
            parent: 'payment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment/payment-dialog.html',
                    controller: 'PaymentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Payment', function(Payment) {
                            return Payment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment', null, { reload: 'payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment.delete', {
            parent: 'payment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment/payment-delete-dialog.html',
                    controller: 'PaymentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Payment', function(Payment) {
                            return Payment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment', null, { reload: 'payment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
