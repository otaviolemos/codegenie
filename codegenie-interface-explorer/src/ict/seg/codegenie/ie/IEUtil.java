/**
 * @author Rafael B. Januzi
 * @date 11/04/2012
 */


package ict.seg.codegenie.ie;

import ict.seg.codegenie.ie.permutationGenerator.PermutationGenerator;
import ict.seg.codegenie.ie.typeCompatibility.CompatibilityLevel;
import ict.seg.codegenie.ie.typeCompatibility.Type;
import java.util.ArrayList;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;

public class IEUtil {
	
	//Types & Compatibility
	//Types criados
	
	//Tipo primitivos
	public static Type typeByteI = new Type("byte");
	public static Type typeShortI = new Type("short");
	public static Type typeIntI = new Type("int");
	public static Type typeLongI = new Type("long");
	public static Type typeFloatI = new Type("float");
	public static Type typeDoubleI = new Type("double");
	public static Type typeChar = new Type("char");
	
	//Classes dos tipos primitivos
	public static Type typeByteII = new Type("Byte");
	public static Type typeShortII = new Type("Short");
	public static Type typeIntII = new Type("Integer");
	public static Type typeLongII = new Type("Long");
	public static Type typeFloatII = new Type("Float");
	public static Type typeDoubleII = new Type("Double");
	
	//Object
	public static Type typeObject = new Type("Object");
	
	//String
	public static Type typeString = new Type("String");
	public static Type typeStringArray = new Type("String[]");
	
	//void
	public static Type typeVoid = new Type("void");
	
	//Vetores de tipos primitivos
	public static Type typeByteIArray = new Type("byte[]");
	public static Type typeShortIArray = new Type("short[]");
	public static Type typeIntIArray = new Type("int[]");
	public static Type typeLongIArray = new Type("long[]");
	public static Type typeFloatIArray = new Type("float[]");
	public static Type typeDoubleIArray = new Type("double[]");
	public static Type typeCharArray = new Type("char[]");
	
	//Vetores de classes dos tipos primitivos
	public static Type typeByteIIArray = new Type("Byte[]");
	public static Type typeShortIIArray = new Type("Short[]");
	public static Type typeIntIIArray = new Type("Integer[]");
	public static Type typeLongIIArray = new Type("Long[]");
	public static Type typeFloatIIArray = new Type("Float[]");
	public static Type typeDoubleIIArray = new Type("Double[]");
	
	//Matrizes de tipos primitivos
	public static Type typeByteIMatrix= new Type("byte[][]");
	public static Type typeShortIMatrix= new Type("short[][]");
	public static Type typeIntIMatrix = new Type("int[][]");
	public static Type typeLongIMatrix = new Type("long[][]");
	public static Type typeFloatIMatrix = new Type("float[][]");
	public static Type typeDoubleIMatrix = new Type("double[][]");
	public static Type typeCharMatrix = new Type("char[][]");
	
	//Matrizes de classes dos tipos primitivos
	public static Type typeByteIIMatrix = new Type("Byte[][]");
	public static Type typeShortIIMatrix = new Type("Short[][]");
	public static Type typeIntIIMatrix = new Type("Integer[][]");
	public static Type typeLongIIMatrix = new Type("Long[][]");
	public static Type typeFloatIIMatrix = new Type("Float[][]");
	public static Type typeDoubleIIMatrix = new Type("Double[][]");
	
	//Object[]
	public static Type typeObjectArray = new Type("Object[]");
	
	//Collections
	public static Type typeArrayList = new Type("ArrayList");
	public static Type typeLinkedList = new Type("LinkedList");
	public static Type typeVector = new Type("Vector");
	
	
	public static CompatibilityLevel compatBaixa = new CompatibilityLevel("Baixa");
	public static CompatibilityLevel compatMedia = new CompatibilityLevel("Media");
	public static CompatibilityLevel compatAlta = new CompatibilityLevel("Alta");
	public static CompatibilityLevel compatMaxima = new CompatibilityLevel("Maxima");
	
	public static boolean ignoreOrder = false;
	
	public static String removeBrackets(String string){
		
		String aux = string;
		
		while(aux.indexOf("[]") > 0){
			aux = (aux.substring(0, aux.indexOf("[]")) + 
					aux.substring(aux.indexOf("[]")+2));
		}
		
		return aux;
	}
	
	public static int countBrackets(String string){
		
		String aux = string;
		int count = 0;
		
		while(aux.indexOf("[]") > 0){
			aux = (aux.substring(0, aux.indexOf("[]")) + 
					aux.substring(aux.indexOf("[]")+2));
			count++;
		}
		
		
		return count;
	}
	
