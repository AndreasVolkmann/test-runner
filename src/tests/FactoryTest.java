package tests;

import annotations.SetUp;
import annotations.TearDown;
import annotations.TestClass;
import annotations.TestMethod;
import data.Factory;
import src.AssertMethods;

/**
 * Created by av on 22/02/16.
 */
@TestClass
public class FactoryTest {

    private static Factory factory;

    @SetUp
    public static void setUp() {
        System.out.println("Set up");
        factory = new Factory(1, "For the People");
    }

    @TearDown
    public static void tear() {

    }

    @TestMethod
    public static void testId() throws Exception {
        System.out.println("Test id");
        AssertMethods.assertEquals(factory.getId(), 1);

    }

}
