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

Accounts (/Accounts):
------------------------
GET Gets the list of accounts and details on those accounts
Input: none
if the url parameter idAccount is included, it gets only the details on the account with the given id
ex: ?idAccount=2


Account  (/Account):
------------------------
GET Gets information about the current user's account
Input: none
Output:
{
    firstName: string,
    lastName: string,
    netid: string,
    id: int
}
If the optional url parameter showPermissions is true, it instead returns an array of permissions the current user has.
ex:
[
    "accounts.get",
    "events.get",
    "accountRequests.view",
    "accountRequests.accept",
    "accountRequests.reject",
    "accounts.delete",
    "dutues.create",
    "duties.assign"
]


Events (/Events):
------------------------
POST Creates an Event
Input:
{
    title: "title",
    startTime: utctimestamp,
    endTime: utctimestamp,
    description: "description"
}
Output:
{
    idEvent: id
}

GET Gets all the events which intersect with a given range
Input:
/Events?startTime=utctimestamp&endTime=utctimestamp
*No body*

Output:
[
    {
        id: id,
        title: "title",
        startTime: utctimestamp,
        endTime: utctimestamp,
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
ex: ?idEvent=2
if the url parameters idAccount and type are included, it gets the number of duties of that type originally assigned to the user with the given id
ex: ?idAccount=3&type=SOBER

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


Fines (/Fines):
------------------------
POST creates a Fine
Input:
{
    idAccount: id,
    amount: decimal,
    reason: "reason"
}
Output: nothing

GET gets either all of the fines or only the fines for a specific person
url params: showAll (boolean)
if showAll is true, this returns all fines, else just the fines for the current user.  The default is false
Input: no body
Output: list of fines
[
    {
        idFine: id,
        idAccount: id,
        amount: decimal,
        reason: "reason"
    },
    {
        ...
    }
]

DELETE removes a fine
Input:
{
    idFine: id
}
Output: nothing
