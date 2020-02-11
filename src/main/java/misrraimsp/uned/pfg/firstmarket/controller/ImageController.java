package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    ImageServer imageServer;

    @Autowired
    public ImageController(ImageServer imageServer) {
        this.imageServer = imageServer;
    }

    @GetMapping("/admin/images")
    public String showImages(Model model){
        model.addAttribute("title", "Images");
        model.addAttribute("logoId", imageServer.getDefaultImageId());
        model.addAttribute("image", new Image());
        model.addAttribute("allMetaInfo", imageServer.getAllMetaInfo());
        return "images";
    }

    @GetMapping("/image/{id}")
    public void showImageById(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Image image = imageServer.findById(id);
        response.setContentType(image.getMimeType());
        InputStream is = new ByteArrayInputStream(image.getData());
        IOUtils.copy(is, response.getOutputStream());
    }

    @PostMapping("/admin/newImage")
    public String processNewImage(@RequestParam Image image, Model model){
        try {
            imageServer.persist(image);
        }
        catch (TransactionSystemException e) {
            Throwable t = e.getCause();
            while ((t != null) && !(t instanceof ConstraintViolationException)) {
                t = t.getCause();
            }
            if (t instanceof ConstraintViolationException) {
                ConstraintViolationException cve = (ConstraintViolationException) t;
                model.addAttribute("title", "Images");
                model.addAttribute("logoId", imageServer.getDefaultImageId());
                model.addAttribute("image", new Image());
                model.addAttribute("allMetaInfo", imageServer.getAllMetaInfo());
                model.addAttribute("constraintViolations", cve.getConstraintViolations());
                return "images";
            }
        }
        return "redirect:/admin/images";
    }

    @GetMapping("/admin/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") Long id){
        imageServer.deleteById(id);
        return "redirect:/admin/images";
    }
}
