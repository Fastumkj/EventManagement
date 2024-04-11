<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Create Person</title>
</head>
<body>
    <h1>Create New Person</h1>
    <form action="Controller/doCreatePerson" method="POST">
        <label for="name">Name:</label><input type="text" id="name" name="name" /><br />
        <label for="email">Email:</label><input type="email" id="email" name="email" /><br />
        <br />
        
        <label for="dob">Date of Birth (yyyy-MM-dd):</label><input type="text" id="dob" name="dob" /><br />
        <input type="submit" value="Submit" />
    </form>
</body>
</html>
