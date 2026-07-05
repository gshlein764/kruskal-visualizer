import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import model.Edge;
import model.Node;
import algorithm.DSU;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main extends Application {

    private final List<Node> nodes = new ArrayList<>();
    private final Pane graphPane = new Pane();
    private final Random random = new Random();
    private final List<Edge> edges = new ArrayList<>();
    private List<Step> steps = new ArrayList<>();
    private int stepIndex = 0;

    @Override
    public void start(Stage stage) {

        graphPane.setStyle("-fx-background-color: #f4f4f4;");

        Button generateButton = new Button("Generate Graph");
        Button kruskalButton = new Button("Run Kruskal");
        Button stepButton = new Button("Next Step");

        generateButton.setOnAction(e -> {
            generateGraph(6);
            generateEdges(6);
            drawGraph();
            drawEdges();
        });

        kruskalButton.setOnAction(e -> {
            printSortedEdges();
            runKruskal();
        });

        stepButton.setOnAction(e -> showStep());

        BorderPane top = new BorderPane();
        BorderPane root = new BorderPane();
        root.setCenter(graphPane);
        top.setLeft(generateButton);
        top.setCenter(kruskalButton);
        top.setRight(stepButton);

        root.setTop(top);

        Scene scene = new Scene(root, 900, 600);

        stage.setTitle("Kruskal Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    // 🎲 генерация вершин
    private void generateGraph(int n) {
        nodes.clear();

        for (int i = 0; i < n; i++) {
            double x = 100 + random.nextInt(700);
            double y = 100 + random.nextInt(400);

            nodes.add(new Node(i, x, y));
        }
    }

    // 🎨 рисуем вершины
    private void drawGraph() {
        graphPane.getChildren().clear();

        for (Node node : nodes) {

            Circle circle = new Circle(node.x, node.y, 20);
            circle.setFill(Color.CORNFLOWERBLUE);
            circle.setStroke(Color.BLACK);

            Text idText = new Text(node.x - 4, node.y + 4,
                    String.valueOf(node.id));

            idText.setFill(Color.WHITE);

            graphPane.getChildren().addAll(circle, idText);
        }
    }

    private void generateEdges(int n) {
        edges.clear();

        Random r = new Random();

        // простой способ: почти полный граф
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {

                if (r.nextDouble() < 0.5) { // 50% шанс ребра
                    int weight = 1 + r.nextInt(20);
                    edges.add(new Edge(i, j, weight));
                }
            }
        }
    }

    private void drawEdges() {

        for (Edge e : edges) {

            Node a = nodes.get(e.u);
            Node b = nodes.get(e.v);

            Line line = new Line(a.x, a.y, b.x, b.y);
            line.setStrokeWidth(2);
            line.setStroke(Color.GRAY);

            // ⭐ вес ребра (в центре линии)
            double midX = (a.x + b.x) / 2;
            double midY = (a.y + b.y) / 2;

            Text weight = new Text(midX, midY, String.valueOf(e.weight));
            weight.setFill(Color.BLACK);

            graphPane.getChildren().addAll(line, weight);
        }
    }

    private static class Step {
        Edge edge;
        boolean accepted;

        Step(Edge edge, boolean accepted) {
            this.edge = edge;
            this.accepted = accepted;
        }
    }

    private void runKruskal() {

        steps.clear();
        stepIndex = 0;

        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort((a, b) -> a.weight - b.weight);

        DSU dsu = new DSU(nodes.size());

        for (Edge e : sorted) {
            boolean ok = dsu.union(e.u, e.v);
            steps.add(new Step(e, ok));
        }
    }

    private void showStep() {

        if (stepIndex >= steps.size()) return;

        Step s = steps.get(stepIndex);
        stepIndex++;

        Node a = nodes.get(s.edge.u);
        Node b = nodes.get(s.edge.v);

        Line line = new Line(a.x, a.y, b.x, b.y);
        line.setStrokeWidth(3);

        if (s.accepted) {
            line.setStroke(Color.GREEN);
        } else {
            line.setStroke(Color.RED);
        }

        graphPane.getChildren().add(line);
    }

    private void printSortedEdges() {

        List<Edge> sorted = new ArrayList<>(edges);
        sorted.sort((a, b) -> a.weight - b.weight);

        System.out.println("=== SORTED EDGES ===");

        for (Edge e : sorted) {
            System.out.println(e.u + " - " + e.v + " | weight = " + e.weight);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}