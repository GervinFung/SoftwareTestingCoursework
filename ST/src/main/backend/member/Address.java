package main.backend.member;

public class Address {

    // create immutable object to reduce bug occurrence
    private final String state, district, area, streetName;
    private final int postalCode, houseNumber;

    public Address(final String data) {
        final String[] dataPartitioned = data.split(",");
        final boolean stateCorrect = "melaka".equalsIgnoreCase(dataPartitioned[0]) || "malacca".equalsIgnoreCase(dataPartitioned[0]);
        if (!stateCorrect) {
            throw new IllegalArgumentException("Only State of Melaka/Malacca allowed");
        }
        this.state = dataPartitioned[0];
        this.district = dataPartitioned[1];
        this.area = dataPartitioned[2];
        this.postalCode = Integer.parseInt(dataPartitioned[3]);
        this.streetName = dataPartitioned[4];
        this.houseNumber = Integer.parseInt(dataPartitioned[5]);
    }

    public Address(final String state, final String district, final String area, final String streetName, final int postalCode, final int houseNumber) {
        final boolean stateCorrect = "melaka".equalsIgnoreCase(state) || "malacca".equalsIgnoreCase(state);
        if (!stateCorrect) {
            throw new IllegalArgumentException("Only State of Melaka/Malacca allowed");
        }
        this.state = state;
        this.district = district;
        this.area = area;
        this.postalCode = postalCode;
        this.streetName = streetName;
        this.houseNumber = houseNumber;
    }

    public String addressFormatToFile() {
        return this.state + "," + this.district + "," + this.area + "," + this.postalCode + "," + this.streetName + "," + this.houseNumber;
    }

    public String getArea() { return this.area; }

    @Override
    public String toString() {
        return "\nAddress\n" + "-".repeat(40)
                + "\nState        :"  + this.state
                + "\nDistrict     :" + this.district
                + "\nArea         :" + this.area
                + "\nPostal Code  :" + this.postalCode
                + "\nStreet Name  :" + this.streetName
                + "\nHouse Number :" + this.houseNumber;
    }
}