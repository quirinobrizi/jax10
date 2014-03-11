package codesketch.x10.controllers.impl;

import java.util.HashMap;
import java.util.Map;

import codesketch.x10.Address;
import codesketch.x10.Function;
import codesketch.x10.bus.Device;
import codesketch.x10.controllers.AbstractUsbX10Controller;

public class CM15 extends AbstractUsbX10Controller {

    private static final byte ACK = 0x55;
    private static final byte EXECUTE = 0x06;
    private static final byte SELECT = 0x04;

    public CM15(Device device) {
        super(device);
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
        return encodingMap;
    }

    @Override
    public Map<String, Byte> getUnitEncodingMap() {
        Map<String, Byte> encodingMap = new HashMap<String, Byte>();
        encodingMap.put("1", (byte) 0x6);
        encodingMap.put("2", (byte) 0xE);
        return encodingMap;
    }

    @Override
    public void execute(Function function, Address address) {
        if (address.isValid()) {
            this.selectModule(address);
        }

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        byte operation = this.mapHouse(address.getHouse());
        operation += function.nibble();
        // introduce function

        byte[] sequence = { EXECUTE, operation };
        this.write(sequence);

    }

    @Override
    public boolean ack() {
		// byte[] bytes = this.read(100);
		// if (bytes.length < 1) {
		// return false;
		// }
		// return (bytes[0] == ACK);
		return true;
    }

    @Override
    protected byte readEndpoint() {
        return (byte) 0x81;
    }

    @Override
    protected byte writeEndpoint() {
        return 0x02;
    }

    private void selectModule(Address address) {
        byte house = this.mapHouse(address.getHouse());
        byte unit = this.mapUnit(address.getUnit());
        byte[] sequence = { SELECT, (byte) (house + unit) };
        this.write(sequence);
    }

}
