package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Artist;
import de.christophlorenz.rmmusic.model.Medium;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * MediumController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Mai 27, 2015</pre>
 */
public class MediumControllerTest {

    public MediumController mediumController;
    @Mock
    MediumRepository dummyRepository;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        mediumController = new CDROMController();
        mediumController.setMediumRepository(dummyRepository);
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: selectSingle(Model model)
     */
    @Test
    public void testSelectSingle() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editSingle(@RequestParam(value="code", required = false) final String code, @RequestParam(value="artist", required = false) final String artist, @RequestParam(value="title", required = false) final String title, @RequestParam(value="exact", required = false) final String exact, Model model)
     */
    @Test
    public void testEditSingleForCodeArtistTitleExactModel() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editSingleById(@PathVariable(value="id") Long id, Model model)
     */
    @Test
    public void testEditSingleById() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editSingle(@Valid @ModelAttribute("medium") Medium medium, BindingResult br, Model model, RedirectAttributes redirectAttributes)
     */
    @Test
    public void testEditSingleForMediumBrModelRedirectAttributes() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editNewSingle(Model model, Artist artist, String title)
     */
    @Test
    public void testEditNewSingle() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: calculateCode(Artist artist)
     */
    @Test
    public void testCalculateCodeOnNullCodeAndNoMediaBeforeReturnsOne() throws Exception {
        assertThat(mediumController.calculateCode(Medium.ROM, null), is("1"));
    }


    @Test
    public void testCalculateCodeOnNullCodeAndExistingMediaBeforeReturnsLastPlusOne() throws Exception {
        List<Medium> twoMedia = new ArrayList<Medium>();
        twoMedia.add(createMedium(Medium.ROM,"1"));
        twoMedia.add(createMedium(Medium.ROM,"2"));

        when(dummyRepository.findByType(eq(Medium.ROM))).thenReturn(twoMedia);

        assertThat(mediumController.calculateCode(Medium.ROM, null), is("3"));
    }

    @Test
    public void testCalculateCodeOnShortSingleWordReturnsThatWordPlusSuffixOneIfNotFoundInDatabase() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZN");

        when(dummyRepository.findByArtist(eq("BZN"))).thenReturn(new ArrayList<Medium>());

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BZN    1"));
    }

    @Test
    public void testCalculateCodeOnLongSingleWordReturnsThatWordTruncatedPlusSuffixOneIfNotFoundInDatabase() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZNBZNBZN");

        when(dummyRepository.findByArtist(eq("BZNBZNBZN"))).thenReturn(new ArrayList<Medium>());

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BZNBZN 1"));
    }

    @Test
    public void testCalculateCodeOnExistingArtistNameIncrementsLastFoundOnSameMedium() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZN");

        List<Medium> twoMedia = new ArrayList<Medium>();
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    1"));
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    2"));

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE), eq("BZN"))).thenReturn(twoMedia);

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BZN    3"));
    }


    @Test
    public void testCalculateCodeOnExistingArtistNameStartingWithOneOnNewMedium() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZN");

        List<Medium> twoMedia = new ArrayList<Medium>();
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    1"));
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    2"));

        when(dummyRepository.findByTypeAndArtist(eq(Medium.CD),eq("BZN"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByArtist(eq("BZN"))).thenReturn(twoMedia);

        assertThat(mediumController.calculateCode(Medium.CD,artist), is("BZN    1"));
    }

    @Test
    public void testCalculateCodeOnExistingArtistNameIncrementsLastFoundAndHandlesOverflow() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZN");

        List<Medium> twoMedia = new ArrayList<Medium>();
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    8"));
        twoMedia.add(createMedium(Medium.SINGLE,"BZN    9"));

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE), eq("BZN"))).thenReturn(twoMedia);

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BZN   10"));
    }


    @Test
    public void testCalculateCodeOnExistingArtistNameIncrementsLastFoundAndHandlesOverflowAndTruncatesNameIfNeccessiary() throws Exception {
        Artist artist = new Artist();
        artist.setName("BZN");

        List<Medium> twoMedia = new ArrayList<Medium>();
        twoMedia.add(createMedium(Medium.SINGLE,"BZNBZNB8"));
        twoMedia.add(createMedium(Medium.SINGLE,"BZNBZNB9"));

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE), eq("BZN"))).thenReturn(twoMedia);

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BZNBZN10"));
    }


    @Test
    public void testCalculateCodeOnNewArtistNameWithComma() throws Exception {
        Artist artist = new Artist();
        artist.setName("Nachname, Vorname");

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE),eq("Nachname, Vorname"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByArtist(eq("Nachname, Vorname"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByCodeStartsWith(eq("NacVor"))).thenReturn(new ArrayList<Medium>());

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("NacVor 1"));
    }


    @Test
    public void testCalculateCodeOnNewArtistNameWithCommaWithSimilarArtistAlreadyExistsReturnsNull() throws Exception {
        Artist artist = new Artist();
        artist.setName("Nachname, Vorname");

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE),eq("Nachname, Vorname"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByArtist(eq("Nachname, Vorname"))).thenReturn(new ArrayList<Medium>());
        List<Medium> mediumList = new ArrayList<Medium>();
        mediumList.add(createMedium(Medium.SINGLE,"NacVor 1"));
        when(dummyRepository.findByCodeStartsWith(eq("NacVor"))).thenReturn(mediumList);

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is(nullValue()));
    }


    @Test
    public void testCalculateCodeOnNewArtistNameWithCommas() throws Exception {
        Artist artist = new Artist();
        artist.setName("Baker, George, Selection");

        when(dummyRepository.findByTypeAndArtist(eq(Medium.SINGLE),eq("Nachname, Vorname"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByArtist(eq("Baker, George, Selection"))).thenReturn(new ArrayList<Medium>());
        when(dummyRepository.findByCodeStartsWith(eq("BaGeSe"))).thenReturn(new ArrayList<Medium>());

        assertThat(mediumController.calculateCode(Medium.SINGLE,artist), is("BaGeSe 1"));
    }


    private Medium createMedium(int type, String code) {
        Medium medium = new Medium();
        medium.setCode(code);
        medium.setType(type);
        return medium;
    }


    /**
     * Method: editFirstSingleInList(List<Medium> media, Model model)
     */
    @Test
    public void testEditFirstSingleInList() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = MediumController.getClass().getMethod("editFirstSingleInList", List<Medium>.class, Model.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
