
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import entity.Event;
import entity.EventPerson;
import entity.Person;
import exception.NoResultException;
import java.util.ArrayList;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import managedbean.UserManagedBean;
import org.primefaces.PrimeFaces;
import session.EventSessionLocal;
import session.PersonSessionLocal;

@Named(value = "eventManagedBean")
@SessionScoped
public class EventManagedBean implements Serializable {

    @Inject
    private EventSessionLocal eventSessionLocal;
    @Inject
    private PersonSessionLocal personSessionLocal;
    @Inject
    private UserManagedBean userManagedBean;

    private boolean showMyEvents; // Flag to control which events to display

    private Long id;
    private String title;
    private Date eventDate;
    private String location;
    private String description;
    private Date registrationDeadline;
    private Event newEvent;
    private List<Event> userEvents;
    private Long eventId;

    private Person creator = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInPerson");

    private List<Event> allEvents;
    private List<Event> myEvents;

    //For Attendance
    private Long selectedEventId;
    private List<EventPerson> registeredAttendees;
    private long eventPersonId;
    private EventPerson eventPerson;

    //for searching
    private String searchType = "TITLE";
    private String searchString;

    @PostConstruct
    public void init() {
        newEvent = new Event();
        refreshEvents();
        listUserEvents();
        listAllEvents();
    }

    public void addEvent() {
        // Basic backend validation example
        if (eventDate != null && registrationDeadline != null && !eventDate.before(registrationDeadline)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event date must be before the registration deadline.", ""));
            return; // Stop the execution and show the error message.
        }

        Event event = new Event();

        event.setId(id);
        System.out.println("id is: " + id);
        event.setTitle(title);
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setDescription(description);
        event.setRegistrationDeadline(registrationDeadline);
        Person creator = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInPerson");
        //event.setCreator(creator);
        System.out.println("Hey: " + creator.getId());
        System.out.println("Hey: " + creator.getName());
        event.setCreatorId(creator.getId());
        event.setCreatorName(creator.getName());

        eventSessionLocal.createEvent(event);
        refreshEvents();
    }

    public void deleteEvent(Long eventId) throws NoResultException {
        if (eventId == null) {
            System.out.println("Event ID is null");
            return; // Or handle the null ID case accordingly
        }

        try {
            eventSessionLocal.deleteEvent(eventId);
            System.out.println("Event deleted successfully");
        } catch (NoResultException e) {
            // Handle exception
            System.out.println("Error deleting event: " + e.getMessage());
        }
        refreshEvents();
    }

    public void refreshEvents() {
        myEvents = eventSessionLocal.getEventsByOrganizer(eventId);
        allEvents = eventSessionLocal.getAllEvents();
        listUserEvents();
        listAllEvents();
    }

    public void listAllEvents() {
        allEvents = eventSessionLocal.getAllEvents();
        showMyEvents = false;
    }

    public void listUserEvents() {
        if (userManagedBean.getLoggedInPerson() != null) {
            myEvents = eventSessionLocal.getEventsByOrganizer(userManagedBean.getLoggedInPerson().getId());
        } else {
            System.out.println("listuserevents empty");
            // Handle case for non-logged-in users. Maybe load a default set of events or leave myEvents empty.
            myEvents = new ArrayList<>(); // Or any other appropriate handling
        }
        showMyEvents = false;
    }

    public void showRegisteredAttendees(Long eventId) {
        System.out.println("registeredAttendeesbefore: " + registeredAttendees);
        selectedEventId = eventId;
        registeredAttendees = eventSessionLocal.getRegisteredAttendeesForEvent(eventId); // Assuming this method retrieves the list of registered attendees for the event
        System.out.println("registeredAttendeesafter: " + registeredAttendees);
        PrimeFaces.current().executeScript("PF('registeredAttendeesDialogWidget').show()");
    }

    public void registerForEvent(Long eventId) {
        Long personId = userManagedBean.getLoggedInPerson().getId();
        // Check if the person is already registered
        if (!eventSessionLocal.isPersonRegisteredForEvent(personId, eventId)) {
            try {
                eventSessionLocal.registerPersonForEvent(personId, eventId);
                refreshEvents(); // Refresh the events after registration
            } catch (Exception e) {
                // Handle exception (e.g., display error message)
            }
        } else {
            // User is already registered, handle accordingly (e.g., display a message)
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registered!", "You have successfully registered for the event."));
        }
    }

    public void deRegisterForEvent(Long eventId) {
        Long personId = userManagedBean.getLoggedInPerson().getId();
        // Check if the person is actually registered before deregistering
        if (eventSessionLocal.isPersonRegisteredForEvent(personId, eventId)) {
            try {
                eventSessionLocal.deRegisterPersonForEvent(personId, eventId);
                refreshEvents(); // Refresh the events after deregistration
            } catch (Exception e) {
                // Handle exception (e.g., display error message)
            }
        } else {
            // User is not registered, handle accordingly (e.g., display a message)
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Deregistered!", "You have successfully deregistered from the event."));
        }
    }

