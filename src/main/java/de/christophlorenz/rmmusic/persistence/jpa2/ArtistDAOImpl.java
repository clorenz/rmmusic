package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.dao.ArtistDAO;
import de.christophlorenz.rmmusic.rest.Artist;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by clorenz on 05.05.15.
 */
public class ArtistDAOImpl implements ArtistDAO {

    private static final String GET = "SELECT a FROM artist a";
    private static final String GET_BY_ID = "SELECT a FROM artist a where a.id = ?1";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Artist> getArtists() {
        TypedQuery<Artist> getQuery = entityManager.createQuery(GET, Artist.class);

        return getQuery.getResultList();
    }

    @Override
    public Artist getArtistById(Long id) {
        try {
            TypedQuery<Artist> getByIdQuery = entityManager.createQuery(GET_BY_ID, Artist.class);
            getByIdQuery.setParameter(1, id);

            return getByIdQuery.getSingleResult();
        } catch ( NoResultException e) {
            return null;
        }
    }

    @Override
    public Long deleteArtistById(Long id) {
        Artist artist = entityManager.find(Artist.class, id);
        entityManager.remove(artist);

        return 1L;
    }

    @Override
    public Long createArtist(Artist artist) {
        entityManager.persist(artist);
        entityManager.flush();

        return -1L;

        //return artist.getId();
    }

    @Override
    public int updateArtist(Artist artist) {
        entityManager.merge(artist);

        return 1;
    }
}
