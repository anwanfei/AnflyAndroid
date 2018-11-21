package com.anfly.anflylibrary.utils;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentLinkedHashMap<K, V> extends ConcurrentHashMap<K, V> {

    private List<K> linkedData = new CopyOnWriteArrayList<>();

    @Override
    public V put(@NonNull K key, @NonNull V value) {
        if (!containsKey(key)) {
            linkedData.add(key);
        }
        return super.put(key, value);
    }

    public List<K> getLinkedKeySet() {
        return linkedData;
    }
}