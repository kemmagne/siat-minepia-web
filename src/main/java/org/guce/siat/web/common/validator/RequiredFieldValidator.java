package org.guce.siat.web.common.validator;

import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author ht
 */
@FacesValidator("org.guce.siat.web.common.validator.RequiredFieldValidator")
public class RequiredFieldValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (!continueValidation()) {
            return;
        }
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "validation error", "validation error");
        throw new ValidatorException(facesMsg);
    }

    protected boolean continueValidation() {
        Map<String, String> map = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String skipValidator = map.get("skipValidator");
        return !(skipValidator != null && skipValidator.equalsIgnoreCase("true"));
    }

}
