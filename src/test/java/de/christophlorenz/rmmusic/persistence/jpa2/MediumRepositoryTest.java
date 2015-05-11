package de.christophlorenz.rmmusic.persistence.jpa2;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by clorenz on 11.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class MediumRepositoryTest {

    @Autowired
    private MediumRepository repository;

    @Autowired
    private ArtistRepository artistRepository;

    @Before
    public void before() {
        repository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test
    public void findWithInvalidIdReturnsEmptyList() {
        List<Medium> ret = repository.findById(0);

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(0));
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetCode() {
        Medium medium = new Medium();
        medium.setType(Medium.CD);

        repository.save(medium);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetType() {
        Medium medium = new Medium();
        medium.setCode("12345678");

        repository.save(medium);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetTypeAndCode() {
        Medium medium = new Medium();
        medium.setTitle("Title");

        repository.save(medium);
    }

    @Test
    public void saveAndRetrievePlainMediumWithoutArtistReference() {
        Medium medium = new Medium();
        medium.setCode("12345678");
        medium.setType(Medium.CD);
        medium.setTitle("Media Title");
        repository.save(medium);

        Medium ret = repository.findAll().get(0);

        assertThat(ret.getCode(), is("12345678"));
        assertThat(ret.getType(), is(Medium.CD));
        assertThat(ret.getTitle(), is("Media Title"));
    }

    @Test
    public void saveAndRetrieveByTypeAndCode() {
        Medium medium = new Medium();
        medium.setCode("12345678");
        medium.setType(Medium.CD);
        medium.setTitle("Media Title");
        repository.save(medium);

        Medium ret = repository.findByTypeAndCode(Medium.CD, "12345678");

        assertThat(ret.getCode(), is("12345678"));
        assertThat(ret.getType(), is(Medium.CD));
        assertThat(ret.getTitle(), is("Media Title"));
    }

    @Test
    public void saveArtistSaveMediumRetrieveMediumWithArtist() {
        Artist artist = new Artist();
        artist.setName("Demo Artist");
        artist = artistRepository.save(artist);

        Medium medium = new Medium();
        medium.setCode("10000001");
        medium.setType(Medium.CD);
        medium.setTitle("Demo Title");
        medium.setArtist(artist);
        repository.save(medium);

        Medium ret = repository.findByTypeAndCode(Medium.CD,"10000001");

        assertThat(ret.getCode(), is("10000001"));
        Artist retArtist = ret.getArtist();
        assertThat(retArtist, is(not(nullValue())));
        assertThat(retArtist.getName(), is("Demo Artist"));
    }
}
