# bitcoin-trends

Insight about trends may lead up to a much more useful desicions and this project provides API to focus on historical trends
as well as it provide strong API to forecast prices for the future.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
* sbt - 1.2.1
* jdk - 1.8

### Installing

1. git clone https://github.com/horizon23/bitcoin-trends.git
2. sbt compile
3. sbt assembly

### Launch application server
```java -cp target/scala-2.11/bitcoin-trends.jar com.trends.bitcoin.server.Api```

* By default it will start application on port: **8089**

To launch the server on specific port:

``` java -cp target/scala-2.11/bitcoin-trends.jar com.trends.bitcoin.server.Api 8088 ```

## Rest API

### Historical Analysis
#### 1. Last week trend [GET]

> /api/v1/last_week

This api call will return the json respone as json array containing price and time for last week:

* response
```json
[
    {
        "price": 6250.88,
        "time": "2018-09-12T00:00:00Z",
        "epochTime": 1536685200000
    },
    {
        "price": 6291.08,
        "time": "2018-09-11T00:00:00Z",
        "epochTime": 1536598800000
    }
]   
```

#### 2. Last month trend [GET]

> /api/v1/last_month

This api call will return the json respone as json array containing price and time for last month:

* response
```json
[
    {
        "price": 6250.88,
        "time": "2018-09-12T00:00:00Z",
        "epochTime": 1536685200000
    },
    {
        "price": 6291.08,
        "time": "2018-09-11T00:00:00Z",
        "epochTime": 1536598800000
    }   
]   
```

#### 3. Price movement by date [POST]

> /api/v1/price_movement_by_date

This api call will return the json respone as json array containing price and time till the date provided by the user:

* request
```json
{"date":"2018-09-08"}
```

* response
```json
[
    {
        "price": 6250.88,
        "time": "2018-09-12T00:00:00Z",
        "epochTime": 1536685200000
    },
    {
        "price": 6291.08,
        "time": "2018-09-11T00:00:00Z",
        "epochTime": 1536598800000
    }   
]   
```

#### 4. Moving/Rolling average between dates [POST]

> /api/v1/moving_avg

This api call will return the json respone as json array containing moving average price and time for the date range
provided by the user:

* request
```json
{
"startDate":"2018-09-01",
"endDate":"2018-09-08",
"period":3
}
```

* response
```json
[
    {
        "movingPrice": 0,
        "time": "2018-09-01T00:00:00Z",
        "epochTime": 1535734800000
    },
    {
        "movingPrice": 0,
        "time": "2018-09-02T00:00:00Z",
        "epochTime": 1535821200000
    }  
]   
```

### Forecasting
Apache Spark is being internally used to perform machine learning algorithms in referrence of calculating model and
predicting the future values.

#### 1. 15 days forecast [GET]

> /api/v1/forecast_15_days

This api call will return the json respone as json array containing forecasted price and time for 15 days:

* response
```json
[
    {
        "date": "2018-09-14",
        "price": 6315.728719800697
    },
    {
        "date": "2018-09-15",
        "price": 6340.165197186838
    } 
]   
```

#### 4. n days forecast [POST]
> /api/v1/forecast

This api call will return the json respone as json array containing forecasted price and time for n days:


* request
```json
{"days": 2}
```

* response
```json
[
    {
        "date": "2018-09-14",
        "price": 6315.728719800697
    },
    {
        "date": "2018-09-15",
        "price": 6340.165197186838
    }
]
```
