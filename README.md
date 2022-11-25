# Food-Recognition-App
group project mobile programming; Kotlin app

Introduction
============

The purpose of this document is to collect, analyze, and describe the
needs and features of the food recognition application for elderly
people called *SnapFood*. The details of how the food recognition
application fulfills these needs are detailed in the Background and
Implementation.

The introduction of the Report document provides an overview of the
entire document. It covers the purpose of the project, scope, the
application areas of the program, and the contributions of the project
members.

Project motivation, aim and objectives
--------------------------------------

Before we started working on the project, the aim was to develop a food
identification program that automatically counts calories and shows
nutritional data. The software should be able to identify the type of
food from the provided image, display its nutritional information, and
determine how many calories are in the entire serving.

Our motivation was to develop a user-friendly, self-explanatory
application for elderly people, thus we made the text and icon sizes
larger and more visible. We were eager to put everything we had learned
on the course into action and see how we could handle everything.
Thankfully, we were able to accomplish our objectives and create a
useful and a functional application.

Scope
-----

The objective of this project was to encourage us to put what we learned
in this course into practice and demonstrate how well we knew the
concepts. To determine our project scope, we first identified the
project's main goals. We projected what would be done, when it would be
done, and how long it would take in the early phases of our project. The
application's features include the ability to load and take pictures.
The food label is then returned by a third-party service. It retrieves
the calorie and nutritional information for that particular food and
retains the food label information for processing daily consumption.
These functionalities are described more detailed in the Background
section.

Application areas
-----------------

The project belongs in the area of the health sector. However, it is
intended more specifically for elderly people who want to track their
nutrition and calculate calories. It's crucial to take your goals into
account when choosing a nutrition app to use, as well as the features
that matter most to the user. Additionally, there are solutions designed
expressly for persons who are pregnant, have food allergies, or have
unique nutritional needs or food preferences. However, *SnapFood* is the
only application designed specifically for older users.

Your contribution
-----------------

The project work was divided in half as the back-end and front-end of
the project at the beginning of development. Since there were four
members in the group, two of them worked on the back-end while the other
two worked on the front-end.

### Marius - Back-end

Marius mainly worked on implementation of Calorie Mama API with OkHttp
client and Google Firestore cloud storage. Before the final
implementation he also did some reading and testing on google cloud
vision API, but did not manage to get desired result in a reasonable
time frame.

### Mateusz - Back-end

Mateusz worked on implementation of Calorie Mama API with OkHttp client
and Google Firestore cloud storage. He also spent some time researching
and implementing secure storage of the API key.

### Viktor - Front-end

Viktor created the dashboard containing the circular and other progress
bars, list of food, navigation drawer and its items (settings, logout,
and report problem), camera, and all their functionalities. He also made
the design of the screen displaying the food of which a photo was taken.

### Berkay - Front-end

Berkay created the app's first page, logo, and buttons. The buttons on
the bottom navigation are fragments. We tried to employ as many
components as we could. The register and login pages were also created
by him. He made use of Firebase Authentication, which enables your app
to confirm the user's identity. Users login and registration details are
stored in cloud storage.

Background
==========

This project is written in Kotlin and is build using Gradle tool. This
application uses both Kotlin and Java standard libraries but also few
and necessary third party libraries and tools described in detail below.

Calorie Mama API by Azumio
--------------------------

Calorie Mama API is the foundation of this project. This API uses
machine learning to detect and classify food items in images. When a
food item is detected, the API returns the name of the food item and its
nutritional values (calories, proteins, fats and carbohydrates).
Response from the API is written in JSON format, which in turn is parsed
and shown in the application.

Calorie Mama API is a paid, subscription based tool, but for the purpose
of this project, our group was granted a free student access.

Google Firebase
---------------

Firebase is Google's Backend-as-a-Service (BaaS) platform. It provides
developers with a variety of tools and services to help build, improve
and grow applications. This project uses Firebase authentication service
and its database service, Firestore database.

Firebase authentication enables users to register new account with their
email address and password. Provided credentials are then stored in
Firebase system and registered users are able to sign in to the
application.

Firestore database is a NoSQL database that stores documents in a JSON
format. The application stores all food items and its values in the
Firestore.

By combining both Firebase authentication and Firestore database we can
make sure that the data is safely stored in cloud and is connected to
the right user. This means that a registered user can only retrieve
his/hers own data and noone elses. This method also ensures that the
data will not get lost/deleted when the application is closed or reset.

