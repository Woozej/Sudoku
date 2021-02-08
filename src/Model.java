import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.swing.JFormattedTextField;

public class Model {
	//Zapis pola gry
	private int[][][] gameField;
	//Kontruktor
	Model(){
		//Wybranie losowej liczby do planszy
		Random rnd = new Random();
		int index = rnd.nextInt(100);
		
		int[][][] field = new int [9][3][3];
		
		//Wczytanie pliku z planszami
		String COMMA_DELIMITER = ",";
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("src/Boards.csv"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(COMMA_DELIMITER);
		        records.add(Arrays.asList(values));
		    }
		}catch (Exception e) {

	        System.out.println("Error in CsvFileWriter !!!");
	        e.printStackTrace();
		}
		//Przepisanie pliku do pola
		String stringField = records.get(index).get(0);
		int z=0;
		String dotString = ".";
		char dotChar = dotString.charAt(0);
		for(int y = 0; y < 9; y=y+3){
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					for(int k = 0; k < 3; k++) {
						if(stringField.charAt(z++) == dotChar)
							field[j+y][i][k] = 0;
						else
							field[j+y][i][k] = Character.getNumericValue(stringField.charAt(z-1)); 
					}
				}
			}
		}
		//Przepisanie pola field do gameField
		gameField = field;
	}
	
	//Model pojedyñczej planszy (do testów)
	Model(int i) {
		int[][][] field = {
			{
				{0,0,7},
				{4,1,0},
				{0,5,0}
			},			
			{
				{2,0,6},
				{0,0,0},
				{4,7,0}
			},
			{
				{5,0,1},
				{0,0,0},
				{0,2,0}
			},
			{
				{0,8,0},
				{0,0,9},
				{0,0,1}
			},
			{
				{0,9,5},
				{0,4,0},
				{3,0,2}
			},
			{
				{6,0,7},
				{0,1,0},
				{0,0,0}
			},
			{
				{7,2,0},
				{0,0,3},
				{9,4,0}
			},
			{
				{9,0,0},
				{0,0,0},
				{6,0,0}
			},
			{
				{0,0,6},
				{0,7,2},
				{0,8,0}
			}
			};
		gameField = field;
	}
	
	//Wype³niona plansza (do testów)
	public void endGame() {
		int[][][] field = {
				{
					{3,9,7},
					{4,1,2},
					{8,5,6}
				},			
				{
					{2,8,6},
					{5,3,9},
					{4,7,1}
				},
				{
					{5,4,1},
					{7,6,8},
					{3,2,9}
				},
				{
					{2,8,4},
					{6,3,9},
					{5,7,1}
				},
				{
					{1,9,5},
					{7,4,8},
					{3,6,2}
				},
				{
					{6,3,7},
					{2,1,5},
					{8,9,4}
				},
				{
					{7,2,8},
					{1,6,3},
					{9,4,5}
				},
				{
					{9,1,3},
					{8,5,4},
					{6,2,7}
				},
				{
					{4,5,6},
					{9,7,2},
					{1,8,3}
				}
				};
			gameField = field;
	}
	
	//Update planszy
	public void updateValues(ArrayList<JFormattedTextField> array) {
		int z = 0;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) {
					try{
						gameField[i][j][k] = Integer.parseInt(array.get(z++).getText());
					}
					catch(Exception e){
						gameField[i][j][k] = 0;
					}	
				}
			}
		}
	}
	
	//Metoda do pobierania pól planszy w widoku
	public int[] getValuesAsTable() {
		int[] values = new int[81];
		int z = 0;
		for(int y = 0; y < 9;y=y+3){
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					for(int k = 0; k < 3; k++) {
						 values[z++] = gameField[j+y][i][k];
					}
				}
			}
		}
		return values;
	}
		
	//Metoda do sprawdzenia czy pola s¹ w³aœciwie wype³nione
	public boolean isWon() {
		int[][] valuesInRows = new int[9][9];
		int z = 0;
		boolean won = true;
		
		//Pobranie wartoœci pojedyñczych kwadratów
		for(int y = 0; y < 9; y=y+3){
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					for(int k = 0; k < 3; k++) {
						valuesInRows[i+y][z++] = gameField[j+y][i][k];
					}
				}
				z=0;
			}
		}
		//Sprawdzenie czy wartoœci kwadratów siê nie dubluj¹
		if(chceckForDuplicates(valuesInRows))
			won = false;
		
		//Pobranie wartoœci lini poziomych
		for(int y = 0; y < 9; y=y+3){
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					for(int k = 0; k < 3; k++) {
						valuesInRows[i+y][z++] = gameField[i+y][j][k];
					}
				}
				z=0;
			}
		}
		//Sprawdzenie czy wartoœci linii poziomych siê nie dubluj¹
		if(chceckForDuplicates(valuesInRows))
			won = false;
		//Pobranie wartoœci lini pionowych
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 9; k++) {
					valuesInRows[z][k] = gameField[k][i][j];
				}
				z++;
			}
		} 
		//Sprawdzenie czy wartoœci linii pionowych siê nie dubluj¹
		if(chceckForDuplicates(valuesInRows))
			won = false;
		return won;
	}
	
	//metoda prywatna sprawdzaj¹ca czy wartoœci siê nie dubluj¹
	private boolean chceckForDuplicates(int[][] valuesInRows) {
		for (int k = 0; k < 9; k++) {
			for (int i = 0; i < 9; i++) {
	            for (int j = 0; j < 9; j++) {
	                if (valuesInRows[k][i] ==  valuesInRows[k][j] && i != j) {
	                    return true;
	                }
	            }
			}
		}
		return false;
	}

	//Metoda s³u¿¹ca sprawdzaniu czy pole jest wype³nione
	public boolean isCompleted() {
		boolean completed=true;
		for(int i = 0; i < 9; i++) {
			for(int j = 0; j < 3; j++) {
				for(int k = 0; k < 3; k++) {
					if(gameField[i][j][k] == 0)
						completed = false;
				}
			}
		}
		return completed;
	}
}
