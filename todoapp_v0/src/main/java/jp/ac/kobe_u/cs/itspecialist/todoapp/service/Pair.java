package jp.ac.kobe_u.cs.itspecialist.todoapp.service;

import lombok.Data;

@Data(staticConstructor = "of")
class Pair<L, R> {
    private final L left;
    private final R right;

    public Pair<R, L> swap() {
        return Pair.of(right, left);
    }
}
