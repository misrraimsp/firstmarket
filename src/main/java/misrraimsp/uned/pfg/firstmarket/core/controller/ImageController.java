package misrraimsp.uned.pfg.firstmarket.core.controller;

import misrraimsp.uned.pfg.firstmarket.config.staticParameter.PageSize;
import misrraimsp.uned.pfg.firstmarket.config.staticParameter.sort.ImageSortCriteria;
import misrraimsp.uned.pfg.firstmarket.core.model.Image;
import misrraimsp.uned.pfg.firstmarket.core.service.*;
import misrraimsp.uned.pfg.firstmarket.util.adt.dto.ImagesWrapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
                                      int pageNo,
                                      PageSize pageSize,
                                      ImageSortCriteria sort) {

        Pageable pageable = PageRequest.of(pageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
        Page<Image> imagePage = imageServer.getPageOfMetaInfo(pageable);
        int lastPageNo = imagePage.getTotalPages() - 1;
        if (lastPageNo > 0 && lastPageNo < pageNo) {
            pageable = PageRequest.of(lastPageNo, pageSize.getSize(), sort.getDirection(), sort.getProperty());
            imagePage = imageServer.getPageOfMetaInfo(pageable);
        }
        model.addAttribute("pageOfEntities", imagePage);
        model.addAttribute("sort", sort);
        model.addAttribute("pageSize", pageSize);
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
    public String showImages(@RequestParam(defaultValue = "${fm.pagination.default-index}") int pageNo,
                             @RequestParam(defaultValue = "${fm.pagination.default-size-index.image}") PageSize pageSize,
                             @RequestParam(defaultValue = "${fm.pagination.default-sort-index.image}") ImageSortCriteria sort,
                             Model model) {

        populateModel(model.asMap(), null);
        populateModelToImage(model, pageNo, pageSize, sort);
        model.addAttribute("imagesWrapper", new ImagesWrapper());
        return "images";
    }

    @PostMapping("/admin/newImage")
    public String processNewImage(@Valid ImagesWrapper imagesWrapper,
                                  Errors errors,
                                  @RequestParam(defaultValue = "${fm.pagination.default-index}") int pageNo,
                                  @RequestParam(defaultValue = "${fm.pagination.default-size-index.image}") PageSize pageSize,
                                  @RequestParam(defaultValue = "${fm.pagination.default-sort-index.image}") ImageSortCriteria sort,
                                  Model model){

        if (errors.hasErrors()) {
            populateModel(model.asMap(), null);
            populateModelToImage(model, pageNo, pageSize, sort);
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
                                               @RequestParam Long imageId,
                                               @RequestParam(name = "pageNo") Optional<String> optPageNo,
                                               @RequestParam(name = "pageSize") Optional<String> optPageSize,
                                               @RequestParam(name = "sort") Optional<String> optSort){

        imageServer.setDefaultImage(imageId);
        LOGGER.debug("Default image changed (id={})", imageId);
        modelAndView.setViewName("redirect:/admin/images");
        optPageNo.ifPresent(pageNo -> modelAndView.addObject("pageNo", pageNo));
        optPageSize.ifPresent(pageSize -> modelAndView.addObject("pageSize", pageSize));
        optSort.ifPresent(sort -> modelAndView.addObject("sort", sort));
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
