package com.agileengine.test.xmlanalizer.comparators;

public interface AttributeMatcher {

    double match(String att1Value, String att2Value);

    String getIdentifier();

}
