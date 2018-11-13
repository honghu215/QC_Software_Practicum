# Spring + MySQL + Thymeleaf
#### Software Practicum project, go to ![](http://honghu215.com) to check the demo.

* Create Event Scheduler

    ```
    CREATE EVENT updatePrice
        ON SCHEDULE  
            EVERY 5 SECOND  
                DO
                    UPDATE stock
                        SET stock_price = round(stock_price + (-1  +  2 * rand() * 10), 3);
    ```  

* Turn on/off event scheduler  

    ```
    SET GLOBAL EVENT_SCHEDULER = ON;
    SET GLOBAL EVENT_SCHEDULER = OFF;
    ```  

* Delete Event  

    ```
    DELETE EVENT updatePrice
    ```

* Modify Event

    ```
    alter event updatePrice 
        on schedule    
            every 30 second       
                do         
                    update stock           
                    set price = round(price + (-1 + 2 * rand()) * 10, 2);
    ```  

