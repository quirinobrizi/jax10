package codesketch.x10.controllers.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import codesketch.x10.Address;
import codesketch.x10.Function;
import codesketch.x10.bus.Device;
import codesketch.x10.controllers.AbstractUsbX10Controller;

public class CM15 extends AbstractUsbX10Controller {

	private static final Logger LOGGER = LoggerFactory.getLogger(CM15.class);

    public CM15(Device device) {
        super(device);
		this.verifyAndSetTime();
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
    public Map<String, Byte> getHouseEncodingMap() {
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

    @Override
    public Map<String, Byte> getUnitEncodingMap() {
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

    @Override
    public void execute(Function function, Address address) {
        if (address.isValid()) {
			byte[] functionByteSequence = defineFunctionByteSequence(function, address);
			this.write(functionByteSequence);
        }

    }

    @Override
    public boolean ack() {
		byte[] bytes = this.read(1);
		if (bytes.length < 1) {
			return false;
		}
		return (bytes[0] == Protocol.ACK.code());
    }

    @Override
    protected byte readEndpoint() {
		return Protocol.READ_ENDPOINT.code();
    }

    @Override
    protected byte writeEndpoint() {
		return Protocol.WRITE_ENDPOINT.code();
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

	// private byte[] defineSelectModuleByteSequence(Address address) {
	// byte house = (byte) (this.mapHouse(address.getHouse()) << 4);
	// byte unit = this.mapUnit(address.getUnit());
	// return new byte[] { Protocol.ADDRESS.symbol(), (byte) (house + unit) };
	// }

	private byte[] defineFunctionByteSequence(Function function, Address address) {
		byte operation = (byte) (this.mapHouse(address.getHouse()) << 4);
		operation += function.nibble();
		return new byte[] { Protocol.FUNCTION.code(), operation };
	}

	public static enum Protocol {

		WRITE_ENDPOINT((byte) 0x02), READ_ENDPOINT((byte) 0x81), ACK((byte) 0x55), FUNCTION((byte) 0x06), ADDRESS((byte) 0x04);

		private byte code;

		private Protocol(byte code) {
			this.code = code;
		}

		public byte code() {
			return this.code;
		}
	}

}
