package no.javazone.submit.integrations.sleepingpill.model.common;

import java.util.List;

public class StringListDataField {

    public boolean privateData;
    public List<String> value;

    @Override
    public String toString() {
        return "StringListDataField{" +
                "privateData=" + privateData +
                ", value=" + value +
                '}';
    }
}
