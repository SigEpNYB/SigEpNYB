SET UP
==========================================
1. Get access to the git repo and pull
2. Download the MySQL server and make sure its running on your local machine
    a. You can do this by going into the command line and opening the mysql terminal
        i. If you set up a password type 'mysql -u root -p' without the quotes and hit enter, then enter you password and hit enter
        ii. If you didn't set up a password type 'mysql -u root' without the quotes and hit enter
    b. You should get a prompt saying welcome to mysql etc, etc...
    c. Quit by typing 'exit' and pressing enter
3. Set up the database by running the setup script
    a. Make sure you are in the sql-scripts directory on the command line
    b. Type 'mysql -u root -p < setup.sql' without the quotes and hit enter
        i. Like before if you didn't set up a password exclude the '-p'
        ii. If you included the '-p' you will have to supply your password on the next line
4. Build everything
    a. Go to the root directory of the project
    b. Type './gradlew' and hit enter
        i. This will build all the code and run the jetty web server
        ii. You will do this every time you want to build and run (every time you make changes and want to test them)
    c. You can stop the server by pressing control-c (or command-c on a mac I think), or by opening up a new tab in the terminal and typing './gradlew jettyStop' and hitting enter
5. Test that it works
    a. Make sure jetty is running
        i. If you stopped it just run it again with './gradlew'
    b. Point a browser at 'http://localhost:8080/Fratsite'
    c. You should see the login page
==========================================



REST API
==========================================

*See http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html for information about date formatting

Events (/Events):
------------------------
POST Creates an Event
Input:
{
    title: "title",
    startTime: "YYYY-MM-dd'T'HH:mm",
    endTime: "YYYY-MM-dd'T'HH:mm",
    description: "description"
}
Output: nothing

GET Gets all the events which intersect with a given range
Input:
/Events?startTime=YYYY-MM-dd'T'HH:mm&endTime=YYYY-MM-dd'T'HH:mm
*No body*

Output:
[
    {
        id: id,
        title: "title",
        startTime: "EEE MMM dd HH:mm:ss z",
        endTime: "EEE MMM dd HH:mm:ss z",
        description: "description"
    },
    ... etc ...
]


Duties (/Duties):
------------------------
POST Creates a Duty
Input:
{
    idEvent: id,
    type: TYPE
}
TYPE must be one of RISKMANAGER, SOBER, DRIVER, SETCLEAN
Output: nothing

GET Gets all of the unassigned events
if the url parameter idEvent is included, it gets all of the duties for the event with that id

PUT Assigns a duty
Input:
{
    idDuty: id,
    idAccount: id
}
Output: nothing

DELETE Removes the Duty
Input:
{
    idDuty: id
}
Output: nothing
