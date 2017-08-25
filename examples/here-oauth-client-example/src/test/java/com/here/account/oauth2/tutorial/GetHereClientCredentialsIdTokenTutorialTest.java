package com.here.account.oauth2.tutorial;

import java.io.File;
import java.lang.reflect.Field;

import com.here.account.auth.OAuth1ClientCredentialsProvider;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

public class GetHereClientCredentialsIdTokenTutorialTest {

    static GetHereClientCredentialsIdTokenTutorial mockTutorial(String[] args) {
        GetHereClientCredentialsIdTokenTutorial mock = Mockito.spy(new
                GetHereClientCredentialsIdTokenTutorial(args));
        Mockito.doThrow(Helper.MyException.class).when(mock).exit(Mockito.anyInt
                ());
        return mock;
    }
    static void setTestCreds(GetHereClientCredentialsIdTokenTutorial tutorial,

                             OAuth1ClientCredentialsProvider systemCredentials) {
        if (null == systemCredentials) {
            throw new RuntimeException("no credentials available for test");
        }
        Class<?> clazz = GetHereClientCredentialsIdTokenTutorial.class;
        try {
            Field field = clazz.getDeclaredField("testCreds");
            field.setAccessible(true);
            field.set(tutorial, systemCredentials);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            throw new RuntimeException("fail to get testCreds declared field: " + e, e);
        }
    }

    @Test(expected = Helper.MyException.class)
    public void test_help() {
        String[] args = {
                "-help"
        };
        GetHereClientCredentialsIdTokenTutorial tutorial = mockTutorial(args);
        tutorial.getToken();
    }

    @Test(expected = Helper.MyException.class)
    public void test_unrecognized() {
        String[] args = {
                "-unrecognized"
        };
        GetHereClientCredentialsIdTokenTutorial tutorial = mockTutorial(args);
        tutorial.getToken();
    }

    @Test(expected = Helper.MyException.class)
    public void test_null() {
        String[] args = null;
        GetHereClientCredentialsIdTokenTutorial tutorial = mockTutorial(args);
        tutorial.getToken();
    }

    @Test
    public void test_id_token()  {
        File file = GetHereClientCredentialsIdTokenTutorial.getDefaultCredentialsFile();
        String path = null != file ? file.getAbsolutePath() : "broken";
        String[] args = {
                path
        };
        GetHereClientCredentialsIdTokenTutorial tutorial = mockTutorial(args);
        if (null == file) {
            setTestCreds(tutorial,
                    Helper.getSystemCredentials());
        }
        String idToken = tutorial.getToken();
        Assert.assertNotNull(idToken);
    }

    @Test(expected = Helper.MyException.class)
    public void test_tooManyArguments() {
        String[] args = {
                "-v",
                "-idToken",
                "-help",
                "filePath",
                "testPath"
        };
        GetHereClientCredentialsIdTokenTutorial tutorial =
                mockTutorial(args);
        tutorial.getToken();
    }
}
