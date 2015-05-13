package de.christophlorenz.rmmusic.persistence.jpa2;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import de.christophlorenz.rmmusic.model.MediumTag;
import de.christophlorenz.rmmusic.model.TagMediumId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by clorenz on 13.05.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })

public class MediumTagRepositoryTest extends BaseRepositoryTest {

    @Test
    public void findWithInvalidIdReturnsEmptyList() {
        TagMediumId id = new TagMediumId("clorenz",1L);
        List<MediumTag> ret = mediumTagRepository.findById(id);

        assertThat(ret, is(not(nullValue())));
        assertThat(ret.size(), is(0));
    }

    @Test
    public void saveAndRetrieveById() {
        TagMediumId id = new TagMediumId("clorenz",1L);
        MediumTag mediumTag = new MediumTag();
        mediumTag.setId(id);
        mediumTag.setAction("action");
        mediumTagRepository.save(mediumTag);

        List<MediumTag> ret = mediumTagRepository.findById(id);

        assertThat(ret, is(not(nullValue())));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId().getUserName(), is("clorenz"));
        assertThat(ret.get(0).getId().getMediumId(), is(1L));
        assertThat(ret.get(0).getAction(), is("action"));
    }

    @Test
    public void saveAndRetrieveByUserName() {
        TagMediumId id = new TagMediumId("clorenz",1L);
        MediumTag mediumTag = new MediumTag();
        mediumTag.setId(id);
        mediumTag.setAction("action");
        mediumTagRepository.save(mediumTag);

        List<MediumTag> ret = mediumTagRepository.findByUserName("clorenz");

        assertThat(ret, is(not(nullValue())));
        assertThat(ret.size(), is(1));
        assertThat(ret.get(0).getId().getUserName(), is("clorenz"));
        assertThat(ret.get(0).getId().getMediumId(), is(1L));
        assertThat(ret.get(0).getAction(), is("action"));
    }

}
