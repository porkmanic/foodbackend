(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .factory('FoodOrderSearch', FoodOrderSearch);

    FoodOrderSearch.$inject = ['$resource'];

    function FoodOrderSearch($resource) {
        var resourceUrl =  'api/_search/food-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
