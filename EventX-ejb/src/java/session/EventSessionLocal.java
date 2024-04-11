package session;

import entity.Event;
import entity.EventPerson;
import exception.NoResultException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

@Local
public interface EventSessionLocal {

    public void createEvent(Event event);

    public Event getEvent(Long eventId) throws NoResultException;

    public void updateEvent(Event event) throws NoResultException;

    public void deleteEvent(Long eventId) throws NoResultException;

    public List<Event> getAllEvents();

    public List<Event> getEventsByOrganizer(Long organizerId);

    public void registerPersonForEvent(Long personId, Long eventId);

    public void deRegisterPersonForEvent(Long personId, Long eventId);

    public boolean isPersonRegisteredForEvent(Long personId, Long eventId);

    public List<EventPerson> getRegisteredAttendeesForEvent(Long eventId);

    public long getEventPersonIdForAttendance(Long eventPersonId);

    public void approveRegistration(Long eventPersonId);

    public void disapproveRegistration(Long eventPersonId);

    public List<Event> searchEventsByTitle(String title);

    public List<Event> searchEventsByLocation(String location);

    public List<Event> searchEventsByEventOrganiser(String eventOrganiser);

}
