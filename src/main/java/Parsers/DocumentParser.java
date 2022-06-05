package Parsers;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface DocumentParser {
    Document ParseUrl(String URL) throws IOException;
}
