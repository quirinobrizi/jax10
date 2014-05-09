package codesketch.driver.x10.controller;

import codesketch.driver.x10.Address;
import codesketch.driver.x10.actuator.Actuator;
import codesketch.driver.x10.actuator.X10ActuatorDefault;
import codesketch.driver.x10.bus.Device;

public abstract class AbstractX10Controller implements X10Controller {

	private final Device device;

	public AbstractX10Controller(Device device) {
		this.device = device;
	}

	@Override
	public Actuator actuator(Address address) {
		return new X10ActuatorDefault(address, this);
	}

	protected Device getDevice() {
		return device;
	}
}
