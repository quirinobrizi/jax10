/**
 * Copyright [2014] [Quirino Brizi (quirino.brizi@gmail.com)]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package codesketch.driver.x10.bus;

import codesketch.driver.Controller;

/**
 * Define the archetype of a bus event listener
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public interface BusEventListener {

	/**
	 * Triggered when a device is connected to the bus.
	 * 
	 * @param controller
	 *            the controller that allows to execute actions on the connected
	 *            device.
	 */
	public void onConnect(Controller controller);

	/**
	 * Triggered when a device is disconnected from the bus
	 * 
	 * @param controller
	 *            the controller that allows to execute actions on the
	 *            disconnected device.
	 */
	public void onDisconnect(Controller controller);
}
