package de.christophlorenz.rmmusic.web.controller;

import de.christophlorenz.rmmusic.model.Recording;
import de.christophlorenz.rmmusic.persistence.jpa2.MediumRepository;
import de.christophlorenz.rmmusic.persistence.jpa2.RecordingRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

/**
 * Created by clorenz on 30.05.15.
 */
public class RecordingControllerTest {

    public RecordingController recordingController;

    @Mock
    RecordingRepository recordingRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        recordingController = new RecordingController();
        recordingController.setRecordingRepository(recordingRepository);
    }

    @Test
    public void testIncrementCounterOnMediumWithNoRecordingsReturnsZero() {
        List<Recording> emptyList = new ArrayList<Recording>();
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(emptyList);
        assertThat(recordingController.incrementCounter(0l), is("0"));
    }

    @Test
    public void testIncrementCounterOnMediumWithOneRecordingReturnsLastCounterPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        Recording recording = createRecordingWithCounter("1234");
        recordingList.add(recording);
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementCounter(0l), is("1235"));
    }

    @Test
    public void testIncrementCounterOnMediumWithMultipleRecordingReturnsLastCounterPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        recordingList.add(createRecordingWithCounter("1234"));
        recordingList.add(createRecordingWithCounter(null));
        recordingList.add(createRecordingWithCounter(""));
        recordingList.add(createRecordingWithCounter("12 40"));
        recordingList.add(createRecordingWithCounter("12"));
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementCounter(0l), is("1241"));
    }

    @Test
    public void testIncrementTrackOnMediumWithNoRecordingsReturnsOne() {
        List<Recording> emptyList = new ArrayList<Recording>();
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(emptyList);
        assertThat(recordingController.incrementTrack(0l), is(1));
    }

    @Test
    public void testIncrementTrackOnMediumWithOneRecordingReturnsLastTrackPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        Recording recording = createRecordingWithTrack(1);
        recordingList.add(recording);
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementTrack(0l), is(2));
    }

    @Test
    public void testIncrementTrackOnMediumWithMultipleRecordingReturnsLastTrackPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        recordingList.add(createRecordingWithTrack(1));
        recordingList.add(createRecordingWithTrack(null));
        recordingList.add(createRecordingWithTrack(2));
        recordingList.add(createRecordingWithTrack(5));
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementTrack(0l), is(6));
    }

    @Test
    public void testIncrementSideOnMediumWithNoRecordingsReturnsA() {
        List<Recording> emptyList = new ArrayList<Recording>();
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(emptyList);
        assertThat(recordingController.incrementSide(0l), is("A"));
    }

    @Test
    public void testIncrementSideOnMediumWithOneRecordingReturnsLastSidePlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        Recording recording = createRecordingWithSide("A");
        recordingList.add(recording);
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementSide(0l), is("B"));
    }

    @Test
    public void testIncrementSideOnMediumWithMultipleRecordingReturnsLastSidePlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        recordingList.add(createRecordingWithSide("A"));
        recordingList.add(createRecordingWithSide(null));
        recordingList.add(createRecordingWithSide(""));
        recordingList.add(createRecordingWithSide("   "));
        recordingList.add(createRecordingWithSide("C"));
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(recordingController.incrementSide(0l), is("D"));
    }

    @Test
    public void testIncrementSideAndTrackOnMediumWithNoRecordingsReturnsA1() {
        List<Recording> emptyList = new ArrayList<Recording>();
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(emptyList);
        assertThat(joinSideAndTrack(recordingController.incrementSideAndTrack(0l)), is("A1"));
    }

    @Test
    public void testIncrementSideAndTrackOnMediumWithOneRecordingReturnsSideAndLastTrackPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        Recording recording = createRecordingWithSideAndTrack("A", 1);
        recordingList.add(recording);
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(joinSideAndTrack(recordingController.incrementSideAndTrack(0l)), is("A2"));
    }

    @Test
    public void testIncrementSideAndTrackOnMediumWithMultipleRecordingReturnsLastSideAndLastTrackPlusOne() {
        List<Recording> recordingList = new ArrayList<Recording>();
        recordingList.add(createRecordingWithSideAndTrack("A", 1));
        recordingList.add(createRecordingWithSideAndTrack(null, null));
        recordingList.add(createRecordingWithSideAndTrack("", 4));
        recordingList.add(createRecordingWithSideAndTrack("B",3));
        recordingList.add(createRecordingWithSideAndTrack("B",4));
        when(recordingRepository.findByMediumId(eq(0l))).thenReturn(recordingList);
        assertThat(joinSideAndTrack(recordingController.incrementSideAndTrack(0l)), is("B5"));
    }

    private Recording createRecordingWithCounter(String counter) {
        Recording recording = new Recording();
        recording.setCounter(counter);
        return recording;
    }

    private Recording createRecordingWithTrack(Integer track) {
        Recording recording = new Recording();
        recording.setTrack(track);
        return recording;
    }

    private Recording createRecordingWithSide(String side) {
        Recording recording = new Recording();
        recording.setSide(side);
        return recording;
    }

    private Recording createRecordingWithSideAndTrack(String side, Integer track) {
        Recording recording = new Recording();
        recording.setSide(side);
        recording.setTrack(track);
        return recording;
    }

    private String joinSideAndTrack(String[] parts) {
        return StringUtils.join(parts,"");
    }
}