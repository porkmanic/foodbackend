(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('TicketDialogController', TicketDialogController);

    TicketDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Ticket', 'FoodOrder', 'User', 'FoodJoint'];

    function TicketDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Ticket, FoodOrder, User, FoodJoint) {
        var vm = this;

        vm.ticket = entity;
        vm.clear = clear;
        vm.save = save;
        vm.foodorders = FoodOrder.query({filter: 'ticket-is-null'});
        $q.all([vm.ticket.$promise, vm.foodorders.$promise]).then(function() {
            if (!vm.ticket.foodOrder || !vm.ticket.foodOrder.id) {
                return $q.reject();
            }
            return FoodOrder.get({id : vm.ticket.foodOrder.id}).$promise;
        }).then(function(foodOrder) {
            vm.foodorders.push(foodOrder);
        });
        vm.users = User.query();
        vm.foodjoints = FoodJoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ticket.id !== null) {
                Ticket.update(vm.ticket, onSaveSuccess, onSaveError);
            } else {
                Ticket.save(vm.ticket, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodininjaApp:ticketUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
