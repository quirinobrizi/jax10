package codesketch.x10.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.Address;
import codesketch.x10.Command;
import codesketch.x10.Function;
import codesketch.x10.bus.Device;
import codesketch.x10.controller.AbstractUsbX10Controller;

public class CM15 extends AbstractUsbX10Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(CM15.class);

    public CM15(Device device) {
        super(device);
        // this.verifyAndSetTime();
    }

    @Override
    public short vendorId() {
        return 0x0BC7;
    }

    @Override
    public short productId() {
        return 0x0001;
    }

    @Override
	public void execute(Command command) {
		List<byte[]> payload = command.toBytePayload();
		for (byte[] chunk : payload) {
			this.write(chunk);
            sleepSilently();
		}

    }

    private void sleepSilently() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * codesketch.x10.controller.X10Controller#command(codesketch.x10.Function,
	 * codesketch.x10.Address, java.lang.Integer)
	 */
	@Override
	public Command command(Function function, Address address, Integer dimAmount) {
		return new CM15Command(function, address, dimAmount);
	}

    @Override
    public boolean ack() {
		try {
			byte[] bytes = this.read(1);
			if (bytes.length < 1) {
				return false;
			}
			return (bytes[0] == CM15Command.Protocol.ACK.code());
		} catch (Exception e) {
			return false;
		}
    }

    @Override
    protected byte readEndpoint() {
		return CM15Command.Protocol.READ_ENDPOINT.code();
    }

    @Override
    protected byte writeEndpoint() {
		return CM15Command.Protocol.WRITE_ENDPOINT.code();
    }

	private void verifyAndSetTime() {
		try {
			byte[] response = this.read(1);
			if (response[0] == 0xA5) {
				LOGGER.info("setting controller time");
				Calendar calendar = Calendar.getInstance();
				byte[] sequence = new byte[8];
				sequence[0] = (byte) 0x9b;
				sequence[1] = (byte) calendar.get(Calendar.SECOND);
				sequence[2] = (byte) (calendar.get(Calendar.MINUTE) + 60 * (calendar.get(Calendar.HOUR) & 1));
				sequence[3] = (byte) (calendar.get(Calendar.HOUR) >> 1);
				sequence[4] = (byte) calendar.get(Calendar.DAY_OF_YEAR);
				sequence[5] = (byte) (1 << calendar.get(Calendar.DAY_OF_WEEK));
				if((calendar.get(Calendar.DAY_OF_WEEK) & 0x100) == 1) {
					sequence[5] |= 0x80;
				}
				sequence[6] = 0x60;
				sequence[7] = 0x00;
	
				this.write(sequence);
			}
		} catch (Exception e) {
			LOGGER.info("unable to set or verify controller time");
		}
	}

	public static final class CM15Command implements Command {

		private final Function function;
		private final Address address;
		private final Integer dimAmount;

		public CM15Command(Function function, Address address, Integer dimAmount) {
			this.function = function;
			this.address = address;
			this.dimAmount = dimAmount;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see codesketch.x10.Command#toBytePayload()
		 */
		@Override
		public List<byte[]> toBytePayload() {
			List<byte[]> payload = new ArrayList<byte[]>();
			if (this.hasValidAddress()) {
				if (!this.isAGroupFunction()) {
					payload.add(this.defineSelectByteSequence(getHouseEncodingMap(), getUnitEncodingMap()));
				}
				payload.add(this.defineFunctionByteSequence(getHouseEncodingMap()));
			}
			return payload;
		}

		private boolean hasValidAddress() {
			return null != this.address && this.address.isValid();
		}

		private boolean isAGroupFunction() {
			return Arrays.asList(Function.ALL_LIGHTS_OFF, Function.ALL_LIGHTS_ON, Function.ALL_UNITS_OFF).contains(this.function);
		}

        private boolean isDimOrBrightFunction() {
            return Arrays.asList(Function.BRIGHT, Function.DIM).contains(this.function);
        }

		private byte[] defineSelectByteSequence(Map<String, Byte> houses, Map<String, Byte> units) {
			byte house = (byte) (houses.get(address.getHouse()) << 4);
			byte unit = units.get(address.getUnit());
			return new byte[] { Protocol.ADDRESS.code(), (byte) (house + unit) };
		}

		private byte[] defineFunctionByteSequence(Map<String, Byte> houses) {
			byte operation = (byte) (houses.get(this.address.getHouse()) << 4);
			operation += function.nibble();
            if (isDimOrBrightFunction()) {
                byte amount = (byte) (this.dimAmount * 2);
                return new byte[] { Protocol.FUNCTION.code(), operation, amount };
            } else {
                return new byte[] { Protocol.FUNCTION.code(), operation };
            }
		}

		private static enum Protocol {

			WRITE_ENDPOINT((byte) 0x02), READ_ENDPOINT((byte) 0x81), ACK((byte) 0x55), FUNCTION((byte) 0x06), ADDRESS((byte) 0x04);

			private final byte code;

			private Protocol(byte code) {
				this.code = code;
			}

			public byte code() {
				return this.code;
			}
		}

		private Map<String, Byte> getHouseEncodingMap() {
			Map<String, Byte> encodingMap = new HashMap<String, Byte>();
			encodingMap.put("A", (byte) 0x6);
			encodingMap.put("B", (byte) 0xE);
			encodingMap.put("C", (byte) 0x2);
			encodingMap.put("D", (byte) 0xA);
			encodingMap.put("E", (byte) 0x1);
			encodingMap.put("F", (byte) 0x9);
			encodingMap.put("G", (byte) 0x5);
			encodingMap.put("H", (byte) 0xD);
			encodingMap.put("I", (byte) 0x7);
			encodingMap.put("J", (byte) 0xF);
			encodingMap.put("K", (byte) 0x3);
			encodingMap.put("L", (byte) 0xB);
			encodingMap.put("M", (byte) 0x0);
			encodingMap.put("N", (byte) 0x8);
			encodingMap.put("O", (byte) 0x4);
			encodingMap.put("P", (byte) 0xC);
			return encodingMap;
		}

		private Map<String, Byte> getUnitEncodingMap() {
			Map<String, Byte> encodingMap = new HashMap<String, Byte>();
			encodingMap.put("1", (byte) 0x6);
			encodingMap.put("2", (byte) 0xE);
			encodingMap.put("3", (byte) 0x2);
			encodingMap.put("4", (byte) 0xA);
			encodingMap.put("5", (byte) 0x1);
			encodingMap.put("6", (byte) 0x9);
			encodingMap.put("7", (byte) 0x5);
			encodingMap.put("8", (byte) 0xD);
			encodingMap.put("9", (byte) 0x7);
			encodingMap.put("10", (byte) 0xF);
			encodingMap.put("11", (byte) 0x3);
			encodingMap.put("12", (byte) 0xB);
			encodingMap.put("13", (byte) 0x0);
			encodingMap.put("14", (byte) 0x8);
			encodingMap.put("15", (byte) 0x4);
			encodingMap.put("16", (byte) 0xC);
			return encodingMap;
		}
	}

}
