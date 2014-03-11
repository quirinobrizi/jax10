package codesketch.x10.controllers;

import java.util.Map;

import codesketch.x10.Address;
import codesketch.x10.actuators.Actuator;
import codesketch.x10.actuators.SwitchableX10Actuator;
import codesketch.x10.bus.Device;

public abstract class AbstractX10Controller implements X10Controller {

    private final Device device;

    public AbstractX10Controller(Device device) {
        this.device = device;
        this.device.open();
        this.device.claim();
    }

    @Override
    public Actuator actuator(Address address) {
        return new SwitchableX10Actuator(address, this);
    }

    protected Device getDevice() {
        return device;
    }

    protected byte mapHouse(String house) {
        Map<String, Byte> houseEncodingMap = this.getHouseEncodingMap();
        return (byte) (houseEncodingMap.get(house) << 4);
    }

    protected byte mapUnit(String unit) {
        Map<String, Byte> unitEncodingMap = this.getUnitEncodingMap();
        return unitEncodingMap.get(unit);
    }
}
