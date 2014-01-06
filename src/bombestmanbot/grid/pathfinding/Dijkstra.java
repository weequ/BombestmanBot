package bombestmanbot.grid.pathfinding;

import bombestmanbot.grid.Grid;
import bombestmanbot.grid.Tile;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class Dijkstra {
    
    private Grid grid;
    private final Tile start;
    private final TargetDecider targetDecider;
    private final WeigthDecider weigthDecider;
    
    public Dijkstra(Grid grid, Tile start, TargetDecider targetDecider, WeigthDecider weigthDecider) {
        this.grid = grid;
        this.start = start;
        this.targetDecider = targetDecider;
        this.weigthDecider = weigthDecider;
    }
    
    
    public List<Tile> findShortestPath() {
        Map<Tile, Tile> previous = new HashMap<>();
        Map<Tile, Double> distances = new HashMap<>();
        TileComparator tileComparator = new TileComparator(distances);
        previous.put(start, null);
        distances.put(start, 0.0);
        Queue<Tile> tileQueue = new PriorityQueue<>(10, tileComparator);
        tileQueue.offer(start);
        Tile current = null;
        while (!tileQueue.isEmpty()) {
            current = tileQueue.poll();
            if (targetDecider.isTarget(current)) { //Maali löytyi
                break;
            }
            double currentDist = distances.get(current);
            for (Tile neighbour : current.getNeighbours()) {
                Double dist = distances.get(neighbour);
                double newDist = currentDist+weigthDecider.getWeigth(neighbour, currentDist);
                if (dist == null) {//naapurissa ei käyty
                    previous.put(neighbour, current);
                    distances.put(neighbour, newDist);
                    tileQueue.offer(neighbour);
                } else if (dist <= newDist) {//käyty, ei tarvitse päivittää
                    continue;
                } else {//käyty, tarvitsee päivittää
                    distances.put(neighbour, newDist);
                    previous.put(neighbour, current);
                    tileQueue.remove(neighbour);
                    tileQueue.offer(neighbour);
                }
            }
        }
        if (!targetDecider.isTarget(current)) return null;
        LinkedList<Tile> path = new LinkedList<>();
        path.push(current);
        while (previous.get(path.peek()) != start) {
            path.push(previous.get(path.peek()));
        }
        return path;
    }
    
}
