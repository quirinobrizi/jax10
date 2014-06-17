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
package codesketch.driver.listener;

/**
 * Wraps the origin event in order to bubble-up events when fired on the active
 * bus.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public interface Event {

	/**
	 * Make explicit the nature of this {@link Event}, error or not.
	 * 
	 * @return true if this event carries an error, false otherwise.
	 */
	Boolean isError();

	/**
	 * Return the exception generated from the underlying bus, populated if and
	 * only if this {@link Event} is an error.
	 * 
	 * @return the exception generated on the active bus.
	 */
	Exception getException();

	/**
	 * Return the data read from the undrlying bus, populated if and only if
	 * this {@link Event} is not an error.
	 * 
	 * @return the data from teh active bus.
	 */
	byte[] getData();
}
