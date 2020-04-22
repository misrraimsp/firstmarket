package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ImagesWrapper;
import misrraimsp.uned.pfg.firstmarket.exception.BadImageException;
import misrraimsp.uned.pfg.firstmarket.exception.ImageNotFoundException;
import misrraimsp.uned.pfg.firstmarket.exception.NoDefaultImageException;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.service.BookServer;
import misrraimsp.uned.pfg.firstmarket.service.CatServer;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class ImageController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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

        LOGGER.trace("{} created", this.getClass().getName());
    }

    @GetMapping("/image/{id}")
    public String showImageById(@PathVariable("id") Long id,
                              HttpServletResponse response) {

        InputStream inputStream = null;
        try {
            Image image = imageServer.findById(id);
            response.setContentType(image.getMimeType());
            inputStream = new ByteArrayInputStream(image.getData());
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (ImageNotFoundException e) {
            LOGGER.warn("Image not found", e);
            return "redirect:/home";
        } catch (IOException e) {
            LOGGER.error("File exception", e);
            return "redirect:/home";
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("InputStream closing exception", e);
                    return "redirect:/home";
                }
            }
        }
        return null;
    }

    @GetMapping("/admin/images")
    public String showImages(@RequestParam(defaultValue = "${pagination.default-index}") String pageNo,
                             @RequestParam(defaultValue = "${pagination.default-size.image}") String pageSize,
                             Model model){

        model.addAttribute("imagesWrapper", new ImagesWrapper());
        populateModel(model, pageNo, pageSize);
        return "images";
    }

    @PostMapping("/admin/newImage")
    public String processNewImage(@Valid ImagesWrapper imagesWrapper,
                                  Errors errors,
                                  @RequestParam(defaultValue = "${pagination.default-index}") String pageNo,
                                  @RequestParam(defaultValue = "${pagination.default-size.image}") String pageSize,
                                  Model model){

        if (errors.hasErrors()) {
            populateModel(model, pageNo, pageSize);
            return "images";
        }
        try {
            imagesWrapper.getImages().forEach(image -> {
                Image persistedImage = imageServer.persist(image);
                LOGGER.trace("Image persisted (id={})", persistedImage.getId());
            });
        }
        catch (ImageNotFoundException e) {
            LOGGER.error("Trying to persist an Image-with-id that is not in the database searching by its id", e);
            return "redirect:/home";
        }
        catch (BadImageException e) {
            LOGGER.error("Trying to persist an image without data or id", e);
            return "redirect:/home";
        }
        return "redirect:/admin/images";
    }

    @PostMapping("/admin/setDefaultImage")
    public ModelAndView processSetDefaultImage(@RequestParam(name = "id") Optional<Long> optImageId,
                                               @RequestParam(name = "pn") Optional<String> optPageNo) {

        AtomicBoolean imageNotFound = new AtomicBoolean(false);
        optImageId.ifPresent(imageId -> {
            try {
                imageServer.setDefaultImage(imageId);
                LOGGER.trace("Default image changed (id={})", imageId);
            } catch (ImageNotFoundException e){
                LOGGER.warn("Trying to set a non-existent image as default", e);
                imageNotFound.set(true);
            }
        });
        ModelAndView modelAndView = new ModelAndView();
        if (imageNotFound.get()) {
            modelAndView.setViewName("redirect:/home");
            return modelAndView;
        }
        modelAndView.setViewName("redirect:/admin/images");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        return modelAndView;
    }

    @GetMapping("/admin/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") Long imageId) {
        try {
            if (imageServer.isDefaultImage(imageId)) {
                LOGGER.warn("Trying to delete the default image (id={})", imageId);
                return "redirect:/home";
            }
            bookServer.updateImageByImageId(imageId, imageServer.getDefaultImage());
            imageServer.deleteById(imageId);
            LOGGER.trace("Image deleted (id={})", imageId);
        }
        catch (ImageNotFoundException e) {
            LOGGER.warn("Trying to delete a non-existent image", e);
            return "redirect:/home";
        }
        catch (NoDefaultImageException e) {
            LOGGER.error("There is no default image", e);
            return "redirect:/home";
        }
        return "redirect:/admin/images";
    }

    private void populateModel(Model model,
                               String pageNo,
                               String pageSize) {

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                Integer.parseInt(pageSize),
                Sort.by("mime_type").descending().and(Sort.by("name").ascending()));

        model.addAttribute("pageOfEntities", imageServer.getPageOfMetaInfo(pageable));
        model.addAttribute("mainCategories", catServer.getMainCategories());
    }
}
