(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('MenuItemController', MenuItemController);

    MenuItemController.$inject = ['$scope', '$state', 'DataUtils', 'MenuItem', 'MenuItemSearch'];

    function MenuItemController ($scope, $state, DataUtils, MenuItem, MenuItemSearch) {
        var vm = this;
        
        vm.menuItems = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            MenuItem.query(function(result) {
                vm.menuItems = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            MenuItemSearch.query({query: vm.searchQuery}, function(result) {
                vm.menuItems = result;
            });
        }    }
})();
