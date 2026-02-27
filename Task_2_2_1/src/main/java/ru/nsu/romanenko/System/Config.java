package ru.nsu.romanenko.System;

import java.util.ArrayList;

public record Config(int bakersCount, ArrayList<Integer> bakersSpeed, int couriersCount,
                     ArrayList<Integer> couriersCapacity, int warehouseCapacity) {}