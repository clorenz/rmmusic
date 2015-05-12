package de.christophlorenz.rmmusic.persistence.jpa2;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by clorenz on 12.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
public class RecordingRepositoryTest extends BaseRepositoryTest {

    @Test(expected = DataIntegrityViolationException.class)
    public void createRecordingWithoutSong() {
        Artist artist = createArtist("First Artist");
        Medium medium = createMedium("CODE", Medium.LP, "First Album", artist);

        Recording recording = new Recording();
        recording.setMedium(medium);
        recordingRepository.save(recording);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createRecordingWithoutMedium() {
        Artist artist = createArtist("First Artist");
        Song song = createSong(artist, "First Title");

        Recording recording = new Recording();
        recording.setSong(song);
        recordingRepository.save(recording);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void createRecordingWithoutSongAndMedium() {
        Recording recording = new Recording();
        recording.setTrack(1);
        recordingRepository.save(recording);
    }

    @Test
    public void createRecordingAndRetrieveByArtistNameAndSongName() {
        Recording recording = createRecording("Artist One","Song One","First CD", Medium.CD, "12345678" ,1);

        List<Recording> ret = recordingRepository.findByArtistNameIgnoreCaseContainingAndAndSongTitleIgnoreCaseContaining("Artist One", "Song One");

        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getSong().getArtist().getName(), is("Artist One"));
        assertThat(ret.get(0).getSong().getTitle(), is("Song One"));
        assertThat(ret.get(0).getMedium().getCode(), is("12345678"));
    }

}
