package misrraimsp.uned.pfg.firstmarket.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HomeControllerTest {

    @InjectMocks
    private HomeController homeController;

    /*
    @Mock
    private UserServer userServer;

    @Mock
    private BookServer bookServer;

    @Mock
    private CatServer catServer;

    @Mock
    private ImageServer imageServer;

    @Mock
    private MessageSource messageSource;

     */

    @BeforeEach
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void redirectHomeTest(){
        //HomeController homeController = new HomeController(userServer, bookServer, catServer, imageServer, messageSource);
        String result = homeController.redirectHome();
        assertEquals(result, "redirect:/home");
    }
}
