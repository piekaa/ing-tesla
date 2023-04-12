package pl.piekoszek.ing.mutable.collections;

import java.io.PrintWriter;

public abstract class MapEntry implements Comparable<MapEntry> {
    MapEntry next;

    protected abstract String key();

    abstract public void printJSON(PrintWriter printWriter);
}
