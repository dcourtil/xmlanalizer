package com.agileengine.test.xmlanalizer.comparators;

public abstract class BaseMatcher implements AttributeMatcher {

    protected static final EqualsMatcher defaultMatcher = new EqualsMatcher();

    protected abstract double matchAttributes(String att1Value, String att2Value);

    @Override
    public double match(String att1Value, String att2Value) {
        double result = matchAttributes(att1Value, att2Value);
        result += defaultMatcher.match(att1Value, att2Value);
        return result;
    }

    protected double evaluateValuesSize(String att1Value, String att2Value, String delimiter) {
        String[] att1list = att1Value.split(delimiter);
        String[] att2list = att2Value.split(delimiter);

        return att1list.length == att2list.length ? 1 : matchesScore(att1list, att2list);
    }
    
    private int matchesScore(String[] att1list, String[] att2list) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int hashCode() {
        return 31 * 7 + getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        AttributeMatcher matcher = (AttributeMatcher) obj;
        return this.getIdentifier().equals(matcher.getIdentifier());
    }

}