OkHttp library
--------------

OkHttp is an open-source third party library by Square that provides a
HTTP client to request and retrieve response from API calls. This
library is used to send images to the Calorie Mama API and retrieve
corresponding responses.

Implementation/proposed methodology
===================================

Secure storage of API key
-------------------------

A unique API key is necessary in order to use the Calorie Mama API. It
is important to safely store the key and deny access for unauthorized
actors. Security of API key is especially important when using a paid
service like Calorie Mama, where the number of available calls for a
period of time is limited by which subscription plan is bought by a
company. If a malicious actor could get access to the key, the company
could suffer from unexpected increase in costs.

When storing API key in plain text in the application, the risk of
discovering the key by reverse-engineering the application is
significant. In order to minimize this risk, we defines the API key in
native Android NDK C++ code [^1]. This method complies the code in
**.so** file and this file only contains raw data and few strings so,
after the reverse engineering process, it would be difficult for
malicious actors to identify the location of the key because its
location changed with respect to PCs.

This method obscures the key, rather then completely securing it, but
this makes it more difficult for a malicious actor to decompile and find
the key, rather then storing it in resource or gradle file.

For the purpose of this project, the C++ file containing the API key
will be included in source file upon final delivery, but we want to
point out that this is not best practice and shouldn't be done
otherwise.

Using Image Recognition API
---------------------------

As stated earlier, we are using an image recognition API to get an image
label and corresponding nutritional information about that particular
food item. The Calorie Mama API bundles all this together in an easy
way. Before explaining how it all works, we need to be familiar with the
libraries used to send the request and parse the data. For the HTTP
request, we use a http client library called OkHttp to build and send
our request to the Calorie Mama API, and the gson library to easily
convert out returned JSON to an object.

The Calorie Mama API requires us to send a HTTP POST request with an
image of resolution 544x544 as content type "multipart/form-data". As
the image taken are create as a bitmap from the camera we can easily
recreate a rescaled bitmap of size 544x544 using the builtin
android.graphic.Bitmap class. In order to send this data in the
"multipart/form-data" field, we further convert the rescaled bitmap to a
bytearray with bultin "compress" method in the Bitmap class.

With the converted image, we simply construct the request with
functionality provided by the OkHttp library and send it to the Calorie
Mama API. The Calorie Mama API response is converted to an object with
google gson library. The data class was created by using an online JSON
to Kotlin data class converter using a sample JSON response. Using this
object, we can manipulate and display the returned data.

Using Firebase authentication
-----------------------------

For authenticating users on the application, we use google Firebase
authentication. This allows users to register and sign in by using the
sign up and sign in functionality provided by google without much
implementation. Upon user registration, a unique user identification is
generated. This identification can be returned after a user sign in and
used to get the users corresponding data from the cloud storage as
described in the next section.

Using Firestore cloud storage
-----------------------------

In the project we use google cloud storage solution Firestore to store
users food items with nutritional information and user settings.
Firestore is a document-based database for mobile, web and server
development. The database itself has a simple structure containing of a
collection of unique users, where each user has one document for each
food item in addition to a document for their user settings. Below a
tree representation of the database structure is shown.

``` {style="tree"}
userid
  └── food-item1
        ├── name: Apple
        ├── protein: 0.003
        ├── calories: 0.052
        ├── fat: 0.002
        ├── carbs: 0.014
        ├── quantity: 100
        ├── date: 22-11-2022  
  └── userSettings
        ├── proteinGoal: 10
        ├── carbsGoal: 10
        ├── fatGoal: 10
        └── caloriesGoal: 10
userid
  └── food-item1
     ...
     ...
```

Use Case diagram
----------------

