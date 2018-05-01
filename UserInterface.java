/**
 * 
 * 			Osman Süzer
 * 			131044051
 * 
 */

package application;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public final class UserInterface extends Application {

	Stage stage;

	private String imagePath;

	private vpt.Image originalImage; 
	private vpt.Image currentImage; 
	
	
	private String image2Path;

	
	private vpt.Image image2;
	
	
	private String maskPath;

	private vpt.Image mask;
	
	private vpt.Image previousImage;
	
	
	final GridPane previousImagePane = new GridPane();
	final GridPane currentImagePane = new GridPane();
	
	
	final Text resultText = new Text("");
	
	@Override
	public void start(final Stage stage) {


		ApplyFilter applyFilter = new ApplyFilter();
		
		
		this.stage = stage;
		stage.setTitle("Image Processing HW2");

		final FileChooser fileChooser = new FileChooser();

		
		//görüntünün seçilmesi için buton/////////////////////////////////////
		final Button selectImgButton = new Button("Select Image");
		GridPane.setConstraints(selectImgButton, 0, 0);
		
		
		final GridPane selectPathPane = new GridPane();
		selectPathPane.setHgap(30);
		selectPathPane.setVgap(25);
		selectPathPane.setAlignment(Pos.CENTER);
		
		selectPathPane.getChildren().add(selectImgButton);
		selectPathPane.setMinSize(50, 50);
		//////////////////////////////////////////////////////////////////////
		
		
		
		// seçilen görüntüyü göstermek için /////////////////////////////////////
		final GridPane myImagePane = new GridPane();
		myImagePane.setPrefSize(10, 10);
		myImagePane.setAlignment(Pos.CENTER);
		
		/////////////////////////////////////////////////////////////////////////

		
		// hesaplamak için domain seçimi ///////////////////////////////////////
		String textStyle2 = "-fx-font: 15px Tahoma; -fx-fill: green; -fx-stroke-width: 2;";

		final ToggleGroup toggleDomainGroup = new ToggleGroup();

		final Text selectDomainText = new Text("Select Domain : ");
		selectDomainText.setStyle(textStyle2);
		
		final RadioButton spatialDomainRadioButton = new RadioButton("Spatial Dom.");
		final RadioButton frequencyDomainRadioButton = new RadioButton("Frequency Dom.");

		spatialDomainRadioButton.setToggleGroup(toggleDomainGroup);
		frequencyDomainRadioButton.setToggleGroup(toggleDomainGroup);
		
		//implement ettikten sonra aç
		frequencyDomainRadioButton.setDisable(true);

		spatialDomainRadioButton.setSelected(true);
		
		
		GridPane.setConstraints(selectDomainText, 0,0);
		GridPane.setConstraints(spatialDomainRadioButton, 0,1);
		GridPane.setConstraints(frequencyDomainRadioButton, 1,1);
		
		
		////////////////////////////////////////////////////////////////////////////////
		
		
		// hesaplamak için domain seçimi ///////////////////////////////////////

		final Text selectWorkOnText = new Text("Work On : ");
		selectWorkOnText.setStyle(textStyle2);
		
		final ToggleGroup toggleOnGroup = new ToggleGroup();
		
		final RadioButton workOnOriginalRadioButton = new RadioButton("Original Image");
		final RadioButton workOnCurrentRadioButton = new RadioButton("Current Image");

		workOnOriginalRadioButton.setToggleGroup(toggleOnGroup);
		workOnCurrentRadioButton.setToggleGroup(toggleOnGroup);
		
		workOnOriginalRadioButton.setSelected(true);
		
		
		GridPane.setConstraints(selectWorkOnText, 0,3);
		GridPane.setConstraints(workOnOriginalRadioButton, 0,4);
		GridPane.setConstraints(workOnCurrentRadioButton, 1,4);
		
		final GridPane workOnSelectPane = new GridPane();
		
		workOnSelectPane.setVgap(10);
		workOnSelectPane.setHgap(10);
		
		workOnSelectPane.getChildren().addAll(selectDomainText, spatialDomainRadioButton, frequencyDomainRadioButton, selectWorkOnText, workOnOriginalRadioButton, workOnCurrentRadioButton);
		
	//	workOnSelectPane.setAlignment(Pos.CENTER);
		

		////////////////////////////////////////////////////////////////////////////////
		
		
		
		
		// kullanýcýnýn genel ayarlarý yapmasý için ////////////////////////////
		final Pane userSelectPane = new VBox(10);
		
		userSelectPane.getChildren().addAll(selectPathPane, myImagePane, workOnSelectPane);

		////////////////////////////////////////////////////////////////////////
		
		
		/// filtre ayarlarý için ///////////////////////////////////////////////
		
		
		final Text filterSizeText = new Text("Filter Size/Radius : ");
		filterSizeText.setStyle(textStyle2);
		
		TextField filterSizeTextField = new TextField ("3");

		filterSizeTextField.setMaxSize(70, 20);
		
		final GridPane filterSizeSelectPane = new GridPane();
		
		GridPane.setConstraints(filterSizeText, 0, 0);
		GridPane.setConstraints(filterSizeTextField,1, 0);
		
	
		
		filterSizeSelectPane.getChildren().addAll(filterSizeText, filterSizeTextField);
		
		////////////////////////////////////////////////////////////////////////
		
		
		
		// mean Filter //////////////////////////////////////////////////////
		
		final Button meanFilterButton = new Button("Mean Filter");
		
		meanFilterButton.setMinSize(150, 20);
		final GridPane meanFilterPane = new GridPane();
		meanFilterPane.setAlignment(Pos.CENTER);
		
		meanFilterPane.setStyle("-fx-base : #D5D8DC;"); 
		
		
		meanFilterPane.getChildren().add(meanFilterButton);
		
		///////////////////////////////////////////////////////////////////////
		
		
		// median Filter //////////////////////////////////////////////////////
		
		final Button medianFilterButton = new Button("Median Filter");
		
		medianFilterButton.setMinSize(150, 20);
		final GridPane medianFilterPane = new GridPane();
		medianFilterPane.setAlignment(Pos.CENTER);
		
		medianFilterPane.setStyle("-fx-base : #D5D8DC;");
		
		medianFilterPane.getChildren().add(medianFilterButton);
		
		
		///////////////////////////////////////////////////////////////////////

		// gaussian Filter //////////////////////////////////////////////////////
		
		
		final Text sigmaText = new Text("Sigma : ");
		sigmaText.setStyle(textStyle2);
		
		TextField sigmaTextField = new TextField ("1");

		sigmaTextField.setMaxSize(70, 20);
		
		final GridPane sigmaPane = new GridPane();
		
		GridPane.setConstraints(sigmaText, 0, 0);
		GridPane.setConstraints(sigmaTextField,1, 0);
		
		
		sigmaPane.setAlignment(Pos.CENTER);
	
		
		sigmaPane.getChildren().addAll(sigmaText, sigmaTextField);
		
		
		final Button gaussianFilterButton = new Button("Gaussian Filter");
		
		gaussianFilterButton.setMinSize(150, 20);
		
		final GridPane gaussianFilterPane = new GridPane();
		gaussianFilterPane.setAlignment(Pos.CENTER);
		
		GridPane.setConstraints(gaussianFilterButton, 0,0);
		gaussianFilterPane.getChildren().add(gaussianFilterButton);
		
		
		// unsharping Filter //////////////////////////////////////////////////////
		
		final Button unsharpingFilterButton = new Button("Unsharping Filter");
		
		unsharpingFilterButton.setMinSize(150, 20);
		
		final GridPane unsharpingFilterPane = new GridPane();
		unsharpingFilterPane.setAlignment(Pos.CENTER);
		
		GridPane.setConstraints(unsharpingFilterButton, 0,0);
		unsharpingFilterPane.getChildren().add(unsharpingFilterButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		
		final Pane sigmaWithFilterPane = new VBox(5);
		
		sigmaWithFilterPane.getChildren().addAll(sigmaPane, gaussianFilterPane, unsharpingFilterPane);
		sigmaWithFilterPane.setStyle("-fx-border-color: gray; -fx-background-color:#FDFEFE; -fx-base : #D5D8DC;");
		sigmaWithFilterPane.setPrefWidth(175);
		sigmaWithFilterPane.setPadding(new Insets(10,10,10,10));
		
		
		
		///////////////////////////////////////////////////////////////////////
		
	
				
		// sobel vertical Filter //////////////////////////////////////////////////////
		
		final Button sobelVerticalFilterButton = new Button("Vertical");
		
		sobelVerticalFilterButton.setMinSize(150, 20);
		final GridPane sobelVerticalFilterPane = new GridPane();
		sobelVerticalFilterPane.setAlignment(Pos.CENTER);
		
		sobelVerticalFilterPane.getChildren().add(sobelVerticalFilterButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		// sobel horizontal Filter //////////////////////////////////////////////////////
		
		final Button sobelHorizontalFilterButton = new Button("Horizontal");
		
		sobelHorizontalFilterButton.setMinSize(150, 20);
		final GridPane sobelHorizontalFilterPane = new GridPane();
		sobelHorizontalFilterPane.setAlignment(Pos.CENTER);
		
		sobelHorizontalFilterPane.getChildren().add(sobelHorizontalFilterButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// sobel gradient magnitude Filter //////////////////////////////////////////////////////
		
		final Button sobelGradientMagnitudeFilterButton = new Button("Gradient Magnitude");
		
		sobelGradientMagnitudeFilterButton.setMinSize(150, 20);
		final GridPane sobelGradientMagnitudeFilterPane = new GridPane();
		sobelGradientMagnitudeFilterPane.setAlignment(Pos.CENTER);
		
		sobelGradientMagnitudeFilterPane.getChildren().add(sobelGradientMagnitudeFilterButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// sobel Direction Filter //////////////////////////////////////////////////////
		
		final Button sobelDirectionFilterButton = new Button("Direction");
		
		sobelDirectionFilterButton.setMinSize(150, 20);
		final GridPane sobelDirectionFilterPane = new GridPane();
		sobelDirectionFilterPane.setAlignment(Pos.CENTER);
		
		sobelDirectionFilterPane.getChildren().add(sobelDirectionFilterButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		/// sobel filtre ayarlarý için ///////////////////////////////////////////////
		
		final Text sobelText = new Text("-Sobel Filters-");
		sobelText.setStyle(textStyle2);
		
		
		final GridPane sobelPane = new GridPane();
		sobelPane.setAlignment(Pos.CENTER);
		
		sobelPane.getChildren().addAll(sobelText);
		GridPane.setConstraints(sobelPane, 0, 0);
		
		
		final Text sobelCoefficientText = new Text("Coefficient : ");
		sobelCoefficientText.setStyle(textStyle2);
		
		TextField sobelCoefficientTextField = new TextField ("0.125");
	
		sobelCoefficientTextField.setMaxSize(70, 20);
		
		final Pane sobelCoefficientPane = new GridPane();
		
		GridPane.setConstraints(sobelCoefficientText, 0, 0);
		GridPane.setConstraints(sobelCoefficientTextField, 1, 0);
	
		sobelCoefficientPane.setLayoutX(50);
		sobelCoefficientPane.setPadding(new Insets(10));
		sobelCoefficientPane.getChildren().addAll(sobelCoefficientText, sobelCoefficientTextField);
		
		////////////////////////////////////////////////////////////////////////
		

		final Pane sobelFilters = new VBox(5);
		sobelFilters.setStyle("-fx-border-color: gray; -fx-background-color:#FDFEFE; -fx-base : #D5D8DC;");
		sobelFilters.setPrefWidth(175);
		sobelFilters.setPadding(new Insets(10,10,10,10));
		
		sobelFilters.getChildren().addAll(sobelPane, sobelCoefficientPane,
										sobelVerticalFilterPane, sobelHorizontalFilterPane, 
										sobelGradientMagnitudeFilterPane, sobelDirectionFilterPane);
		
		
		
		
		
		
		// inversionOperation //////////////////////////////////////////////////////
		
		final Button inversionOperationButton = new Button("Inversion");
		
		inversionOperationButton.setMinSize(150, 20);
		final GridPane inversionOperationPane = new GridPane();
		inversionOperationPane.setAlignment(Pos.CENTER);
		
		inversionOperationPane.getChildren().add(inversionOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		// additionOperation //////////////////////////////////////////////////////
		
		final Button additionOperationButton = new Button("Addition");
		
		additionOperationButton.setMinSize(150, 20);
		final GridPane additionOperationPane = new GridPane();
		additionOperationPane.setAlignment(Pos.CENTER);
		
		additionOperationPane.getChildren().add(additionOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// subtractionOperation //////////////////////////////////////////////////////
		
		final Button subtractionOperationButton = new Button("Subtraction");
		
		subtractionOperationButton.setMinSize(150, 20);
		final GridPane subtractionOperationPane = new GridPane();
		subtractionOperationPane.setAlignment(Pos.CENTER);
		
		subtractionOperationPane.getChildren().add(subtractionOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// powerOperation //////////////////////////////////////////////////////
		
		final Button powerOperationButton = new Button("Power");
		
		powerOperationButton.setMinSize(150, 20);
		final GridPane powerOperationPane = new GridPane();
		powerOperationPane.setAlignment(Pos.CENTER);
		
		powerOperationPane.getChildren().add(powerOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		/// pointOperations ayarlarý için ///////////////////////////////////////////////
		
		final Text pointOperationsText = new Text("-Point Operations-");
		pointOperationsText.setStyle(textStyle2);
		
		
		final GridPane pointOpPane = new GridPane();
		pointOpPane.setAlignment(Pos.CENTER);
		
		pointOpPane.getChildren().addAll(pointOperationsText);
		GridPane.setConstraints(pointOpPane, 0, 0);
		
		
		final Text withConstantText = new Text("*with Constant");
		withConstantText.setStyle(textStyle2);
		
		
		final GridPane withConstantPane = new GridPane();
		withConstantPane.setAlignment(Pos.CENTER);
		
		withConstantPane.getChildren().addAll(withConstantText);
		GridPane.setConstraints(withConstantPane, 0, 0);
		
		
		
		final Text pointOperationsConstantText = new Text("Constant : ");
		pointOperationsConstantText.setStyle(textStyle2);
		
		TextField pointOperationsCoefficientTextField = new TextField ("0.2");
		
		pointOperationsCoefficientTextField.setMaxSize(70, 20);
		
		
		final Pane pointOperationsConstantPane = new GridPane();
		
		GridPane.setConstraints(pointOperationsConstantText, 0, 0);
		GridPane.setConstraints(pointOperationsCoefficientTextField, 1, 0);
		
		pointOperationsConstantPane.setLayoutX(25);
		pointOperationsConstantPane.setPadding(new Insets(10));
		pointOperationsConstantPane.getChildren().addAll(pointOperationsConstantText, pointOperationsCoefficientTextField);
		
		////////////////////////////////////////////////////////////////////////
		
		final Pane pointOperationsWithConstantPane = new VBox(5);
		pointOperationsWithConstantPane.setStyle("-fx-border-color: gray;");
		pointOperationsWithConstantPane.setPrefWidth(170);
		pointOperationsWithConstantPane.setPadding(new Insets(5,5,5,5));
		
		pointOperationsWithConstantPane.getChildren().addAll(withConstantPane, pointOperationsConstantPane, additionOperationPane, subtractionOperationPane, powerOperationPane);
		
		
		
		// additionOperation //////////////////////////////////////////////////////
		
		final Button additionWithImageOperationButton = new Button("Addition");
		
		additionWithImageOperationButton.setMinSize(150, 20);
		final GridPane additionWithImageOperationPane = new GridPane();
		additionWithImageOperationPane.setAlignment(Pos.CENTER);
		
		additionWithImageOperationPane.getChildren().add(additionWithImageOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// subtractionOperation //////////////////////////////////////////////////////
		
		final Button subtractionWithImageOperationButton = new Button("Subtraction");
		
		subtractionWithImageOperationButton.setMinSize(150, 20);
		final GridPane subtractionWithImageOperationPane = new GridPane();
		subtractionWithImageOperationPane.setAlignment(Pos.CENTER);
		
		subtractionWithImageOperationPane.getChildren().add(subtractionWithImageOperationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		
		
		final Text withImageText = new Text("*with Image");
		withImageText.setStyle(textStyle2);
		
		
		final GridPane withImagePane = new GridPane();
		withImagePane.setAlignment(Pos.CENTER);
		
		withImagePane.getChildren().addAll(withImageText);
		GridPane.setConstraints(withImagePane, 0, 0);
		
		
		final Button selectImg2Button = new Button("Other Image");
		GridPane.setConstraints(selectImg2Button, 0, 0);
		
		final GridPane selectPath2Pane = new GridPane();
		selectPath2Pane.setHgap(30);
		selectPath2Pane.setVgap(25);
		selectPath2Pane.setAlignment(Pos.CENTER);
		
		selectPath2Pane.getChildren().add(selectImg2Button);
		selectPath2Pane.setMinSize(50, 50);
		

		////////////////////////////////////////////////////////////////////////
		
		final Pane pointOperationsWithImagePane = new VBox(5);
		pointOperationsWithImagePane.setStyle("-fx-border-color: gray;");
		pointOperationsWithImagePane.setPrefWidth(170);
		pointOperationsWithImagePane.setPadding(new Insets(5,5,5,5));
		
		pointOperationsWithImagePane.getChildren().addAll(withImagePane, selectPath2Pane, additionWithImageOperationPane,subtractionWithImageOperationPane);

		
		
		final Pane pointOperationsPane = new VBox(5);
		pointOperationsPane.setStyle("-fx-border-color: gray; -fx-background-color:#FDFEFE; -fx-base : #D5D8DC;");
		pointOperationsPane.setPrefWidth(175);
		pointOperationsPane.setPadding(new Insets(10,10,10,10));
		
		pointOperationsPane.getChildren().addAll(pointOpPane, inversionOperationPane, pointOperationsWithConstantPane, pointOperationsWithImagePane);
		
		
		//////////////////////////////////////////////////////////////////////////7
		

		/// Morphologicals ayarlarý için ///////////////////////////////////////////////
		
		final Text morphologicalOperationsText = new Text("-Morphological Operations-");
		morphologicalOperationsText.setStyle(textStyle2);
		
		final GridPane morphologicalOpPane = new GridPane();
		morphologicalOpPane.setAlignment(Pos.CENTER);
		
		morphologicalOpPane.getChildren().addAll(morphologicalOperationsText);
		GridPane.setConstraints(morphologicalOpPane, 0, 0);
		

		final Text selectFilterText = new Text("Select Filter : ");
		
		final ToggleGroup filterToggleGroup = new ToggleGroup();
		
				
		final RadioButton squareFilterRadioButton = new RadioButton("Square");
		final RadioButton diskFilterRadioButton = new RadioButton("Disk");
		final RadioButton circleFilterRadioButton = new RadioButton("Circle");
		final RadioButton crossFilterRadioButton = new RadioButton("Cross");
		
		
	
		squareFilterRadioButton.setToggleGroup(filterToggleGroup);
		diskFilterRadioButton.setToggleGroup(filterToggleGroup);
		circleFilterRadioButton.setToggleGroup(filterToggleGroup);
		crossFilterRadioButton.setToggleGroup(filterToggleGroup);
		
		//TODO implement ettikten sonra aç
		circleFilterRadioButton.setDisable(true);
		
		squareFilterRadioButton.setSelected(true);
		
		
		GridPane.setConstraints(selectFilterText, 0,0);
		GridPane.setConstraints(squareFilterRadioButton, 0,1);
		GridPane.setConstraints(diskFilterRadioButton, 0,2);
		GridPane.setConstraints(circleFilterRadioButton, 1,1);
		GridPane.setConstraints(crossFilterRadioButton, 1,2);
		
		final GridPane morphologicalFilterSelectPane = new GridPane();
		
		morphologicalFilterSelectPane.setVgap(10);
		morphologicalFilterSelectPane.setHgap(10);
		
		morphologicalFilterSelectPane.getChildren().addAll(selectFilterText, squareFilterRadioButton, diskFilterRadioButton,circleFilterRadioButton,crossFilterRadioButton);
		
		morphologicalFilterSelectPane.setAlignment(Pos.CENTER);
		
		
		// dilation //////////////////////////////////////////////////////
		
		final Button dilationButton = new Button("Dilation");
		
		dilationButton.setMinSize(160, 20);
		
		final GridPane dilationPane = new GridPane();
		dilationPane.setAlignment(Pos.CENTER);
		
		dilationPane.getChildren().add(dilationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// erosion //////////////////////////////////////////////////////
		
		final Button erosionButton = new Button("Erosion");
		
		erosionButton.setMinSize(160, 20);
		
		final GridPane erosionPane = new GridPane();
		erosionPane.setAlignment(Pos.CENTER);
		
		erosionPane.getChildren().add(erosionButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// gradient //////////////////////////////////////////////////////
		
		final Button gradientButton = new Button("Gradient");
		
		gradientButton.setMinSize(160, 20);
		
		final GridPane gradientPane = new GridPane();
		gradientPane.setAlignment(Pos.CENTER);
		
		gradientPane.getChildren().add(gradientButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		// opening //////////////////////////////////////////////////////
		
		final Button openingButton = new Button("Opening");
		
		openingButton.setMinSize(160, 20);
		
		final GridPane openingPane = new GridPane();
		openingPane.setAlignment(Pos.CENTER);
		
		openingPane.getChildren().add(openingButton);
				
		///////////////////////////////////////////////////////////////////////
		

		//  closing //////////////////////////////////////////////////////
		
		final Button closingButton = new Button("Closing");
		
		closingButton.setMinSize(160, 20);
		
		final GridPane closingPane = new GridPane();
		closingPane.setAlignment(Pos.CENTER);
		
		closingPane.getChildren().add(closingButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		// geodesic dilation //////////////////////////////////////////////////////
		
		final Button geodesicDilationButton = new Button("Geodesic Dilation");
		
		geodesicDilationButton.setMinSize(160, 20);
		
		final GridPane geodesicDilationPane = new GridPane();
		geodesicDilationPane.setAlignment(Pos.CENTER);
		
		geodesicDilationPane.getChildren().add(geodesicDilationButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// geodesic erosion //////////////////////////////////////////////////////
		
		final Button geodesicErosionButton = new Button("Geodesic Erosion");
		
		geodesicErosionButton.setMinSize(160, 20);
		
		final GridPane geodesicErosionPane = new GridPane();
		geodesicErosionPane.setAlignment(Pos.CENTER);
		
		geodesicErosionPane.getChildren().add(geodesicErosionButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// dilation ByReconstruction //////////////////////////////////////////////////////
		
		final Button dilationByReconstructionButton = new Button("Dilation By Reconstruction");
		
		dilationByReconstructionButton.setMinSize(160, 20);
		
		final GridPane dilationByReconstructionPane = new GridPane();
		dilationByReconstructionPane.setAlignment(Pos.CENTER);
		
		dilationByReconstructionPane.getChildren().add(dilationByReconstructionButton);
				
		///////////////////////////////////////////////////////////////////////
		
		// erosion ByReconstruction//////////////////////////////////////////////////////
		
		final Button erosionByReconstructionButton = new Button("Erosion By Reconstruction");
		
		erosionByReconstructionButton.setMinSize(160, 20);
		
		final GridPane erosionByReconstructionPane = new GridPane();
		erosionByReconstructionPane.setAlignment(Pos.CENTER);
		
		erosionByReconstructionPane.getChildren().add(erosionByReconstructionButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		final Text maskText = new Text("*Requires Mask");
		maskText.setStyle(textStyle2);
		
		final GridPane maskTextPane = new GridPane();
		maskTextPane.setAlignment(Pos.CENTER);
		GridPane.setConstraints(maskText, 0, 0);
		maskTextPane.getChildren().addAll(maskText);
		
		
		
		final Button selectMaskButton = new Button("Select Mask");
		GridPane.setConstraints(selectMaskButton, 0, 0);
		
		final GridPane selectMaskPane = new GridPane();
		selectMaskPane.setHgap(30);
		selectMaskPane.setVgap(25);
		selectMaskPane.setAlignment(Pos.CENTER);
		
		selectMaskPane.getChildren().add(selectMaskButton);
		selectMaskPane.setMinSize(50, 50);


		final Pane maskPane = new VBox(5);
		maskPane.setStyle("-fx-border-color: gray;");
		maskPane.setPrefWidth(170);
		maskPane.setPadding(new Insets(5,5,5,5));
		
		maskPane.getChildren().addAll(maskTextPane, selectMaskPane, geodesicDilationPane, geodesicErosionPane,
				dilationByReconstructionPane, erosionByReconstructionPane);


		final Pane morphologicalOperationsPane = new VBox(5);
		morphologicalOperationsPane.setStyle("-fx-border-color: gray; -fx-background-color:#FDFEFE; -fx-base : #D5D8DC;");
		morphologicalOperationsPane.setPrefWidth(175);
		morphologicalOperationsPane.setPadding(new Insets(10,10,10,10));

		
		morphologicalOperationsPane.getChildren().addAll(morphologicalOpPane,morphologicalFilterSelectPane, 
													dilationPane, erosionPane, gradientPane, openingPane, closingPane,
													maskPane);
		
		
		
		
	
		////////////////////////////////////////////////////////
		
		final Button lowPassButton = new Button("Low-Pass");
		
		lowPassButton.setMinSize(160, 20);
		
		final GridPane lowPassPane = new GridPane();
		lowPassPane.setAlignment(Pos.CENTER);
		
		lowPassPane.getChildren().add(lowPassButton);
				
		///////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////
		
		final Button highPassButton = new Button("High-Pass");
		
		highPassButton.setMinSize(160, 20);
		
		final GridPane highPassPane = new GridPane();
		highPassPane.setAlignment(Pos.CENTER);
		
		highPassPane.getChildren().add(highPassButton);
				
		///////////////////////////////////////////////////////////////////////
		
		///////////////////////////////////////////////////////
		
		final Button bandRejectButton = new Button("Band-Reject");
		
		bandRejectButton.setMinSize(160, 20);
		
		final GridPane bandRejectPane = new GridPane();
		bandRejectPane.setAlignment(Pos.CENTER);
		
		bandRejectPane.getChildren().add(bandRejectButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		///////////////////////////////////////////////////////
		
		final Button bandPassButton = new Button("Band-Pass");
		
		bandPassButton.setMinSize(160, 20);
		
		final GridPane bandPassPane = new GridPane();
		bandPassPane.setAlignment(Pos.CENTER);
		
		bandPassPane.getChildren().add(bandPassButton);
				
		///////////////////////////////////////////////////////////////////////
		
		
		final Pane passAndRejectPane = new VBox(5);
		passAndRejectPane.setStyle("-fx-border-color: gray; -fx-background-color:#FDFEFE; -fx-base : #D5D8DC;");
	
		passAndRejectPane.setPrefWidth(175);
		passAndRejectPane.setPadding(new Insets(10,10,10,10));

		passAndRejectPane.getChildren().addAll(lowPassPane,highPassPane,bandRejectPane, bandPassPane);
		
		

		final Pane filtersPane = new VBox(10);
		filtersPane.setPadding(new Insets(15,10,15,10));


		
		
		
		filtersPane.getChildren().addAll(meanFilterPane, medianFilterPane, sigmaWithFilterPane, sobelFilters, pointOperationsPane,passAndRejectPane, morphologicalOperationsPane);
		

		ScrollPane allFilters = new ScrollPane();


		allFilters.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		allFilters.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
		allFilters.setFitToWidth(true);
		allFilters.setContent(filtersPane);
		
		

		final Pane filterSelectPane = new VBox(10);
		filterSelectPane.setStyle("-fx-border-color: gray;");
		filterSelectPane.setPadding(new Insets(10,5,10,5));
		filterSelectPane.setMinWidth(240);
		
		filterSelectPane.getChildren().addAll(userSelectPane,filterSizeSelectPane,allFilters);
		
		
		ScrollPane root = new ScrollPane();
		TilePane tile = new TilePane();
		root.setStyle("-fx-background-color: DAE6F3;");
		tile.setPadding(new Insets(10, 10, 10, 10));
		tile.setHgap(15);


		root.setFitToWidth(true);
		root.setContent(tile);


	
		previousImagePane.setAlignment(Pos.CENTER);
		previousImagePane.setPadding(new Insets(10, 0,10, 10));
		previousImagePane.setMinWidth(430);
		
		
	
		currentImagePane.setAlignment(Pos.CENTER);
		currentImagePane.setPadding(new Insets(10, 10,10, 0));
		currentImagePane.setMinWidth(430);
		
		
		final Pane resultPane = new VBox(5);
		
		final Pane imageResultPane = new HBox(5);
		imageResultPane.setMinHeight(620);
		imageResultPane.setStyle("-fx-border-color: #2874A6; -fx-background-color:#FDFEFE;");
		
		imageResultPane.getChildren().addAll(previousImagePane, currentImagePane);
		
		
		final Pane timeResultAndWarningPane = new VBox(5);
		timeResultAndWarningPane.setPrefHeight(45);
		
		timeResultAndWarningPane.setStyle("-fx-border-color: #2874A6; -fx-background-color:#FDFEFE;");
		
		
		String textStyle = "-fx-font: 22px Tahoma; -fx-fill: #2874A6; -fx-stroke-width: 2;";
		resultText.setStyle(textStyle);
		

		GridPane resultTextPane = new GridPane();
		resultTextPane.setAlignment(Pos.CENTER);
		
		
		GridPane.setConstraints(resultText, 0 , 0);
		
		resultTextPane.getChildren().add(resultText);
		
		timeResultAndWarningPane.getChildren().add(resultTextPane);
		
		
		resultPane.getChildren().addAll(imageResultPane, timeResultAndWarningPane);
		
		
		
		final Pane hPane = new HBox(10);
		hPane.getChildren().addAll(filterSelectPane,resultPane);
		
		
		final Pane rootGroup = new VBox(10);
		rootGroup.getChildren().addAll(hPane);
		
		
		
		rootGroup.setPadding(new Insets(12, 12, 12, 12));
		
		stage.setMinWidth(1155);
		stage.setMinHeight(740);
		
		stage.setMaxWidth(1155);
		stage.setMaxHeight(740);
		

		
		stage.setScene(new Scene(rootGroup));
		stage.show();
		

		selectImgButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					
					
					imagePath = file.getPath();
				
					originalImage = vpt.algorithms.io.Load.invoke(imagePath);
					
					currentImage = originalImage.newInstance(true);
					previousImage = originalImage.newInstance(true);

					myImagePane.getChildren().clear();

					ImageView myImageWiev = createImageView(file, 150);

					GridPane.setConstraints(myImageWiev, 0, 0);
					myImagePane.getChildren().add(myImageWiev);

					workOnCurrentRadioButton.setDisable(false);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
		});
		
		
		stage.setOnCloseRequest(event -> {
			
			String currentDir=System.getProperty("user.dir");

			File previous = new File(currentDir+"\\previous.png");
			File current = new File(currentDir+"\\current.png");
			
			previous.delete();
			current.delete();
		});
		

		selectImg2Button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					
					image2Path = file.getPath();
				
					image2 = vpt.algorithms.io.Load.invoke(image2Path);
					
				}
			}
		});
		
		selectMaskButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					
					maskPath = file.getPath();
					mask = vpt.algorithms.io.Load.invoke(maskPath);
					
				}
			}
		});
		
		
		meanFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					long tStart = System.currentTimeMillis();

					 
					if(workOnOriginalRadioButton.isSelected()) {
					
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyMeanFilter(originalImage, filterSize);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyMeanFilter(currentImage, filterSize);

					}
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();

				}
			}
			
		});
		
		
		medianFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					// TODO deðerleri doðru girip girmediðini kontrol et
					Integer filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					
					long tStart = System.currentTimeMillis();

					if(workOnOriginalRadioButton.isSelected()) {
					
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyMedianFilter(originalImage, filterSize);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyMedianFilter(currentImage, filterSize);

					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		gaussianFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					int sigma = Integer.parseInt(sigmaTextField.getText());
					
					
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyGaussianFilter(originalImage, filterSize, sigma);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyGaussianFilter(currentImage, filterSize, sigma);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		unsharpingFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					int sigma = Integer.parseInt(sigmaTextField.getText());
					
					
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyUnsharpMasking(originalImage, filterSize, sigma);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyUnsharpMasking(currentImage, filterSize, sigma);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		sobelVerticalFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					Double coeff = Double.parseDouble(sobelCoefficientTextField.getText());


					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applySobelVerticalFilter(originalImage, filterSize, coeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applySobelVerticalFilter(currentImage, filterSize, coeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		sobelHorizontalFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					Double coeff = Double.parseDouble(sobelCoefficientTextField.getText());
					
					long tStart = System.currentTimeMillis();
					
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applySobelHorizontalFilter(originalImage, filterSize, coeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applySobelHorizontalFilter(currentImage, filterSize, coeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		sobelGradientMagnitudeFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					Double coeff = Double.parseDouble(sobelCoefficientTextField.getText());
					
					
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applySobelGradientMagnitude(originalImage, filterSize, coeff);
					}
					else if(workOnCurrentRadioButton.isSelected()){
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applySobelGradientMagnitude(currentImage, filterSize, coeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		sobelDirectionFilterButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int filterSize = Integer.parseInt(filterSizeTextField.getText());
					
					Double coeff = Double.parseDouble(sobelCoefficientTextField.getText());
					
					
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applySobelDirection(originalImage, filterSize, coeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applySobelDirection(currentImage, filterSize, coeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		inversionOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.inversion(originalImage);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.inversion(currentImage);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		additionOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					double operCoeff = Double.parseDouble(pointOperationsCoefficientTextField.getText());
							
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.addition(originalImage, operCoeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.addition(currentImage, operCoeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		subtractionOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					double operCoeff = Double.parseDouble(pointOperationsCoefficientTextField.getText());
							
					long tStart = System.currentTimeMillis();

					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.subtraction(originalImage, operCoeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.subtraction(currentImage, operCoeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		powerOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {

					double operCoeff = Double.parseDouble(pointOperationsCoefficientTextField.getText());
					

					long tStart = System.currentTimeMillis();
						 
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.power(originalImage, operCoeff);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.power(currentImage, operCoeff);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		
		additionWithImageOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(image2 == null) {
					String warning = "Please select the other Image!";
					writeWarning(warning);
				}
				else {
	
					long tStart = System.currentTimeMillis();
					
							
					 
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.addition(originalImage, image2);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.addition(currentImage, image2);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		subtractionWithImageOperationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(image2 == null) {
					String warning = "Please select the other Image!";
					writeWarning(warning);
				}
				else {
	
					long tStart = System.currentTimeMillis();
					
					
					 
					
					if(workOnOriginalRadioButton.isSelected()) {
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.subtraction(originalImage, image2);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.subtraction(currentImage, image2);
					}
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		

		dilationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
						
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					
	
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					
					long tStart = System.currentTimeMillis();
					
					
					if(workOnOriginalRadioButton.isSelected()) {									
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.dilation(originalImage, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.dilation(currentImage, filter);
					}
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);

					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		

		erosionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					
					long tStart = System.currentTimeMillis();
					
					
					 
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.erosion(originalImage, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.erosion(currentImage, filter);
						
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
	
		gradientButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					
					long tStart = System.currentTimeMillis();
					
					
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.morphologicalGradient(originalImage, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.morphologicalGradient(currentImage, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result); 
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		

		openingButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					long tStart = System.currentTimeMillis();
					
					
					 
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.opening(originalImage, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.opening(currentImage, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		
	
		closingButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					 
					long tStart = System.currentTimeMillis();
					
					
					
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.closing(originalImage, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.closing(currentImage, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
	
		geodesicDilationButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(mask == null) {
					String warning = "Please select an mask!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					 
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					long tStart = System.currentTimeMillis();
	
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.geodesicDilation(originalImage, mask, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.geodesicDilation(currentImage, mask, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		

		geodesicErosionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(mask == null) {
					String warning = "Please select an mask!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					 
					long tStart = System.currentTimeMillis();
					
					
					if(workOnOriginalRadioButton.isSelected()) {									
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.geodesicErosion(originalImage, mask, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.geodesicErosion(currentImage, mask, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		dilationByReconstructionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(mask == null) {
					String warning = "Please select an mask!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					

					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					
					long tStart = System.currentTimeMillis();

					
					if(workOnOriginalRadioButton.isSelected()) {									

						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.dilationByReconstruction(originalImage, mask, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.dilationByReconstruction(currentImage, mask, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		


		erosionByReconstructionButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else if(mask == null) {
					String warning = "Please select an mask!";
					writeWarning(warning);
				}
				else {
					
					// TODO deðerleri doðru girip girmediðini kontrol et
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					double [][] filter = ApplyFilter.squareFilter(N);
					if(squareFilterRadioButton.isSelected()) {
						//nothing
					}
					else if(diskFilterRadioButton.isSelected()) {
						filter = ApplyFilter.diskFilter(N);
					}
					else if(circleFilterRadioButton.isSelected()) {
						//TODO implement ettikten sonra ekle
					}
					else if(crossFilterRadioButton.isSelected()) {
						filter = ApplyFilter.crossFilter(N);
					}
					
					long tStart = System.currentTimeMillis();

					if(workOnOriginalRadioButton.isSelected()) {	
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.erosionByReconstruction(originalImage, mask, filter);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.erosionByReconstruction(currentImage, mask, filter);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		

		lowPassButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					long tStart = System.currentTimeMillis();
					
					
					if(workOnOriginalRadioButton.isSelected()) {	
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyLowPassFilter(originalImage, N);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyLowPassFilter(currentImage, N);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		
		highPassButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					long tStart = System.currentTimeMillis();
					
					String result = "...";
					writeResult(result);
					
					if(workOnOriginalRadioButton.isSelected()) {	
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(originalImage, N, N);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(currentImage, N, N);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;
					
					
					result = elapsedSeconds + " seconds";
					writeResult(result);
					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
				}
			}
			
		});
		
		
		//TODO implement edilmedi 
		bandRejectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
				
				String warning = "This was not implemented!";
				writeWarning(warning);
				
				/*
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);				
				}
				else {
					
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					long tStart = System.currentTimeMillis();

					if(workOnOriginalRadioButton.isSelected()) {	
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(originalImage, N, N);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(currentImage, N, N);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;

					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();
					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
				}
				*/
				
			}
			
		});
		
		
		//TODO implement edilmedi 
		bandPassButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {
			
				
				String warning = "This was not implemented!";
				writeWarning(warning);
				/*
				if(originalImage == null) {
					String warning = "Please select an image!";
					writeWarning(warning);
				}
				else {
					
					int N = Integer.parseInt(filterSizeTextField.getText());
					
					long tStart = System.currentTimeMillis();
					
					if(workOnOriginalRadioButton.isSelected()) {	
						
						previousImage = originalImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(originalImage, N, N);
					}
					else if(workOnCurrentRadioButton.isSelected()) {
						
						previousImage = currentImage.newInstance(true);
						currentImage = applyFilter.applyHighPassFilter(currentImage, N, N);
					}
					
					long tEnd = System.currentTimeMillis();
					long tDelta = tEnd - tStart;
					double elapsedSeconds = tDelta / 1000.0;

					
					vpt.algorithms.io.Save.invoke(currentImage, "current.png");
					vpt.algorithms.io.Save.invoke(previousImage, "previous.png");
					
					showImage();

					
					String result = elapsedSeconds + " seconds";
					writeResult(result);
					
				}
				*/
			}
			
		});

	}

	private ImageView createImageView(final File imageFile, double width, double height) {

		ImageView imageView = null;
		try {
			Image image = new Image(new FileInputStream(imageFile), width, height, true, true);
			
			imageView = new ImageView(image);
			imageView.setFitWidth(width);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {

					if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

						if (mouseEvent.getClickCount() == 2) {
							try {
								BorderPane borderPane = new BorderPane();
								ImageView imageView = new ImageView();
								Image image = new Image(new FileInputStream(imageFile));
								imageView.setImage(image);
								imageView.setStyle("-fx-background-color: BLACK");
								//imageView.setFitHeight(stage.getHeight() - 10);
								imageView.setPreserveRatio(true);
								imageView.setSmooth(true);
								imageView.setCache(true);
								borderPane.setCenter(imageView);
								borderPane.setStyle("-fx-background-color: BLACK");
								Stage newStage = new Stage();
								newStage.setWidth(stage.getWidth());
								newStage.setHeight(stage.getHeight());
								newStage.setTitle(imageFile.getName());
								Scene scene = new Scene(borderPane, Color.BLACK);
								newStage.setScene(scene);
								newStage.show();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

						}
					}
				}
			});
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return imageView;
	}
	
	
	private void showImage() {
		
		double sizeX = 420;
		double sizeY = 600;
		
		//sizeX = 420; sizeY = 600
		
		if(currentImage.getXDim()< sizeX && currentImage.getYDim() < sizeY) {
			sizeX = currentImage.getXDim();
			sizeY = currentImage.getYDim();
		}
		else{

			double x = 420.0/currentImage.getXDim();
			
			if(currentImage.getYDim()*x < 600) {
				sizeX = 420;
				sizeY = currentImage.getYDim()*x;
			}
			else {
				x = 600.0/currentImage.getYDim();
				
				sizeX = x*currentImage.getXDim();
				sizeY = 600;
			}

		}
		
		currentImagePane.getChildren().clear();
		previousImagePane.getChildren().clear();
		
		final ImageView currentImageWiev;
		

		currentImageWiev= createImageView(new File("current.png"), sizeX, sizeY);
		
		GridPane.setConstraints(currentImageWiev, 0, 0);
		currentImagePane.getChildren().add(currentImageWiev);
	
		final ImageView previousImageWiev;
		
		previousImageWiev= createImageView(new File("previous.png"), sizeX, sizeY);
		
		GridPane.setConstraints(previousImageWiev, 0, 0);
		previousImagePane.getChildren().add(previousImageWiev);
		
		
	}
	
	private ImageView createImageView(final File imageFile, double width) {

		ImageView imageView = null;
		try {
			Image image = new Image(new FileInputStream(imageFile), width, 0, true, true);

			imageView = new ImageView(image);
			imageView.setFitWidth(width);
			imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent mouseEvent) {

					if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {

						if (mouseEvent.getClickCount() == 2) {
							try {
								BorderPane borderPane = new BorderPane();
								ImageView imageView = new ImageView();
								Image image = new Image(new FileInputStream(imageFile));
								imageView.setImage(image);
								imageView.setStyle("-fx-background-color: BLACK");
								// imageView.setFitHeight(stage.getHeight() - 10);
								imageView.setPreserveRatio(true);
								imageView.setSmooth(true);
								imageView.setCache(true);
								borderPane.setCenter(imageView);
								borderPane.setStyle("-fx-background-color: BLACK");
								Stage newStage = new Stage();
								newStage.setWidth(stage.getWidth());
								newStage.setHeight(stage.getHeight());
								newStage.setTitle(imageFile.getName());
								Scene scene = new Scene(borderPane, Color.BLACK);
								newStage.setScene(scene);
								newStage.show();
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}

						}
					}
				}
			});
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		return imageView;
	}
	
	
	private void writeResult(String result) {
		
		resultText.setText("	Result : " + result);
		String textStyle = "-fx-font: 22px Tahoma; -fx-fill: #229954; -fx-stroke-width: 2;";
		resultText.setStyle(textStyle);
	}
	
	private void writeWarning(String warning) {
		
		resultText.setText("	Warning : " + warning);
		String textStyle = "-fx-font: 22px Tahoma; -fx-fill: #CB4335; -fx-stroke-width: 2;";
		resultText.setStyle(textStyle);
	}
	
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
	
	
}