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
