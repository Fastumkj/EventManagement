package manager;

import entity.Person;
import entity.Event;
import exception.NoResultException;
import javax.ejb.EJB;
import session.EventSessionLocal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import session.PersonSessionLocal;

public class PersonManager {

    @EJB
    private PersonSessionLocal personSessionLocal;

    @EJB
    private EventSessionLocal eventSessionLocal;

    public PersonManager() {
    }

    public PersonManager(PersonSessionLocal personSessionLocal) {
        this.personSessionLocal = personSessionLocal;
    }

    public PersonManager(PersonSessionLocal personSessionLocal, EventSessionLocal eventSessionLocal) {
        this.personSessionLocal = personSessionLocal;
        this.eventSessionLocal = eventSessionLocal;
    }

    public void createPerson(String name, String email, Date dob) throws Exception {
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        person.setDob(dob);
        personSessionLocal.createPerson(person);
    }

    public void updatePerson(Long personId, String name, String email, byte gender, String uniqueId,
            Date dob) throws Exception {
        Person person = personSessionLocal.getPerson(personId);
        person.setName(name);
        person.setEmail(email);
        person.setGender(gender);
        person.setDob(dob);
        person.setUniqueId(uniqueId);
        personSessionLocal.updatePerson(person);
    }

    public Person getPerson(Long personId) throws NoResultException {
        return personSessionLocal.getPerson(personId);
    }

    public void deletePerson(Long personId) throws NoResultException {
        personSessionLocal.deletePerson(personId);
    }

    public void createEvent(Long personId, String title, Date eventDate, String location, String description) throws Exception {
        Person person = personSessionLocal.getPerson(personId); // Fetch the creator person
        if (person == null) {
            throw new NoResultException("Person not found");
        }

        Event event = new Event();
        event.setTitle(title);
        event.setEventDate(eventDate);
        event.setLocation(location);
        event.setDescription(description);
        event.setCreator(person); // Associate the event with the person
        eventSessionLocal.createEvent(event); // Save the event
    }
}
