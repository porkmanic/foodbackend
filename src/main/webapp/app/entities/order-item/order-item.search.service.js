(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .factory('OrderItemSearch', OrderItemSearch);

    OrderItemSearch.$inject = ['$resource'];

    function OrderItemSearch($resource) {
        var resourceUrl =  'api/_search/order-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
