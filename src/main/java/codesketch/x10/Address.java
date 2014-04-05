package codesketch.x10;

public class Address {

    private final String house;
    private final String unit;
    private final Boolean valid;

    private Address(String house, String unit) {
		if (null != house) {
			this.house = house.toUpperCase();
		} else {
			this.house = null;
		}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((house == null) ? 0 : house.hashCode());
		result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		result = prime * result + ((valid == null) ? 0 : valid.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Address other = (Address) obj;
		if (house == null) {
			if (other.house != null) {
				return false;
			}
		} else if (!house.equals(other.house)) {
			return false;
		}
		if (unit == null) {
			if (other.unit != null) {
				return false;
			}
		} else if (!unit.equals(other.unit)) {
			return false;
		}
		if (valid == null) {
			if (other.valid != null) {
				return false;
			}
		} else if (!valid.equals(other.valid)) {
			return false;
		}
		return true;
	}

}
