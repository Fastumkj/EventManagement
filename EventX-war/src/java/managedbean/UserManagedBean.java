/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package managedbean;

import entity.Person;
import exception.NoResultException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;
import session.PersonSessionLocal;

@Named(value = "userManagedBean")
@SessionScoped
public class UserManagedBean implements Serializable {

    @Inject
    private PersonSessionLocal personSessionLocal;

    private long pId;
    private String name;
    private String contactDetails;
    private Date dob; // Assuming the format is "yyyy-MM-dd"
    private byte[] profilePhoto; // This will be handled differently as it's binary data
    private UploadedFile uploadedFile; //upload for profilephoto
    private byte gender;
    private String email;
    private String password;
    private String uniqueId;

    private List<Person> people;

    private Person loggedInPerson;
    private boolean editMode;

    private static final Logger logger = Logger.getLogger(UserManagedBean.class.getName());

    //method to initialise the logged-in user
    @PostConstruct
    public void init() {
        loggedInPerson = (Person) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("loggedInPerson");
        if (loggedInPerson == null) {
            loadUserDetailsIntoBean(null);
        }
    }

    private void loadUserDetailsIntoBean(ComponentSystemEvent event) {
        if (loggedInPerson != null) {
            name = loggedInPerson.getName();
            email = loggedInPerson.getEmail();
            dob = loggedInPerson.getDob();
            contactDetails = loggedInPerson.getContactDetails();
            gender = loggedInPerson.getGender();
            System.out.println("managedbean.UserManagedBean.loadUserDetailsIntoBean()");
            // Add other fields as necessary
        }
    }

    // Method to toggle edit mode
    public void toggleEditMode() {
        editMode = !editMode;
    }

    public String login() {
        logger.warning("Attempting login for email with WARNING level: " + email);
        System.out.println("Login button clicked.");
        loggedInPerson = personSessionLocal.findPersonByEmail(email);
        if (loggedInPerson != null && loggedInPerson.getPassword().equals(password)) {
            loadUserDetailsIntoBean(null);
            // Login successful
            FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().getSessionMap().put("loggedInPerson", loggedInPerson);
            pId = loggedInPerson.getId();

            return "/searchEvents.xhtml?faces-redirect=true";
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login failed, Wrong Email or Password.", null));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "/home.xhtml?faces-redirect=true";
    }

    public void loadSelectedCustomer(ComponentSystemEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            this.loggedInPerson = personSessionLocal.getPerson(pId);
            name = this.loggedInPerson.getName();
            gender = this.loggedInPerson.getGender();
            dob = this.loggedInPerson.getDob();
            contactDetails = this.loggedInPerson.getContactDetails();
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Unable to load customer"));
        }
    } //end loadSelectedCustomer

    // Method to update user profile
    public void updateProfile() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        loggedInPerson.setName(name);
        loggedInPerson.setGender(gender);
        loggedInPerson.setDob(dob);
        loggedInPerson.setContactDetails(contactDetails);
        System.err.println("contactdeets updateprofile: " + contactDetails);

        try {
            personSessionLocal.updatePerson(loggedInPerson);
            loggedInPerson = personSessionLocal.findPersonByEmail(email);
            context = FacesContext.getCurrentInstance();
            context.getExternalContext().redirect("createEvent.xhtml");
        } catch (NoResultException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error updating profile.", null));
        }
        //need to make sure reinitialize the customers collection
        init();
        context.addMessage(null, new FacesMessage("Success", "Successfully updated customer"));
    }

    public StreamedContent getProfileImage() {
        if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // Return a stub StreamedContent so that it will generate right URL.
            return DefaultStreamedContent.builder().build();
        } else if (loggedInPerson.getProfilePhoto() != null) {
            return DefaultStreamedContent.builder()
                    .stream(() -> new ByteArrayInputStream(loggedInPerson.getProfilePhoto()))
                    .contentType("image/png") // Or whichever MIME type is appropriate
                    .build();
        } else {
            // Return a default image or a placeholder
            return null;
        }
    }

    public void handleFileUpload(FileUploadEvent event) throws NoResultException {
        System.out.println("File upload listened");
        UploadedFile file = event.getFile();
        if (file != null && file.getContent() != null) {
            System.out.println("File name: " + file.getFileName());
            // Here, directly set the profile photo to the user and save
            loggedInPerson.setProfilePhoto(file.getContent());
            personSessionLocal.updatePerson(loggedInPerson);
            try {
                personSessionLocal.updatePerson(loggedInPerson); // Save the updated person
                FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded.");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (NoResultException e) {
                e.printStackTrace();
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Upload failed", "Failed to upload " + file.getFileName());
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public void removeProfileImage() {
        loggedInPerson.setProfilePhoto(null); // Remove the profile photo
        try {
            personSessionLocal.updatePerson(loggedInPerson); // Save the updated person without the profile photo
            FacesMessage message = new FacesMessage("Successful", "Profile image removed.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } catch (NoResultException e) {
            e.printStackTrace();
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Remove failed", "Failed to remove profile image.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public PersonSessionLocal getPersonSessionLocal() {
        return personSessionLocal;
    }

    public void setPersonSessionLocal(PersonSessionLocal personSessionLocal) {
        this.personSessionLocal = personSessionLocal;
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

    public Person getLoggedInPerson() {
        return loggedInPerson;
    }

    public void setLoggedInPerson(Person loggedInPerson) {
        this.loggedInPerson = loggedInPerson;
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

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public byte[] getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(byte[] profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public byte getGender() {
        return gender;
    }

    public void setGender(byte gender) {
        this.gender = gender;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public long getpId() {
        return pId;
    }

    public void setpId(long pId) {
        this.pId = pId;
    }

    public static Logger getLogger() {
        return logger;
    }

    // Method to navigate to the search event page after successful login
    public String navigateToSearchEvent() {
        return loggedInPerson != null ? "auth/searchEvent.xhtml?faces-redirect=true" : null;
    }

    public String navigateToViewProfile() {
        return loggedInPerson != null ? "auth/viewProfile.xhtml?faces-redirect=true" : null;
    }

    public String getLoggedInPersonName() {
        return loggedInPerson != null ? loggedInPerson.getName() : null;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

}
