(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemController', MenuItemController);

    MenuItemController.$inject = ['$scope', '$state', 'MenuItem'];

    function MenuItemController ($scope, $state, MenuItem) {
        var vm = this;
        
        vm.menuItems = [];

        loadAll();

        function loadAll() {
            MenuItem.query(function(result) {
                vm.menuItems = result;
            });
        }
    }
})();
