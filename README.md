# supplier

## Exposed end points:

### 1. POST:  http://localhost:8082/supplier/v1/book :


It takes as input check-in date, hotel name, number of guest and return a booking reference and a booking status (which can be CONFIRMED or CANCELED)


### Sample Request:


{

    "bookingId": "e1e050c4-be96-4c58-bc78-480449f0247e",
    "checkInDate": "2021-05-10 15:08:01",
    "checkOutDate": "2021-05-12 15:08:01",
    "hotelName": "Paasha",
    "noOfGuests": 4,
    "status": "",
    "bookingReference": "",
    "createdOn": "2021-01-03 11:31:52",
    "updatedOn": "2021-01-03 11:31:52"

}

### Sample Response

{

    "httpStatus": 200,
    "bookings": [
        {
            "bookingId": "e1e050c4-be96-4c58-bc78-480449f0247e",
            "checkInDate": "2021-05-10 15:08:01",
            "checkOutDate": "2021-05-12 15:08:01",
            "hotelName": "Paasha",
            "noOfGuests": 4,
            "status": "CONFIRMED",
            "bookingReference": "1420a6fc-e402-46b1-8e8c-b7c9ce96c5d1",
            "createdOn": "2021-01-03 11:31:52",
            "updatedOn": "2021-01-03 11:31:52"
        }
    ],
    "errorMessage": null

}

### 2. GET:  http://localhost:8082/supplier/v1/status/{booking_reference} :


It takes a booking reference and return check-in and check-out dates and a status (which can be CONFIRMED or CANCELED)


### Sample Request :   http://localhost:8082/supplier/v1/status/299d63f8-6110-48a8-8a10-60b08cf9eb3f 

### Sample response:

{

    "httpStatus": 200,
    "bookingInfo": {
        "bookingId": null,
        "checkInDate": "2021-02-09 15:08:01",
        "checkOutDate": "2021-02-15 15:08:01",
        "hotelName": null,
        "noOfGuests": 0,
        "status": "CONFIRMED",
        "bookingReference": "299d63f8-6110-48a8-8a10-60b08cf9eb3f",
        "createdOn": null,
        "updatedOn": null
    },
    "errorMessage": null

}
    