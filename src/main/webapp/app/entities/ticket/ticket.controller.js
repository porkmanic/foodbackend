(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('TicketController', TicketController);

    TicketController.$inject = ['$scope', '$state', 'Ticket'];

    function TicketController ($scope, $state, Ticket) {
        var vm = this;
        
        vm.tickets = [];

        loadAll();

        function loadAll() {
            Ticket.query(function(result) {
                vm.tickets = result;
            });
        }
    }
})();
