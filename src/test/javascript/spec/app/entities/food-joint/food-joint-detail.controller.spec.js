'use strict';

describe('Controller Tests', function() {

    describe('FoodJoint Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFoodJoint, MockTicket, MockMenuItem;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFoodJoint = jasmine.createSpy('MockFoodJoint');
            MockTicket = jasmine.createSpy('MockTicket');
            MockMenuItem = jasmine.createSpy('MockMenuItem');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'FoodJoint': MockFoodJoint,
                'Ticket': MockTicket,
                'MenuItem': MockMenuItem
            };
            createController = function() {
                $injector.get('$controller')("FoodJointDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:foodJointUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
