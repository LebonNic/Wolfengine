package com.arvernistudio.wolfengine.utils;

public class Bag<E> {
    private E[] _data;
    private int _size = 0;
    public static final int BAG_DEFAULT_CAPACITY = 64;

    public Bag () {
        this(Bag.BAG_DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public Bag (int capacity) {
        _data = (E[])new Object[capacity];
    }

    public E remove (int index) {
        E e = _data[index]; // make copy of element to remove so it can be returned
        _data[index] = _data[--_size]; // overwrite item to remove with last element
        _data[_size] = null; // null last element, so gc can do its work
        return e;
    }

    public E removeLast () {
        if (_size > 0) {
            E e = _data[--_size];
            _data[_size] = null;
            return e;
        }

        return null;
    }

    public boolean remove (E e) {
        for (int i = 0; i < _size; i++) {
            E e2 = _data[i];

            if (e == e2) {
                _data[i] = _data[--_size]; // overwrite item to remove with last element
                _data[_size] = null; // null last element, so gc can do its work
                return true;
            }
        }

        return false;
    }

    public boolean contains (E e) {
        for (int i = 0; _size > i; i++) {
            if (e == _data[i]) {
                return true;
            }
        }
        return false;
    }

    public E get (int index) {
        return _data[index];
    }

    public int getSize () {
        return _size;
    }

    public int getCapacity () {
        return _data.length;
    }

    public boolean isIndexWithinBounds (int index) {
        boolean isIndWithinBounds = false;

        if(index >= 0 && index < getCapacity())
            isIndWithinBounds = true;

        return isIndWithinBounds;
    }

    public boolean isEmpty () {
        return _size == 0;
    }

    public void add (E e) {
        // is size greater than capacity increase capacity
        if (_size == _data.length) {
            grow();
        }

        _data[_size++] = e;
    }

    public void set (int index, E e) {
        if (index >= _data.length) {
            grow(index * 2);
        }
        _size = index + 1;
        _data[index] = e;
    }

    public void clear () {
        // null all elements so gc can clean up
        for (int i = 0; i < _size; i++) {
            _data[i] = null;
        }

        _size = 0;
    }

    private void grow () {
        int newCapacity = (_data.length * 3) / 2 + 1;
        grow(newCapacity);
    }

    @SuppressWarnings("unchecked")
    private void grow (int newCapacity) {
        E[] oldData = _data;
        _data = (E[])new Object[newCapacity];
        System.arraycopy(oldData, 0, _data, 0, oldData.length);
    }
}