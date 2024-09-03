package com.bz.demo.controller.signUp;

import com.bz.demo.repository.RoleRepository;
import com.bz.demo.controller.FileUploadSupport;
import com.bz.demo.controller.WebLinkFactory;
import com.bz.demo.facade.SessionManagementService;
import com.bz.demo.facade.data.UserData;
import com.bz.demo.service.DocumentService;
import com.bz.demo.service.UserService;
import com.bz.demo.validator.SignUpFormValidator;
import com.bz.demo.validator.UserSignUpFormValidator;
import com.bz.demo.model.common.UserStatus;
import com.bz.demo.model.common.UserType;
import com.bz.demo.model.doc.Document;
import com.bz.demo.model.doc.DocumentSource;

import com.bz.demo.model.security.User;
import com.bz.demo.util.Constants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class SignUpController {

    @NonNull
    private final UserService userService;
    @NonNull
    private final SessionManagementService sessionManagementService;
    @NonNull private final PasswordEncoder passwordEncoder;
    @NonNull private final SignUpFormValidator signUpFormValidator;
    @NonNull private final RoleRepository roleRepository;
    @NonNull private final UserSignUpFormValidator userSignUpFormValidator;
    @NonNull private final DocumentService documentService;
    @NonNull private final FileUploadSupport fileUploadSupport;
    @NonNull private final WebLinkFactory webLinkFactory;

    private static final String SIGN_UP ="/sign-up";
    private static final String LOGON ="/login";
    private static final String SIGN_SEARCH_ALL ="/sign-up/search/all";
    private static final String NEW_USER_CREATE =SIGN_UP+"/new/user/create/update";
    private static final String NEW_USER_EDIT =SIGN_UP+"/new/user/create/update/edit/{id}";
    private static final String NEW_USER_SAVE =SIGN_UP+"/new/user/create/update/save";
    private static final String SIGN_UP_CHANGE_PASSWORD =SIGN_UP+"/user/password-change";
    private static final String ROUTE_CHANGE_PASSWORD_SAVE= SIGN_UP + "/change-password/save";
    public static final String ROUTE_PROFILE_VIEW= SIGN_UP + "/personal/user-profile-view/update/{id}";
    private static final String ROUTE_PROFILE_UPDATE= SIGN_UP + "/user-profile-view/personal-info/update";

    private static final String REDIRECT = "redirect:";

    @GetMapping(SIGN_UP)
    public String newSignUp(Model model) {
        populateSignUpForm(model,new SignUpForm());
        return "/web/pages/signUp/signUp";
    }

    @GetMapping(NEW_USER_CREATE)
    public String newUserCreate(Model model) {
        populateSignUpForm(model,new SignUpForm());
        return "/web/pages/signUp/create";
    }

    private void populateSignUpForm(Model model,SignUpForm signUpForm){
        model.addAttribute("signUpForm",signUpForm);
        model.addAttribute("userTypes", UserType.values());
        model.addAttribute("userStatusList", UserStatus.values());
        model.addAttribute("userStatus", UserStatus.values());
    }

    @PostMapping(SIGN_UP)
    public String saveUser(Model model,  @ModelAttribute("signUpForm") SignUpForm signUpForm, BindingResult result, RedirectAttributes redirectAttributes) {
        signUpFormValidator.validate(signUpForm,result);
        if (result.hasErrors()) {
            populateSignUpForm(model,signUpForm);
            return "/web/pages/signUp/signUp";
        }
        redirectAttributes.addFlashAttribute("message", "signUp.created");
        return REDIRECT + LOGON;
    }

    @GetMapping(SIGN_SEARCH_ALL)
    public String newEmployeeSignUp(Model model) {
        populateSignUpForm(model,new SignUpForm());
        return "/web/pages/signUp/search";
    }

    @PostMapping(SIGN_SEARCH_ALL)
    public String searchUsers(Model model,@RequestParam(name = "userType", required = false) UserType userType, @RequestParam(name = "userName", required = false) String userName, @RequestParam(name = "userStatus", required = false) UserStatus userStatus) {
        List<UserData> userDataList=userService.userList(userName,userType,userStatus);
        populateSignUpForm(model,new SignUpForm());
        model.addAttribute("userName",userName);
        model.addAttribute("userType",userType);
        model.addAttribute("uStatus",userStatus);
        model.addAttribute("userList",userDataList);
        return "/web/pages/signUp/search";
    }

    @PostMapping(NEW_USER_SAVE)
    public String createNewUser(Model model,  @ModelAttribute("signUpForm") SignUpForm signUpForm, BindingResult result, RedirectAttributes redirectAttributes) {
        userSignUpFormValidator.validate(signUpForm,result);
        if (result.hasErrors()) {
            populateSignUpForm(model,signUpForm);
            return "/web/pages/signUp/create";
        }
        User user=prepareNewUser(signUpForm);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("message", "signUp.created");
        return REDIRECT + SIGN_SEARCH_ALL;
    }



    @GetMapping(SIGN_UP_CHANGE_PASSWORD)
    public String changePassword(Model model) {
        User user=userService.findUserById(sessionManagementService.getAuthenticatedUser().getId()).orElse(null);
        model.addAttribute("user",user);
        return "/web/pages/signUp/change-password";
    }

    @PostMapping(ROUTE_CHANGE_PASSWORD_SAVE)
    public String updateUserProfile(Model model, @RequestParam(name="password",required = false) String password,RedirectAttributes redirectAttributes)  {
        User user=userService.findUserById(sessionManagementService.getAuthenticatedUser().getId()).orElse(null);
        if (user.getId() != null) {
            user.setPassword(passwordEncoder.encode(password));
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("message","user.password.change");
        }
        return REDIRECT+SIGN_UP_CHANGE_PASSWORD;
    }

    @GetMapping(ROUTE_PROFILE_VIEW)
    public String showProfile(Model model,@PathVariable Long id) {
        User user=userService.findUserById(id).orElse(null);
        model.addAttribute("user",user);
        if(user.getId() !=null){
            if (hasUserPhotoId(user)!=null) {
                documentService.findDocumentById(hasUserPhotoId(user)).ifPresent(document -> {
                    if (document.isImageFile()) {
                        model.addAttribute("userImage", document);
                    }
                });
            }
            else {
                model.addAttribute("userImage", new Document());
            }
        }
        else  model.addAttribute("userImage", new Document());

        return "/web/pages/signUp/profile";
    }

    private Long hasUserPhotoId(User user) {
        Document document=documentService.findDocumentBySourceAndObject(DocumentSource.USER_PHOTO,user.getId()).orElse(new Document());
        Long userPhotoId=null;
        if(document !=null){
            userPhotoId=document.getId();
        }
        return userPhotoId;
    }

    @PostMapping(ROUTE_PROFILE_UPDATE)
    public String updateUserProfile(Model model, @RequestParam(name="id",required = true) Long id, @RequestParam(name="fullName",required = false) String fullName,@RequestParam(name="contactNo",required = false) String contactNo, @RequestParam(name="emailAddress") String email, @RequestParam(value = "userImageFile", required = false) MultipartFile userImageFile, RedirectAttributes redirectAttributes) {
        User user=userService.findUserById(id).orElse(null);
        if(user !=null){
            user.setFullName(fullName);
            user.setEmailAddress(email);
            user.setContactNo(contactNo);
            userService.saveUser(user);
            if (userImageFile.getSize() > 0) {
                Document userPhotoDocument = fileUploadSupport.uploadUserProfilePicture(user, userImageFile, hasUserPhotoId(user));
                userPhotoDocument = documentService.saveDocument(userPhotoDocument);
            }
        }


        redirectAttributes.addFlashAttribute("message","user.information.update");
        return REDIRECT+webLinkFactory.editUserProfileUrl(user.getId());
    }

    @GetMapping(NEW_USER_EDIT)
    public String editAllUser(Model model, @PathVariable Long id){
        User user=userService.getUser(id);
        SignUpForm signUpForm=new SignUpForm(user);
        populateSignUpForm(model,signUpForm);
        return "/web/pages/signUp/create";
    }


    private User prepareNewUser(SignUpForm signUpForm){
        User user;
        if(signUpForm.isPersisted()){
            user=userService.getUser(signUpForm.getId());
            user.setUpdatedBy(sessionManagementService.getAuthenticatedUser().getId());
        }
        else{
            user=User.builder()
                    .activeStatus(Constants.ACTIVE_STATUS)
                    .build();
        }
        user.setUserType(signUpForm.getUserType());
        user.setFullName(signUpForm.getFullName());
        user.setUsername(signUpForm.getUserName());
        user.setEmailAddress(signUpForm.getEmail());
        user.setContactNo(signUpForm.getContactNo());
        if(!signUpForm.isPersisted()){
            user.setPassword(passwordEncoder.encode(signUpForm.getPassword()));
        }
        user.setUserStatus(signUpForm.isPersisted() ? signUpForm.getUserStatus() : UserStatus.ACTIVE);
        return user;
    }
}
