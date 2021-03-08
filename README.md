Project can be setup by calling following the below steps:-

a) mvn clean package

b) java -jar target/upgrade-0.0.1-SNAPSHOT.jar


Project makes use of the h2 database, the console of which can be accessed by the below link:-

http://127.0.0.1:8080/h2-console

Username:-  sa
Password:-  sa


There are four endpoints:-

a) Get All Available Schedules

curl -v -X GET 'http://127.0.0.1:8080/getAvailabilitySchedule' | json_pp

b) Create Reservation

curl -v -X POST 'http://127.0.0.1:8080/createReservation' --header 'Content-Type: application/json' --data-raw '{"firstName" : "anuj", "lastName" : "sharma", "email" : "anuj@gmail.com", "startDate" : "2021-03-26", "durationInDays": 2}'

c) Modify Reservation

curl -v -X POST 'http://127.0.0.1:8080/modifyReservation' --header 'Content-Type: application/json'  --data-raw '{"reservationId" : 3, "startDate" : "2021-03-26", "durationInDays": 2}'

d) Cancel Reservation

curl -v -X POST 'http://127.0.0.1:8080/cancelReservation' --header 'Content-Type: application/json'  --data-raw '{"reservationId" : 3}'

