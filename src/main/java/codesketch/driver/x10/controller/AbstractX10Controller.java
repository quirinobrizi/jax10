package codesketch.driver.x10.controller;

import codesketch.driver.Actuator;
import codesketch.driver.Address;
import codesketch.driver.Device;
import codesketch.driver.listener.EventListener;
import codesketch.driver.x10.actuator.DefaultX10Actuator;

public abstract class AbstractX10Controller implements X10Controller {

	private final Device device;

	public AbstractX10Controller(Device device) {
		this.device = device;
	}

	@Override
	public Actuator actuator(Address address) {
		return new DefaultX10Actuator(address, this);
	}

	protected Device getDevice() {
		return device;
	}

	@Override
	public void addListener(EventListener eventListener) {
		this.device.attachListener(eventListener);
	}
}
