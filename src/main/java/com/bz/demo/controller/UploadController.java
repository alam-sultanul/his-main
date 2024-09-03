package com.bz.demo.controller;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class UploadController {

    @NonNull private final FileUploadSupport fileUploadSupport;

    private static final String BASE_ROUTE = "/upload";

    @Value("${app.tmp.upload.dir:${user.home}}")
    public String tmpUploadDir;

    @RequestMapping(value = BASE_ROUTE, method = RequestMethod.POST)
    public ModelAndView upload(@RequestParam("file") MultipartFile file, ModelMap modelMap) {
        MappingJackson2JsonView view = new MappingJackson2JsonView();
        view.setContentType("text/plain");

        modelMap.addAttribute("responseCode", 1);
        modelMap.addAttribute("originalFilename", file.getOriginalFilename());

        if (!(file.getSize() > 0)) {
            modelMap.addAttribute("responseMsg", "Please choose a file to upload");
            return new ModelAndView(view, modelMap);
        }

        String uploadedFilename = StringUtils.trimToNull(StringUtils.trimToNull(fileUploadSupport.cleanUploadFilename(file.getOriginalFilename() + "")));
        if (!fileUploadSupport.isValidFileName(uploadedFilename)) {
            modelMap.addAttribute("responseMsg", "File type is not supported");
            return new ModelAndView(view, modelMap);
        }

        String fileName = fileUploadSupport.addSuffixToFileName(uploadedFilename, UUID.randomUUID().toString().substring(0, 6));

        try {
            Path copyLocation = Paths.get(tmpUploadDir + File.separator + fileName);
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

            modelMap.addAttribute("file", fileName);
            modelMap.addAttribute("responseCode", 0);
            modelMap.addAttribute("responseMsg", "Success");
        } catch (Exception e) {
            modelMap.addAttribute("responseMsg", "Could not store file " + file.getOriginalFilename()
                    + ". Please try again!");
        }

        return new ModelAndView(view, modelMap);
    }
}
