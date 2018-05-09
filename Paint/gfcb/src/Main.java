package application;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

import com.sun.javafx.scene.control.skin.ColorPalette;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		Stack<Shape> undoHistory = new Stack();
		Stack<Shape> redoHistory = new Stack();

/* 
* Sal's Part
*
* Made the buttons, style, color picker, slider, scene (down below)
* 
*/
		// Creates the buttons
		ToggleButton drawBtn = new ToggleButton("Draw");
		ToggleButton lineBtn = new ToggleButton("Line");
		ToggleButton rectBtn = new ToggleButton("Rectangle");
		ToggleButton circleBtn = new ToggleButton("Circle");
		ToggleButton ovalBtn = new ToggleButton("Oval");
		ToggleButton eraseBtn = new ToggleButton("Erase");
		ToggleButton textBtn = new ToggleButton("Text");
		ToggleButton[] toolsArr = {drawBtn, lineBtn, rectBtn, circleBtn, ovalBtn, eraseBtn, textBtn};
		ToggleGroup tools = new ToggleGroup();

		// Sets the buttons size and the style of cursor when hovering over
		for (ToggleButton tool : toolsArr) 
		{
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		// Color picker for line and fill
		ColorPicker lineColor = new ColorPicker(Color.BLACK);
		ColorPicker fillColor = new ColorPicker(Color.BLACK);

		// Creates the text box option
		TextArea textBox = new TextArea();
		textBox.setPrefRowCount(1);

		// Sets the slider for width of brush
		Slider slider = new Slider();
		slider.setShowTickLabels(true);
		slider.setShowTickMarks(true);

		// Labeling the Color and Fill Pickers
		Label line_color = new Label("Line Color");
		line_color.setTextFill(Color.WHITE);
		Label fill_color = new Label("Fill Color");
		fill_color.setTextFill(Color.WHITE);
		Label line_width = new Label("2.0");
		line_width.setTextFill(Color.WHITE);

		// Creates the labeled buttons below everything else
		Button undo = new Button("Undo");
		Button redo = new Button("Redo");
		Button save = new Button("Save");
		Button open = new Button("Open");
		Button[] basicArr = {undo, redo, save, open};

		// Creates the style of the undo, redo, save, open buttons
		for(Button btn : basicArr) 
		{
			btn.setMinWidth(90);
			btn.setCursor(Cursor.HAND);
			btn.setTextFill(Color.BLACK);
		}
		save.setStyle("-fx-background-color: RED;");
		open.setStyle("-fx-background-color: RED;");

		// Sets up the vbox for all the buttons and options
		VBox btns = new VBox(10);
		btns.getChildren().addAll(open, save, drawBtn, lineBtn, rectBtn, circleBtn, ovalBtn, eraseBtn, textBtn, textBox, line_color, lineColor, fill_color, fillColor, line_width, slider, undo, redo);
		btns.setPadding(new Insets(5));
		btns.setStyle("-fx-background-color: #100");
		btns.setPrefWidth(100);

/* 
* Alonna's Part
*
* Made the canvas, objects, actions when mouse clicked a certain button 
* 
*/
		
		// Creating the Canvas
		Canvas canvas = new Canvas(1000, 800);
		GraphicsContext gc;
		gc = canvas.getGraphicsContext2D();
		gc.setLineWidth(1);

		// Creating the objects
		Line line = new Line();
		Rectangle rect = new Rectangle();
		Circle circ = new Circle();
		Ellipse oval = new Ellipse();

		// Mouse Pressed actions
		canvas.setOnMousePressed(e->{
			// Action for draw button
			if(drawBtn.isSelected()) {
				gc.setStroke(lineColor.getValue());
				gc.beginPath();
				gc.lineTo(e.getX(), e.getY());
			}
			// Action for erase button
			else if(eraseBtn.isSelected()) {
				double lineWidth = gc.getLineWidth();
				gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
			}
			// Action for line button
			else if(lineBtn.isSelected()) {
				gc.setStroke(lineColor.getValue());
				line.setStartX(e.getX());
				line.setStartY(e.getY());
			}
			// Action for rectangle button
			else if(rectBtn.isSelected()) {
				gc.setStroke(lineColor.getValue());
				gc.setFill(fillColor.getValue());
				rect.setX(e.getX());                
				rect.setY(e.getY());
			}
			// Action for circle button
			else if(circleBtn.isSelected()) {
				gc.setStroke(lineColor.getValue());
				gc.setFill(fillColor.getValue());
				circ.setCenterX(e.getX());
				circ.setCenterY(e.getY());
			}
			// Action for oval button
			else if(ovalBtn.isSelected()) {
				gc.setStroke(lineColor.getValue());
				gc.setFill(fillColor.getValue());
				oval.setCenterX(e.getX());
				oval.setCenterY(e.getY());
			}
			// Action for text box button
			else if(textBtn.isSelected()) {
				gc.setLineWidth(1);
				gc.setFont(Font.font(slider.getValue()));
				gc.setStroke(lineColor.getValue());
				gc.setFill(fillColor.getValue());
				gc.fillText(textBox.getText(), e.getX(), e.getY());
				gc.strokeText(textBox.getText(), e.getX(), e.getY());
			}
		});

		// Drawing on the canvas
		canvas.setOnMouseDragged(e->{
			if(drawBtn.isSelected()) {
				gc.lineTo(e.getX(), e.getY());
				gc.stroke();
			}
		// Erase
			else if(eraseBtn.isSelected()){
				double lineWidth = gc.getLineWidth();
				gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
			}
		});

		canvas.setOnMouseReleased(e->{
			if(drawBtn.isSelected()) {
				gc.lineTo(e.getX(), e.getY());
				gc.stroke();
				gc.closePath();
			}
			else if(eraseBtn.isSelected()) {
				double lineWidth = gc.getLineWidth();
				gc.clearRect(e.getX() - lineWidth / 2, e.getY() - lineWidth / 2, lineWidth, lineWidth);
			}
			else if(lineBtn.isSelected()) {
				line.setEndX(e.getX());
				line.setEndY(e.getY());
				gc.strokeLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
				
				undoHistory.push(new Line(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY()));
			}
			else if(rectBtn.isSelected()) 
			{
				rect.setWidth(Math.abs((e.getX() - rect.getX())));
				rect.setHeight(Math.abs((e.getY() - rect.getY())));
				
				if(rect.getX() > e.getX()) 
				{
					rect.setX(e.getX());
				}
				if(rect.getY() > e.getY()) 
				{
					rect.setY(e.getY());
				}

				gc.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
				gc.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());

				undoHistory.push(new Rectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));

			}
			else if(circleBtn.isSelected()) 
			{
				circ.setRadius((Math.abs(e.getX() - circ.getCenterX()) + Math.abs(e.getY() - circ.getCenterY())) / 2);

				if(circ.getCenterX() > e.getX()) 
				{
					circ.setCenterX(e.getX());
				}
				if(circ.getCenterY() > e.getY()) 
				{
					circ.setCenterY(e.getY());
				}

				gc.fillOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());
				gc.strokeOval(circ.getCenterX(), circ.getCenterY(), circ.getRadius(), circ.getRadius());

				undoHistory.push(new Circle(circ.getCenterX(), circ.getCenterY(), circ.getRadius()));
			}
			else if(ovalBtn.isSelected()) {
				oval.setRadiusX(Math.abs(e.getX() - oval.getCenterX()));
				oval.setRadiusY(Math.abs(e.getY() - oval.getCenterY()));

				if(oval.getCenterX() > e.getX()) {
					oval.setCenterX(e.getX());
				}
				if(oval.getCenterY() > e.getY()) {
					oval.setCenterY(e.getY());
				}

				gc.strokeOval(oval.getCenterX(), oval.getCenterY(), oval.getRadiusX(), oval.getRadiusY());
				gc.fillOval(oval.getCenterX(), oval.getCenterY(), oval.getRadiusX(), oval.getRadiusY());

				undoHistory.push(new Ellipse(oval.getCenterX(), oval.getCenterY(), oval.getRadiusX(), oval.getRadiusY()));
			}
			redoHistory.clear();
			Shape lastUndo = undoHistory.lastElement();
			lastUndo.setFill(gc.getFill());
			lastUndo.setStroke(gc.getStroke());
			lastUndo.setStrokeWidth(gc.getLineWidth());
		});

