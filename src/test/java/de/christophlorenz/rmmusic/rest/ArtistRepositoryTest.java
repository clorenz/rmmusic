package de.christophlorenz.rmmusic.rest;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.persistence.jpa2.ArtistRepository;
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
public class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository repository;

    @Before
    public void before() {
        repository.deleteAll();
    }


    @Test
    public void findWithInvalidIdReturnsEmptyList() {
        List<Artist> ret = repository.findById(0);

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(0));
    }

    @Test
    public void createAndFind() {
        Artist artist = new Artist();
        artist.setName("First artist");
        artist = repository.save(artist);

        List<Artist> ret = repository.findAll();

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist.getId()));
    }

    @Test
    public void createAndFindById() {
        Artist artist1 = new Artist();
        artist1.setName("First artist");
        artist1 = repository.save(artist1);
        Artist artist2 = new Artist();
        artist2.setName("Second artist");
        artist2 = repository.save(artist2);

        List<Artist> ret = repository.findById(artist1.getId());

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist1.getId()));
    }

    @Test
    public void createAndFindByName() {
        Artist artist1 = new Artist();
        artist1.setName("First artist");
        artist1 = repository.save(artist1);
        Artist artist2 = new Artist();
        artist2.setName("Second artist");
        artist2 = repository.save(artist2);

        List<Artist> ret = repository.findByName("First artist");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist1.getId()));
    }

    @Test
    public void createAndFindByNameIgnoreCaseContaining() {
        Artist artist1 = new Artist();
        artist1.setName("First artist");
        artist1 = repository.save(artist1);
        Artist artist2 = new Artist();
        artist2.setName("Second artist");
        artist2 = repository.save(artist2);

        List<Artist> ret = repository.findByNameIgnoreCaseContaining("sec");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId(), is(artist2.getId()));
    }

    @Test
    public void createAndFindByNameIgnoreCaseContainingMultipleResults() {
        Artist artist1 = new Artist();
        artist1.setName("First artist");
        artist1 = repository.save(artist1);
        Artist artist2 = new Artist();
        artist2.setName("Second artist");
        artist2 = repository.save(artist2);

        List<Artist> ret = repository.findByNameIgnoreCaseContaining("artist");

        assertThat(ret, not(nullValue()));
        assertThat(ret.size(), is(2));
        assertThat(ret.get(0).getId(), is(not(ret.get(1).getId())));
    }

    @Test
    public void createAndUpdateModifiesArtist() {
        Artist artist1 = new Artist();
        artist1.setName("First artist");
        artist1 = repository.save(artist1);
        artist1.setName("Updated artist");
        repository.save(artist1);

        List<Artist> ret = repository.findAll();

        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getName(), is("Updated artist"));
    }
}
