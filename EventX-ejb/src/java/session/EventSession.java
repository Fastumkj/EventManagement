package session;

import entity.Event;
import entity.EventPerson;
import entity.Person;
import exception.NoResultException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class EventSession implements EventSessionLocal {

    @PersistenceContext(unitName = "EventX-ejbPU")
    private EntityManager em;

    @Override
    public void createEvent(Event event) {
        em.persist(event);
    }

    @Override
    public Event getEvent(Long eventId) throws NoResultException {
        Event event = em.find(Event.class, eventId);
        if (event == null) {
            throw new NoResultException("Event not found");
        }
        return event;
    }

    @Override
    public void updateEvent(Event event) throws NoResultException {
        if (em.find(Event.class, event.getId()) == null) {
            throw new NoResultException("Event not found");
        }
        em.merge(event);
    }

    @Override
    public void deleteEvent(Long eventId) throws NoResultException {
        Event event = em.find(Event.class, eventId);
        if (event == null) {
            throw new NoResultException("Event not found");
        }
        em.remove(event);
    }

    @Override
    public List<Event> getAllEvents() {
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e", Event.class);
        return query.getResultList();
    }

    @Override
    public List<Event> getEventsByOrganizer(Long organizerId) {
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e WHERE e.creatorId = :organizerId", Event.class);
        query.setParameter("organizerId", organizerId);
        return query.getResultList();
    }

    public void persist(Object object) {
        em.persist(object);
    }

    @Override
    public void registerPersonForEvent(Long personId, Long eventId) {
        // Check if the person is already registered for the event
        List<EventPerson> existingRegistrations = em.createQuery(
                "SELECT ep FROM EventPerson ep WHERE ep.person.id = :personId AND ep.event.id = :eventId", EventPerson.class)
                .setParameter("personId", personId)
                .setParameter("eventId", eventId)
                .getResultList();

        if (existingRegistrations.isEmpty()) {
            Person person = em.find(Person.class, personId);
            Event event = em.find(Event.class, eventId);

            if (person != null && event != null) {
                EventPerson eventPerson = new EventPerson();
                eventPerson.setPerson(person);
                eventPerson.setEvent(event);
                eventPerson.setAttendeeName(person.getName());
                // Set isApproved to false by default or based on your business logic
                eventPerson.setIsApproved(false);

                em.persist(eventPerson);
            } else {
                throw new IllegalArgumentException("Person or Event not found");
            }
        } else {
            // Handle the case where the person is already registered. For example, log or throw an exception.
        }
    }

    @Override
    public void deRegisterPersonForEvent(Long personId, Long eventId) {
        // Instead of trying to get a single result and catching an exception, get a list and check if it's empty
        List<EventPerson> registrations = em.createQuery(
                "SELECT ep FROM EventPerson ep WHERE ep.person.id = :personId AND ep.event.id = :eventId", EventPerson.class)
                .setParameter("personId", personId)
                .setParameter("eventId", eventId)
                .getResultList();

        if (!registrations.isEmpty()) {
            for (EventPerson registration : registrations) {
                em.remove(registration);
            }
        } else {
            // Handle the case where there is no registration found to de-register. For example, log or throw an exception.
        }
    }

    @Override
    public boolean isPersonRegisteredForEvent(Long personId, Long eventId) {
        List<EventPerson> registrations = em.createQuery(
                "SELECT ep FROM EventPerson ep WHERE ep.person.id = :personId AND ep.event.id = :eventId", EventPerson.class)
                .setParameter("personId", personId)
                .setParameter("eventId", eventId)
                .getResultList();

        return !registrations.isEmpty();
    }

    @Override
    public List<EventPerson> getRegisteredAttendeesForEvent(Long eventId) {
        TypedQuery<EventPerson> query = em.createQuery("SELECT ep FROM EventPerson ep WHERE ep.event.id = :eventId", EventPerson.class);
        query.setParameter("eventId", eventId);
        List<EventPerson> eventPersons = query.getResultList();

        return eventPersons; // Return the list of EventPerson entities directly
    }

    @Override
    public long getEventPersonIdForAttendance(Long eventPersonId) {
        return eventPersonId;
    }

    @Override
    public void approveRegistration(Long eventPersonId) {
        EventPerson eventPerson = em.find(EventPerson.class, eventPersonId);
        System.out.println("Eventperson: " + eventPerson);
        System.out.println("EventpersonID: " + eventPersonId);
        if (eventPerson != null) {

            eventPerson.setIsApproved(true);
            em.merge(eventPerson);
        }
    }

    @Override
    public void disapproveRegistration(Long eventPersonId) {
        EventPerson eventPerson = em.find(EventPerson.class, eventPersonId);
        if (eventPerson != null) {
            eventPerson.setIsApproved(false);
            em.merge(eventPerson);
        }
    }

    //THIS IS FOR REGISTRATION FOR ATTENDEE, ABOVE IS ATTENDANCE
    public void registerAttendeeForEvent(Long personId, Long eventId) {
        List<EventPerson> registrations = em.createQuery("SELECT ep FROM EventPerson ep WHERE ep.person.id = :personId AND ep.event.id = :eventId", EventPerson.class)
                .setParameter("personId", personId)
                .setParameter("eventId", eventId)
                .getResultList();
        if (registrations.isEmpty()) {
            // No existing registration found, create a new one
            EventPerson newRegistration = new EventPerson();
            Person person = em.find(Person.class, personId);
            Event event = em.find(Event.class, eventId);
            if (person == null || event == null) {
                // Handle error: person or event not found
                return;
            }
            newRegistration.setPerson(person);
            newRegistration.setEvent(event);
            newRegistration.setRegisteredAttendee(true);
            em.persist(newRegistration);
        } else {
            // Existing registration found, set isRegistered to true
            for (EventPerson registration : registrations) {
                registration.setRegisteredAttendee(true);
                em.merge(registration);
            }
        }
    }

    public void cancelRegistrationAttendeeForEvent(Long personId, Long eventId) {
        List<EventPerson> registrations = em.createQuery("SELECT ep FROM EventPerson ep WHERE ep.person.id = :personId AND ep.event.id = :eventId", EventPerson.class)
                .setParameter("personId", personId)
                .setParameter("eventId", eventId)
                .getResultList();
        if (!registrations.isEmpty()) {
            for (EventPerson registration : registrations) {
                registration.setRegisteredAttendee(false);
                em.merge(registration);
            }
        } else {
            // Handle case where no registration found to cancel
        }
    }

    @Override
    public List<Event> searchEventsByTitle(String title) {
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.title) LIKE LOWER(:title)", Event.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList();
    }

    @Override
    public List<Event> searchEventsByEventOrganiser(String eventOrganiser) {
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.creatorName) LIKE LOWER(:creatorName)", Event.class);
        query.setParameter("creatorName", "%" + eventOrganiser + "%");
        return query.getResultList();
    }

    @Override
    public List<Event> searchEventsByLocation(String location1) {
        TypedQuery<Event> query = em.createQuery("SELECT e FROM Event e WHERE LOWER(e.location) LIKE LOWER(:location)", Event.class);
        query.setParameter("location", "%" + location1 + "%");
        return query.getResultList();
    }

}
