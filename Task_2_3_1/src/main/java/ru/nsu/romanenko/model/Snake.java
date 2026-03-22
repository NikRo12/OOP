package ru.nsu.romanenko.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Snake {
    private final Deque<Position> body;
    private Direction direction;

    private int pendingGrowth = 0;


    public Snake(Position startPosition, Direction startDirection) {
        this.body = new ArrayDeque<>();
        this.body.addFirst(startPosition);
        this.direction = startDirection;
    }

    public void move() {
        this.body.addFirst(this.direction.next(this.body.getFirst()));
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            this.body.pollLast();
        }
    }


    public void wrapHead(int fieldWidth, int fieldHeight) {
        Position head = this.body.removeFirst();
        int x = ((head.getHorizontal() % fieldWidth) + fieldWidth) % fieldWidth;
        int y = ((head.getVertical() % fieldHeight) + fieldHeight) % fieldHeight;
        this.body.addFirst(new Position(x, y));
    }

    public void setDirection(Direction direction) {
        if (!direction.equals(this.direction.opposite())) {
            this.direction = direction;
        }
    }

    public void growUp(int size) {
        pendingGrowth += size;
    }

    public List<Position> getBody() {
        return new ArrayList<>(this.body);
    }

    public Position getHead() {
        return this.body.getFirst();
    }

    public int getSize() {
        return this.body.size();
    }
}