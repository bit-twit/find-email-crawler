FIND-EMAIL-CRAWLER (FEC)
========================

1) RUN

2) TODO

## 1) RUN ##
* change properties in src/main/resources/fec.properties
	* you can choose to run single-threaded (+webdriver) / or multi-threaded (only tested with Apache HTTP client)
* for webdriver run:
	*  install xvfb so you can run firefox headless with webdriver
		* on Linux, apt-get install xvfb
	* sudo Xvfb :10 -ac ;starts Xvfb
	* in another window do 
		* export DISPLAY :10
		* java -jar fec-1.0-SNAPSHOT-jar-with-dependencies.jar

## 2) TODO ##

* + check http client to add timeout, for sites that do not load
* + parse all sites and display results
* +  display results in xlsx
* + add crawl logic, to parse all pages of a site with a specified depth
* +  add custom logic for sites that do not match
	* + added contact url parse and follow
	* + added meta http refresh parse and follow
	* + added frames parse and follow, still doesn't work properly
		* - after first frame follow, the email rules should get the content from each frame and parse that
		* - need to add an extra isFrameSite param for that, when true, email rules parse on frame content, not just page content
* + add threads
	* + work fine now, only with HttpClient, I added timeouts for lazy sites
* - make selenium webdriver work with multiple threads

