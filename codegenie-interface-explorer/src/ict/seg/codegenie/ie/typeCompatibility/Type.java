/**
 * @author Rafael B. Januzi
 * @date 24/08/2011
 */

package ict.seg.codegenie.ie.typeCompatibility;

import java.util.ArrayList;
import java.util.HashMap;


public class Type{
	
	private String name;
	private int id;
	private static int typesQuantity = 0;
	private static ArrayList<Type> existingTypesList = new ArrayList<Type>();
	private ArrayList<Type> compativesTypesList = new ArrayList<Type>();
	private HashMap<Type, Double> compatibilityMap = new HashMap<Type, Double>();
	
	public Type(String nome){
		
		id = ++typesQuantity;
		this.name = nome;
		existingTypesList.add(this);	
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId(){
		return id;
	}
	
	public int getTypesQuantity() {
		return typesQuantity;
	}
	
	public double getCompatibility(Type type) {
		
		if(compatibilityMap.containsKey(type))
			return compatibilityMap.get(type);
		
//		System.err.println("A compatibilidade entre os tipos " + this + " e " + type + " " + "não foi definida!");
		
		return 0;
	}
	
	public static Type[] getExistingTypes(){
		
		Type[] array = new Type[existingTypesList.size()];
		
		existingTypesList.toArray(array);
		
		return array;
	}
	
	private static Type getTypePerId(int id){
		
		for(int i = 0; i < existingTypesList.size(); i++)
			if(id == existingTypesList.get(i).getId())
				return existingTypesList.get(i);
		
		System.err.println("Type not found in search per id.");
		
		return null;
		
	}
	
	public Type[] getSortedArrayOfCompativeTypes(){
		
		Type[] array = new Type[this.existingTypesList.size()];
		
		existingTypesList.toArray(array);
		
		for(int i = 0; i < array.length; i++){
			
			int posicaoMaior = i;
			
			//Encontra o maior
			for(int j = i; j < array.length; j++)
				if(this.getCompatibility(array[j]) > this.getCompatibility(array[posicaoMaior]))
					posicaoMaior = j;
			
			//Troca elemento i, com o maior elemento
			Type aux = array[i];
			array[i] = array[posicaoMaior];
			array[posicaoMaior] = aux;
				
		}
		
		return array;
	}
	
	public static Type getTypePerName(String name){
		
		for(int i = 0; i < existingTypesList.size(); i++)
			if(existingTypesList.get(i).getName().equals(name))
				return existingTypesList.get(i);
		
		System.err.println("Type not found in search per name.");
		
		return null;

	}
	                                      
	public void addCompatibleTypeBidirectional(Type type, CompatibilityLevel levelCompat){
		
		//Fazer "this" compativel a "tipo"
		this.addCompativeType(type, levelCompat);
		
		//Fazer "tipo" compativel a "this"
		type.addCompativeType(this, levelCompat);
		
		
	}
	
    public void addCompativeType(Type type, CompatibilityLevel levelCompat){
    	
    	if(!compatibilityMap.containsKey(type)){
    		
    		Double CompatibilityValue = new Double((double)levelCompat.getLevel()/(double)levelCompat.getLastLevel()); // Entre 0 e 1
    		
    		//Adiciona o tipo ao mapa de compatibilidade
    		compatibilityMap.put(type, CompatibilityValue);
    		
    		//Adiciona o tipo na lista de tipos compativeis
    		compativesTypesList.add(type);
    	}
//    	else
//    		System.err.println("Já existe compatibilidade entre esses tipos!");
    }
	
	public static double[][] generateCompatibilityMatrix() {
		
		if(typesQuantity < 0){
			System.err.println("Impossivel gerar a Matriz de Compatibilidade, pois não existem tipos criados.");
			return null;
		}
		
		//Indice + 1 é o Tipo correspondente aquela linha ou coluna
		double[][] CompatibilityMatrix = new double[typesQuantity][typesQuantity];
		
		for(int i = 0; i < typesQuantity; i++)
			for(int j = 0; j < typesQuantity; j++)
				CompatibilityMatrix[i][j] = getTypePerId(i+1).getCompatibility(getTypePerId(j+1));
			
		System.out.println("Matriz de compatibilidade gerada!");
		
		/*
		for(int i = 0; i < quantidadeDeTipos; i++){
			System.out.println();
			for(int j = 0; j < quantidadeDeTipos; j++)
				System.out.print(" " + matrizCompatibilidade[i][j] + " ");
		}
		*/

		
		return CompatibilityMatrix;
	}
	
	@Override
	public String toString(){
		return "Nome: " + name;
	}
	
	
	@Override
	public boolean equals(Object o){
		
		boolean equal = true;
		Type type = (Type) o;
		
		if(!name.equals(type.getName()))
			equal = false;
		
		if(id != type.getId())
			equal = false;
		
		return equal;
	}
	
	
	
	
	/**********               Atividade 03                **********/
	
	
	
	//O scoreMinimo representa a porcentagem de compatibilidade (entre 0 e 1) por exemplo:  100% -> 1.0 ; 50% -> 0.5 ; 25% -> 0.25
	public ArrayList<Type> getLimitedCompativeTypes(double minimumScore){
		
		Type[] sortedArrayOfCompativesTypes = this.getSortedArrayOfCompativeTypes();
		ArrayList<Type> limitedCompativeTypesList = new ArrayList<Type>();
		
		for(int i = 0; i < sortedArrayOfCompativesTypes.length && this.getCompatibility(sortedArrayOfCompativesTypes[i]) >= minimumScore; i++)
			if(!sortedArrayOfCompativesTypes[i].equals(this))
				limitedCompativeTypesList.add(sortedArrayOfCompativesTypes[i]);
			
			
		return limitedCompativeTypesList;
	}
	
	//Retorna um HashMap onde a chave é o nome de um Tipo e o conteúdo é um ArrayList de Tipos compatíveis, de acordo com o score mínimo,
	//para o tipo com o nome da chave
	public static HashMap<Type, ArrayList<Type>> getCompativesTypesForMethodInterface(ArrayList<Type> typesOfInterfaceList, double minimumScore){
		
		HashMap<Type, ArrayList<Type>> compativesTypesMap = new HashMap<Type, ArrayList<Type>>();
		
		for(int i = 0; i < typesOfInterfaceList.size(); i++)
			if(!compativesTypesMap.containsKey(typesOfInterfaceList.get(i)))
				compativesTypesMap.put(typesOfInterfaceList.get(i), typesOfInterfaceList.get(i).getLimitedCompativeTypes(minimumScore));
		
		
		return compativesTypesMap;
	}


}
