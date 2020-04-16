package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ImagesWrapper;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    private ImageServer imageServer;
    private BookServer bookServer;
    private CatServer catServer;

    @Autowired
    public ImageController(ImageServer imageServer,
                           BookServer bookServer,
                           CatServer catServer) {

        this.imageServer = imageServer;
        this.bookServer = bookServer;
        this.catServer = catServer;
    }

    @GetMapping("/image/{id}")
    public void showImageById(@PathVariable("id") Long id, HttpServletResponse response) {
        try {
            Image image = imageServer.findById(id);
            response.setContentType(image.getMimeType());
            InputStream is = new ByteArrayInputStream(image.getData());
            IOUtils.copy(is, response.getOutputStream());
        } catch (IOException | IllegalArgumentException e) {
            // TODO log this situation
        }
    }

    @GetMapping("/admin/images")
    public String showImages(Model model){
        model.addAttribute("imagesWrapper", new ImagesWrapper());
        populateModel(model);
        return "images";
    }

    @PostMapping("/admin/newImage")
    public String processNewImage(@Valid ImagesWrapper imagesWrapper, Errors errors, Model model){
        if (errors.hasErrors()) {
            populateModel(model);
            return "images";
        }
        try {
            imagesWrapper.getImages().forEach(image -> imageServer.persist(image));
        }
        catch (IllegalArgumentException e) {
            // TODO log this situation
            return "redirect:/home";
        }
        return "redirect:/admin/images";
    }

    @GetMapping("/admin/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") Long imageId){
        try {
            if (imageServer.isDefaultImage(imageId)) {
                // TODO log this situation
                System.out.println("trying to delete default image");
                return "redirect:/home";
            }
            bookServer.updateImageByImageId(imageId, imageServer.getDefaultImage());
            imageServer.deleteById(imageId);
        } catch (IllegalArgumentException e) {
            // TODO log this situation
            System.out.println("image does not exist: " + e.getMessage());
            return "redirect:/home";
        }
        return "redirect:/admin/images";
    }

    private void populateModel(Model model) {
        model.addAttribute("allMetaInfo", imageServer.getAllMetaInfo());
        model.addAttribute("mainCategories", catServer.getMainCategories());
    }
}
