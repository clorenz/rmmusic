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
public class MediumRepositoryTest extends BaseRepositoryTest{

    @Test
    public void findWithInvalidIdReturnsEmptyList() {
        List<Medium> ret = mediumRepository.findById(0);

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(0));
    }


    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetCode() {
        Medium medium = new Medium();
        medium.setType(Medium.CD);

        mediumRepository.save(medium);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetType() {
        Medium medium = new Medium();
        medium.setCode("12345678");

        mediumRepository.save(medium);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void rejectSaveWithUnsetTypeAndCode() {
        Medium medium = new Medium();
        medium.setTitle("Title");

        mediumRepository.save(medium);
    }

    @Test
    public void saveAndRetrievePlainMediumWithoutArtistReference() {
        Medium medium = createMedium("12345678", Medium.CD, "Media Title");

        Medium ret = mediumRepository.findAll().get(0);

        assertThat(ret.getCode(), is("12345678"));
        assertThat(ret.getType(), is(Medium.CD));
        assertThat(ret.getTitle(), is("Media Title"));
    }

    @Test
    public void saveAndRetrieveByTypeAndCode() {
        Medium medium = createMedium("12345678", Medium.CD, "Media Title");

        Medium ret = mediumRepository.findByTypeAndCode(Medium.CD, "12345678");

        assertThat(ret.getCode(), is("12345678"));
        assertThat(ret.getType(), is(Medium.CD));
        assertThat(ret.getTitle(), is("Media Title"));
    }

    @Test
    public void saveArtistSaveMediumRetrieveMediumWithArtist() {
        Artist artist = createArtist("Demo Artist");
        Medium medium = createMedium("10000001", Medium.CD, "Demo Title", artist);

        Medium ret = mediumRepository.findByTypeAndCode(Medium.CD,"10000001");

        assertThat(ret.getCode(), is("10000001"));
        Artist retArtist = ret.getArtist();
        assertThat(retArtist, is(not(nullValue())));
        assertThat(retArtist.getName(), is("Demo Artist"));
    }
}
