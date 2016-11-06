'use strict';

describe('Controller Tests', function() {

    describe('MenuItem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMenuItem, MockFoodJoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMenuItem = jasmine.createSpy('MockMenuItem');
            MockFoodJoint = jasmine.createSpy('MockFoodJoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MenuItem': MockMenuItem,
                'FoodJoint': MockFoodJoint
            };
            createController = function() {
                $injector.get('$controller')("MenuItemDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:menuItemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
