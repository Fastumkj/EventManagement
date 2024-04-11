<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Event</title>
</head>
<body>
    <h1>Create New Event</h1>
    <form action="Controller/doCreateEvent" method="POST">
        <input type="hidden" name="personId" value="PERSON_ID" /> <!-- Adjust PERSON_ID accordingly -->
        <label for="title">Title:</label><input type="text" id="title" name="title" /><br />
        <label for="eventDate">Event Date (yyyy-MM-dd):</label><input type="text" id="eventDate" name="eventDate" /><br />
        <label for="location">Location:</label><input type="text" id="location" name="location" /><br />
        <label for="description">Description:</label><textarea id="description" name="description"></textarea><br />
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
