package misrraimsp.uned.pfg.firstmarket.controller;

import misrraimsp.uned.pfg.firstmarket.model.Image;
import misrraimsp.uned.pfg.firstmarket.service.ImageServer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
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
        model.addAttribute("title", "Images Manager");
        model.addAttribute("ids", imageServer.getIds());
        return "images";
    }

    @GetMapping("/image/{id}")
    public void showImageById(@PathVariable("id") Long id, HttpServletResponse response) throws IOException {
        Image image = imageServer.findById(id);
        response.setContentType(image.getMimeType());
        InputStream is = new ByteArrayInputStream(image.getData());
        IOUtils.copy(is, response.getOutputStream());
    }
}
