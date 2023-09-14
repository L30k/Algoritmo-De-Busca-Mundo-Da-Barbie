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
        // Inicializa as estruturas de dados
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getTotalCost));
        Set<Node> closedSet = new HashSet<>();

        // Cria o nó de início e adiciona ao openSet
        Node startNode = new Node(startX, startY, 0, calculateHeuristic(startX, startY, endX, endY), null);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            // Pega o nó com o menor custo total do openSet
            Node current = openSet.poll();

            // Verifica se alcançou o destino
            if (current.getX() == endX && current.getY() == endY) {
                return reconstructPath(current); // Retorna o caminho encontrado
            }

            // Adiciona o nó atual ao closedSet
            closedSet.add(current);

            // Obtém os vizinhos do nó atual
            for (Node neighbor : getNeighbors(current)) {
                if (closedSet.contains(neighbor)) {
                    continue; // Ignora nós no closedSet
                }

                // Calcula o custo tentativo para alcançar o vizinho
                int tentativeCost = current.getCost() + grid[neighbor.getX()][neighbor.getY()];

                if (!openSet.contains(neighbor) || tentativeCost < neighbor.getCost()) {
                    // Atualiza as informações do vizinho
                    neighbor.setCost(tentativeCost);
                    neighbor.setCostPorMovimento(grid[neighbor.getX()][neighbor.getY()]);
                    neighbor.setHeuristic(calculateHeuristic(neighbor.getX(), neighbor.getY(), endX, endY));
                    neighbor.setParent(current);

                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor); // Adiciona o vizinho ao openSet
                    }
                }
            }
        }

        return null; // Nenhum caminho encontrado
    }

    // Função para obter os vizinhos válidos de um nó
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int x = node.getX();
        int y = node.getY();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Possíveis movimentos: cima, baixo, esquerda, direita

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

    // Função para verificar se uma coordenada é válida no grid
    private boolean isValidLocation(int x, int y) {
        return x >= 0 && x < numRows && y >= 0 && y < numCols && grid[x][y] != 0;
    }

    // Função para calcular a heurística (distância de Manhattan)
    private int calculateHeuristic(int x, int y, int endX, int endY) {
        return Math.abs(endX - x) + Math.abs(endY - y);
    }

    // Função para reconstruir o caminho a partir do nó de destino
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
