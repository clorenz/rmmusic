package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.model.Song;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by clorenz on 12.05.15.
 */
public class BaseRepositoryTest {

    @Autowired
    protected ArtistRepository artistRepository;

    @Autowired
    protected MediumRepository mediumRepository;

    @Autowired
    protected SongRepository songRepository;

    @Autowired
    protected RecordingRepository recordingRepository;

    @Autowired
    protected MediumTagRepository mediumTagRepository;

    @Before
    public void before() {
        recordingRepository.deleteAll();
        songRepository.deleteAll();
        mediumRepository.deleteAll();
        artistRepository.deleteAll();
        mediumTagRepository.deleteAll();
    }

    public Artist createArtist(String name) {
        Artist artist = new Artist();
        artist.setName(name);
        artist = artistRepository.save(artist);
        return artist;
    }

    public Medium createMedium(String code, int type, String title, Artist artist) {
        Medium medium = new Medium();
        medium.setCode(code);
        medium.setType(type);
        medium.setTitle(title);
        if ( artist!=null ) {
            medium.setArtist(artist);
        }
        medium = mediumRepository.save(medium);
        return medium;
    }

    public Medium createMedium(String code, int type, String title) {
        return createMedium(code, type, title, null);
    }

    public Song createSong(String artistName, String songTitle) {
        Artist artist = createArtist(artistName);

        return createSong(artist, songTitle);
    }

    public Song createSong(Artist artist, String songTitle) {
        Song song = new Song();
        song.setArtist(artist);
        song.setTitle(songTitle);
        song = songRepository.save(song);
        return song;
    }

    public Recording createRecording(String artistName, String songTitle, String mediumName, int mediumType, String mediumCode, int track) {
        Artist artist = createArtist(artistName);
        Medium medium = createMedium(mediumCode, mediumType, mediumName, artist);
        Song song = createSong(artist, songTitle);
        Recording recording = new Recording();
        recording.setSong(song);
        recording.setMedium(medium);
        recording.setTrack(track);

        return recordingRepository.save(recording);
    }
}
