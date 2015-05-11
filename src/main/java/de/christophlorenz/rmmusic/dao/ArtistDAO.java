package de.christophlorenz.rmmusic.dao;


import de.christophlorenz.rmmusic.model.Artist;

import java.util.List;

/**
 * Created by clorenz on 05.05.15.
 */
public interface ArtistDAO {

    public List<Artist> getArtists();

    public Artist getArtistById(Long id);
    public Long deleteArtistById(Long id);
    public Long createArtist(Artist artist);
    public int updateArtist(Artist artist);

}
