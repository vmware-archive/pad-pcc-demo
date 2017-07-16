## Caching Data with Pivotal Cloud Cache (PCC)
This guide walks through the process of using PCC to cache certain results to speed up your frontend app.

![IMG_002](https://github.com/Pivotal-Field-Engineering/pad-pcc-demo/blob/master/images/IMG_002.png) 
  
#### What you will build
You will build a service that requests customer information from a CloudFoundry hosted Customer Search service and caches them in PCC. You will then see that fetching the same customer information again eliminates the expensive call to retrieve customer information from MySQL.

The Customer Search service has the following APIs:

`- GET /api/showcache`          - get all customer info in PCC  
`- GET /api/clearcache`         - remove all customer info in PCC  
`- GET /api/showdb`  	- get all customer info in MySQL  
`- GET /api/cleardb`         - remove all customer info in MySQL  
`- GET /api/loaddb`         - load 500 customer info into MySQL  
`- GET /api/customerSearch?email={email}`   - get specific customer info  
  
  
#### What you will need
- About 30 minutes
- A favorite text editor or IDE
- JDK 1.8 or later
- Maven 3.0+
- You can also import the code straight into your IDE: 
Spring Tool Suite (STS) / IntelliJ IDEA
  
  
![IMG_001](https://github.com/Pivotal-Field-Engineering/pad-pcc-demo/blob/master/images/IMG_001.png)



#### TO DO
- ADD CQ and Client Subscriptions
