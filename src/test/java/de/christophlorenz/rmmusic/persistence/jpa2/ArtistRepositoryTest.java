package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import com.github.springtestdbunit.DbUnitTestExecutionListener;

import java.util.List;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created by clorenz on 07.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class ArtistRepositoryTest extends BaseRepositoryTest {

    @Test
    public void findWithInvalidIdReturnsOptionalFalse() {
        Optional<Artist> ret = artistRepository.findById(0);

        assertThat(ret.isPresent(), is(false));
    }

    @Test
    public void createAndFind() {
        Artist artist = createArtist("First artist");

        List<Artist> ret = artistRepository.findAll();

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist.getId()));
    }

    @Test
    public void createAndFindById() {
        Artist artist1 = createArtist("First artist");
        Artist artist2 = createArtist("Second artist");

        Optional<Artist> ret = artistRepository.findById(artist1.getId());

        assertThat(ret, not(nullValue()));
        assertThat(ret.isPresent(), is(true));
        assertThat(ret.get().getId(), is(artist1.getId()));
    }

    @Test
    public void createAndFindByName() {
        Artist artist1 = createArtist("First artist");
        Artist artist2 = createArtist("Second artist");

        List<Artist> ret = artistRepository.findByName("First artist");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist1.getId()));
    }

    @Test
    public void createAndFindByNameIgnoreCaseContaining() {
        Artist artist1 = createArtist("First artist");
        Artist artist2 = createArtist("Second artist");

        List<Artist> ret = artistRepository.findByNameIgnoreCaseContaining("sec");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist2.getId()));
    }

    @Test
    public void createAndFindByNameIgnoreCaseContainingMultipleResults() {
        Artist artist1 = createArtist("First artist");
        Artist artist2 = createArtist("Second artist");

        List<Artist> ret = artistRepository.findByNameIgnoreCaseContaining("artist");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(2));
        assertThat(ret.get(0).getId(), is(not(ret.get(1).getId())));
    }

    @Test
    public void createAndUpdateModifiesArtist() {
        Artist artist1 = createArtist("First artist");
        artist1.setName("Updated artist");
        artistRepository.save(artist1);

        List<Artist> ret = artistRepository.findAll();

        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getName(), is("Updated artist"));
    }
}
