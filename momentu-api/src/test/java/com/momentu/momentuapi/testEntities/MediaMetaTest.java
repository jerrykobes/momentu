package com.momentu.momentuapi.testEntities;

import com.momentu.momentuapi.entities.Hashtag;
import com.momentu.momentuapi.entities.MediaMeta;
import com.momentu.momentuapi.entities.UserLike;
import com.momentu.momentuapi.entities.keys.UserLikeKey;
import junit.framework.TestCase;
import org.junit.Test;
import java.util.Date;
import javax.xml.crypto.Data;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.*;

//This is a test class for MediaMeta class
public class MediaMetaTest {

    //This is a test object from MediaMeta class
    MediaMeta mediaMetaTest;

    //Testing MediaMeta constructor without parameters
    @Test
    public void testMediaMeta() {
        try {
            mediaMetaTest = new MediaMeta();
            assertTrue(mediaMetaTest.getUserId() == null);
        } catch (Exception e) {
            fail("MediaMeta constructor without parameters can't be used");
        }
    }

    //Testing MediaMeta constructor with parameters
//    TODO: Remove? Changed to set values in setters for more flexibility with setting location
//    @Test
//    public void testMediaMetaWithParameters() {
//        try {
//            assertTrue(mediaMetaTest == null);
//            Long userId = 1L;
//            String hashtagLabel = "hashtagLabelTest";
//            Long locationId = 2L;
//            mediaMetaTest = new MediaMeta(userId, hashtagLabel, locationId);
//            assertTrue(mediaMetaTest != null);
//            assertEquals(userId, mediaMetaTest.getUserId());
//            assertEquals(hashtagLabel, mediaMetaTest.getHashtagLabel());
//            assertEquals(locationId, mediaMetaTest.getLocationId());
//            TestCase.assertEquals(false, mediaMetaTest.isRemoved());
//            assertNotNull(mediaMetaTest.getCreated());
//
//        } catch (Exception e) {
//            fail("MediaMeta constructor with parameters can't be used");
//        }
//    }

    //Testing userId set method
    @Test
    public void testSetUserId() {
        try {
            mediaMetaTest = new MediaMeta();
            Long userId = 1L;
            mediaMetaTest.setUserId(userId);
            assertEquals(userId, mediaMetaTest.getUserId());
        }catch (Exception e){
            fail("userId value has not been set correctly");
        }
    }

    //Testing userId get method
    @Test
    public void testGetUserId(){
        try {
            mediaMetaTest = new MediaMeta();
            Long userId = 1L;
            mediaMetaTest.setUserId(userId);
            assertEquals(userId, mediaMetaTest.getUserId());
        }catch (Exception e){
            fail("userId value has not been gotten correctly");
        }
    }

//    //Testing locationId set method
//    TODO: Remove? Update? MediaMeta now references location by object in Entity class
//    @Test
//    public void testSetLocationId() {
//        try {
//            mediaMetaTest = new MediaMeta();
//            Long locationId = 1L;
//            mediaMetaTest.setLocationId(locationId);
//            assertEquals(locationId, mediaMetaTest.getLocationId());
//        }catch (Exception e){
//            fail("locationId value has not been set correctly");
//        }
//    }

//    //Testing locationId get method
//    TODO: Remove? Update? MediaMeta now references location by object in Entity class
//    @Test
//    public void testGetLocationId(){
//        try {
//            mediaMetaTest = new MediaMeta();
//            Long locationId = 1L;
//            mediaMetaTest.setLocationId(locationId);
//            assertEquals(locationId, mediaMetaTest.getLocationId());
//        }catch (Exception e){
//            fail("locationId value has not been gotten correctly");
//        }
//    }

    //Testing hashtagLabel set method
    @Test
    public void testSetHashtagLabel() {
        try {
            mediaMetaTest = new MediaMeta();
            String hashtagLabel = "hashtagLabelTest";
            mediaMetaTest.setHashtagLabel(hashtagLabel);
            assertEquals(hashtagLabel, mediaMetaTest.getHashtagLabel());
        }catch (Exception e){
            fail("hashtagLabel value has not been set correctly");
        }
    }

    //Testing hashtagLabel get method
    @Test
    public void testGetHashtagLabel(){
        try {
            mediaMetaTest = new MediaMeta();
            String hashtagLabel = "hashtagLabelTest";
            mediaMetaTest.setHashtagLabel(hashtagLabel);
            assertEquals(hashtagLabel, mediaMetaTest.getHashtagLabel());
        }catch (Exception e){
            fail("hashtagLabel value has not been gotten correctly");
        }
    }

    //Testing removed set method
    @Test
    public void testSetRemoved() {
        try {
            mediaMetaTest = new MediaMeta();
            boolean removed = true;
            mediaMetaTest.setRemoved(removed);
            assertEquals(true, mediaMetaTest.isRemoved());
        }catch (Exception e){
            fail("removed value has not been set correctly");
        }
    }

    //Testing isRemoved method
    @Test
    public void testIsRemoved(){
        try {
            mediaMetaTest = new MediaMeta();
            boolean removed = true;
            mediaMetaTest.setRemoved(removed);
            assertEquals(true, mediaMetaTest.isRemoved());
        }catch (Exception e){
            fail("isRemoved method has not been implemented correctly");
        }
    }

    //Testing created set method
    @Test
    public void testSetCreated() {
        try {
            mediaMetaTest = new MediaMeta();
            Date created = new Date();
            mediaMetaTest.setCreated(created);
            assertEquals(created, mediaMetaTest.getCreated());
        }catch (Exception e){
            fail("created value has not been set correctly");
        }
    }

    //Testing created get method
    @Test
    public void testGetCreated(){
        try {
            mediaMetaTest = new MediaMeta();
            Date created = new Date();
            mediaMetaTest.setCreated(created);
            assertEquals(created, mediaMetaTest.getCreated());
        }catch (Exception e){
            fail("created method has not been gotten correctly");
        }
    }

}
