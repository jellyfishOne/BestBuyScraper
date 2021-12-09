# BestBuyScraper

Tracks if a product on Bestbuy.com is on sale. 

### How to run on eclipse

* The application saves product details to a MongoDB repository. For details on how to install and run MongoDB locally you can find instructions [here](https://docs.mongodb.com/manual/administration/install-community/).
* Download code.
* Open eclipse and select a directory for a workspace.
* File -> Import -> Maven -> Existing Maven Projects -> Browse... -> select dowloaded project folder -> make sure Projects: pom.xml is selected -> Finish
* Right click pom.xml -> Maven -> Update Project
* Start ScraperApplication.Java 
* Open web browser to [localhost:8080/scraper-api](http://localhost:8080/scraper-api)
