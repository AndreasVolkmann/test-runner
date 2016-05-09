package tests;

import annotations.SetUp;
import annotations.TearDown;
import annotations.TestClass;
import annotations.TestMethod;
import data.Person;
import exceptions.AssertException;
import src.AssertMethods;


/**
 * Created by av on 22/02/16.
 */
@TestClass
public class PersonTest {

    static Person person;

    @SetUp
    public static void setUp() {
        System.out.println("SetUp");
        person = new Person("Name", 20);
    }

    @TearDown
    public static void tearDown() {
        System.out.println("Tear Down");
    }

    @TestMethod
    public static void testName() throws AssertException {
        System.out.println("Test Name");
        AssertMethods.assertEquals(person.getName(), "Wrong name");
    }

    @TestMethod
    public static void testAge() throws AssertException {
        System.out.println("Test Age");
        AssertMethods.assertEquals(person.getAge(), 20);
    }


}