	//Transform TableItem {nameOfItem} in nameOfItem
	public static String transformNameOfItemInInterface(String item){
		return item.substring(11, item.length()-1);
	}
	
	public static String[] transformInterfaceOfItemInQueryItem(String nameOfClass, String nameOfIten){
		
		/** int soma (int,int) **/
		
		/**
		 * query[]:
		 * 
		 * wantedMethodInterface[0] = packageName
		 * wantedMethodInterface[1] = className
		 * wantedMethodInterface[2] = methodName
		 * wantedMethodInterface[3] = returnType
		 * wantedMethodInterface[4] = parametersTypes : "(type1,type2"
		 */
		
		String[] returnStr = new String[5];
		String[] spaceSplit = nameOfIten.split(" ");
		
		//get return type
		String returnType = spaceSplit[0];
		
		//get name of method
		String nameOfMethod = spaceSplit[1];
		
		//get parameter types
		String parameterTypes = spaceSplit[2].substring(0, spaceSplit[2].length()-1);
		
		returnStr[0] = "";
		returnStr[1] = nameOfClass;
		returnStr[2] = nameOfMethod;
		returnStr[3] = returnType;
		returnStr[4] = parameterTypes;
		
		return returnStr;
	}

	public static ArrayList<String> cartesianProduct(ArrayList<String> a, ArrayList<String> b, String  separator, String putsAtTheEnd) {
		
		ArrayList<String> aXb = new ArrayList<String>();
		
		for (int i = 0; i < a.size(); i++) {
			for (int j = 0; j < b.size(); j++) {
				aXb.add(a.get(i) + separator + b.get(j) + putsAtTheEnd);
			}
		}
		
		return aXb;
	}
	
	//Here is the combination of possibilities
	public static ArrayList<String> assembleItemsTable(String returnTypeName, String methodName, String[] parameterTypesNames, double minimumScore){
			
		//Compatives Interfaces
		ArrayList<String> relatedInterface = new ArrayList<String>();
		
		//Getting return types and parameter types
		Type returnType = Type.getTypePerName(returnTypeName);
		Type[] parameterTypes = new Type[parameterTypesNames.length];
		ArrayList<ArrayList<Type>> compativeTypes = new ArrayList<ArrayList<Type>>();
		
		//No parameters
		if(parameterTypesNames[0].equals("")){
			
			//Getting compative types
			compativeTypes.add(returnType.getLimitedCompativeTypes(minimumScore));
			
			for (int i = 0; i < compativeTypes.get(0).size(); i++) {
				relatedInterface.add(compativeTypes.get(0).get(i).getName() + " " + methodName + " ();");
			}
		}
		
		//Yes parameters
		else{
			
			ArrayList<String> listAux = new ArrayList<String>();
			
			//Getting compative types
			compativeTypes.add(returnType.getLimitedCompativeTypes(minimumScore));
			
			for(int i = 0; i < parameterTypesNames.length; i++)
				parameterTypes[i] = Type.getTypePerName(parameterTypesNames[i]);
			
			for(int i = 0; i < parameterTypes.length; i++)
				compativeTypes.add(parameterTypes[i].getLimitedCompativeTypes(minimumScore));
			
			for (int i = 0; i < compativeTypes.get(0).size(); i++) 
				relatedInterface.add(compativeTypes.get(0).get(i).getName() + " " + methodName + " (");
			
			//Combine
			for(int i = 1; i < compativeTypes.size(); i++){
				
				listAux.clear();
				
				for(int j = 0; j < compativeTypes.get(i).size(); j++)
					listAux.add(compativeTypes.get(i).get(j).getName());
				
				if(i == (compativeTypes.size() - 1) && i == 1)
					relatedInterface = IEUtil.cartesianProduct(relatedInterface, listAux, "", ")");
				else
					if(i == (compativeTypes.size() - 1) && i != 1)
						relatedInterface = IEUtil.cartesianProduct(relatedInterface, listAux, ",", ")");
					else
						if(i == 1)
							relatedInterface = IEUtil.cartesianProduct(relatedInterface, listAux, "", "");
						else
							relatedInterface = IEUtil.cartesianProduct(relatedInterface, listAux, ",", "");
			}
		}
			
		return relatedInterface;
		
	}
	
