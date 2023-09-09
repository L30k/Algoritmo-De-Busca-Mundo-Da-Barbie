package com.l30k.myapplication;

import java.util.*;

public class AStar {
    private int[][] grid;
    private int numRows;
    private int numCols;

    public AStar(int[][] grid) {
        this.grid = grid;
        this.numRows = grid.length;
        this.numCols = grid[0].length;
    }

    public List<Node> findPath(int startX, int startY, int endX, int endY) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        Set<Node> closedSet = new HashSet<>();

        Node startNode = new Node(startX, startY, 0, calculateHeuristic(startX, startY, endX, endY), null);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.getX() == endX && current.getY() == endY) {
                return reconstructPath(current);
            }

            closedSet.add(current);

            for (Node neighbor : getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue;
                }

                int tentativeCost = current.getCost() + grid[neighbor.getX()][neighbor.getY()];

                if (!openSet.contains(neighbor) || tentativeCost < neighbor.getCost()) {
                    neighbor.setCost(tentativeCost);
                    neighbor.setCostPorMovimento(grid[neighbor.getX()][neighbor.getY()]);
                    neighbor.setHeuristic(calculateHeuristic(neighbor.getX(), neighbor.getY(), endX, endY));
                    neighbor.setParent(current);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }

        return null; // No path found
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possible moves: up, down, left, right

        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];

            if (isValidLocation(newX, newY)) {
                Node neighbor = new Node(newX, newY, 0, 0, null);
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < numRows && y >= 0 && y < numCols && grid[x][y] != 0;
    }

    private int calculateHeuristic(int x, int y, int endX, int endY) {
        // Use the Manhattan distance as the heuristic
        return Math.abs(endX - x) + Math.abs(endY - y);
    }

    private List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        while (node != null) {
            path.add(node);
            node = node.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}

