package no.javazone.integrations.sleepingpill.model.common;

public class StringDataField {

    public boolean privateData;
    public String value;

    @SuppressWarnings("unused")
    private StringDataField() { }

    public StringDataField(boolean privateData, String value) {
        this.privateData = privateData;
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringDataField{" +
                "privateData=" + privateData +
                ", value='" + value + '\'' +
                '}';
    }
}
