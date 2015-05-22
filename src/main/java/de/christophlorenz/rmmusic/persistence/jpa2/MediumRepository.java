package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Medium;
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

    List<Medium> findById(@Param("id") long id);

    Medium findByTypeAndCode(@Param("type") int type, @Param("code") String code);

    // http://www.petrikainulainen.net/programming/spring-framework/spring-from-the-trenches-new-like-expressions-of-spring-data-jpa/
    @Query(value = "Select distinct(label) from Medium m WHERE label like :label%", nativeQuery = true)
    List<String> findLabelsIgnoreCaseStartingWithAsc(@Param("label") String label);
}
