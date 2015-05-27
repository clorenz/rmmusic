package de.christophlorenz.rmmusic.persistence.jpa2;

import de.christophlorenz.rmmusic.model.Label;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by clorenz on 27.05.15.
 */
@Repository
@Configurable
@RepositoryRestResource(collectionResourceRel="label", path="label")
public interface LabelRepository extends JpaRepository<Label, String> {

    List<Label> findByLabelIgnoreCaseStartingWithOrderByLabelAsc(@Param("label") String label);

}
