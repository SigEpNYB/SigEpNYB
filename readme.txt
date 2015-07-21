REST API
==============================

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
