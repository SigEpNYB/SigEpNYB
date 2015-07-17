REST API
==============================

*See http://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html for information about date formatting

Events (/Events):
-----------------
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
        id: "id",
        title: "title",
        startTime: "EEE MMM dd HH:mm:ss z",
        endTime: "EEE MMM dd HH:mm:ss z",
        description: "description"
    },
    ... etc ...
]
