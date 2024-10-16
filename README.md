# Booking Management API
This API provides functionalities for managing bookings in a meeting room application. Users can create, retrieve, and cancel bookings for specific meeting rooms. The API is designed to handle various scenarios and includes validation to ensure proper usage.

## Table of Contents
* Features
* API Endpoints
  * Get Bookings
  * Create Booking
  * Cancel Booking
* Data Models
* Error Handling
* Technologies Used
* Setup Instructions

## Features
  * Retrieve Bookings: Get a list of bookings for a specific meeting room on a given date.
  * Create Bookings: Create a new booking for a meeting room with specified time slots.
  * Cancel Bookings: Cancel an existing booking by its unique identifier.
  * Validation: Ensure that bookings do not conflict and are valid (e.g., time intervals, booking existence).
  * Error Handling: Respond with appropriate error messages for various scenarios (e.g., booking not found, room already booked).
  
## API Endpoints
  
### Get Bookings 
* Endpoint: GET /v1/api/bookings


* Description: Retrieve all bookings for a specific meeting room on a specific date.


* Parameters:

  1. roomName (required): The name of the meeting room.
  2. date (required): The date for which bookings are to be retrieved (format: yyyy-MM-dd).
  

* Responses:

  * 200 OK: Returns a list of bookings.

    
    {
    "employeeEmail": "john.doe@acme.com",
    "date": "2024-10-20",
    "timeFrom": "09:00:00",
    "timeTo": "10:00:00"
    }
    

  * 204 No Content: No bookings found for the specified room and date.
### Create Booking
  
* Endpoint: POST /v1/api/bookings

* Description: Create a new booking for a meeting room with a specified time slot.

* Request Body:

    
    {
     "meetingRoomName": "Room B",
     "employeeEmail": "john.doe@acme.com",
     "date": "2024-10-22",
     "timeFrom": "10:00:00",
     "timeTo": "12:00:00"
    }

* Responses:

  * 201 Created: Booking created successfully.
  
      
     {
      "meetingRoomName": "Room B",
      "employeeEmail": "john.doe@acme.com",
      "date": "2024-10-22",
      "timeFrom": "10:00:00",
      "timeTo": "12:00:00"
      }
      
  * 400 Bad Request: Returns error message if the booking cannot be created (e.g., room already booked).
  
### Cancel Booking
  
* Endpoint: DELETE /v1/api/bookings/{id}

* Description: Cancel a specific booking by its ID.

* Path Parameter:

  1. id (required): The ID of the booking to be canceled.
  
* Responses:

  * 200 OK: Booking canceled successfully.

    
    "Booking was canceled successfully!"
    
  
  * 400 Bad Request: Returns error message if the booking is not found or cannot be canceled.
  
## Data Models
  
### Booking

 * Fields:
   * id: Long
   * meetingRoom: MeetingRoom
   * employee: Employee
   * date: LocalDate
   * timeFrom: LocalTime
   * timeTo: LocalTime
   
### MeetingRoom

* Fields:
  * id: Long
  * name: String

### Employee

* Fields:
    * id: Long
    * email: String

### CreateBookingRequestDTO

* Fields:
  * meetingRoomName: String
  * employeeEmail: String
  * date: LocalDate
  * timeFrom: LocalTime
  * timeTo: LocalTime
  
### GetBookingsResponseDTO

* Fields:
  * meetingRoomName: String
  * employeeEmail: String
  * date: LocalDate
  * timeFrom: LocalTime
  * timeTo: LocalTime
  
## Error Handling
  The API provides meaningful error messages in the event of bad requests or conflicts. Below are common error responses:

* 400 Bad Request: Indicates that the request was invalid (e.g., booking conflicts, invalid parameters).


* 404 Not Found: Indicates that a resource was not found (e.g., trying to cancel a non-existing booking).
Example Error Response

    
    "error": "Meeting room not found"
    
## Technologies Used

* Spring Boot: For creating RESTful web services.
* Spring Data JPA: For interacting with the database.
* Postgresql: Relational Database.
* Spock Framework: For writing integration tests in Groovy.
* Swagger: For API documentation.

## Setup Instructions

* Clone the Repository:

        git clone https://github.com/AlexQutz/ACME-booking-API.git
        cd ACME-booking-API

* Have Docker installed in your desktop.

  * Run the Application:
  
        docker compose up --build

Access the API: The API will be available at http://localhost:8080/v1/api/bookings.

Swagger Documentation: Access Swagger UI at http://localhost:8080/swagger-ui.html to explore and test the API endpoints interactively.