/*
* Sal and Alonna
* 
*  Created the action for the color picker (fill and line), slider
*  
*/
		
		// Color Picker
		lineColor.setOnAction(e->{
			gc.setStroke(lineColor.getValue());
		});
		fillColor.setOnAction(e->{
			gc.setFill(fillColor.getValue());
		});

		// Slider
		slider.valueProperty().addListener(e->{
			double width = slider.getValue();
			if(textBtn.isSelected())
			{
				gc.setLineWidth(1);
				gc.setFont(Font.font(slider.getValue()));
				line_width.setText(String.format("%.1f", width));
				return;
			}
			line_width.setText(String.format("%.1f", width));
			gc.setLineWidth(width);
		});

/* 
* Jason's Part
*
* Made the actions work for Undo, Redo, Open, Save
* 
*/
		
		// Undo
		undo.setOnAction(e->{
			if(!undoHistory.empty()){
				gc.clearRect(0, 0, 1080, 790);
				Shape removedShape = undoHistory.lastElement();
				if(removedShape.getClass() == Line.class) {
					Line tempLine = (Line) removedShape;
					tempLine.setFill(gc.getFill());
					tempLine.setStroke(gc.getStroke());
					tempLine.setStrokeWidth(gc.getLineWidth());
					redoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));

				}
				// Undo the rectangle
				else if(removedShape.getClass() == Rectangle.class) {
					Rectangle tempRect = (Rectangle) removedShape;
					tempRect.setFill(gc.getFill());
					tempRect.setStroke(gc.getStroke());
					tempRect.setStrokeWidth(gc.getLineWidth());
					redoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
				}
				// Undo the circle
				else if(removedShape.getClass() == Circle.class) {
					Circle tempCirc = (Circle) removedShape;
					tempCirc.setStrokeWidth(gc.getLineWidth());
					tempCirc.setFill(gc.getFill());
					tempCirc.setStroke(gc.getStroke());
					redoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
				}
				// Undo the oval
				else if(removedShape.getClass() == Ellipse.class) {
					Ellipse tempOval = (Ellipse) removedShape;
					tempOval.setFill(gc.getFill());
					tempOval.setStroke(gc.getStroke());
					tempOval.setStrokeWidth(gc.getLineWidth());
					redoHistory.push(new Ellipse(tempOval.getCenterX(), tempOval.getCenterY(), tempOval.getRadiusX(), tempOval.getRadiusY()));
				}

				Shape lastRedo = redoHistory.lastElement();
				lastRedo.setFill(removedShape.getFill());
				lastRedo.setStroke(removedShape.getStroke());
				lastRedo.setStrokeWidth(removedShape.getStrokeWidth());
				undoHistory.pop();

				for(int i=0; i < undoHistory.size(); i++) {
					Shape shape = undoHistory.elementAt(i);
					if(shape.getClass() == Line.class) {
						Line temp = (Line) shape;
						gc.setLineWidth(temp.getStrokeWidth());
						gc.setStroke(temp.getStroke());
						gc.setFill(temp.getFill());
						gc.strokeLine(temp.getStartX(), temp.getStartY(), temp.getEndX(), temp.getEndY());
					}
					else if(shape.getClass() == Rectangle.class) {
						Rectangle temp = (Rectangle) shape;
						gc.setLineWidth(temp.getStrokeWidth());
						gc.setStroke(temp.getStroke());
						gc.setFill(temp.getFill());
						gc.fillRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
						gc.strokeRect(temp.getX(), temp.getY(), temp.getWidth(), temp.getHeight());
					}
					else if(shape.getClass() == Circle.class) {
						Circle temp = (Circle) shape;
						gc.setLineWidth(temp.getStrokeWidth());
						gc.setStroke(temp.getStroke());
						gc.setFill(temp.getFill());
						gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
						gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadius(), temp.getRadius());
					}
					else if(shape.getClass() == Ellipse.class) {
						Ellipse temp = (Ellipse) shape;
						gc.setLineWidth(temp.getStrokeWidth());
						gc.setStroke(temp.getStroke());
						gc.setFill(temp.getFill());
						gc.fillOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
						gc.strokeOval(temp.getCenterX(), temp.getCenterY(), temp.getRadiusX(), temp.getRadiusY());
					}
				}
			} 
			else 
			{
				System.out.println("CANNOT UNDO");
			}
		});

		// Redo
		redo.setOnAction(e->{
			if(!redoHistory.empty()) {
				Shape shape = redoHistory.lastElement();
				gc.setLineWidth(shape.getStrokeWidth());
				gc.setStroke(shape.getStroke());
				gc.setFill(shape.getFill());

				redoHistory.pop();
				if(shape.getClass() == Line.class) {
					Line tempLine = (Line) shape;
					gc.strokeLine(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY());
					undoHistory.push(new Line(tempLine.getStartX(), tempLine.getStartY(), tempLine.getEndX(), tempLine.getEndY()));
				}
				else if(shape.getClass() == Rectangle.class) {
					Rectangle tempRect = (Rectangle) shape;
					gc.fillRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());
					gc.strokeRect(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight());

					undoHistory.push(new Rectangle(tempRect.getX(), tempRect.getY(), tempRect.getWidth(), tempRect.getHeight()));
				}
				else if(shape.getClass() == Circle.class) {
					Circle tempCirc = (Circle) shape;
					gc.fillOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());
					gc.strokeOval(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius(), tempCirc.getRadius());

					undoHistory.push(new Circle(tempCirc.getCenterX(), tempCirc.getCenterY(), tempCirc.getRadius()));
				}
				else if(shape.getClass() == Ellipse.class) {
					Ellipse tempOval = (Ellipse) shape;
					gc.fillOval(tempOval.getCenterX(), tempOval.getCenterY(), tempOval.getRadiusX(), tempOval.getRadiusY());
					gc.strokeOval(tempOval.getCenterX(), tempOval.getCenterY(), tempOval.getRadiusX(), tempOval.getRadiusY());

					undoHistory.push(new Ellipse(tempOval.getCenterX(), tempOval.getCenterY(), tempOval.getRadiusX(), tempOval.getRadiusY()));
				}
				Shape lastUndo = undoHistory.lastElement();
				lastUndo.setFill(gc.getFill());
				lastUndo.setStroke(gc.getStroke());
				lastUndo.setStrokeWidth(gc.getLineWidth());
			} else {
				System.out.println("CANNOT REDO");
			}
		});

		// Open a file as image
		open.setOnAction((e)->{
			FileChooser openFile = new FileChooser();
			openFile.setTitle("Open File");
			File file = openFile.showOpenDialog(primaryStage);
			if (file != null) {
				try {
					InputStream io = new FileInputStream(file);
					Image img = new Image(io);
					gc.drawImage(img, 0, 0);
				} catch (IOException ex) {
					System.out.println("Error!");
				}
			}
		});

		// Save the file as .png
		save.setOnAction((e)->{
			FileChooser savefile = new FileChooser();
			savefile.setTitle("Save File");

			File file = savefile.showSaveDialog(primaryStage);
			if (file != null) {
				try {
					WritableImage writableImage = new WritableImage(1080, 790);
					canvas.snapshot(null, writableImage);
					RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
					ImageIO.write(renderedImage, "png", file);
				} catch (IOException ex) {
					System.out.println("Error!");
				}
			}

		});

/* 
* Sal's Part
*
* Setting the scene
* 
*/
		BorderPane pane = new BorderPane();
		pane.setRight(btns);
		pane.setCenter(canvas);

		Scene scene = new Scene(pane, 1200, 800);

		primaryStage.setTitle("Paint");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
