package com.agileengine.test.xmlanalizer;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.agileengine.test.xmlanalizer.comparators.ActionMatcher;
import com.agileengine.test.xmlanalizer.comparators.ClassMatcher;
import com.agileengine.test.xmlanalizer.comparators.MatcherManager;

public class XmlAnalyzer {

    private static String CHARSET_NAME = "utf8";

    private static final int levelsThreshold = 4;

    private MatcherManager matcherManager;

    public XmlAnalyzer(MatcherManager matcher) {
        this.matcherManager = matcher;
    }

    /**
     * @param originalFile
     * @param diffCaseFile
     * @param targetElementId
     */
    public void findBestMatch(File originalFile, File diffCaseFile, String targetElementId) {

        Document originalDoc = parseHtmlFile(originalFile);
        Document diffCaseDoc = parseHtmlFile(diffCaseFile);

        Optional<Element> targetElement = findElementById(originalDoc, targetElementId);

        if (!targetElement.isPresent()) {
            throw new RuntimeException("Target element ID not found.");
        }

        Element target = targetElement.get();

        String targetSelector = getElementSelector(target);

        Element closerParent = findCloseParent(targetSelector, diffCaseDoc);

        Attributes attrs = target.attributes();
        attrs.put("text", target.text());

        Element bestMatch = findMatchingElement(closerParent, attrs);

        System.out.println(getElementSelector(bestMatch));
    }

    /**
     * Finds closest parent taking into account a threshold that indicates the count of levels to go up
     * @param selector
     * @param doc
     * @return
     */
    private Element findCloseParent(String selector, Document doc) {
        if (selector.isEmpty()) {
            return doc.firstElementSibling();
        }
        Elements targetElements = doc.select(selector);
        if (!targetElements.isEmpty() ) {
            Element parent = targetElements.first();
            int parentLevel = 0;
            while (parentLevel < levelsThreshold && parent.hasParent()) {
                parent = parent.parent();
                parentLevel++;
            }
            return parent;
        }
        return findCloseParent(removeLastSelector(selector), doc);
    }

    /**
     * @param root
     * @param targetAttributes
     * @return
     */
    private Element findMatchingElement(Element root, Attributes targetAttributes) {
        LinkedList<Element> nextToVisit = new LinkedList<>();
        HashSet<Element> visited = new HashSet<>();
        CandidateElement bestCandidate = new CandidateElement();
        CandidateElement candidate = null;
        nextToVisit.add(root);
        while (!nextToVisit.isEmpty()) {
            Element node = nextToVisit.remove();
            candidate = new CandidateElement(node);
            candidate.evaluate(matcherManager, targetAttributes);

            if (candidate.compareTo(bestCandidate) > 0) {
                bestCandidate = candidate;
            }

            if (visited.contains(node)) {
                continue;
            }
            visited.add(node);

            for (Element child : node.children()) {
                nextToVisit.add(child);
            }
        }
        return bestCandidate.getNode();
    }


    private String removeLastSelector(String selector) {
        return selector.substring(0, selector.lastIndexOf(">") - 1);
    }

    private String getElementSelector(Node node) {
        Node parent = node.parent();
        if (parent == null) {
            return StringUtils.EMPTY;
        }
        StringBuilder selector = new StringBuilder().append(getElementSelector(parent) + " > " + node.nodeName());
        selector.append(indexOfSelector(parent.childNodes(), node));
        return selector.toString();
    }

    /**
     * Returns the index of the element between the siblings with
     * @param sibilings
     * @param node
     * @return
     */
    private String indexOfSelector(List<Node> children, Node node) {
        if (!children.isEmpty()) {
            List<Node> similarChildren =
                    children.stream().filter(n -> n.nodeName().equals(node.nodeName())).collect(Collectors.toList());
            int index = 0;
            for (Node child : similarChildren) {
                if (!child.equals(node)) {
                    index++;
                } else {
                    break;
                }
            }
            return index == 0 ? StringUtils.EMPTY : ":eq(" + index + ") ";
        }
        return StringUtils.EMPTY;
    }

    private Document parseHtmlFile(File htmlFile) {
        try {
            return Jsoup.parse(htmlFile, CHARSET_NAME, htmlFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Optional<Element> findElementById(Document doc, String targetElementId) {
        return Optional.of(doc.getElementById(targetElementId));
    }

    public static class CandidateElement implements Comparable<CandidateElement> {

        private final Element node;
        private double score = 0;

        public CandidateElement() {
            this.node = new Element("div");
        }

        public CandidateElement(Element node) {
            this.node = node;
        }

        public Element getNode() {
            return node;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }

        public void evaluate(MatcherManager matchers, Attributes attr) {
            matchers.evaluate(this, attr);
        }

        @Override
        public int compareTo(CandidateElement o) {
            double comp = this.getScore() - o.getScore();
            if (comp == 0) {
                return 0;
            } else if (comp > 0) {
                return 1;
            }
            return -1;
        }

    }

    private static void validateArguments(String[] args) {
        if (args.length < 3 || StringUtils.trim(args[0]).isEmpty() || StringUtils.trim(args[1]).isEmpty()
                || StringUtils.trim(args[2]).isEmpty()) {
            throw new IllegalArgumentException(
                    "Missing parameters. Please, provide <input_origin_file_path> <input_other_sample_file_path> <target_element_id>");
        }
    }

    public static void main(String[] args) {
        validateArguments(args);

        MatcherManager matcherManager = new MatcherManager();
        matcherManager.add(new ActionMatcher());
        matcherManager.add(new ClassMatcher());

        XmlAnalyzer xmlAnalizer = new XmlAnalyzer(matcherManager);
        File originalFile = new File(args[0]);
        File diffCaseFile = new File(args[1]);
        String targetElementId = args[2];

        xmlAnalizer.findBestMatch(originalFile, diffCaseFile, targetElementId);
    }
}
