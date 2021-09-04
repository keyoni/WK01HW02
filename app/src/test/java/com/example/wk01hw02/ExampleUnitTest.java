package com.example.wk01hw02;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void userTest() {
        User user = new User(1,"user1", "pass1");
        String username = "user1";
        String password = "pass1word";

        assertEquals(username,user.getUsername());
        assertNotEquals(password,user.getPassword());

    }
}