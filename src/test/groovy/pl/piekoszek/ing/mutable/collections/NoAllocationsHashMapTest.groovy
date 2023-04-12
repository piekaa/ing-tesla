package pl.piekoszek.ing.mutable.collections


import spock.lang.Specification

//        "a".hashCode() % 2 -> 1
//        "b".hashCode() % 2 -> 0
//        "c".hashCode() % 2 -> 1
//        "d".hashCode() % 2 -> 0
//        "e".hashCode() % 2 -> 1
//        "f".hashCode() % 2 -> 0

class NoAllocationsHashMapTest extends Specification {
    def "should find added item"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        and: "item"
        def item = new TestMapEntry("a")

        expect: "map does not contain item"
        !map.contains("a")

        when: "item is inserted"
        map.put(item)

        then: "map contains item"
        map.contains("a")
    }

    def "should find added items, even though they are likely in the same bucket"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        and: "two items"
        def firstItem = new TestMapEntry("a")
        def secondItem = new TestMapEntry("c")

        when: "first item is inserted"
        map.put(firstItem)

        then: "map does not contain second item"
        !map.contains("c")

        when: "second item is inserted (likely in same bucket)"
        map.put(secondItem)

        then: "map contains both items"
        map.contains("a")
        map.contains("c")
    }

    def "should find added items which are likely in different buckets"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        and: "two items"
        def firstItem = new TestMapEntry("a")
        def secondItem = new TestMapEntry("b")

        when: "first item is inserted"
        map.put(firstItem)

        then: "map does not contain second item"
        !map.contains("b")

        when: "second item is inserted (likely in different bucket)"
        map.put(secondItem)

        then: "map contains both items"
        map.contains("a")
        map.contains("b")
    }


    def "should find multiple inserted items"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        when: "multiple items are inserted"
        map.put(new TestMapEntry("a"))
        map.put(new TestMapEntry("b"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("d"))

        then: "map contains all items"
        map.contains("a")
        map.contains("b")
        map.contains("c")
        map.contains("d")
    }

    def "should clear map"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        when: "multiple items are inserted"
        map.put(new TestMapEntry("a"))
        map.put(new TestMapEntry("b"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("f"))

        and: "map is cleared"
        map.clear()

        then: "map does not contain any of inserted items"
        !map.contains("a")
        !map.contains("b")
        !map.contains("c")
        !map.contains("d")
        !map.contains("e")
        !map.contains("f")
    }

    def "should clear connections between items in bucket"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        and: "two items"
        def fistItem = new TestMapEntry("a")
        def secondItem = new TestMapEntry("c")

        when: "two items are inserted into map"
        map.put(fistItem)
        map.put(secondItem)

        and: "one new items is inserted (all likely in same bucket)"
        map.put(new TestMapEntry("e"))

        and: "map is cleared"
        map.clear()

        and: "first items is inserted again"
        map.put(fistItem)

        then: "map contains fist item"
        map.contains("a")

        and: "map does not contain second and third item thus connection between first and second is cleared"
        !map.contains("c")
        !map.contains("e")

        when: "second item is inserted"
        map.put(secondItem)

        then: "map contains second item"
        map.contains("c")

        and: "map does not contain third item thus connection between second and third is also cleared"
        !map.contains("e")
    }

    def "should sort items"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(20)

        when: "multiple items are inserted"
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("b"))
        map.put(new TestMapEntry("a"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("g"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("f"))
        map.put(new TestMapEntry("h"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("k"))
        map.put(new TestMapEntry("j"))

        and: "map sorts items"
        def sorted = Arrays.stream(map.sorted()).limit(11).toList()

        then: "items are sorted"
        sorted.collect { it.key() } == ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]
    }


    def "should sort items and remove duplicates"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(11)

        when: "multiple items are added, including duplicates"
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("b"))
        map.put(new TestMapEntry("a"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("e"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("g"))
        map.put(new TestMapEntry("g"))
        map.put(new TestMapEntry("g"))
        map.put(new TestMapEntry("c"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("f"))
        map.put(new TestMapEntry("f"))
        map.put(new TestMapEntry("f"))
        map.put(new TestMapEntry("f"))
        map.put(new TestMapEntry("h"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("i"))
        map.put(new TestMapEntry("k"))
        map.put(new TestMapEntry("k"))
        map.put(new TestMapEntry("k"))
        map.put(new TestMapEntry("j"))

        and: "map sorts items"
        def sorted = Arrays.stream(map.sorted()).limit(11).toList()

        then: "items are sorted, and duplicates are removed"
        sorted.collect { it.key() } == ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k"]
    }


    def "should sort items, even though most likely there are multiple bucket collisions"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(4)

        when: "multiple items are added most likely causing multiple bucket collisions"
        map.put(new TestMapEntry("l"))
        map.put(new TestMapEntry("p"))
        map.put(new TestMapEntry("h"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("p"))
        map.put(new TestMapEntry("l"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("h"))
        map.put(new TestMapEntry("d"))
        map.put(new TestMapEntry("h"))
        map.put(new TestMapEntry("l"))
        map.put(new TestMapEntry("p"))


        and: "map sorts items"
        def sorted = Arrays.stream(map.sorted()).limit(4).toList()

        then: "items are sorted"
        sorted.collect { it.key() } == ["d", "h", "l", "p"]
    }


    def "should keep all items in bucket if first is inserted again"() {
        given: "empty map"
        def map = new SortedNoAllocationsHashMap<TestMapEntry>(2)

        and: "two items"
        def firstItem = new TestMapEntry("a")
        def secondItem = new TestMapEntry("c")

        when: "two items are inserted into map"
        map.put(firstItem)
        map.put(secondItem)

        then: "map contains second item"
        map.contains("c")

        when: "first item is inserted again"
        map.put(firstItem)

        then: "map still contains second item"
        map.contains("c")
    }
}

class TestMapEntry extends MapEntry {
    String key

    TestMapEntry(String key) {
        this.key = key
    }

    @Override
    int compareTo(MapEntry o) {
        return key <=> o.key()
    }

    @Override
    protected String key() {
        return key
    }

    @Override
    void printJSON(PrintWriter printWriter) {

    }
}
