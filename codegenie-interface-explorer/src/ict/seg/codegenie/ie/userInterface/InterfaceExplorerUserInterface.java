/**
 * @author Rafael B. Januzi
 * @date 23/09/2011
 */

package ict.seg.codegenie.ie.userInterface;


import java.util.ArrayList;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import ict.seg.codegenie.ie.search.IESearch;
import ict.seg.codegenie.ie.IEUtil;


public class InterfaceExplorerUserInterface{

	Shell shell;
	TabFolder tabFolder1;
	GridLayout shellLayout;
	GridLayout group1Layout;
	GridLayout group2Layout;
	GridLayout group3Layout;
	Group group1;
	Group group2;
	Group group3;
	boolean selectAll = false;
	boolean considerClass = true;
	boolean considerReturn = true;
	boolean considerName = true;
	boolean considerParameters = true;
	
	//Related methods area
	private int numberOfItensInTable = 0;
	
	
	private void addItemInTable(Table table, String itemText){
		
		TableItem tableItem = new TableItem(table, SWT.SIMPLE, numberOfItensInTable++);
		
		tableItem.setText(itemText);

		
	}
	
	private void addItensInTable(Table table, ArrayList<String> itensText){
		
		for(int i = 0; i < itensText.size(); i++){
		
			TableItem tableItem = new TableItem(table, SWT.SIMPLE, numberOfItensInTable++);
			
			tableItem.setText(itensText.get(i));
		}
		
	}

	

	
public void openIE(final String nameOfClass, String returnType, String nameOfMethod, String parameterTypes, final IJavaProject jp, final ISelection currentSelection){
		
		//Initiate
		shell = new Shell(SWT.CLOSE);
		tabFolder1 = new TabFolder(shell, SWT.TOP);
		shellLayout = new GridLayout();
		group1Layout = new GridLayout();
		group2Layout = new GridLayout();
		group3Layout = new GridLayout();
		group1 = new Group(tabFolder1, SWT.SHADOW_IN);
		group2 = new Group(tabFolder1, SWT.SHADOW_IN);
		group3 = new Group(tabFolder1, SWT.SHADOW_IN);
		IEUtil.defineCompatibility();
		
		final Text classNameField = new Text(group1, SWT.SEARCH | SWT.CENTER);
		final Text returnTypeField = new Text(group1, SWT.SEARCH | SWT.CENTER);
		final Text nameOfMethodField = new Text(group1, SWT.SEARCH | SWT.CENTER);
		final Label leftParentesis = new Label(group1, SWT.CENTER);
		final Text parametersTypeField = new Text(group1, SWT.SEARCH | SWT.CENTER);
		final Label rightParentesis = new Label(group1, SWT.CENTER);
		final Label semicolon = new Label(group1, SWT.CENTER);
		final Button goButton = new Button(group1, SWT.PUSH);
		final Label interfaceExample = new Label(group1, SWT.CENTER);
		
		final Button selectAllButton = new Button(group2, SWT.CHECK);
		final Table resultsTable = new Table(group2, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		
		final Label scaleLabel = new Label(group3, SWT.SIMPLE);
		final Scale compatibilityScale = new Scale(group3, SWT.VERTICAL);
		final Button ignoreOrderButton = new Button(group3, SWT.CHECK);
		final Button considerClassButton = new Button(group3, SWT.CHECK);
		final Button considerReturnButton = new Button(group3, SWT.CHECK);
		final Button considerNameButton = new Button(group3, SWT.CHECK);
		final Button considerParametersButton = new Button(group3, SWT.CHECK);
		final Button searchButton = new Button(group3, SWT.PUSH);

		
		
		//Adjust of Layouts
		//Shell layout
		shellLayout.numColumns = 1;
		shell.setLayout(shellLayout);
		shell.setSize(600, 500);
		shell.setText("Interface Explorer");
		shell.setLocation(500, 10);
		
		//TabFolder layout
		tabFolder1.setLocation(20, 10);
		tabFolder1.setSize(600, 600);
		
		
		//Group1 layout
		group1Layout.numColumns = 7;
		group1.setText("Looked method");
		group1.setLayout(group1Layout);
		group1.setLocation(10, 10);
		group1.setSize(600, 70);
		
		//Group2 layout
		group2Layout.numColumns = 1;
		group2.setText("Related methods");
		group2.setLayout(group2Layout);
		group2.setLocation(10, 80);
		group2.setSize(340, 560);
		
		//Group3 layout
		group3Layout.numColumns = 3;
		group3.setText("Search Options");
		group3.setLayout(group3Layout);
		group3.setLocation(360, 80);
		group3.setSize(250, 560);

		
		
		//Add components to shell
		
		//Class name field
		classNameField.setText(nameOfClass);
		classNameField.setLocation(20, 40);
		classNameField.setSize(50, 20);
		
		//Return type field
		returnTypeField.setText(returnType);
		returnTypeField.setLocation(90, 40);
		returnTypeField.setSize(80, 20);
		
		//Name of Method
		nameOfMethodField.setLocation(190, 40);
		nameOfMethodField.setSize(100, 20);
		nameOfMethodField.setText(nameOfMethod);
		
		//Left parentesis
		leftParentesis.setLocation(300, 40);
		leftParentesis.setText("(");
		leftParentesis.setSize(10,15);
		
		//Parameters types
		parametersTypeField.setText(parameterTypes);
		parametersTypeField.setLocation(310, 40);
		parametersTypeField.setSize(200, 20);
		
		//Right parentesis
		rightParentesis.setLocation(510, 40);
		rightParentesis.setText(")");
		rightParentesis.setSize(10,15);
		
		//Semicolon
		semicolon.setLocation(520, 40);
		semicolon.setText(";");
		semicolon.setSize(10, 15);
		
		//Interface example
		interfaceExample.setLocation(10, 20);
		interfaceExample.setText("    Class               returntype               nameOfMethod                       (parameters types);");
		interfaceExample.setSize(450, 15);
		
		//Go button
		goButton.setLocation(540, 30);
		goButton.setText("Go!");
		goButton.setSize(40, 30);
		
		SelectionAdapter adapterGo = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				String[] parameterTypesNames = parametersTypeField.getText().split(",");
				
				resultsTable.removeAll();
				numberOfItensInTable = 0;
				
//				ArrayList<String> itensForTable = IEUtil.assembleItemsTable(returnTypeField.getText(), nameOfMethodField.getText(), parameterTypesNames, 
//				(compatibilityScale.getSelection()/100.0));
				ArrayList<String> itensForTable = IEUtil.assembleItemsTableInBlocks(returnTypeField.getText(), nameOfMethodField.getText(), parameterTypesNames, 
						(compatibilityScale.getSelection()/100.0));
				
				for(int i = 0; i < itensForTable.size(); i++){
					addItemInTable(resultsTable, itensForTable.get(i));
				}
				
			}
		};
		goButton.addSelectionListener(adapterGo);

		
		//select/Deselect all button
		selectAllButton.setLocation(10, 20);
		selectAllButton.setText("Select/Deselect all");
		selectAllButton.setSize(120, 20);
		
