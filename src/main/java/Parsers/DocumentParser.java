package Parsers;

import org.jsoup.nodes.Document;

public interface DocumentParser {
    Document ParseUrl(String URL);
}
