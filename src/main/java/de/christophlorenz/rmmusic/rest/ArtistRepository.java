package de.christophlorenz.rmmusic.rest;

import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by clorenz on 05.05.15.
 */
@RepositoryRestResource(collectionResourceRel="artist", path="artist")
public interface ArtistRepository extends JpaRepository<Artist, Long>{

    List<Artist> findById(@Param("id") long id);

    List<Artist> findByNameIgnoreCaseContaining(@Param("name") String name);

    List<Artist> findByName(@Param("name") String name);
}
