package codesketch.driver;

public interface Actuator {

	Boolean on();

	Boolean off();

	Boolean dim(int amount);

	Boolean allLightsOn();

	Boolean allLightsOff();

	Boolean allUnitsOff();

    String status();
}
