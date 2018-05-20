package com.agileengine.test.xmlanalizer.comparators;

import java.util.HashMap;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;

import com.agileengine.test.xmlanalizer.XmlAnalyzer.CandidateElement;

public class MatcherManager {

    private static final String DEFAULT = "default";

    private HashMap<String, AttributeMatcher> matchers;

    public MatcherManager() {
        matchers = new HashMap<>();
        matchers.put(DEFAULT, new EqualsMatcher());
    }

    public void evaluate(CandidateElement element, Attributes attr) {
        int attrSize = attr.size();
        int matches = 0;
        double score = 0;
        for (Attribute attribute : attr) {
            String attKey = attribute.getKey();
            AttributeMatcher matcher = get(attKey);
            if (matcher != null) {
                double matchScore = matcher.match(attribute.getValue(), element.getNode().attr(attKey));
                score += matchScore;
                if (matchScore > 0) {
                    matches++;
                }
            }
        }
        score += (double) matches / (double) attrSize;
        element.setScore(score);
    }

    public void add(AttributeMatcher matcher) {
        matchers.put(matcher.getIdentifier(), matcher);
    }

    public AttributeMatcher get(String matcherIdentifier) {
        AttributeMatcher matcher = matchers.get(matcherIdentifier);
        if (matcher == null) {
            return matchers.get(DEFAULT);
        }
        return matcher;
    }
}
