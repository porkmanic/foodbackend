(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemDialogController', MenuItemDialogController);

    MenuItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MenuItem', 'FoodJoint'];

    function MenuItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MenuItem, FoodJoint) {
        var vm = this;

        vm.menuItem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.foodjoints = FoodJoint.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.menuItem.id !== null) {
                MenuItem.update(vm.menuItem, onSaveSuccess, onSaveError);
            } else {
                MenuItem.save(vm.menuItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('foodininjaApp:menuItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
