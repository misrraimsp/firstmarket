package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.adt.dto.ImagesWrapper;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.service.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

@Controller
public class ImageController extends BasicController  {

    @Autowired
    public ImageController(UserServer userServer,
                           BookServer bookServer,
                           CatServer catServer,
                           ImageServer imageServer,
                           MessageSource messageSource,
                           OrderServer orderServer) {

        super(userServer, bookServer, catServer, imageServer, messageSource, orderServer);
    }

    private void populateModelToImage(Model model,
                                      String pageNo,
                                      int size) {

        Pageable pageable = PageRequest.of(
                Integer.parseInt(pageNo),
                size,
                Sort.by("mime_type").descending().and(Sort.by("name").ascending()));

        model.addAttribute("pageOfEntities", imageServer.getPageOfMetaInfo(pageable));
    }

    @GetMapping("/image/{id}")
    public void showImageById(@PathVariable("id") Long id,
                              HttpServletResponse response) throws IOException {

        Image image = imageServer.findById(id);
        response.setContentType(image.getMimeType());
        try (InputStream inputStream = new ByteArrayInputStream(image.getData())) {
            IOUtils.copy(inputStream, response.getOutputStream());
        }
    }

    @GetMapping("/admin/images")
    public String showImages(@RequestParam(defaultValue = "${fm.pagination.default-index}") String pageNo,
                             @RequestParam(defaultValue = "${fm.pagination.default-size-index.image}") PageSize pageSize,
                             Model model) {

        populateModel(model.asMap(), null);
        populateModelToImage(model, pageNo, pageSize.getSize());
        model.addAttribute("imagesWrapper", new ImagesWrapper());
        return "images";
    }

    @PostMapping("/admin/newImage")
    public String processNewImage(@Valid ImagesWrapper imagesWrapper,
                                  Errors errors,
                                  @RequestParam(defaultValue = "${fm.pagination.default-index}") String pageNo,
                                  @RequestParam(defaultValue = "${fm.pagination.default-size-index.image}") PageSize pageSize,
                                  Model model){

        if (errors.hasErrors()) {
            populateModel(model.asMap(), null);
            populateModelToImage(model, pageNo, pageSize.getSize());
            return "images";
        }
        imagesWrapper.getImages().forEach(image -> {
            Image persistedImage = imageServer.persist(image);
            LOGGER.debug("Image persisted (id={})", persistedImage.getId());
        });
        return "redirect:/admin/images";
    }

    @PostMapping("/admin/setDefaultImage")
    public ModelAndView processSetDefaultImage(ModelAndView modelAndView,
                                               @RequestParam(name = "id") Optional<Long> optImageId,
                                               @RequestParam(name = "pn") Optional<String> optPageNo) {

        optImageId.ifPresent(imageId -> {
            imageServer.setDefaultImage(imageId);
            LOGGER.debug("Default image changed (id={})", imageId);
        });
        modelAndView.setViewName("redirect:/admin/images");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        return modelAndView;
    }

    @Transactional
    @GetMapping("/admin/deleteImage/{id}")
    public String deleteImage(@PathVariable("id") Long imageId) {
        if (imageServer.isDefaultImage(imageId)) {
            LOGGER.warn("Trying to delete the default image (id={})", imageId);
            return "redirect:/home";
        }
        bookServer.updateImageByImageId(imageId, imageServer.getDefaultImage());
        imageServer.deleteById(imageId);
        LOGGER.debug("Image deleted (id={})", imageId);
        return "redirect:/admin/images";
    }
}
