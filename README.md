# Financial Trading System

The project is accessable via http://honghu215.com. It is a market trading simulator, including calculation practices. Traders can buy stocks, bonds and options in the virtual market, and exercise options in their home pages.

## Software Structure 
* ### Back-end(Spring)

	1. ##### Spring Security

		> Spring Security is a powerful and highly customizable authentication and access-control framework. It is the de-facto standard for securing Spring-based applications. The log in moduel is using Spring Security. It provides built-in user login authentication, and automatic login success redirect to different default pages: admin will be redirected to admin home and user will be redirected to user home page.
		
	2. ##### Spring Data

		> Spring Data’s mission is to provide a familiar and consistent, Spring-based programming model for data access while still retaining the special traits of the underlying data store. 
		
	3. #####Spring Data JDBC 
	
		> Spring Data JDBC, part of the larger Spring Data family, makes it easy to implement JDBC based repositories. This module deals with enhanced support for JDBC based data access layers. The system connects to database via JDBC.
		
		Like you define objects in Java, you can just define a model for an object, then Spring Data JDBC will create an entity in the database, and do CRUD operations as well.
		
* ### Front End

	1. #####Thymeleaf

		> Thymeleaf is a modern server-side Java template engine for both web and standalone environments. Thymeleaf's main goal is to bring elegant natural templates to development workflow — HTML that can be correctly displayed in browsers and also work as static prototypes.
		
	2. #####JavaScript

		JavaScript is used in html templates for dynamically redering the web content. 
		
	3. #####Jquery & Ajax

		JQuery is used to get the web content by HTML tags and  manipulate HTML content. Ajax(Asynchronous JavaScript and Xml)  allows web pages to be updated asynchronously by exchanging data with a web server behind the scenes. This means that it is possible to update parts of a web page, without reloading the whole page.
		
* ###Database

	AWS RDS with MySQL provides a remote and reliable database.
	

## Functionality

* ###Sign in/up

	The langind page is sign in/up. New users must sign up to access the content, and they are not allowed to sign up with an already-used email.
	
* ###Admin 

	Administrator is able to delete an existing user, add and delete a stock, a option and a bond.
	
	When adding new stocks, new bonds and new options, admin doesn't input the price for them, because the price of stocks, bonds and options are changing all the time. To simulate the change of price, an event of random number generator is created in the database: the stock_price will change every 5 seconds.
		
	#####Create Event Scheduler for the Change of Price
	```
	    CREATE EVENT updatePrice
	        ON SCHEDULE  
	            EVERY 5 SECOND  
	                DO
	                    UPDATE stock
	                        SET stock_price = round(stock_price + (-1  +  2 * rand() * 5), 2);
	```     

	#####Turn on/off event scheduler  

    ```
    SET GLOBAL EVENT_SCHEDULER = ON;
    SET GLOBAL EVENT_SCHEDULER = OFF;
    ```  
    
    
* ###User

	1. **Home**

		The user home page shows all the options owned by the user, along with button to exercise, as well as bonds the user bought.
		
	2. **Portfolio**

		Portfolio page shows stocks, bonds and options the user owns till now, including quantity of each stock, bond and option and asset as well. 
		
		If it passed maturity date of a bond, then it'll be deleted automatically. 
		
	2. **Market**

		* Display of market derivates.(the content retrieved from database be refreshed every 5 seconds to simulate the change of price)
		* Buy/Sell the derivates.
		* The bond's fair price and the yield rate calculations by bisection method.
		* The option's fair price and volatility caculations by binomial model.
	
	2. **History**

		The display of trading history, including stock, bond and option, along with their corresponding price and quantity. The feature of filtering is also available for all.