	public static ArrayList<String> assembleItemsTableInBlocks(String returnTypeName, String methodName, String[] parameterTypesNames, double minimumScore){
		
		ArrayList<String> items = new ArrayList<String>();
		ArrayList<Type> typesInInterface = new ArrayList<Type>();
		String initialInterface;
		
		//Build the searched interface
		String parametersAux = new String(" (");
		for(int i = 0; i < parameterTypesNames.length; i++){
			if(i < (parameterTypesNames.length - 1))
				parametersAux += (parameterTypesNames[i] + ",");
			else
				parametersAux += parameterTypesNames[i];
		}
		parametersAux += ")";
		items.add(new String(returnTypeName + " " + methodName + parametersAux));
		initialInterface = items.get(0);
		
		//Getting all diferent types in searched interface
		typesInInterface.add(Type.getTypePerName(returnTypeName));
		for(int i = 0; i < parameterTypesNames.length; i++){
			
			Type typeAux = Type.getTypePerName(parameterTypesNames[i]);
			
			if(!typesInInterface.contains(typeAux))
				typesInInterface.add(typeAux);
		}
		
		
		
		//For each type in interface
		for(int i = 0; i < typesInInterface.size(); i++){
			
			ArrayList<Type> compativeTypesToActualType = typesInInterface.get(i).getLimitedCompativeTypes(minimumScore);
			
			//For each compative type to a actual type of interface
			for(int j = 0; j < compativeTypesToActualType.size(); j++){
					String antiqueTerm = typesInInterface.get(i).getName();
					String newTerm = compativeTypesToActualType.get(j).getName();
					
					if(countBrackets(antiqueTerm) > 0 && countBrackets(newTerm) > 0)// if the antiqueTerm and the newTerm have brackets
						items.add(initialInterface.replaceAll(removeBrackets(antiqueTerm), removeBrackets(newTerm)));
					else
						if(countBrackets(antiqueTerm) > 0)//if just the antiqueTerm have brackets
							items.add(removeBrackets(initialInterface.replaceAll(removeBrackets(antiqueTerm), newTerm)));
						else//if just the newTerm have brackets
							items.add(initialInterface.replaceAll(antiqueTerm, newTerm));
			}
		}

		//Removing the equal items
		for(int i = 0; i < items.size(); i++)
			for(int j = (i+1); j < items.size(); j++)
				if(items.get(i).equals(items.get(j))){
					items.remove(j);
					j--;
				}
		
		return items;
		
	}
	
	public static String getIgnoredOrderParameters(String parameters){
		
		//Parameters: "(parameter01,parameter02,...,parameterN"
		
		String parametersPermutationsReturn = new String();
		String[] parametersPermutations = PermutationGenerator.getParametersPermutations(parameters.substring(1).split(","));
		//parametersPermutations: {"par1,par2,par3", "par1,par3,par2", "par3,par2,par1", ...}
		
		parametersPermutationsReturn += " " + parametersPermutations[0];
		
		for(int i = 1; i < parametersPermutations.length; i++)
			parametersPermutationsReturn += " OR " + parametersPermutations[i];
		
		//Removing the space from the begining of string
		return parametersPermutationsReturn.substring(1);
	}

	public static String[] getFinalQuery(ArrayList<String[]> selectedQueryItens, ISelection currentSelection){
		
		String[] finalQuery = new String[5];
		
		//Getting the name of the package
		IType selection = (IType) ((TreeSelection)currentSelection).getFirstElement();
		String wantedPackageName = new String();
		
		try {
			wantedPackageName = selection.getCompilationUnit().getPackageDeclarations()[0].getElementName();
				} catch (Exception e) {
					e.printStackTrace();
		}

		if(selectedQueryItens.size() <= 0){
			System.err.println("InterfaceExplorerUserInterface.getFinalQuery: selectedQueryItens is empty");
			return null;
		}	
		
		
		if(!ignoreOrder){ 	
			
			//finalQuery[0] = selectedQueryItens.get(0)[0];
			finalQuery[0] = wantedPackageName;
			finalQuery[1] = selectedQueryItens.get(0)[1];
			finalQuery[2] = selectedQueryItens.get(0)[2];
			finalQuery[3] = selectedQueryItens.get(0)[3];
			finalQuery[4] = selectedQueryItens.get(0)[4];
			
			for(int i = 1; i < selectedQueryItens.size(); i++){
				finalQuery[3] += " OR " + selectedQueryItens.get(i)[3]; //(TipoDeRetorno1 OR TipoDeRetorno2 OR TipoDeRetornoN
				finalQuery[4] += " OR " + selectedQueryItens.get(i)[4].substring(1); //(conjuntoDeTiposParâmetros1 OR conjuntoDeTiposParâmetros2 OR conjuntoDeTiposParâmetrosN
			}
		}
		else{
			
			/**
			 * query[]:
			 * 
			 * wantedMethodInterface[0] = packageName
			 * wantedMethodInterface[1] = className
			 * wantedMethodInterface[2] = methodName
			 * wantedMethodInterface[3] = returnType
			 * wantedMethodInterface[4] = parametersTypes : "(type1,type2"
			 */
			
			//finalQuery[0] = selectedQueryItens.get(0)[0];
			finalQuery[0] = wantedPackageName;
			finalQuery[1] = selectedQueryItens.get(0)[1];
			finalQuery[2] = selectedQueryItens.get(0)[2];
			finalQuery[3] = selectedQueryItens.get(0)[3];
			finalQuery[4] = "(" + IEUtil.getIgnoredOrderParameters(selectedQueryItens.get(0)[4]);
			
			for(int i = 1; i < selectedQueryItens.size(); i++){
				finalQuery[3] += " OR " + selectedQueryItens.get(i)[3];
				finalQuery[4] += " OR " + IEUtil.getIgnoredOrderParameters(selectedQueryItens.get(i)[4]); //(conjuntoDeTiposParâmetros1 OR conjuntoDeTiposParâmetros2 OR conjuntoDeTiposParâmetrosN
			}
			
			
		}
		
		return finalQuery;
		
		
	}
	
