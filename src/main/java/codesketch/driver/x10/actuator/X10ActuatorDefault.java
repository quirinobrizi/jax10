package codesketch.driver.x10.actuator;

import codesketch.driver.x10.Address;
import codesketch.driver.x10.Command;
import codesketch.driver.x10.Function;
import codesketch.driver.x10.controller.X10Controller;

public class X10ActuatorDefault extends AbstractActuator {

	public X10ActuatorDefault(Address address, X10Controller controller) {
		super(address, controller);
	}

	@Override
	public Boolean on() {
		X10Controller controller = getController();
		Command command = controller.command(Function.ON, getAddress(), null);
		controller.execute(command);
		return controller.ack();
	}

	@Override
	public Boolean off() {
		X10Controller controller = getController();
		Command command = controller.command(Function.OFF, getAddress(), null);
		controller.execute(command);
		return controller.ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#dim(int)
	 */
	@Override
	public Boolean dim(int amount) {
		X10Controller controller = getController();
		Command command = controller.command(Function.DIM, getAddress(), amount);
		controller.execute(command);
		return controller.ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allLightsOn()
	 */
	@Override
	public Boolean allLightsOn() {
		X10Controller controller = getController();
		Command command = controller.command(Function.ALL_LIGHTS_ON, getAddress(), null);
		controller.execute(command);
		return controller.ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allLightsOff()
	 */
	@Override
	public Boolean allLightsOff() {
		X10Controller controller = getController();
		Command command = controller.command(Function.ALL_LIGHTS_OFF, getAddress(), null);
		controller.execute(command);
		return controller.ack();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see codesketch.x10.actuator.Actuator#allUnitsOn()
	 */
	@Override
	public Boolean allUnitsOff() {
		X10Controller controller = getController();
		Command command = controller.command(Function.ALL_UNITS_OFF, getAddress(), null);
		controller.execute(command);
		return controller.ack();
	}

}
