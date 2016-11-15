(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('TicketController', TicketController);

    TicketController.$inject = ['$scope', '$state', 'DataUtils', 'Ticket', 'TicketSearch'];

    function TicketController ($scope, $state, DataUtils, Ticket, TicketSearch) {
        var vm = this;
        
        vm.tickets = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Ticket.query(function(result) {
                vm.tickets = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TicketSearch.query({query: vm.searchQuery}, function(result) {
                vm.tickets = result;
            });
        }    }
})();
