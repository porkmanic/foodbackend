(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodOrderDialogController', FoodOrderDialogController);

    FoodOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'FoodOrder', 'Payment', 'OrderItem', 'Ticket'];

    function FoodOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, FoodOrder, Payment, OrderItem, Ticket) {
        var vm = this;

        vm.foodOrder = entity;
        vm.clear = clear;
        vm.save = save;
        vm.payments = Payment.query({filter: 'foodorder-is-null'});
        $q.all([vm.foodOrder.$promise, vm.payments.$promise]).then(function() {
            if (!vm.foodOrder.payment || !vm.foodOrder.payment.id) {
                return $q.reject();
            }
            return Payment.get({id : vm.foodOrder.payment.id}).$promise;
        }).then(function(payment) {
            vm.payments.push(payment);
        });
        vm.orderitems = OrderItem.query();
        vm.tickets = Ticket.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.foodOrder.id !== null) {
                FoodOrder.update(vm.foodOrder, onSaveSuccess, onSaveError);
            } else {
                FoodOrder.save(vm.foodOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodininjaApp:foodOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
