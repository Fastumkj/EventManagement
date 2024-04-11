package session;

import entity.Person;
import exception.NoResultException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class PersonSession implements PersonSessionLocal {

    @PersistenceContext(unitName = "EventX-ejbPU")
    private EntityManager em;

    @Override
    public void createPerson(Person person) {
        em.persist(person);
    }

    @Override
    public Person getPerson(Long personId) throws NoResultException {
        Person person = em.find(Person.class, personId);
        if (person == null) {
            throw new NoResultException("Person not found");
        }
        return person;
    }

    @Override
    public void updatePerson(Person person) throws NoResultException {
        Person existingPerson = getPerson(person.getId());
        if (existingPerson != null) {
            existingPerson.setDob(person.getDob());
            existingPerson.setGender(person.getGender());
            existingPerson.setName(person.getName());
            existingPerson.setContactDetails(person.getContactDetails()); // Make sure you're updating this field as well
            existingPerson.setProfilePhoto(person.getProfilePhoto());
            // Uncomment this line to actually persist the changes
            em.merge(existingPerson);
        } else {
            throw new NoResultException("Person not found");
        }
    }

    @Override
    public void deletePerson(Long personId) throws NoResultException {
        Person person = em.find(Person.class, personId);
        if (person == null) {
            throw new NoResultException("Person not found");
        }
        em.remove(person);
    }

    @Override
    public Person findPersonByEmail(String email) {
        try {
            return em.createQuery("SELECT p FROM Person p WHERE p.email = :email", Person.class)
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    public void persist(Object object) {
        em.persist(object);
    }
}
