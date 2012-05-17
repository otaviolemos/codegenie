/**
 * @author Rafael B. Januzi
 * @date 24/08/2011
 */

/**
 * Esta classe representa um nivel de compatibilidade entre tipos, por exemplo:
 * 
 *   "nivel" - "nome"
 *   
 *		1 - Baixa
 * 		2 - Média
 *		3 - Alta 
 * 
 */


package ict.seg.codegenie.ie.typeCompatibility;

import java.util.ArrayList;

public class CompatibilityLevel{
	
	private static int lastLevel = 0;
	private static ArrayList<CompatibilityLevel> existingLevels = new ArrayList<CompatibilityLevel>();
	private String name;
	private int level;
	
	//O único construtor, permite a criação de um objeto somente com a passagem de um nome
	//e seta automáticamente um valor para nivel seguindo a sequencia 1,2,3,...,n.
	public CompatibilityLevel(String nome){
		
		this.name = nome;
		level = ++lastLevel;
		existingLevels.add(this);
		
	}
	
	public int getLevel(){
		return level;
	}
	
	public int getLastLevel(){
		return lastLevel;
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public CompatibilityLevel[] getExistingLevels(){
		
		CompatibilityLevel[] array = new CompatibilityLevel[existingLevels.size()]; 
		
		existingLevels.toArray(array);
		
		return array;
	}
	
	public void listExistingLevels(){
		
		for(int i = 0; i < existingLevels.size(); i++)
			System.out.println(existingLevels.get(i).toString());
		
	}
	
	@Override
	public String toString(){
		
		return "\n\nLevel: " + level + "\nName: " + 
		name + "\n\n";
	}
}