	public static void defineCompatibility(){
	
		
	/***************************** Tipos básicos **********************************/
		
		/** For int  **/
	
	/** Compatibility int - primitive **/
	
	//Difinindo compatibilidade entre int - byte
	typeIntI.addCompatibleTypeBidirectional(typeByteI, compatAlta);
	
	//Difinindo compatibilidade entre int - short
	typeIntI.addCompatibleTypeBidirectional(typeShortI, compatAlta);
	
	//Difinindo compatibilidade entre int - int
	typeIntI.addCompatibleTypeBidirectional(typeIntI, compatMaxima);
	
	//Difinindo compatibilidade entre int - long
	typeIntI.addCompatibleTypeBidirectional(typeLongI, compatAlta);
	
	//Difinindo compatibilidade entre int - float
	typeIntI.addCompatibleTypeBidirectional(typeFloatI, compatMedia);
	
	//Difinindo compatibilidade entre int - double
	typeIntI.addCompatibleTypeBidirectional(typeDoubleI, compatMedia);
	
	/** Compatibilidade int - classes **/
	
	//Difinindo compatibilidade entre int - Integer
	typeIntI.addCompatibleTypeBidirectional(typeIntII, compatMaxima);
	
	
	
			/** Para byte  **/
	
	
	
	/** Compatibilidade byte - primitivos **/
	
	//Difinindo compatibilidade entre byte - byte
	typeByteI.addCompatibleTypeBidirectional(typeByteI, compatMaxima);
	
	//Difinindo compatibilidade entre byte - short
	typeByteI.addCompatibleTypeBidirectional(typeShortI, compatAlta);
	
	//Difinindo compatibilidade entre byte - long
	typeByteI.addCompatibleTypeBidirectional(typeLongI, compatBaixa);
	
	//Difinindo compatibilidade entre byte - float
	typeByteI.addCompatibleTypeBidirectional(typeFloatI, compatBaixa);
	
	//Difinindo compatibilidade entre byte - double
	typeByteI.addCompatibleTypeBidirectional(typeDoubleI, compatBaixa);
	
	/** Compatibilidade byte - classes **/
	
	//Difinindo compatibilidade entre byte - Byte
	typeByteI.addCompatibleTypeBidirectional(typeByteII, compatMaxima);
	
	
			/** Para short  **/
	
	
	
	/** Compatibilidade short - primitivos **/
	
	//Difinindo compatibilidade entre short - short
	typeShortI.addCompatibleTypeBidirectional(typeShortI, compatMaxima);
	
	//Difinindo compatibilidade entre short - long
	typeShortI.addCompatibleTypeBidirectional(typeLongI, compatAlta);
	
	//Difinindo compatibilidade entre short - float
	typeShortI.addCompatibleTypeBidirectional(typeFloatI, compatMedia);
	
	//Difinindo compatibilidade entre short - double
	typeShortI.addCompatibleTypeBidirectional(typeDoubleI, compatBaixa);
	
	
	/** Compatibilidade short - classes **/
	
	
	//Difinindo compatibilidade entre short - Short
	typeShortI.addCompatibleTypeBidirectional(typeShortII, compatMaxima);
	
	
	
			/** Para long  **/
	
	
	
	/** Compatibilidade long - primitivos **/
	
	//Difinindo compatibilidade entre long - long
	typeLongI.addCompatibleTypeBidirectional(typeLongI, compatMaxima);
	
	//Difinindo compatibilidade entre long - float
	typeLongI.addCompatibleTypeBidirectional(typeFloatI, compatMedia);
	
	//Difinindo compatibilidade entre long - double
	typeLongI.addCompatibleTypeBidirectional(typeDoubleI, compatMedia);
	
	/** Compatibilidade long - classes **/
	
	//Difinindo compatibilidade entre long - Long
	typeLongI.addCompatibleTypeBidirectional(typeLongII, compatMaxima);
	
	
	
			/**  Para float  **/
	
	
	
	/** Compatibilidade float - primitivos **/
	
	//Difinindo compatibilidade entre float - float
	typeFloatI.addCompatibleTypeBidirectional(typeFloatI, compatMaxima);
	
	//Difinindo compatibilidade entre float - double
	typeFloatI.addCompatibleTypeBidirectional(typeDoubleI, compatAlta);
	
	/** Compatibilidade float - classes **/
	
	//Difinindo compatibilidade entre float - Float
	typeFloatI.addCompatibleTypeBidirectional(typeFloatII, compatMaxima);
	
	
	
			/**  Para double  **/
	
	
	
	/** Compatibilidade double - primitivos **/
	
	//Difinindo compatibilidade entre double - double
	typeDoubleI.addCompatibleTypeBidirectional(typeDoubleI, compatMaxima);
	
	/** Compatibilidade double - classes **/
	
	
	//Difinindo compatibilidade entre double - Double
	typeDoubleI.addCompatibleTypeBidirectional(typeDoubleII, compatMaxima);
	
	
	
	
			/**  Para Byte  **/
	
	
	/** Compatibilidade Byte - classes **/
	
	//Difinindo compatibilidade entre Byte - Byte
	typeByteII.addCompatibleTypeBidirectional(typeByteII, compatMaxima);
	
	
	
			/**  Para Short  **/
	
	
	/** Compatibilidade Short - classes **/
	
	//Difinindo compatibilidade entre Short - Short
	typeShortII.addCompatibleTypeBidirectional(typeShortII, compatMaxima);
	
	
	
	
			/**  Para Integer  **/
	
	
	/** Compatibilidade Integer - classes **/
	
	//Difinindo compatibilidade entre Integer - Integer
	typeIntII.addCompatibleTypeBidirectional(typeIntII, compatMaxima);
	
	
	
			/**  Para Long  **/
	
	
	
	/** Compatibilidade Long - classes **/
	
	//Difinindo compatibilidade entre Long - Long
	typeLongII.addCompatibleTypeBidirectional(typeLongII, compatMaxima);
	
	
	
	
			/**  Para Float  **/
	
	
	
	/** Compatibilidade Long - classes **/
	
	//Difinindo compatibilidade entre Float - Float
	typeFloatII.addCompatibleTypeBidirectional(typeFloatII, compatMaxima);
	
	
	
			/**  Para Double  **/
	
	
	
	/** Compatibilidade Double - classes **/
	
	//Difinindo compatibilidade entre Float - Double
	typeDoubleII.addCompatibleTypeBidirectional(typeDoubleII, compatMaxima);
	
	
	
			/**  Para String  **/
	
	
	//Difinindo compatibilidade entre String - String
	typeString.addCompatibleTypeBidirectional(typeString, compatMaxima);	
	
	
			/**  Para char  **/
	
	//Difinindo compatibilidade entre char - char
	typeChar.addCompatibleTypeBidirectional(typeChar, compatMaxima);
	
	
			/**  Para void **/
	
	
	/** Compatibilidade void - void **/
	
	//Difinindo compatibilidade entre Float - Double
	typeVoid.addCompatibleTypeBidirectional(typeVoid, compatMaxima);
	
	
	
	/***************************** Vetores **********************************/
	
	
		/**  Para byte[] **/
	
	//Difinindo compatibilidade entre byte[] - byte[]
	typeByteIArray.addCompatibleTypeBidirectional(typeByteIArray, compatMaxima);
	//Difinindo compatibilidade entre byte[] - short[]
	typeByteIArray.addCompatibleTypeBidirectional(typeShortIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - int[]
	typeByteIArray.addCompatibleTypeBidirectional(typeIntIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - long[]
	typeByteIArray.addCompatibleTypeBidirectional(typeLongIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - float[]
	typeByteIArray.addCompatibleTypeBidirectional(typeFloatIArray, compatMedia);
	//Difinindo compatibilidade entre byte[] - double[]
	typeByteIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatMedia);
	
	//Difinindo compatibilidade entre byte[] - Byte[]
	typeByteIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatMaxima);
	//Difinindo compatibilidade entre byte[] - Short[]
	typeByteIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - Int[]
	typeByteIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - Long[]
	typeByteIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre byte[] - Float[]
	typeByteIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre byte[] - Double[]
	typeByteIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
	
		/**  Para short[] **/
		
	//Difinindo compatibilidade entre short[] - short[]
	typeShortIArray.addCompatibleTypeBidirectional(typeShortIArray, compatMaxima);
	//Difinindo compatibilidade entre short[] - int[]
	typeShortIArray.addCompatibleTypeBidirectional(typeIntIArray, compatAlta);
	//Difinindo compatibilidade entre short[] - long[]
	typeShortIArray.addCompatibleTypeBidirectional(typeLongIArray, compatAlta);
	//Difinindo compatibilidade entre short[] - float[]
	typeShortIArray.addCompatibleTypeBidirectional(typeFloatIArray, compatMedia);
	//Difinindo compatibilidade entre short[] - double[]
	typeShortIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatMedia);
	
	//Difinindo compatibilidade entre short[] - Byte[]
	typeShortIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatAlta);
	//Difinindo compatibilidade entre short[] - Short[]
	typeShortIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatMaxima);
	//Difinindo compatibilidade entre short[] - Int[]
	typeShortIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatAlta);
	//Difinindo compatibilidade entre short[] - Long[]
	typeShortIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre short[] - Float[]
	typeShortIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre short[] - Double[]
	typeShortIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para int[] **/
		
	//Difinindo compatibilidade entre int[] - int[]
	typeIntIArray.addCompatibleTypeBidirectional(typeIntIArray, compatMaxima);
	//Difinindo compatibilidade entre int[] - long[]
	typeIntIArray.addCompatibleTypeBidirectional(typeLongIArray, compatAlta);
	//Difinindo compatibilidade entre int[] - float[]
	typeIntIArray.addCompatibleTypeBidirectional(typeFloatIArray, compatMedia);
	//Difinindo compatibilidade entre int[] - double[]
	typeIntIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatMedia);
	
	//Difinindo compatibilidade entre int[] - Byte[]
	typeIntIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatAlta);
	//Difinindo compatibilidade entre int[] - Short[]
	typeIntIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatAlta);
	//Difinindo compatibilidade entre int[] - Int[]
	typeIntIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatMaxima);
	//Difinindo compatibilidade entre int[] - Long[]
	typeIntIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre int[] - Float[]
	typeIntIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre int[] - Double[]
	typeIntIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para long[] **/
		
	//Difinindo compatibilidade entre long[] - long[]
	typeLongIArray.addCompatibleTypeBidirectional(typeLongIArray, compatMaxima);
	//Difinindo compatibilidade entre long[] - float[]
	typeLongIArray.addCompatibleTypeBidirectional(typeFloatIArray, compatMedia);
	//Difinindo compatibilidade entre long[] - double[]
	typeLongIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatMedia);
	
	//Difinindo compatibilidade entre long[] - Byte[]
	typeLongIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatAlta);
	//Difinindo compatibilidade entre long[] - Short[]
	typeLongIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatAlta);
	//Difinindo compatibilidade entre long[] - Int[]
	typeLongIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatAlta);
	//Difinindo compatibilidade entre long[] - Long[]
	typeLongIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatMaxima);
	//Difinindo compatibilidade entre long[] - Float[]
	typeLongIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre long[] - Double[]
	typeLongIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para float[] **/
		
	//Difinindo compatibilidade entre float[] - float[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeFloatIArray, compatMaxima);
	//Difinindo compatibilidade entre float[] - double[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatAlta);
	
	//Difinindo compatibilidade entre float[] - Byte[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatMedia);
	//Difinindo compatibilidade entre float[] - Short[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatMedia);
	//Difinindo compatibilidade entre float[] - Int[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatMedia);
	//Difinindo compatibilidade entre float[] - Long[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatMedia);
	//Difinindo compatibilidade entre float[] - Float[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMaxima);
	//Difinindo compatibilidade entre float[] - Double[]
	typeFloatIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatAlta);
	
	
		/**  Para double[] **/
		
	//Difinindo compatibilidade entre double[] - double[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeDoubleIArray, compatMaxima);
	
	//Difinindo compatibilidade entre double[] - Byte[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatMedia);
	//Difinindo compatibilidade entre double[] - Short[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatMedia);
	//Difinindo compatibilidade entre double[] - Int[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatMedia);
	//Difinindo compatibilidade entre double[] - Long[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatMedia);
	//Difinindo compatibilidade entre double[] - Float[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatAlta);
	//Difinindo compatibilidade entre double[] - Double[]
	typeDoubleIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMaxima);
	
	
		/** char[] **/
	typeCharArray.addCompatibleTypeBidirectional(typeString, compatAlta);
	
	
		/**  Para Byte[] **/
	
	//Difinindo compatibilidade entre Byte[] - Byte[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeByteIIArray, compatMaxima);
	//Difinindo compatibilidade entre Byte[] - Short[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatAlta);
	//Difinindo compatibilidade entre Byte[] - Int[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatAlta);
	//Difinindo compatibilidade entre Byte[] - Long[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre Byte[] - Float[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre Byte[] - Double[]
	typeByteIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para Short[] **/

	//Difinindo compatibilidade entre Short[] - Short[]
	typeShortIIArray.addCompatibleTypeBidirectional(typeShortIIArray, compatMaxima);
	//Difinindo compatibilidade entre Short[] - Int[]
	typeShortIIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatAlta);
	//Difinindo compatibilidade entre Short[] - Long[]
	typeShortIIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre Short[] - Float[]
	typeShortIIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre Short[] - Double[]
	typeShortIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para Int[] **/
	
	//Difinindo compatibilidade entre Int[] - Int[]
	typeIntIIArray.addCompatibleTypeBidirectional(typeIntIIArray, compatMaxima);
	//Difinindo compatibilidade entre Int[] - Long[]
	typeIntIIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatAlta);
	//Difinindo compatibilidade entre Int[] - Float[]
	typeIntIIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre Int[] - Double[]
	typeIntIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para Long[] **/

	//Difinindo compatibilidade entre Long[] - Long[]
	typeLongIIArray.addCompatibleTypeBidirectional(typeLongIIArray, compatMaxima);
	//Difinindo compatibilidade entre Long[] - Float[]
	typeLongIIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMedia);
	//Difinindo compatibilidade entre Long[] - Double[]
	typeLongIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMedia);
	
	
		/**  Para Float[] **/
	
	//Difinindo compatibilidade entre Float[] - Float[]
	typeFloatIIArray.addCompatibleTypeBidirectional(typeFloatIIArray, compatMaxima);
	//Difinindo compatibilidade entre Float[] - Double[]
	typeFloatIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatAlta);
	
	
		/**  Para Double[] **/
		
	//Difinindo compatibilidade entre Double[] - Double[]
	typeDoubleIIArray.addCompatibleTypeBidirectional(typeDoubleIIArray, compatMaxima);
	
	
		/**  Para String[] **/
		
	//Difinindo compatibilidade entre String[] - String[]
	typeStringArray.addCompatibleTypeBidirectional(typeStringArray, compatMaxima);

	
	
	/***************************** Matrizes **********************************/
	
		
		
		/**  Para byte[][] **/
	
	//Difinindo compatibilidade entre byte[][] - byte[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeByteIMatrix, compatMaxima);
	//Difinindo compatibilidade entre byte[][] - short[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeShortIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - int[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeIntIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - long[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeLongIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - float[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeFloatIMatrix, compatMedia);
	//Difinindo compatibilidade entre byte[][] - double[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatMedia);
	
	//Difinindo compatibilidade entre byte[][] - Byte[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre byte[][] - Short[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - Integer[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - Long[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre byte[][] - Float[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre byte[][] - Double[][]
	typeByteIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
	
		/**  Para short[][] **/
		
	//Difinindo compatibilidade entre short[][] - short[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeShortIMatrix, compatMaxima);
	//Difinindo compatibilidade entre short[][] - int[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeIntIMatrix, compatAlta);
	//Difinindo compatibilidade entre short[][] - long[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeLongIMatrix, compatAlta);
	//Difinindo compatibilidade entre short[][] - float[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeFloatIMatrix, compatMedia);
	//Difinindo compatibilidade entre short[][] - double[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatMedia);
	
	//Difinindo compatibilidade entre short[][] - Byte[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatAlta);
	//Difinindo compatibilidade entre short[][] - Short[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre short[][] - Int[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatAlta);
	//Difinindo compatibilidade entre short[][] - Long[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre short[][] - Float[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre short[][] - Double[][]
	typeShortIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para int[][] **/
		
	//Difinindo compatibilidade entre int[][] - int[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeIntIMatrix, compatMaxima);
	//Difinindo compatibilidade entre int[][] - long[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeLongIMatrix, compatAlta);
	//Difinindo compatibilidade entre int[][] - float[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeFloatIMatrix, compatMedia);
	//Difinindo compatibilidade entre int[][] - double[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatMedia);
	
	//Difinindo compatibilidade entre int[][] - Byte[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatAlta);
	//Difinindo compatibilidade entre int[][] - Short[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatAlta);
	//Difinindo compatibilidade entre int[][] - Int[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre int[][] - Long[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre int[][] - Float[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre int[][] - Double[][]
	typeIntIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para long[][] **/
		
	//Difinindo compatibilidade entre long[][] - long[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeLongIMatrix, compatMaxima);
	//Difinindo compatibilidade entre long[][] - float[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeFloatIMatrix, compatMedia);
	//Difinindo compatibilidade entre long[][] - double[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatMedia);
	
	//Difinindo compatibilidade entre long[][] - Byte[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatAlta);
	//Difinindo compatibilidade entre long[][] - Short[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatAlta);
	//Difinindo compatibilidade entre long[][] - Int[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatAlta);
	//Difinindo compatibilidade entre long[][] - Long[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre long[][] - Float[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre long[][] - Double[][]
	typeLongIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para float[][] **/
		
	//Difinindo compatibilidade entre float[][] - float[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeFloatIMatrix, compatMaxima);
	//Difinindo compatibilidade entre float[][] - double[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatAlta);
	
	//Difinindo compatibilidade entre float[][] - Byte[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatMedia);
	//Difinindo compatibilidade entre float[][] - Short[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatMedia);
	//Difinindo compatibilidade entre float[][] - Int[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatMedia);
	//Difinindo compatibilidade entre float[][] - Long[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatMedia);
	//Difinindo compatibilidade entre float[][] - Float[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre float[][] - Double[][]
	typeFloatIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatAlta);
	
	
		/**  Para double[][] **/
		
	//Difinindo compatibilidade entre double[][] - double[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeDoubleIMatrix, compatMaxima);
	
	//Difinindo compatibilidade entre double[][] - Byte[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatMedia);
	//Difinindo compatibilidade entre double[][] - Short[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatMedia);
	//Difinindo compatibilidade entre double[][] - Int[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatMedia);
	//Difinindo compatibilidade entre double[][] - Long[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatMedia);
	//Difinindo compatibilidade entre double[][] - Float[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatAlta);
	//Difinindo compatibilidade entre double[][] - Double[][]
	typeDoubleIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMaxima);
	
	
		/** char[][] **/
	typeCharMatrix.addCompatibleTypeBidirectional(typeStringArray, compatAlta);
	
	
		/**  Para Byte[][] **/
	
	//Difinindo compatibilidade entre Byte[][] - Byte[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeByteIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre Byte[][] - Short[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Byte[][] - Int[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Byte[][] - Long[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Byte[][] - Float[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre Byte[][] - Double[][]
	typeByteIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para Short[][] **/
	
	//Difinindo compatibilidade entre Short[][] - Short[][]
	typeShortIIMatrix.addCompatibleTypeBidirectional(typeShortIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre Short[][] - Int[][]
	typeShortIIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Short[][] - Long[][]
	typeShortIIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Short[][] - Float[][]
	typeShortIIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre Short[][] - Double[][]
	typeShortIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para Int[][] **/
	
	//Difinindo compatibilidade entre Int[][] - Int[][]
	typeIntIIMatrix.addCompatibleTypeBidirectional(typeIntIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre Int[][] - Long[][]
	typeIntIIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatAlta);
	//Difinindo compatibilidade entre Int[][] - Float[][]
	typeIntIIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre Int[][] - Double[][]
	typeIntIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para Long[][] **/
	
	//Difinindo compatibilidade entre Long[][] - Long[][]
	typeLongIIMatrix.addCompatibleTypeBidirectional(typeLongIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre Long[][] - Float[][]
	typeLongIIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMedia);
	//Difinindo compatibilidade entre Long[][] - Double[][]
	typeLongIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMedia);
	
	
		/**  Para Float[][] **/
	
	//Difinindo compatibilidade entre Float[][] - Float[][]
	typeFloatIIMatrix.addCompatibleTypeBidirectional(typeFloatIIMatrix, compatMaxima);
	//Difinindo compatibilidade entre Float[][] - Double[][]
	typeFloatIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatAlta);
	
	
		/**  Para Double[][] **/
		
	//Difinindo compatibilidade entre Double[][] - Double[][]
	typeDoubleIIMatrix.addCompatibleTypeBidirectional(typeDoubleIIMatrix, compatMaxima);
	
	/***************************** Collections **********************************/

		/**  Para ArrayList **/
	typeArrayList.addCompatibleTypeBidirectional(typeLinkedList, compatAlta);
	typeArrayList.addCompatibleTypeBidirectional(typeVector, compatAlta);
	typeArrayList.addCompatibleTypeBidirectional(typeObjectArray, compatMedia);
	
		/**  Para LinkedList **/
	typeLinkedList.addCompatibleTypeBidirectional(typeVector, compatAlta);
	typeLinkedList.addCompatibleTypeBidirectional(typeObjectArray, compatMedia);
	
	   /** Para Vector **/
	typeVector.addCompatibleTypeBidirectional(typeObjectArray, compatMedia);
}
	
	
}
