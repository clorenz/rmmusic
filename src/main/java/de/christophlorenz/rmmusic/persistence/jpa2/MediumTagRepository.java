package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.MediumTag;
import de.christophlorenz.rmmusic.model.TagMediumId;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by clorenz on 13.05.15.
 */
@Repository
@Configurable
@RepositoryRestResource(collectionResourceRel="medium_tag", path="mediumtag")
public interface MediumTagRepository extends JpaRepository<MediumTag, TagMediumId> {

    Optional<MediumTag> findById(@Param("id") TagMediumId tagMediumId);

    @Query("SELECT m FROM MediumTag m WHERE m.id.userName = :userName")
    List<MediumTag> findByUserName(@Param("userName") String userName);

    @Query("SELECT m FROM MediumTag m WHERE m.id.userName = :userName AND m.action = :action")
    List<MediumTag> findByUserNameAndAction(@Param("userName") String userName, @Param("action") String action);

    /*
    List<MediumTag> findByUserNameAndMediumId(@Param("username") String userName, @Param("mediumid") Long mediumId);

    List<MediumTag> findByUserName(@Param("username") String userName);
    */
}
