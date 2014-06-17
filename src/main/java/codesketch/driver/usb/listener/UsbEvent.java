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
package codesketch.driver.usb.listener;

import java.util.Arrays;

import codesketch.driver.listener.Event;

/**
 * An {@link Event} on the USB bus.
 * 
 * @author Quirino Brizi (quirino.brizi@gmail.com)
 * 
 */
public class UsbEvent implements Event {

	private final Boolean error;
	private final Exception exception;
	private final byte[] data;

	private UsbEvent(Boolean error, Exception exception, byte[] data) {
		this.error = error;
		this.exception = exception;
		this.data = data;
	}

	/* (non-Javadoc)
	 * @see codesketch.driver.listener.Event#isError()
	 */
	@Override
	public Boolean isError() {
		return error;
	}

	/* (non-Javadoc)
	 * @see codesketch.driver.listener.Event#getException()
	 */
	@Override
	public Exception getException() {
		return exception;
	}

	/* (non-Javadoc)
	 * @see codesketch.driver.listener.Event#getData()
	 */
	@Override
	public byte[] getData() {
		return data;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("UsbEvent [error=").append(error).append(", exception=").append(exception).append(", data=")
		        .append(Arrays.toString(data)).append("]");
		return builder.toString();
	}

	public static UsbEvent success(byte[] data) {
		return new UsbEvent(false, null, data);
	}

	public static UsbEvent failure(Exception exception) {
		return new UsbEvent(true, exception, null);
	}
}
