### Required constants you might want to change:
1. in Crawlers.LinksCrawler: MAX_LINKED_WEBSITES (default = 3) - means only this number of links will be crawled from websites (on all depths/levels)
2. in Translation.JsoupTranslatorApi: AUTH_KEY - authentication key to the DeepL translation API (use your own, you will need to register with a credit card for free 500.000 characters/month)

### The program accepts exactly 3 arguments (in this exact order):
1. MAX_HEADINGS_DEPTH - represents level of HTML headings (an integer in range from 1 to 6)
2. MAX_WEBSITES_DEPTH - represents depth of crawling of websites (a positive integer)
3. LANGUAGE_CODE - an abbreviation of a language (e.g. IT for Italian, DE for German, etc.)
4. URLs - a list of URLs to the web pages you want to crawl (space separated)

Example console arguments: 6 2 DE https://www.github.com https://www.github.com

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
<MAX_HEADINGS_DEPTH> <MAX_WEBSITES_DEPTH> <LANGUAGE_CODE> <URL> [URL ...]

To run the program with Maven you can execute the following:  
mvn exec:java -Dexec.mainClass=Main "-Dexec.args=YOUR ARGUMENTS HERE"  
For example:  
mvn exec:java -Dexec.mainClass=Main "-Dexec.args=6 2 DE https://www.github.com https://www.github.com"

After a successful execution the result will be saved in a file with name "summary.md" in the same directory where this README file is located.  
