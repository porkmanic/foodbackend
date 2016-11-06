'use strict';

describe('Controller Tests', function() {

    describe('Ticket Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockTicket, MockFoodOrder, MockUser, MockFoodJoint;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockTicket = jasmine.createSpy('MockTicket');
            MockFoodOrder = jasmine.createSpy('MockFoodOrder');
            MockUser = jasmine.createSpy('MockUser');
            MockFoodJoint = jasmine.createSpy('MockFoodJoint');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Ticket': MockTicket,
                'FoodOrder': MockFoodOrder,
                'User': MockUser,
                'FoodJoint': MockFoodJoint
            };
            createController = function() {
                $injector.get('$controller')("TicketDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'foodininjaApp:ticketUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
