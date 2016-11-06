(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemDeleteController',MenuItemDeleteController);

    MenuItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'MenuItem'];

    function MenuItemDeleteController($uibModalInstance, entity, MenuItem) {
        var vm = this;

        vm.menuItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MenuItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
