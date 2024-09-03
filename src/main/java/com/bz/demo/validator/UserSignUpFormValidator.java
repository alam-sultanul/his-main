package com.bz.demo.validator;
/**
 * created by srana on 27/4/21 at 12.53 AM
 */
import com.bz.demo.controller.signUp.SignUpForm;
import com.bz.demo.model.security.User;
import com.bz.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class UserSignUpFormValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpForm.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        SignUpForm form = (SignUpForm) target;
        User user;
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "fullName", "fullName.required", "Please Enter Full Name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "userName.required", "Please Enter Login User Name");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userType", "userType.required", "Please Select User Type");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "Please Enter Email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contactNo", "contactNo.required", "Please Enter Contact Number");


        if(!form.isPersisted()){
            if (userService.isUsernameExists(form.getUserName())) {
                errors.rejectValue("userName", "duplicate.userName");
            }
            if (userService.isUserEmailExists(form.getEmail())) {
                errors.rejectValue("email", "duplicate.email");
            }
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Please Enter Password");
        }
        else {
            user=userService.getUser(form.getId());
            if(!Objects.equals(user.getUsername(),form.getUserName())){
                if (userService.isUsernameExists(form.getUserName())) {
                    errors.rejectValue("userName", "duplicate.userName");
                }
            }
            if(!Objects.equals(user.getEmailAddress(),form.getEmail())){
                if (userService.isUserEmailExists(form.getEmail())) {
                    errors.rejectValue("email", "duplicate.email");
                }
            }
        }
    }
}
