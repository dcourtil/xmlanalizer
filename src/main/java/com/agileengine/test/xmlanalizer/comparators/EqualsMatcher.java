package com.agileengine.test.xmlanalizer.comparators;

public class EqualsMatcher implements AttributeMatcher {

    private static final String DEFAULT = "default";

    @Override
    public double match(String att1Value, String att2Value) {
        return att1Value.equals(att2Value) ? 1 : 0;
    }

    @Override
    public String getIdentifier() {
        return DEFAULT;
    }

}
