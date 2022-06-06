package Parsers;

import Data.WebDocument;

import java.io.IOException;

public interface DocumentParser {
    WebDocument ParseUrl(String URL) throws IOException;
}
