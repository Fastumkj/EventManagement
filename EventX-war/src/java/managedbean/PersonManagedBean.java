package managedbean;

import entity.Person;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import session.PersonSessionLocal; // Assume this session bean exists and is similar to CustomerSessionLocal
import util.IdGenerator;

@Named(value = "personManagedBean")
@RequestScoped
public class PersonManagedBean {

    @Inject
    private PersonSessionLocal personSessionLocal;

    private String name;
    private String contactDetails;
    private Date dob; // Assuming the format is "yyyy-MM-dd"
    private byte[] profilePhoto; // This will be handled differently as it's binary data
    private byte gender;
    private String email;
    private String password;
    private String uniqueId;

    private List<Person> people;

    public PersonManagedBean() {
        this.uniqueId = IdGenerator.generateUniqueId();
    }

    public void addPerson() {

        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        //Date dobParsed = null;
        Person person = new Person();
        person.setName(name);
        person.setContactDetails(contactDetails);
        person.setDob(dob);
        person.setEmail(email);
        person.setGender(gender);
        person.setPassword(password);
        person.setUniqueId(uniqueId);
        // Handling of profilePhoto would depend on how you intend to upload and process the binary data

        personSessionLocal.createPerson(person);
    }

    public PersonSessionLocal getPersonSessionLocal() {
        return personSessionLocal;
    }

    public void setPersonSessionLocal(PersonSessionLocal personSessionLocal) {
        this.personSessionLocal = personSessionLocal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(String contactDetails) {
        this.contactDetails = contactDetails;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

}
