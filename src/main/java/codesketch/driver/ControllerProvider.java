package codesketch.driver;

import java.util.List;

import codesketch.driver.x10.bus.BusEventListener;
import codesketch.driver.x10.bus.Definition;

public interface ControllerProvider {

	/**
	 * Scan the connected bus in order to create a controller for all the known
	 * connected devices.
	 * 
	 * @return a list of controllers on per known connected device.
	 */
	<C extends Controller> List<C> provideAllAvailableControllers();

	/**
	 * Scan the connected bus for the requested device definition, and create a
	 * controller if a device is found.
	 * 
	 * @param definition
	 *            search criteria for the device to lookup
	 * @return a controller for the device if found.
	 */
	<C extends Controller> C provideControllerBy(Definition definition);

	/**
	 * Register a listener for event that may occur on the connected bus.
	 * 
	 * @param busEventListener
	 *            the event listener implementation
	 */
	public void registerEventListener(BusEventListener busEventListener);
}
