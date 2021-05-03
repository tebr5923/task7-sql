package com.foxminded.assigner;

import java.util.List;

public interface Assigner<T, E> {
    List<T> assign(List<T> tList, List<E> eList);
}
