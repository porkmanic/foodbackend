(function() {
    'use strict';

    angular
        .module('foodininjaApp')
        .factory('FoodJointSearch', FoodJointSearch);

    FoodJointSearch.$inject = ['$resource'];

    function FoodJointSearch($resource) {
        var resourceUrl =  'api/_search/food-joints/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
