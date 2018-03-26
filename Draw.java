/*
When the user clicks into the drawing space, the pen is activated.
The user can now move the mouse to draw. 
When the user clicks again, the pen is de-activated and no longer draws. 
Important Note: The user does not click-and-drag to draw. 
The functionality is: a) click once to turn on, b) move to draw, c) click once to turn off.

The user can select from three different pen colors.
The program displays whether the pen is on or off (in "draw" mode or "off").
There is a clear button that clears the entire drawing space. (Hint: think about emptying a list!)

Extra Credit A: An Eraser (15 points)
Add support for an eraser. 
When the eraser is activated, the user can remove previously drawn points from the drawing space. 
For full extra credit, the points should actually be removed.
To complete this extra credit, add a third state for the pen: it is now draw/off/erase. (Hint: consider using an enum!)
Hint: use an iterator and check out the "contains" method in the Node class!

Extra Credit B: Extra Feature (10 points)
Add an additional drawing functionality. 
This must be different from simply choosing another pen color- that will not count for any extra credit. 
Be creative!
 */

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.application.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;


public class Draw extends Application {

	private final String title = "Draw Something";
	
	private final int 	BRUSH_SIZE_1 = 3, 
						BRUSH_SIZE_2 = 4, 
						BRUSH_SIZE_3 = 5;
	
	private final int 	DRAWING_PANE_X = 530,
						DRAWING_PANE_Y = 470;
	
	private final int 	MAIN_WINDOW_X = 580,
						MAIN_WINDOW_Y = 580;
	
	private final int	BORDER_PADDING = 30;
	
	private Rectangle frame;
	private Text mode;
	private Text sizeText;
	private Text colorText;
	private Text shapeText;
	
	private VBox shapes;
	private VBox shapeMenu;
	
	private HBox sizes;
	private VBox sizeMenu;
	
	private HBox colors;
	private VBox colorMenu;
	
	private HBox choices;
	private HBox menu;
	private Pane drawingPane;
	private VBox mainWindow;
	
	private ToggleGroup shape;
	private RadioButton circle, triangle;

	private ToggleGroup size;
	private RadioButton size1, size2, size3;
	
	private ToggleGroup color;
	private RadioButton red, blue, yellow, eraser;

	private Button clear;
	
	private boolean isDrawing;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		frame = new Rectangle(BORDER_PADDING, 0, DRAWING_PANE_X, DRAWING_PANE_Y);
		frame.setFill(Color.TRANSPARENT);
		frame.setStroke(Color.BLACK);
		
		drawingPane = new Pane(frame);
		drawingPane.setPrefSize(DRAWING_PANE_X, DRAWING_PANE_Y);
		
		size = new ToggleGroup();
		color = new ToggleGroup();
		shape = new ToggleGroup();
		
		sizeText = new Text("size");
		colorText = new Text("color");
		shapeText = new Text("shape");
		
		mode = new Text("Pen: OFF");
		mode.setFont(Font.font("Times", 16));
		
		size1 = new RadioButton("1");
		size1.setSelected(true);
		size1.setToggleGroup(size);
		
		size2 = new RadioButton("2");
		size2.setToggleGroup(size);
		
		size3 = new RadioButton("3");
		size3.setToggleGroup(size);

		red = new RadioButton("Red");
		red.setSelected(true);
		red.setToggleGroup(color);
		
		blue = new RadioButton("Blue");
		blue.setToggleGroup(color);
		
		yellow = new RadioButton("Yellow");
		yellow.setToggleGroup(color);
		
		eraser = new RadioButton("Eraser");
		eraser.setToggleGroup(color);
		
		clear = new Button("Clear");
		clear.setOnAction(this::handleClear);
		
		triangle = new RadioButton("Triangle");
		triangle.setToggleGroup(shape);
		
		circle = new RadioButton("Circle");
		circle.setSelected(true);
		circle.setToggleGroup(shape);
		