![Use Case diagram](images/use case.png){#fig:my_label}

User manual with images
-----------------------

![Home screen of the application](images/home screen.png){#fig:my_label}

![Register screen](images/login.png){#fig:test2}

![Register screen](images/register.png){#fig:test2}

![Food information screen opens after image is taken. Displays
nutritional values for given food item per 100g. User can specify
quantity.](images/dashboard.png){#fig:test2}

![Food information screen opens after image is taken. Displays
nutritional values for given food item per 100g. User can specify
quantity.](images/food item.png){#fig:test2}

![Setting screen to specify the goal for daily calorie and nutritional
value intake.](images/dash with settings.png){#fig:test2}

![Setting screen to specify the goal for daily calorie and nutritional
value intake.](images/settings.png){#fig:test2}

Discussion
==========

What you've learned and achieved
--------------------------------

Throughout this project we learned a lot about Kotlin, working with
Android Studio and developing an Android application in general. We got
much better understanding of working with concurrency, sending and
retrieving HTTP requests and working with cloud storage. We also learned
about fragments, working with and communicating between different
activities. We also have gained more experience working as a team, which
will be important when we start looking for employment.

Looking back at this project, from where we started to the finished
product that will bu submitted for review, we feel both proud and
satisfied of what we have managed to learn and achieve in such short
amount of time.

How you've worked as a team
---------------------------

The work was divided into front-end and back-end parts. We used Trello
as our scrum board to make sure everyone was aware of what they were
working on and finished their tasks by the set deadlines. We held
meetings every other week in the beginning of the development and weekly
meetings in the final weeks. Although we had meetings on campus as well,
the meetings were conducted mainly online via Discord.

The overall group cooperation went smoothly and well, where everyone
contributed to the project and helped each other when necessary. The
discussion and brainstorming sessions were constructive and relevant and
there were no conflicts in the group.

What challenges, issues you faced and how you've resolved them
--------------------------------------------------------------

Back-end: We had some challenges creating a MealItem object from data
returned from the OkHttp request in a function. The function which
creates, executes and handles the response from the API would execute
the return statement before the Json parsing and object creation were
complete because it runs asynchronous This was solved by using
"CountDownLatch.countDown()" at the end of both "onResponse" and
"onFailure" of the request to ensure it counts in either scenario.
Before returning the MealItem object we use "CountDownLatch.await()" to
ensure it finished the request.

We had some similar challenges creating a function "fun getUserData():
MealItem {Query db $\rightarrow$ convert to MealItem object
$\rightarrow$ return MealItem object}". The return statement would also
execute before the query data was returned as this runs asynchronous.
The same method of using CountDownLatch did not work as the application
would freeze when we tried to implement a counter with await before
returning. This was ultimately solved by doing the query directly in the
activity where we needed the data and use the data in the
".addOnSuccessListener". This resulted in duplicate code (db query code)
in multiple activities.

Which things are incomplete that you envisioned in your original proposal plan?
-------------------------------------------------------------------------------

Regarding the project proposal, there are two aspects that we didn't
manage to implement in time. The first is the option to make text in the
application bigger and more visible. The application is made from the
beginning with visibility and ease of use in mind, but a method to make
the text even bigger would be preferred. We have implemented a settings
menu for the user, but sadly there was no time left to create this
particular method.

The second aspect is the calendar option that lets you switch between
days to check your daily intake from previous days. At this point, the
application shows users intake data only for the current day. The data
from previous days are stored in the cloud database, but we didn't
manage to implement a method to display them to the user in time.

There was also one, slightly ambitious, aspect that wasn't mentioned in
the proposal, but one that we wished to implement if given spare time
and that is the notification system. There was sadly no spare time left
and the notification system remains an aspect that can be implemented at
later stage.

Conclusion
==========

Food recognition apps can help people in staying healthy. It makes it
easier to oversee the daily nutrient score, goals, and registered food.
Because of its importance to elderly people, we focused on an app that
is user-friendly and self-explanatory. *SnapFood* is specifically for
aiding older users in monitoring their daily nutrient intake. This
project was written in Kotlin using Android Studio as an integrated
development environment, thus the application was built using the Gradle
tool. Next to a few essential third-party libraries, both Kotlin and
Java standard libraries were used. For detecting and classifying food we
used Calorie Mama API, for storing the user's data Google Firebase and
lastly for API calls, the OkHttp library. Our team was divided into two
groups: back-end and front-end. These groups had two members each. We
worked in an agile way using Trello as our scrum board to keep us aware
of the ongoing tasks and each other's work. The team dynamic was great,
assisting each other when necessary. Each member fulfilled their
assigned tasks as expected. The objective of this project was achieved,
as we have learned a lot about programming an application in Kotlin and
gained valuable experience in working as a team.

[^1]: <https://blog.kotlin-academy.com/how-to-secure-secrets-in-android-android-security-01-a345e97c82be>
