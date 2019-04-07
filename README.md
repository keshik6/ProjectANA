# ProjectANA: Ask and Answer
# Dynamic Learning Platform

![ana](https://user-images.githubusercontent.com/21276946/55679511-45c71f00-593f-11e9-84e3-f4bc68881ded.jpg)

## Problem statement
How interactive are our classroom activities? Why are some classes interactive and others being dead silent? Do we find our grades declining semester after semester? Are the teaching strategies we had always relied on not working as well as they once did? These are some of the tantalizing questions that spontaneously seemed to emerge during our discussion of classroom experience. We found it extremely important to address the needs of both students and teachers for efficient learning experience.

For students, they have the problem of not knowing what they don't know during lessons, as well as not wanting to ask questions in class, possibly due to either shyness or not wanting to take up precious class time / disturb the flow of the class. They may also be afraid that their question may be considered by their peers to be a "stupid question", resulting in them being ridiculed. In other case, once the teacher has to focus on covering all the concepts within the limited time, the situation will snowball until the student become totally lost for the remaining season.

As for the teachers, similar to the students, they do not know what the students do not know / understand. Limited class time also means that there is a need for teachers to prioritize doubts that more students are having. Currently, our learning environment encourages questions from those who are more vocal or willing to clarify their doubts, but those questions are not necessarily the questions that most other students are struggling with. Lastly, the teachers also lack accurate quantitative feedback for their teaching.

Though there are popular dynamic learning platforms including Piazza, Learning catalytics, socrative, Hodder education etc., most of them fail to address the needs of students and teachers completely. Hence, we propose an interactive dynamic leaning platform where Students learn through their participation in the attainment of knowledge by gathering information and processing it by solving problems and articulating what they have discovered with teachers and TAs optimally contributing towards this dynamic learning process.

## Our solution

### Features

Our solution takes some inspiration from other question-and-answer platforms, and implements the following features.

• ***Voting system (Inspired from Stack Overflow)***
Students can upvote questions which they think are relevant or that they also need to clarify. This allows teachers to know which questions are the most important ones to answer. Additionally, students can also downvote questions which are irrelevant, such as questions about different topics or even questions that are too advanced and outside of the scope of what is to be covered in class. Voting can also be done for answers.

• ***Anonymity and sorting Questions by topics (Inspired from Piazza)***
Piazza is specifically targeted at students and teachers, and differentiates responses by students and teachers. It also has the options for students to remain anonymous, to combat the problem of students being too shy to raise doubts. Questions are also sorted by topic, which makes it easy to find questions relating to certain topics

• ***Live in-class questioning***
Students can ask questions using this platform in-class while the teacher is there, and everyone can upvote relevant questions. Teachers can then choose appropriate breaks in their lesson to look at these questions and can answer the questions that have the most upvotes. Additionally, after the teacher answers the question, the students can vote on the teacher's answer, and the teacher will know if the answer was satisfactory or not.

While live questioning is happening, students who already understand can help out their peers either by supplementing the teacher's answers with their own explanation, or by sharing some links to online resources which may be helpful.

• ***Monitor the active involvement of students continuously***
Our application allows teachers to view statistics of students on their usage of their app (e.g. frequency of question asking, answering and voting) to gain a better understanding of how involved each student is in class.

• ***Mobile application facilitates ease of use***
The platform is a mobile app, allowing students to enjoy a designed-for-mobile interface, which allows them to easily raise questions in class even if they do not have a laptop to do so. It also ensures that students can easily ask questions anytime that a doubt arises, no matter where they may be or what they may be doing.

## Implementation

Our platform is implemented as an Android application. Additionally, we also used Firebase to store all the relevant information, such as the questions, answers and their vote scores. This ensures that the data will be synchronized across all users. We chose Firebase among other cloud storage services as it offers simple integration with android, as well as a web interface for easy access for data. Furthermore, it also offers authentication services, allowing us to also use Firebase for managing user accounts, without us having to worry about backend encryption and security issues.

We heavily used object-oriented design in designing the overall architecture of our system. We incorporated object-oriented design into our application by making Students, Teachers, Subjects, Topics, Questions, Answers and Feedback as Java objects.

We also made use of a slightly modified Observer design pattern. In our implementation, User objects store a list of Subjects, which in turn store a list of Topics, which store a list of Questions, which store a list of Answers. This way, data is only downloaded onto the app when it is needed. For example, the answers to a particular question will only be downloaded if a student clicks on the question. This pattern is also particularly useful for Firebase usage, as Firebase is not a relational database but rather uses a tree structure to store data. In our case, we are able to store Subjects as child objects of Users, Topics as child objects of Subjects and so on.

Objects were stored in and retrieved from Firebase as JSON objects, which the Firebase API already allows for. In terms of database structure, we had the following nodes in our database, all of which are all children of the root node:

• ***UserType:*** has a user's UID (generated by Firebase authentication) as a key, and a string, which is either "student" or "teacher" as its value. This node allows us to tell if a user is a student or teacher.

• ***UserInfo:*** this node stores the User (Student or Teacher) objects. Properties stored in the User objects include the user's UID, email address, name, enrolled subjects, number of questions asked, and answers given (for students), and a list of notifications for students who have had their questions answered.

• ***Subjects:*** this node stores the Subject objects. Properties of these objects include the subject code, subject title, a list of Topics under this subject, and a boolean that shows if this subject is currently "Live".

• ***Topics:*** this node stores the Topic objects. Properties of these objects include the title of the topic, a boolean that shows if the Topic is "Live", and a list of Questions for this topic.

• ***Questions:*** this node stores the Question objects. Properties of these objects include the title and body of the question, the asker of the question, the number of votes for the question, a list of those who have upvoted the question, a list of those who have downvoted the question, and a boolean that shows if the question is "Live".

• ***Feedback:*** this node stores the Feedback objects, used to store feedback of students for a teacher's explanation. Properties stored include a list of students who have voted, the number of students who understood the explanation, and the number of students who didn't.

As mentioned above, we made use of a modified Observer design pattern, which meant that intuitively, many of these nodes can all be combined into one node as all Topic objects can be under the Subjects node, all Question objects can be under the Topic objects which are in turn under the Subjects node, and so on. However, this would make navigating to the specific objects consume a large bandwidth and computational power. As a result, we decided to structure the database with many nodes rather than have everything stored under one node.

## How to use the application
The application is very intuitive to use. Use the authentication steps below to login to the app.

| User type        | Email           | Password  |
| ------------- |:-------------:| -----:|
| Student      | s@g.com | bE+m!E^9n;7GW_w# |
| Student      | examplestudent@school.com      |   bE+m!E^9n;7GW_w# |
| Teacher | t@g.com      |    bE+m!E^9n;7GW_w# |
| Teacher | exampleteacher@school.com      |    bE+m!E^9n;7GW_w# |

╚ **Do note that the project is not under active development and the firebase api endpoints are deprecated.**

## Future work
In the initial phase of the project, we planned to have our own server system to store data and reduce bandwidth costs with the aim of hosting it in the local environment, and we designed a server-client architecture as well. But due to issues with non-blocking sockets in java we proceeded to use Firebase cloud storage. So, in the future, we are planning to design our own server as well as develop the UI of the application aesthetically with additional features such as users being able to use their desired pictures as profile pictures, giving ratings to users based on how active they are etc.
