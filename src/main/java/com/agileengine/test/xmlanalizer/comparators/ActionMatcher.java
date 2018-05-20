package com.agileengine.test.xmlanalizer.comparators;

import org.apache.commons.lang3.StringUtils;

public class ActionMatcher extends BaseMatcher {

    private static final String ONCLICK = "onclick";
    private static final String JAVASCRIPT_DELIMITER = ":";
    private static final String SENTENCES_DELIMITER = ";";

    @Override
    public String getIdentifier() {
        return ONCLICK;
    }

    @Override
    protected double matchAttributes(String att1Value, String att2Value) {
        String action1 = StringUtils.EMPTY;
        String action2 = StringUtils.EMPTY;
        if (!att1Value.isEmpty() && att1Value.indexOf(JAVASCRIPT_DELIMITER) > 0) {
            action1 = att1Value.substring(att1Value.indexOf(JAVASCRIPT_DELIMITER),
                    att1Value.indexOf(SENTENCES_DELIMITER));
        }
        if (!att2Value.isEmpty() && att2Value.indexOf(JAVASCRIPT_DELIMITER) > 0) {
            action2 =
                att2Value.substring(att2Value.indexOf(JAVASCRIPT_DELIMITER), att2Value.indexOf(SENTENCES_DELIMITER));
        }
        return !action1.isEmpty() && !action2.isEmpty() && action1.equals(action2) ? 1 : 0;
    }



}
