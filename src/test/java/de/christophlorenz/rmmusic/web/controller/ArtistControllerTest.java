package de.christophlorenz.rmmusic.web.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * ArtistController Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>Mai 21, 2015</pre>
 */
public class ArtistControllerTest {
    ArtistController controller;

    @Before
    public void before() throws Exception {
        controller = new ArtistController();
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: selectArtist(Model model)
     */
    @Test
    public void testSelectArtist() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editArtistById(@PathVariable(value="id") Long id, Model model)
     */
    @Test
    public void testEditArtistById() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: editArtist(@RequestParam(value = "artist", required = true ) final String name, @RequestParam(value = "exact", required = false) final String exact, Model model)
     */
    @Test
    public void testEditArtist() throws Exception {
//TODO: Test goes here... 
    }

    /**
     * Method: calculatePrintName(String name)
     */
    @Test
    public void testCalculatePrintNameNullRemainsUnchanged() throws Exception {
        assertThat(controller.calculatePrintName(null), is(nullValue()));
    }

    @Test
    public void testCalculatePrintNameEmptyRemainsUnchanged() throws Exception {
        assertThat(controller.calculatePrintName(""), is(""));
    }

    @Test
    public void testCalculatePrintNameWithoutCommaRemainsUnchanged() throws Exception {
        String name="Artist";
        assertThat(controller.calculatePrintName(name), is(name));
    }

    @Test
    public void testCalculatePrintNameWithOneCommaChangesTheOrderAndRemovesComma() throws Exception {
        assertThat(controller.calculatePrintName("Name, first name"), is("first name Name"));
    }

    @Test
    public void testCalculatePrintNameWithMoreThanOneCommaChangesOnlyFirstTwoPartsAndRemovesCommas() throws Exception {
        assertThat(controller.calculatePrintName("Baker, George, Selection"), is("George Baker Selection") );
    }


    /**
     * Method: editNewArtist(@RequestParam(value = "artist", required = true) String name, Model model)
     */
    @Test
    public void testEditNewArtist() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = ArtistController.getClass().getMethod("editNewArtist", @RequestParam(value.class, Model.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
