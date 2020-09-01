package com.vaadin.bugrap.views.report;

import java.util.Objects;

public class DistributionBarData<T> {

    private T id;
    private int count;
    private String color;

    public DistributionBarData() {
    }

    public DistributionBarData(T id, int count, String color) {
        this.id = id;
        this.count = count;
        this.color = color;
    }

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DistributionBarData<?> that = (DistributionBarData<?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
