package codesketch.x10.actuator;

import codesketch.x10.Address;
import codesketch.x10.Command;
import codesketch.x10.Function;
import codesketch.x10.controller.X10Controller;

public class X10ActuatorDefault extends AbstractActuator {

    public X10ActuatorDefault(Address address, X10Controller controller) {
        super(address, controller);
    }

    @Override
    public Boolean on() {
		getController().execute(Command.with(Function.ON, getAddress()));
        return getController().ack();
    }

    @Override
    public Boolean off() {
		getController().execute(Command.with(Function.OFF, getAddress()));
        return getController().ack();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#dim(int)
	 */
	@Override
	public Boolean dim(int amount) {
		X10Controller controller = getController();
		controller.execute(Command.with(Function.DIM, getAddress(), amount));
		return controller.ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allLightsOn()
	 */
	@Override
	public Boolean allLightsOn() {
		getController().execute(Command.with(Function.ALL_LIGHTS_ON, getAddress()));
		return getController().ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allLightsOff()
	 */
	@Override
	public Boolean allLightsOff() {
		getController().execute(Command.with(Function.ALL_LIGHTS_OFF, getAddress()));
		return getController().ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allUnitsOn()
	 */
	@Override
	public Boolean allUnitsOff() {
		getController().execute(Command.with(Function.ALL_UNITS_OFF, getAddress()));
		return getController().ack();
	}

}
