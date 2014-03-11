package codesketch.x10.actuators;

import codesketch.x10.Address;
import codesketch.x10.Function;
import codesketch.x10.controllers.X10Controller;

public class SwitchableX10Actuator extends AbstractActuator {

    public SwitchableX10Actuator(Address address, X10Controller controller) {
        super(address, controller);
    }

    @Override
    public Boolean on() {
        getController().execute(Function.ON, getAddress());
        return getController().ack();
    }

    @Override
    public Boolean off() {
        getController().execute(Function.OFF, getAddress());
        return getController().ack();
    }

}
