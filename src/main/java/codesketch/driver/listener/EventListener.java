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
package codesketch.driver.listener;

import codesketch.driver.Controller;

/**
 * Define the archetype of a devive event listener
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public interface EventListener {

	/**
	 * Callback method, invoked when a device is disconnected from the active
	 * bus.
	 * 
	 * @param controller
	 *            the device {@link Controller}.
	 */
	void onDisconnect(Controller controller);

	/**
	 * Callback method, invoked when an event is triggered on the active bus.
	 * 
	 * @param controller
	 *            the device {@link Controller}
	 * @param event
	 *            the fired {@link Event}.
	 */
	void onEvent(Controller controller, Event event);
	 
}
