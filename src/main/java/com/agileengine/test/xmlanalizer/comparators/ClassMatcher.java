package com.agileengine.test.xmlanalizer.comparators;

public class ClassMatcher extends BaseMatcher {

    private static final String CLASS = "class";
    private static final String CLASSES_DELIMITER = " ";

    @Override
    public double matchAttributes(String att1Value, String att2Value) {
        return evaluateValuesSize(att1Value, att2Value, CLASSES_DELIMITER);
    }

    @Override
    public String getIdentifier() {
        return CLASS;
    }
}
