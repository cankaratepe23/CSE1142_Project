import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        File level1 = new File("res/level1.txt");
        Scanner levelScanner = null;
        try {
            levelScanner = new Scanner(level1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GridPane mainGrid = new GridPane();
        Scene mainScene = new Scene(mainGrid);
        int startPipe = 0;
        int endPipe = 0;
        // assert levelScanner != null;
        while (levelScanner.hasNext()) {
            String[] inputArgs = levelScanner.nextLine().split(",");
            int index = (Integer.parseInt(inputArgs[0]) - 1);
            int row = index / 4;
            int column = index % 4;
            Tile tileToAdd;
            ImageView pepe = new ImageView("file:res/pepe.png");
            pepe.fitWidthProperty().bind(mainScene.widthProperty().divide(4.0));
            pepe.fitHeightProperty().bind(mainScene.heightProperty().divide(4.0));
            pepe.setPreserveRatio(true);

            if (inputArgs[1].startsWith("Tile")) {
                boolean isStatic = inputArgs[1].length() > 4;
                if (inputArgs[2].equals("Vertical"))
                    tileToAdd = new VerticalPipe(isStatic);
                else if (inputArgs[2].equals("Horizontal"))
                    tileToAdd = new HorizontalPipe(isStatic);
                else
                    tileToAdd = new BentPipe(isStatic, inputArgs[2]);
                tileToAdd.fitWidthProperty().bind(mainScene.widthProperty().divide(4.0));
                tileToAdd.fitHeightProperty().bind(mainScene.heightProperty().divide(4.0));
                tileToAdd.setPreserveRatio(true);
                mainGrid.add(tileToAdd, column, row);
            }
            else if (inputArgs[1].equals("Starter")) {
                tileToAdd = new StartPipe(inputArgs[2].equals("Vertical"));
                //TODO eliminate copy-pasted code
                tileToAdd.fitWidthProperty().bind(mainScene.widthProperty().divide(4.0));
                tileToAdd.fitHeightProperty().bind(mainScene.heightProperty().divide(4.0));
                tileToAdd.setPreserveRatio(true);
                mainGrid.add(tileToAdd, column, row);
                startPipe = row * 4 + column;
            }
            else if (inputArgs[1].equals("End")) {
                tileToAdd = new EndPipe(inputArgs[2].equals("Vertical"));
                tileToAdd.fitWidthProperty().bind(mainScene.widthProperty().divide(4.0));
                tileToAdd.fitHeightProperty().bind(mainScene.heightProperty().divide(4.0));
                tileToAdd.setPreserveRatio(true);
                mainGrid.add(tileToAdd, column, row);
                endPipe = row * 4 + column;
            }
            else {
                mainGrid.add(pepe, column, row);
            }
        }

        int lastBox = 0;
        int currentBox = 4;
        while (currentBox != endPipe) {
            int diff = currentBox - lastBox;
            int enteredFrom;
            if (diff == 4)
                enteredFrom = Tile.TOP;
            else if (diff == 1)
                enteredFrom = Tile.LEFT;
            else if (diff == -1)
                enteredFrom = Tile.RIGHT;
            else
                enteredFrom = Tile.BOTTOM;

            int moveValue = ((Tile) mainGrid.getChildren().get(currentBox)).values[enteredFrom];
            if (moveValue == 0) {
                currentBox = lastBox;
                break;
            }

            lastBox = currentBox;
            currentBox += moveValue;
        }
        System.out.println(currentBox);
        mainGrid.setAlignment(Pos.CENTER);
        stage.setScene(mainScene);
        stage.show();
    }
}