		SelectionAdapter adapterSelectAll = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				TableItem[] items = resultsTable.getItems();
				
				if(selectAll == false){
					
					for(int i = 0; i < items.length; i++)
						items[i].setChecked(true);
					
				}
				
				else{
					for(int i = 0; i < items.length; i++)
						items[i].setChecked(false);
				}
				
				selectAll = !selectAll;
				
			}
		};
		
		selectAllButton.addSelectionListener(adapterSelectAll);
		
		
		//Ignore order button
		ignoreOrderButton.setSelection(true);
		ignoreOrderButton.setLocation(100, 40);
		ignoreOrderButton.setText("Ignore order");
		ignoreOrderButton.setSize(120, 20);
		
		SelectionAdapter adapterIgnoreOrder = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				IEUtil.ignoreOrder = !IEUtil.ignoreOrder;
			}
		};
		
		ignoreOrderButton.addSelectionListener(adapterIgnoreOrder);
		
		
		//Consider class button
		considerClassButton.setSelection(true);
		considerClassButton.setLocation(100, 80);
		considerClassButton.setText("Consider class");
		considerClassButton.setSize(120, 20);
		
		SelectionAdapter adapterConsiderClass = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				if(considerClass)
					if(!(considerReturn || considerParameters || considerName))
						considerClassButton.setSelection(true);
					else
						considerClass = !considerClass;
				else
					considerClass = !considerClass;
				
