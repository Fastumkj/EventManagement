package servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manager.PersonManager;
import session.PersonSessionLocal;

public class Controller extends HttpServlet {

    @EJB
    private PersonSessionLocal personSessionLocal;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            PersonManager personManager = new PersonManager(personSessionLocal);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String path = request.getPathInfo();
            path = path.split("/")[1];

            switch (path) {
                case "createPerson":
                    // Logic for displaying person creation page
                    request.getRequestDispatcher("/createPerson.jsp").forward(request, response);
                    break;
                /* case "doCreatePerson":
                    // Extract parameters from the request
                    String name = request.getParameter("name");
                    String email = request.getParameter("email");
                    // Assuming date of birth (dob) is sent as a string in the format "yyyy-MM-dd"
                    String dobStr = request.getParameter("dob");
                    sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date dob = sdf.parse(dobStr); // Convert String to Date

                    personManager.createPerson(name, email, dob); // Use PersonManager to create the person
                    response.sendRedirect(request.getContextPath() + "/Controller/listPersons");
                    return;
                case "listPersons":
                    // List all persons
                    // Example: List<Person> persons = personManager.listAllPersons();
                    // request.setAttribute("persons", persons);
                    request.getRequestDispatcher("/listPersons.jsp").forward(request, response);
                    break;
                case "createEvent":
                    // Logic for displaying event creation page
                    request.getRequestDispatcher("/createEvent.jsp").forward(request, response);
                    break;
                case "doCreateEvent":
                    // Extract person ID and event details from the request
                    Long personId = Long.parseLong(request.getParameter("personId")); // Example: get personId
                    String title = request.getParameter("title");
                    String eventDateStr = request.getParameter("eventDate");
                    Date eventDate = sdf.parse(eventDateStr); // Convert String to Date
                    String location = request.getParameter("location");
                    String description = request.getParameter("description");

                    personManager.createEvent(personId, title, eventDate, location, description); // Use PersonManager to create the event
                    response.sendRedirect(request.getContextPath() + "/Controller/listEvents");
                    return;*/
                default:
                    // Unmatched path leads to error page
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
