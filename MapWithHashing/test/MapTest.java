import static org.junit.Assert.assertEquals;

import org.junit.Test;

import components.map.Map;

/**
 * JUnit test fixture for {@code Map<String, String>}'s constructor and kernel
 * methods.
 *
 * @author Ben Walls, Matt Chandran
 *
 */
public abstract class MapTest {

    /**
     * Invokes the appropriate {@code Map} constructor for the implementation
     * under test and returns the result.
     *
     * @return the new map
     * @ensures constructorTest = {}
     */
    protected abstract Map<String, String> constructorTest();

    /**
     * Invokes the appropriate {@code Map} constructor for the reference
     * implementation and returns the result.
     *
     * @return the new map
     * @ensures constructorRef = {}
     */
    protected abstract Map<String, String> constructorRef();

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the implementation
     * under test type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsTest = [pairs in args]
     */
    private Map<String, String> createFromArgsTest(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorTest();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     *
     * Creates and returns a {@code Map<String, String>} of the reference
     * implementation type with the given entries.
     *
     * @param args
     *            the (key, value) pairs for the map
     * @return the constructed map
     * @requires <pre>
     * [args.length is even]  and
     * [the 'key' entries in args are unique]
     * </pre>
     * @ensures createFromArgsRef = [pairs in args]
     */
    private Map<String, String> createFromArgsRef(String... args) {
        assert args.length % 2 == 0 : "Violation of: args.length is even";
        Map<String, String> map = this.constructorRef();
        for (int i = 0; i < args.length; i += 2) {
            assert !map.hasKey(args[i]) : ""
                    + "Violation of: the 'key' entries in args are unique";
            map.add(args[i], args[i + 1]);
        }
        return map;
    }

    /**
     * constructor test.
     */
    @Test
    public final void constructorTestNoArg() {
        Map<String, String> m = this.constructorTest();
        Map<String, String> mExpected = this.constructorRef();

        assertEquals(m, mExpected);
    }

    /**
     * empty case for add.
     */
    @Test
    public final void addEmpty() {
        Map<String, String> m = this.constructorTest();
        Map<String, String> mExpected = this.constructorRef();

        mExpected.add("1", "red");
        m.add("1", "red");

        assertEquals(m, mExpected);
    }

    /**
     * normal case for add.
     */
    @Test
    public final void addNonEmpty() {
        Map<String, String> m = this.createFromArgsTest("1", "red");
        Map<String, String> mExpected = this.createFromArgsRef("1", "red", "2",
                "blue");

        m.add("2", "blue");

        assertEquals(m, mExpected);
    }

    /**
     * add to a bucket with other elements.
     */
    @Test
    public final void addSameBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red");
        Map<String, String> mExpected = this.createFromArgsRef("1", "red",
                "102", "blue");

        m.add("102", "blue");

        assertEquals(m, mExpected);
    }

    /**
     * add a key with a large hashCode for add.
     */
    @Test
    public final void addLargeKey() {
        Map<String, String> m = this.createFromArgsTest("2", "blue");
        Map<String, String> mExpected = this.createFromArgsRef("98233243",
                "red", "2", "blue");

        m.add("98233243", "red");

        assertEquals(m, mExpected);
    }

    /**
     * normal case for remove.
     */
    @Test
    public final void removeNonEmpty() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "2",
                "blue");
        Map<String, String> mExpected = this.createFromArgsRef("2", "blue");

        m.remove("1");

        assertEquals(m, mExpected);
    }

    /**
     * remove from a bucket with multiple entries.
     */
    @Test
    public final void removeSameBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "102",
                "blue");
        Map<String, String> mExpected = this.createFromArgsRef("102", "blue");

        m.remove("1");

        assertEquals(m, mExpected);
    }

    /**
     * remove a key with a large hashCode value.
     */
    @Test
    public final void removeLargeValue() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "98233243",
                "blue");
        Map<String, String> mExpected = this.createFromArgsRef("1", "red");

        m.remove("98233243");

        assertEquals(m, mExpected);
    }

    /**
     * smallest case for removeAny.
     */
    @Test
    public final void removeAnySize1() {
        Map<String, String> m = this.createFromArgsTest("1", "red");
        Map<String, String> mExpected = this.createFromArgsRef();

        m.removeAny();

        assertEquals(m, mExpected);
    }

    /**
     * normal case for removeAny.
     */
    @Test
    public final void removeAnyTest() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "2",
                "blue");
        Map<String, String> mExpected = this.createFromArgsRef("2", "blue");

        m.removeAny();

        assertEquals(m, mExpected);
    }

    /**
     * first bucket has an element to remove.
     */
    @Test
    public final void removeAnyFirstBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "2", "blue",
                "3", "green", "4", "yellow", "101", "black");
        Map<String, String> mExpected = this.createFromArgsTest("1", "red", "2",
                "blue", "3", "green", "4", "yellow");

        m.removeAny();

        assertEquals(m, mExpected);
    }

    /**
     * smallest case for value.
     */
    @Test
    public final void valueSize1() {
        Map<String, String> m = this.createFromArgsTest("1", "red");

        String valueExpected = "red";

        assertEquals(m.value("1"), valueExpected);
    }

    /**
     * normal case for value.
     */
    @Test
    public final void valueTest() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "2",
                "blue");

        String valueExpected = "red";

        assertEquals(m.value("1"), valueExpected);
    }

    /**
     * value test with multiple elements in the same bucket.
     */
    @Test
    public final void valueSameBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "102",
                "blue");

        String valueExpected = "red";

        assertEquals(m.value("1"), valueExpected);
    }

    /**
     * normal true case for hasKey.
     */
    @Test
    public final void hasKeyTrueTest() {
        Map<String, String> m = this.createFromArgsTest("1", "red");
        boolean expected = true;

        assertEquals(m.hasKey("1"), expected);
    }

    /**
     * multiple elements in the same bucket true case for hasKey.
     */
    @Test
    public final void hasKeySameBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "101",
                "blue");
        boolean expected = true;

        assertEquals(m.hasKey("101"), expected);
    }

    /**
     * key is in the first bucket for hasKey.
     */
    @Test
    public final void hasKeyFirstBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "0",
                "blue");
        boolean expected = true;

        assertEquals(m.hasKey("0"), expected);
    }

    /**
     * key is in the last bucket for hasKey.
     */
    @Test
    public final void hasKeyLastBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "99",
                "blue");
        boolean expected = true;

        assertEquals(m.hasKey("99"), expected);
    }

    /**
     * false case for hasKey.
     */
    @Test
    public final void hasKeyFalseTest() {
        Map<String, String> m = this.createFromArgsTest("1", "red");
        boolean expected = false;

        assertEquals(m.hasKey("2"), expected);
    }

    /**
     * empty and false case for hasKey.
     */
    @Test
    public final void hasKeyEmptyTest() {
        Map<String, String> m = this.createFromArgsTest();
        boolean expected = false;

        assertEquals(m.hasKey("1"), expected);
    }

    /**
     * normal case for size.
     */
    @Test
    public final void sizeTest() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "2",
                "blue");
        int expected = 2;

        assertEquals(m.size(), expected);
    }

    /**
     * empty case for size.
     */
    @Test
    public final void sizeTestEmpty() {
        Map<String, String> m = this.createFromArgsTest();
        int expected = 0;

        assertEquals(m.size(), expected);
    }

    /**
     * multiple entries in same bucket for size.
     */
    @Test
    public final void sizeSameBucket() {
        Map<String, String> m = this.createFromArgsTest("1", "red", "102",
                "blue", "203", "green");
        int expected = 3;

        assertEquals(m.size(), expected);
    }

}
