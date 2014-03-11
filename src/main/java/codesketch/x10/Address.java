package codesketch.x10;

public class Address {

    private final String house;
    private final String unit;
    private final Boolean valid;

    private Address(String house, String unit) {
        this.house = house.toUpperCase();
        this.unit = unit;
        this.valid = null != this.house && null != this.unit;
    }

    public String getHouse() {
        return house;
    }

    public String getUnit() {
        return unit;
    }

    public Boolean isValid() {
        return this.valid;
    }

    public static Address forAll() {
        return new Address(null, null);
    }

    public static Address forModule(String house, String unit) {
        return new Address(house, unit);
    }

}
