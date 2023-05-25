package com.es.phoneshop.utils;

public class ReferenceTool<T> {

    private T referent;

    public ReferenceTool(T initialValue) {
        referent = initialValue;
    }

    public void set(T newVal) {
        referent = newVal;
    }

    public T get() {
        return referent;
    }
}