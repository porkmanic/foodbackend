(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .controller('PaymentController', PaymentController);

    PaymentController.$inject = ['$scope', '$state', 'Payment', 'PaymentSearch'];

    function PaymentController ($scope, $state, Payment, PaymentSearch) {
        var vm = this;
        
        vm.payments = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Payment.query(function(result) {
                vm.payments = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            PaymentSearch.query({query: vm.searchQuery}, function(result) {
                vm.payments = result;
            });
        }    }
})();
