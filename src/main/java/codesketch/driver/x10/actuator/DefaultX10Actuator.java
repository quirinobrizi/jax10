package codesketch.driver.x10.actuator;

import codesketch.driver.Address;
import codesketch.driver.Command;
import codesketch.driver.x10.Function;
import codesketch.driver.x10.controller.X10Controller;
import codesketch.driver.x10.Utils;

public class DefaultX10Actuator extends AbstractActuator {

	public DefaultX10Actuator(Address address, X10Controller controller) {
		super(address, controller);
	}

	@Override
	public Boolean on() {
		X10Controller controller = getController();
		Command command = controller.command(Function.ON, getAddress(), null);
		return controller.execute(command);
	}

	@Override
	public Boolean off() {
		X10Controller controller = getController();
		Command command = controller.command(Function.OFF, getAddress(), null);
		return controller.execute(command);
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
		return controller.execute(command);
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
		return controller.execute(command);
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
		return controller.execute(command);
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
		return controller.execute(command);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see codesketch.x10.actuator.Actuator#allUnitsOn()
	 */
	@Override
	public String status() {
		X10Controller controller = getController();
		Command command = controller.command(Function.STATUS, null, null);
		controller.execute(command);
		byte[] bytes = controller.read(16);
		return Utils.formatHexToString(bytes);
	}

}
