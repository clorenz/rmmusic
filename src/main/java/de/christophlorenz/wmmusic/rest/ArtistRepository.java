package de.christophlorenz.wmmusic.rest;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * Created by clorenz on 05.05.15.
 */
@RepositoryRestResource(collectionResourceRel = "artist", path="artist")
public interface ArtistRepository extends PagingAndSortingRepository<Artist, Long>{

    List<Artist> findById(@Param("id") long id);
}
