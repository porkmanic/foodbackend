(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('FoodJointDialogController', FoodJointDialogController);

    FoodJointDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FoodJoint', 'Ticket', 'MenuItem'];

    function FoodJointDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FoodJoint, Ticket, MenuItem) {
        var vm = this;

        vm.foodJoint = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tickets = Ticket.query();
        vm.menuitems = MenuItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.foodJoint.id !== null) {
                FoodJoint.update(vm.foodJoint, onSaveSuccess, onSaveError);
            } else {
                FoodJoint.save(vm.foodJoint, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodininjaApp:foodJointUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
