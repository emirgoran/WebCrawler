### Required constants you might want to change:
1. in Crawlers.LinksCrawler: MAX_LINKED_WEBSITES (default = 1) - means only one link will be crawled from a webpage
2. in Data.Website: MAX_INNER_WEBSITES_NUM (default = 5) - limits the number of linked websites
3. in Translation.Translator: AUTH_KEY - authentication key to the translation API (use your own or the one provided in the submitted PDF summary)

### The program accepts exactly 3 arguments (in this exact order):
1. URL - a URL to the web page you want to crawl
2. HEADING_DEPTH - represents level of HTML headings (from 1 to 6)
3. LANGUAGE_CODE - an abbreviation of a language (e.g. IT for Italian, DE for German, etc.)

### Supported target languages (dynamically fetched):  
DE: German  
FI: Finnish  
RU: Russian  
BG: Bulgarian  
LT: Lithuanian  
LV: Latvian  
FR: French  
HU: Hungarian  
EN-GB: English (British)  
SK: Slovak  
SL: Slovenian  
PT-PT: Portuguese (European)  
SV: Swedish  
EL: Greek  
IT: Italian  
ES: Spanish  
ZH: Chinese (simplified)  
ET: Estonian  
CS: Czech  
EN-US: English (American)  
JA: Japanese  
PT-BR: Portuguese (Brazilian)  
PL: Polish  
DA: Danish  
RO: Romanian  
NL: Dutch

### Executing the program
To run the program in Java you can execute the Main class with following arguments:
<URL> <HEADING_DEPTH> <LANGUAGE_CODE>

To run the program with Maven you can execute the following:  
mvn exec:java -Dexec.mainClass=Main "-Dexec.args=URL HEADING_DEPTH LANGUAGE_CODE"

