package codesketch.driver.x10.controller;

import codesketch.driver.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractUsbX10Controller extends AbstractX10Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractUsbX10Controller.class);

	public AbstractUsbX10Controller(Device device) {
		super(device);
	}

	@Override
	public void open() {
		Device device = getDevice();
		if (null != device) {
			device.open();
			device.claim();
		}
	}

	@Override
	public void close() {
		Device device = getDevice();
		if (null != device) {
			device.close();
		}
	}

	@Override
	public byte[] read(final int length) {
		return doExecute(new Function() {
			@Override public <T> T execute() {
				return (T) getDevice().read(readEndpoint(), length);
			}
		});
	}

	@Override
	public int write(final byte[] sequence) {
		return doExecute(new Function() {
			@Override public <T> T execute() {
				Integer writtenBytes = getDevice().write(writeEndpoint(), sequence);
				if(writtenBytes != sequence.length) {
					LOGGER.error("written {} byte of {}", writtenBytes, sequence.length);
				}
				return (T) writtenBytes;
			}
		});
	}

	private <T> T doExecute(Function function) {
		try {
			this.open();
			return function.execute();
		} finally {
			this.close();
		}
	}

	protected abstract byte readEndpoint();

	protected abstract byte writeEndpoint();

	private interface Function {
		public <T> T execute();
	}
}