//				System.out.println("considerClass: " + considerClass);
			}
		};
		
		considerClassButton.addSelectionListener(adapterConsiderClass);
		
		
		//Consider return button
		considerReturnButton.setSelection(true);
		considerReturnButton.setLocation(100, 120);
		considerReturnButton.setText("Consider return");
		considerReturnButton.setSize(120, 20);
		
		SelectionAdapter adapterConsiderReturn = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				if(considerReturn)
					if(!(considerClass || considerParameters || considerName))
						considerReturnButton.setSelection(true);
					else
						considerReturn = !considerReturn;
				else
					considerReturn = !considerReturn;
			}
		};
		
		considerReturnButton.addSelectionListener(adapterConsiderReturn);
		
		
		//Consider name button
		considerNameButton.setSelection(true);
		considerNameButton.setLocation(100, 160);
		considerNameButton.setText("Consider name");
		considerNameButton.setSize(120, 20);
		
		SelectionAdapter adapterConsiderName = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				if(considerName)
					if(!(considerClass || considerParameters || considerReturn))
						considerNameButton.setSelection(true);
					else
						considerName = !considerName;
				else
					considerName = !considerName;
			}
		};
		
		considerNameButton.addSelectionListener(adapterConsiderName);
		
		
		//Consider parameters button
		considerParametersButton.setSelection(true);
		considerParametersButton.setLocation(100, 200);
		considerParametersButton.setText("Consider parameters");
		considerParametersButton.setSize(130, 20);
		
		SelectionAdapter adapterConsiderParameters = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
				if(considerParameters)
					if(!(considerClass || considerName || considerReturn))
						considerParametersButton.setSelection(true);
					else
						considerParameters = !considerParameters;
				else
					considerParameters = !considerParameters;
			}
		};
		
		considerParametersButton.addSelectionListener(adapterConsiderParameters);
		
		//SearchButton
		searchButton.setLocation(130, 300);
		searchButton.setSize(50, 30);
		searchButton.setText("Search");
		
		SelectionAdapter adapterSearchButton = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			
			ArrayList<String[]> selectedQueryItens = new ArrayList<String[]>();
			TableItem[] itensOfTable = resultsTable.getItems();
			String[] finalQuery = new String[5];
			boolean[] consideringPartsOfInterface = new boolean[4];
			
			
			for(int i = 0; i < itensOfTable.length; i++)
				if(itensOfTable[i].getChecked() == true)
					selectedQueryItens.add(IEUtil.transformInterfaceOfItemInQueryItem(classNameField.getText(), IEUtil.transformNameOfItemInInterface(itensOfTable[i].toString())));
			
			finalQuery = IEUtil.getFinalQuery(selectedQueryItens, currentSelection);
			consideringPartsOfInterface[0] = considerClass;
			consideringPartsOfInterface[1] = considerReturn;
			consideringPartsOfInterface[2] = considerName;
			consideringPartsOfInterface[3] = considerParameters;
			
			
			if(finalQuery != null)
				IESearch.runSearch(finalQuery, jp, consideringPartsOfInterface, currentSelection);
			
		}
	};
	
		searchButton.addSelectionListener(adapterSearchButton);
		
		
		//Related methods area
		resultsTable.setLocation(10,40);
		resultsTable.setSize(320,500);
		resultsTable.setLinesVisible(true);
		resultsTable.setHeaderVisible(true);

		
		//Scale
		scaleLabel.setLocation(20, 20);
		scaleLabel.setSize(10, 500);
		scaleLabel.setText("    0%\t\t\t\t\t\t\t\t\t               50%\t\t\t\t\t\t\t\t\t\t  100%");
		compatibilityScale.setLocation(30, 30);
		compatibilityScale.setSize(40, 450);
		compatibilityScale.setMaximum(100);
		compatibilityScale.setMinimum(0);
		compatibilityScale.setPageIncrement(25);
		compatibilityScale.setSelection(50);
		
		
		//Open shell
		shell.pack();
		shell.open();
		
	}
}
	

