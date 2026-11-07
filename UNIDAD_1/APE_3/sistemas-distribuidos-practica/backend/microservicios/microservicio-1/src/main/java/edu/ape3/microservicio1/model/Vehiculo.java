package edu.ape3.microservicio1.model;

import java.util.Objects;

public class Vehiculo implements Comparable<Vehiculo> {
    private final int id;

    public Vehiculo(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Vehiculo otro) {
        return Integer.compare(id, otro.id);
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Vehiculo otro)) {
            return false;
        }
        return id == otro.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
