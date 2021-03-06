package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Medium;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by clorenz on 11.05.15.
 */
@Repository
@Configurable
@RepositoryRestResource(collectionResourceRel="medium", path="medium")
public interface MediumRepository extends JpaRepository<Medium, Long> {

    Optional<Medium> findById(@Param("id") long id);

    Medium findByTypeAndCode(@Param("type") int type, @Param("code") String code);

    List<Medium> findByCodeStartsWith(@Param("code") String code);

    List<Medium> findByTypeAndCodeIgnoreCaseStartingWithOrderByCodeAsc(@Param("type") int type, @Param("code") String code);

    @Query(value = "Select m from Medium m, Artist a where m.type=:type and a.name=:artist and m.artist.id = a.id")
    List<Medium> findByTypeAndArtist(@Param("type") Integer type, @Param("artist") String artist);

    @Query(value = "Select m from Medium m, Artist a where m.type=:type and lower(m.title) like lower(concat('%',:title,'%')) and lower(a.name) like lower(concat('%',:artist,'%')) and m.artist.id = a.id")
    List<Medium> findByTypeAndArtistAndTitle(@Param("type") Integer type, @Param("artist") String artist, @Param("title") String title);

    @Query(value = "Select m from Medium m, Artist a where m.type=:type and m.title=:title and a.name=:artist and m.artist.id = a.id")
    List<Medium> findByTypeAndArtistAndTitleExact(@Param("type") Integer type, @Param("artist") String artist, @Param("title") String title);

    @Query(value = "Select m from Medium m, Artist a where a.name=:artist and m.artist.id = a.id")
    List<Medium> findByArtist(@Param("artist") String artist);

    List<Medium> findByType(@Param("type") int type);
}
