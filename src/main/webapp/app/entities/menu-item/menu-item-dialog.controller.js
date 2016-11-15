(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemDialogController', MenuItemDialogController);

    MenuItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'MenuItem', 'FoodJoint'];

    function MenuItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, MenuItem, FoodJoint) {
        var vm = this;

        vm.menuItem = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
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


        vm.setImage = function ($file, menuItem) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        menuItem.image = base64Data;
                        menuItem.imageContentType = $file.type;
                    });
                });
            }
        };

    }
})();
