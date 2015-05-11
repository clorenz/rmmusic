package de.christophlorenz.rmmusic.persistence.jpa2;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Song;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Created by clorenz on 11.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class SongRepositoryTest {

    @Autowired
    private SongRepository repository;

    @Autowired
    private ArtistRepository artistRepository;

    @Before
    public void before() {
        repository.deleteAll();
        artistRepository.deleteAll();
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addWithMissingArtistAndTitle() {
        Song song = new Song();
        song.setRelease("S/A");
        repository.save(song);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addWithMissingArtist() {
        Song song = new Song();
        song.setTitle("Songtitle");
        repository.save(song);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void addWithMissingTitle() {
        Artist artist = new Artist();
        artist.setName("The Artist");
        artist = artistRepository.save(artist);

        Song song = new Song();
        song.setArtist(artist);
        repository.save(song);
    }

    @Test
    public void saveAndRetrieveSong() {
        Artist artist = new Artist();
        artist.setName("The Artist");
        artist = artistRepository.save(artist);

        Song song = new Song();
        song.setArtist(artist);
        song.setTitle("Song Title");
        repository.save(song);

        Song ret = repository.findByArtistAndTitle(artist, "Song Title").get(0);

        assertThat(ret, is(not(nullValue())));
        assertThat(ret.getTitle(), is("Song Title"));
        assertThat(ret.getArtist().getName(), is("The Artist"));
    }

    @Test
    public void saveAndFindFuzzy() {
        Artist artist = new Artist();
        artist.setName("The Artist");
        artist = artistRepository.save(artist);

        Song song1 = new Song();
        song1.setArtist(artist);
        song1.setTitle("Song Title One");
        repository.save(song1);

        Song song2 = new Song();
        song2.setArtist(artist);
        song2.setTitle("Song Title Two");
        repository.save(song2);

        List<Song> ret = repository.findByArtistAndTitleIgnoreCaseContaining(artist, "Song Title");

        assertThat(ret.size(), is(2));
        assertThat(ret.get(0).getTitle(), is(not(ret.get(1).getTitle())));
    }


    @Test
    public void saveAndFindAllFuzzy() {
        Artist artist = new Artist();
        artist.setName("The Artist");
        artist = artistRepository.save(artist);

        Song song1 = new Song();
        song1.setArtist(artist);
        song1.setTitle("Song Title One");
        repository.save(song1);

        Song song2 = new Song();
        song2.setArtist(artist);
        song2.setTitle("Song Title Two");
        repository.save(song2);

        List<Song> ret = repository.findByArtistNameIgnoreCaseContainingAndTitleIgnoreCaseContaining("Art", "Song Title");

        assertThat(ret.size(), is(2));
        assertThat(ret.get(0).getTitle(), is(not(ret.get(1).getTitle())));
    }


    @Test
    public void saveAndFindExact() {
        Artist artist = new Artist();
        artist.setName("The Artist");
        artist = artistRepository.save(artist);

        Song song1 = new Song();
        song1.setArtist(artist);
        song1.setTitle("Song Title One");
        repository.save(song1);

        Song song2 = new Song();
        song2.setArtist(artist);
        song2.setTitle("Song Title One plus two");
        repository.save(song2);

        List<Song> ret = repository.findByArtistAndTitle(artist, "Song Title One");

        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getTitle(), is("Song Title One"));
    }
}
