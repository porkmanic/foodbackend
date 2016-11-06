(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('PaymentDetailController', PaymentDetailController);

    PaymentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Payment', 'FoodOrder'];

    function PaymentDetailController($scope, $rootScope, $stateParams, previousState, entity, Payment, FoodOrder) {
        var vm = this;

        vm.payment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('foodininjaApp:paymentUpdate', function(event, result) {
            vm.payment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
