package session;

import entity.Person;
import exception.NoResultException;
import java.util.Date;
import java.util.Set;
import javax.ejb.Local;

@Local
public interface PersonSessionLocal {

    public void createPerson(Person person);

    public Person getPerson(Long personId) throws NoResultException;

    public void updatePerson(Person person) throws NoResultException;

    public void deletePerson(Long personId) throws NoResultException;

    public Person findPersonByEmail(String email);

}
