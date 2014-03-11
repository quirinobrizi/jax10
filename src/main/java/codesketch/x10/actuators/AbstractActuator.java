package codesketch.x10.actuators;

import codesketch.x10.Address;
import codesketch.x10.controllers.X10Controller;

public abstract class AbstractActuator implements Actuator {

    private final Address address;
    private final X10Controller controller;

    public AbstractActuator(Address address, X10Controller controller) {
        this.address = address;
        this.controller = controller;
    }

    protected Address getAddress() {
        return address;
    }

    protected X10Controller getController() {
        return controller;
    }
}
