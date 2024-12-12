# RESTSECURITY
---

# API Endpoints Documentation
This format includes all endpoints and uses `TOKEN HASH` as a placeholder for the token.
#### Base URL: http://connectlyapi.wintherdev.com/

| **Operation**                  | **Request Details**                                                                                                                                                                                                                                                                                                                                                                                  | **Example Response**                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
|--------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Register User**              | **POST** `/api/auth/register`<br><br>**Body**:<br>{<br>  "email": "admin@example.com",<br>  "password": "adminpass",<br> "firstname":"adminname", <br> "lastname":"adminlastname" }                                                                                                                                                                                                                  | {<br>  "token": "TOKEN HASH",<br>  "username": "admin@example.com",<br> "firstname":"adminname", <br> "lastname":"adminlastname"}                                                                                                                                                                                                                                                                                                                                    |
| **Login User**                 | **POST** `/api/auth/login`<br><br>**Body**:<br>{<br>  "email": "admin@example.com",<br>  "password": "adminpass"<br>}                                                                                                                                                                                                                                                                                | {<br>  "token": "TOKEN HASH",<br>  "username": "admin@example.com"<br>, "firstname":"adminname", <br> "lastname":"adminlastname"}                                                                                                                                                                                                                                                                                                                                    |
| **New Connection Request**     | **POST** `/api/connection/request/new`<br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br><br>**Body**:<br>{<br>  "connection": {<br>    "email": "Coolgutmand2",<br>    "firstName": "auto",<br>    "lastName": "terry"<br>  },<br>  "connectionTypes": []<br>}                                                                                                                           | {<br>  "id": 1,<br>  "requester": {<br>    "email": "user1@example.com",<br>    "firstName": null,<br>    "lastName": null,<br>    "roles": [ "USER" ],<br>    "id": "user1@example.com"<br>  },<br>  "receiver": {<br>    "email": "admin@example.com",<br>    "firstName": null,<br>    "lastName": null,<br>    "roles": [ "USER" ],<br>    "id": "admin@example.com"<br>  },<br>  "types": []<br>}                                                               |
| **Confirm Connection Request** | **PUT** `/api/connection/request/confirm`<br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br><br>**Body**:<br>{<br>  "id": 7,<br>  "receiver": {<br>    "email": "Coolgutmand2",<br>    "firstName": "auto",<br>    "lastName": "terry"<br>  },<br>  "requester": {<br>    "email": "Coolgutmand",<br>    "firstName": "auto",<br>    "lastName": "terry"<br>  },<br>  "types": [ 1 ]<br>} | NOT WORKING ATM                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Test Connection**            | **GET** `/api/connection/test`<br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`                                                                                                                                                                                                                                                                                                             | ONLY FOR TESTING                                                                                                                                                                                                                                                                                                                                                                                                                                                     | |
| **Get Posts (Paged)**          | **GET** `/api/post/posts?visibility=FRIEND&page=1&size=10`<br><br>**Body**:<br>{<br>  "email": "user1@example.com"<br>}                                                                                                                                                                                                                                                                              | NOT WORKING ATM                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Get Posts (Visibility)**     | **GET** `/api/post/posts?visibility=FRIEND`<br><br>**Body**:<br>{<br>  "email": "user1@example.com"<br>}                                                                                                                                                                                                                                                                                             | NOT WORKING ATM                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| **Create Post**                | **POST** `/api/post/create`<br><br>**Body**:<br>{<br>  "author": "user1@example.com",<br>  "title": "test",<br>  "content": "test",<br>  "visibility": "FRIEND"<br>}                                                                                                                                                                                                                                 | { "id": 1, "author": "user1@example.com", "date_created": 1733228437708, "title": "test", "content": "test", "visibility": "FRIEND" }                                                                                                                                                                                                                                                                                                                                |
| **Create Chat**                | **POST** `/api/chat/createChat`<br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br><br>**Body**:<br>{<br> "participants": [<br>"testgutmail@coolmail.dk", <br>"testdamemail4@cooleremail.dk"<br>] <br>}                                                                                                                                                                                    | { "_id": 1,<br>"participants": [<br>"testgutmail@coolmail.dk",<br>"testdamemail4@cooleremail.dk"<br>],<br>"created_at": "Thu Dec 05 12:09:30 CET 2024",<br>"last_message_at": null,<br>"id": 1<br>}                                                                                                                                                                                                                                                                  |
| **Get Chat By Participants**   | **POST** `/api/chat/getChatByParticipants` <br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br><br>**Body**:<br>{<br> "participants": [<br>"testgutmail@coolmail.dk",<br>"testdamemail@cooleremail.dk"<br>] <br>}                                                                                                                                                                          | {<br>"_id": 2,<br>"participants": [ <br>"testgutmail@coolmail.dk",<br>"testdamemail@cooleremail.dk"<br>],<br>"created_at": "Thu Dec 05 16:26:52 CET 2024","last_message_at": null,<br>"id": 2<br>}                                                                                                                                                                                                                                                                   |
| **Get Chat By Id**             | **GET** `/api/chat/getChatById/{id}` <br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br>                                                                                                                                                                                                                                                                                                  | {<br>"_id": 1,<br>"participants": [<br>"testgutmail@coolmail.dk","testdamemail4@cooleremail.dk"<br>],<br>"created_at": "Thu Dec 05 12:09:30 CET 2024",<br>"last_message_at": null,<br>"id": 1 <br>}                                                                                                                                                                                                                                                                  |
| **Send Message**               | **POST** `/api/chat/sendMessage` <br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br><br>**Body**:<br>{<br>"chat_id": 1,<br>"messenger": "testgutmail@coolmail.dk",<br>"message": "Hi how are you doing :)"<br>}                                                                                                                                                                           | {<br>"chat_id": 1,<br>"messenger": "testgutmail@coolmail.dk",<br>"message": "Hi how are you doing :)",<br>"sent_at": "Thu Dec 05 17:37:19 CET 2024"<br>}                                                                                                                                                                                                                                                                                                             |
| **Get Messages By Id**         | **GET** `/api/chat/getMessagesByChatId/{id}` <br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br>                                                                                                                                                                                                                                                                                          | [<br>{<br>"chat_id": 1,<br>"messenger": "testgutmail@coolmail.dk",<br>"message": "Hi how are you doing :)",<br>"sent_at": "Thu Dec 05 17:37:19 CET 2024"<br>},<br>**(...)**<br>]                                                                                                                                                                                                                                                                                     |
| **Get Chats By User**          | **GET** `/api/chat/getChatsByUser/{email}` <br><br>**Headers**:<br>`Authorization: Bearer TOKEN HASH`<br>                                                                                                                                                                                                                                                                                            | [<br>{<br>"_id": 1,<br>"participants": [<br>"testgutmail@coolmail.dk",<br>"testdamemail4@cooleremail.dk"<br>],<br>"created_at": "Thu Dec 05 12:09:30 CET 2024",<br>"last_message_at": "Thu Dec 05 17:37:20 CET 2024",<br>"id": 1 <br>}, <br>{<br>"_id": 2,<br>"participants": [<br>"testgutmail@coolmail.dk",<br>"testdamemail@cooleremail.dk"<br>],<br>"created_at": "Thu Dec 05 16:26:52 CET 2024",<br>"last_message_at": null,<br>"id": 2 <br>}<br>**(...)**<br>] |
| **Block user**     | **Post** `/api/blocking/block`<br><br>**Body**:<br>{<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  }                                                                                                                                                                                                                                                                                             | {<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  }
| **Block user**     | **Post** `/api/blocking/unblock`<br><br>**Body**:<br>{<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  }                                                                                                                                                                                                                                                                                             | {<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  }
| **Block user**     | **Post** `/api/blocking/blocked`<br><br>**Body**:<br>{<br>  "blocking_email": "user1@example.com" }                                                                                                                                                                                                                                                                                             | [<br>{<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  } <br> {<br>  "blocking_email": "user1@example.com",<br> "blocked_email": "user1@example.com"  } <br> ]




