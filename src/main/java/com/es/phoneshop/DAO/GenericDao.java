package com.es.phoneshop.DAO;

import com.es.phoneshop.model.entity.GenericEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class GenericDao<T extends GenericEntity> {

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();
    private List<T> items;

    protected GenericDao() {
        items = new ArrayList<>();
    }

    public void save(T item) throws IllegalArgumentException {
        if (item == null) throw new IllegalArgumentException("item has no data");
        writeLock.lock();
        try {
            Optional<T> oldItem = getItemById(item.getId());
            if (oldItem.isPresent()) {
                items.set(items.indexOf(oldItem.get()), item);
            } else {
                items.add(item);
            }
        } finally {
            writeLock.unlock();
        }
    }

    public Optional<T> getItemById(Long id) {
        readLock.lock();
        try {
            return items.stream()
                    .filter(item -> id.equals(item.getId()))
                    .findAny();
        } finally {
            readLock.unlock();
        }
    }

    public void delete(Long id) {
        writeLock.lock();
        try {
            items = items.stream()
                    .filter(item -> !id.equals(item.getId()))
                    .collect(Collectors.toList());
        } finally {
            writeLock.unlock();
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}
