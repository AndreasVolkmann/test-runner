package src;

import exceptions.AssertException;

/**
 * Created by av on 22/02/16.
 */
public class AssertMethods {

    public static void assertEquals(Object a, Object b) throws AssertException {
        if (!a.equals(b)) {
            throw new AssertException("Not equal");
        }
    }

}