    public void approveRegistration(Long eventPersonId) {
        eventSessionLocal.approveRegistration(eventSessionLocal.getEventPersonIdForAttendance(eventPersonId));
        registeredAttendees = eventSessionLocal.getRegisteredAttendeesForEvent(selectedEventId);
    }

    public void approveAllAttendees() {
        for (EventPerson eventPerson : registeredAttendees) {
            eventSessionLocal.approveRegistration(eventPerson.getId());
        }
        registeredAttendees = eventSessionLocal.getRegisteredAttendeesForEvent(selectedEventId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "All Attended!", "All attendees have been marked as attended."));
    }

    public void disapproveRegistration(Long eventPersonId) {
        eventSessionLocal.disapproveRegistration(eventPersonId);
        // Refresh the registered attendees list after disapproval
        registeredAttendees = eventSessionLocal.getRegisteredAttendeesForEvent(selectedEventId);
    }

    public void disapproveAllAttendees() {
        for (EventPerson eventPerson : registeredAttendees) {
            eventSessionLocal.disapproveRegistration(eventPerson.getId());
        }
        registeredAttendees = eventSessionLocal.getRegisteredAttendeesForEvent(selectedEventId);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "All Missing?!", "All attendees have been marked as missing."));
    }

    public void changeApprovalStatus(Long eventPersonId, boolean newStatus) {
        if (newStatus) {
            eventSessionLocal.approveRegistration(eventPersonId);
        } else {
            eventSessionLocal.disapproveRegistration(eventPersonId);
        }
    }

    //FOR ATTENDEE
    public boolean isUserRegisteredForEvent(Long eventId) {
        // Check if userManagedBean is null
        if (userManagedBean == null) {
            return false;
        }

        // Check if the loggedInPerson or eventId is null
        if (userManagedBean.getLoggedInPerson() == null || eventId == null) {
            return false;
        }

        Long personId = userManagedBean.getLoggedInPerson().getId();
        // Assuming eventSessionLocal is correctly injected and not null.
        // Add a null check if necessary.
        return eventSessionLocal.isPersonRegisteredForEvent(personId, eventId);
    }

    public void handleSearch() throws NoResultException {
        // Logic to perform search based on searchType and searchString
        switch (searchType) {
            case "TITLE":
                allEvents = eventSessionLocal.searchEventsByTitle(searchString);
                break;
            case "CREATOR_NAME":
                allEvents = eventSessionLocal.searchEventsByEventOrganiser(searchString);
                break;
            case "LOCATION":
                allEvents = eventSessionLocal.searchEventsByLocation(searchString);
                break;
            default:
                allEvents = eventSessionLocal.getAllEvents(); // Fallback to displaying all events
                break;
        }
        showMyEvents = false; // Assuming you want to show search results in the same list
    }

    public List<Event> getMyEvents() {
        return myEvents;
    }

    public void setMyEvents(List<Event> myEvents) {
        this.myEvents = myEvents;
    }

    // Getters and setters
    public EventSessionLocal getEventSessionLocal() {
        return eventSessionLocal;
    }

    public void setEventSessionLocal(EventSessionLocal eventSessionLocal) {
        this.eventSessionLocal = eventSessionLocal;
    }

    public Event getNewEvent() {
        return newEvent;
    }

    public void setNewEvent(Event newEvent) {
        this.newEvent = newEvent;
    }

    public List<Event> getUserEvents() {
        return userEvents;
    }

    public void setUserEvents(List<Event> userEvents) {
        this.userEvents = userEvents;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
    }

    public UserManagedBean getUserManagedBean() {
        return userManagedBean;
    }

    public void setUserManagedBean(UserManagedBean userManagedBean) {
        this.userManagedBean = userManagedBean;
    }

    public List<Event> getAllEvents() {
        return allEvents;
    }

    public void setAllEvents(List<Event> allEvents) {
        this.allEvents = allEvents;
    }

    public Person getCreator() {
        return creator;
    }

    public void setCreator(Person creator) {
        this.creator = creator;
    }

    public boolean isShowMyEvents() {
        return showMyEvents;
    }

    public void setShowMyEvents(boolean showMyEvents) {
        this.showMyEvents = showMyEvents;
    }

    public PersonSessionLocal getPersonSessionLocal() {
        return personSessionLocal;
    }

    public void setPersonSessionLocal(PersonSessionLocal personSessionLocal) {
        this.personSessionLocal = personSessionLocal;
    }

    public Long getSelectedEventId() {
        return selectedEventId;
    }

    public void setSelectedEventId(Long selectedEventId) {
        this.selectedEventId = selectedEventId;
    }

    public List<EventPerson> getRegisteredAttendees() {
        return registeredAttendees;
    }

    public void setRegisteredAttendees(List<EventPerson> registeredAttendees) {
        this.registeredAttendees = registeredAttendees;
    }

    public long getEventPersonId() {
        return eventPersonId;
    }

    public void setEventPersonId(long eventPersonId) {
        this.eventPersonId = eventPersonId;
    }

    public EventPerson getEventPerson() {
        return eventPerson;
    }

    public void setEventPerson(EventPerson eventPerson) {
        this.eventPerson = eventPerson;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
