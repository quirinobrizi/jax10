/**
 * Copyright [2014] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package codesketch.driver;

import codesketch.driver.listener.EventListener;
import codesketch.driver.x10.Function;
import codesketch.driver.x10.controller.X10Controller;

/**
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public interface Controller {

	short vendorId();

	short productId();

	/**
	 * Execute a function on the controller. i.e. Turn on a light.
	 * 
	 * @param command
	 *            the command to execute
	 */
	void execute(Command command);

	/**
	 * Open the device and set up the controller.
	 */
	void open();

	/**
	 * Close the controller
	 */
	void close();

	/**
	 * Read from the controller
	 * 
	 * @param lenght
	 *            the amount of byte to read.
	 * @return the read bytes.
	 */
	byte[] read(int lenght);

	/**
	 * Write a sequence off bytes to the controller.
	 * 
	 * @param sequence
	 *            the sequence of byte to have to write.
	 */
	void write(byte[] sequence);

	/**
	 * Verifies whether the controller responded well or not.
	 * 
	 * @return true if the controller responded well false otherwise.
	 */
	boolean ack();

	/**
	 * Get an actuator for a module on this controller.
	 * 
	 * @param address
	 *            the module address
	 * @return the Actuator.
	 */
	Actuator actuator(Address address);

	/**
	 * Generate a command instance that can be used whit this
	 * {@link X10Controller}.
	 * 
	 * @param function
	 *            the protocol function to operate
	 * @param address
	 *            the device address as house-unit code
	 * @param dimAmount
	 *            the dim amount if the requested function is dim.
	 * @return a command
	 */
	Command command(Function function, Address address, Integer dimAmount);

	/**
	 * Attach an event listener to the device controlled by this controller.
	 * 
	 * @param listener
	 *            the {@link EventListener} implementation.
	 */
	void addListener(EventListener listener);
}