		shapes = new VBox(circle, triangle);
		//shapes.setSpacing(4);
		shapeMenu = new VBox(shapeText, shapes);
		
		sizes = new HBox(size1, size2, size3);
		sizes.setSpacing(4);
		sizeMenu = new VBox(sizeText, sizes);
		
		colors = new HBox(red, blue, yellow, eraser);
		colors.setSpacing(4);
		colorMenu = new VBox(colorText, colors);
		
		choices = new HBox(shapeMenu, sizeMenu, colorMenu);
		choices.setSpacing(50);
		
		menu = new HBox(choices, clear);
		menu.setSpacing(10);
		menu.setAlignment(Pos.CENTER);
		menu.setPadding(new Insets(10));
		
		mainWindow = new VBox(mode, drawingPane, menu);
		mainWindow.setAlignment(Pos.CENTER);

		drawingPane.setOnMouseClicked(this::handleMouseClick);
		drawingPane.setOnMouseMoved(this::handleMouseMotion);
		
		Scene scene = new Scene(mainWindow, MAIN_WINDOW_X, MAIN_WINDOW_Y, Color.TRANSPARENT);
		
		primaryStage.setTitle(title);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private void handleMouseClick(MouseEvent click) {
		isDrawing = !isDrawing;
	
		if(isDrawing && eraser.isSelected()) {
			mode.setText("Pen: ERASE");
		} else if(isDrawing) {
			mode.setText("Pen: DRAW");
		} else {
			mode.setText("Pen: OFF");
		}
	}
	
	private void handleClear(ActionEvent clear) {
		drawingPane.getChildren().clear();
		drawingPane.getChildren().add(frame);
	}
	
	private void handleEraser(MouseEvent erase) {
		if(isDrawing && eraser.isSelected()) {
			
			if(erase.getSource() instanceof Circle) {
				Circle eraseBrush = (Circle) erase.getSource();
				drawingPane.getChildren().remove(eraseBrush);
				
			} else if(erase.getSource() instanceof Polygon) {
				Polygon eraseBrush = (Polygon) erase.getSource();
				drawingPane.getChildren().remove(eraseBrush);
			}
		} 
	}
	
	private void handleMouseMotion(MouseEvent move) {
		int r = BRUSH_SIZE_1;
		
		if(size.getSelectedToggle().equals(size2)) {
			r = BRUSH_SIZE_2;
		} else if(size.getSelectedToggle().equals(size3)) {
			r = BRUSH_SIZE_3;
		}
		
		if(isDrawing && !eraser.isSelected()) {
			double x = move.getX();
			double y = move.getY();
			
			if(		x <= DRAWING_PANE_X + BORDER_PADDING && x >= BORDER_PADDING && 
					y <= DRAWING_PANE_Y + BORDER_PADDING && y >= 0) {
				
				if(circle.isSelected()) {
					Circle circle = new Circle(x, y, r);
					if(red.isSelected()) {
						circle.setFill(Color.RED);
					} else if(blue.isSelected()) {
						circle.setFill(Color.BLUE);
					} else if(yellow.isSelected()) {
						circle.setFill(Color.YELLOW);
					} 
					
					circle.setOnMouseEntered(this::handleEraser);
					drawingPane.getChildren().add(circle);
				} else if(triangle.isSelected()) {
					Polygon triangle = new Polygon();
					if(red.isSelected()) {
						triangle.setFill(Color.RED);
					} else if(blue.isSelected()) {
						triangle.setFill(Color.BLUE);
					} else if(yellow.isSelected()) {
						triangle.setFill(Color.YELLOW);
					}
					triangle.getPoints().addAll(new Double[] {
							x, y - 5,
							x + (r * 2), y + (r * 2),
							x - (r * 2), y + (r * 2) });
					triangle.setOnMouseEntered(this::handleEraser);
					drawingPane.getChildren().add(triangle);
				}
				
			} 
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
