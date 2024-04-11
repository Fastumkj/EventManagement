package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    private String location;
    @Lob
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date registrationDeadline;

    @Column(name = "creator_idd")
    private Long creatorId;

    @Column(name = "creator_name")
    private String creatorName;

    //Relationship with Person entity
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "creator_id")
    private Person creator;

    //@ManyToMany
    //private List<Person> registeredPersons; //Persons registered for the event
    //@ManyToMany
    //private List<Person> attendedPersons; //Persons who attended the event
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EventPerson> eventPersons;

    public Event() {

    }

    public Date getRegistrationDeadline() {
        return registrationDeadline;
    }

    public void setRegistrationDeadline(Date registrationDeadline) {
        this.registrationDeadline = registrationDeadline;
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

    public List<EventPerson> getEventPersons() {
        return eventPersons;
    }

    public void setEventPersons(List<EventPerson> eventPersons) {
        this.eventPersons = eventPersons;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    public Person getCreator() {
        return creator;
    }

    @Override
    public String toString() {
        return "entity.Event[ id=" + id + " ]";
    }

    public void setCreator(Person person) {
        this.creator = person; // Assign the person to the creator field
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /*public List<Person> getAttendedPersons() {
        return attendedPersons;
    }

    public void setAttendedPersons(List<Person> attendedPersons) {
        this.attendedPersons = attendedPersons;
    }

    public List<Person> getRegisteredPersons() {
        return registeredPersons;
    }

    public void setRegisteredPersons(List<Person> registeredPersons) {
        this.registeredPersons = registeredPersons;
    }
     */
}
