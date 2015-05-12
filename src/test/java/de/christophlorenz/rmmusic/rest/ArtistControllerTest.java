package de.christophlorenz.rmmusic.rest;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.RequestBuilder;
import org.springframework.test.web.server.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;
import java.util.List;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

/**
 * Created by clorenz on 08.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
//@DatabaseSetup("toDoData.xml")
@Ignore
public class ArtistControllerTest {

    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    @Autowired
    ArtistRepository artistRepository;

    private MockMvc mockMvc;

    @Before
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(artistRepository).build();
    }

    @Test
    public void selectFromEmptyArtists() throws Exception {
        mockMvc.perform(get("/artist/0")
                .contentType(APPLICATION_JSON_UTF8))
                .andExpect((status().isNotFound()));
    }

    @Test
    public void createAndRetrieveArtist() throws Exception {
        Artist artist = new Artist();
        artist.setName("First artist");

        RequestBuilder postBuilder = post("artist", artist).contentType(APPLICATION_JSON_UTF8);

        System.out.println(postBuilder);

        mockMvc.perform(postBuilder)
                .andExpect(status().isCreated());
    }
}
