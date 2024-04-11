package validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.Date;

@FacesValidator("dateRangeValidator")
public class DateRangeValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }

        // Assuming the component has an attribute "registrationDeadline"
        Object endDateValue = component.getAttributes().get("registrationDeadline");
        if (endDateValue == null || !(endDateValue instanceof Date)) {
            return; // Or handle appropriately
        }

        Date startDate = (Date) value;
        Date endDate = (Date) endDateValue;

        if (startDate.after(endDate)) {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Event date must be before the registration deadline.", null));
        }
    }
}
