	import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import static javax.swing.JOptionPane.showMessageDialog;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;


@SuppressWarnings("serial")
public class View extends JFrame {
	//Stworzenie listy pól wejœciowych
	public ArrayList<JFormattedTextField> gameFields = new ArrayList<JFormattedTextField>();
	
	//Stworzenie modelu gry
	public Model model = new Model();
	
	//Do poprawy
	public void updateValues() {
		  model.updateValues(gameFields);
	  }
	 
    @SuppressWarnings({ "deprecation" })
	public View() {
    	
    	//stworzenie formattera aby mo¿na by³o dodaæ tylko cyfry
    	NumberFormat format = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(format) {
    		//Override aby mo¿na by³o kasowaæ wartoœæ
    		@Override
			public Object stringToValue(String text) throws ParseException{
    			try {
	    	        if(text.trim().isEmpty())
	    				return null;
	    	        return super.stringToValue(text);
    			}catch (ParseException e) {
    				// TODO Auto-generated catch block
				return "";}
    	    }	
    	};
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(9);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        
        //Font liczb
        Font numberFont = new Font("ComicSans", Font.BOLD, 30);
        //Storzenie g³ównego panelu
        JSplitPane mainWindowPanel =  new JSplitPane(JSplitPane.VERTICAL_SPLIT); 
    	//stworzenie panelu gry
    	JPanel gameFieldPanel = new JPanel(new GridLayout(9,9)); 
    	//stworzenie pól do gry z odopwiednimi ramkami
    	for (int i = 0; i < 81; i++) {
    		int top=1,left=1,bottom=1,right=1;
    		gameFields.add(new JFormattedTextField(formatter));
    		gameFields.get(i).setHorizontalAlignment(JTextField.CENTER);
    		gameFields.get(i).setPreferredSize(new Dimension( 50, 50 ));
    		gameFields.get(i).setText("");
    		gameFields.get(i).setFont(numberFont);
    		if(i % 3 == 0)
    			left = 3;
    		if(i % 9 == 8)
    			right = 3;
    		if(i/9 == 0 || i/9 == 3 || i/9 == 6)
    			top=3;
    		if(i/9==8)
    			bottom = 3;
    		gameFields.get(i).setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.black));
    		gameFields.get(i).getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  
			  }
			  public void removeUpdate(DocumentEvent e) {
				  updateValues();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  updateValues();
			  }	
    		});
    		gameFieldPanel.add(gameFields.get(i));
    		gameFields.get(i).disable();

    	}
    	
    	//Panel z górnym menu
    	JPanel infoPanel = new JPanel(new GridLayout(1,5));
    	
    	//Przycisk nowej gry
    	JButton NewGameButton = new JButton();
    	NewGameButton.setText("Nowa gra");
    	//Po naciœniêciu stworzenie nowego modelu
    	NewGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            model = new Model();
      		  int[] values = model.getValuesAsTable();
    		  for(int i = 0; i < gameFields.size(); i++ )
    		  {
				  //Je¿eli pole zawiera cyfrê wy³¹czenie go i nadpisanie wartoœci
				  if(values[i]>0) 
				  {
					  gameFields.get(i).setValue(null);
					  gameFields.get(i).disable();  
					  gameFields.get(i).setText(""+values[i]);
					  
				  }
				  //Je¿eli pole nie zawiera cyfry zresetowanie wartoœci i w³¹czenie go	
				  else
				  {
					  gameFields.get(i).setValue(null);
					  gameFields.get(i).enable(); 
					  gameFields.get(i).setText("");
				  }
    		  }
            }
        });
    	infoPanel.add(NewGameButton);
    	
    	//Dodanie przycisku o autorze
    	JButton aboutButton = new JButton();
    	aboutButton.setText("O autorze");
    	aboutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	showMessageDialog(null, "Gra stworzona na zaliczenie przedmiotu INU przez Krystiana Kolcan","O autorze" ,JOptionPane.PLAIN_MESSAGE);
            }
        });
    	infoPanel.add(aboutButton);
    	
    	//Dodanie przycisku pomocy
    	JButton helpButton = new JButton();
    	helpButton.setText("Pomoc");
    	helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	showMessageDialog(null, "Aby rozpocz¹æ grê naciœnij przycisk 'Nowa gra'\n"
            			+ "Aby wygraæ nale¿y uzupe³niæ pola liczbami od 1 do 9 tak aby w ka¿dej lini\n"
            			+ "poziomej, pionowej oraz w ka¿dym kwadracie wystêpowa³y unikalne cyfry od 1 do 9.\n"
            			+ "Aby sprawdziæ cys siê uda³o naciœnij przycisk sprawdz","O autorze" ,JOptionPane.PLAIN_MESSAGE);
            }
        });
    	infoPanel.add(helpButton);

    	//Stowrzenie przycisku wype³nienia pól
    	JButton endGameButton = new JButton();
    	endGameButton.setText("Wype³nij Pola");
    	
    	//Wype³nienie pól poprawnymi danymi
    	endGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            	model.endGame();
      		  int[] values = model.getValuesAsTable();
    		  for(int i = 0; i < gameFields.size(); i++ )
    		  {
    			  if(values[i]>0) 
    			  {
    				  gameFields.get(i).setText(""+values[i]);
				  }
    			  		
    			  else
    			  {
    				  gameFields.get(i).setText("");
    			  }
    		  }
            }
        });
    	infoPanel.add(endGameButton);
    	
    	//Stworzenie przycisku sprawdzaj¹cego czy gra jest zakoñczona
    	JButton checkGameButton = new JButton();
    	checkGameButton.setText("Sprawdz");
    	checkGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
              //sprawdzenie czy gra jest skoñczona
      		  if(model.isCompleted()) {
      			//sprawdzanie czy gra jest wygrana
            	if(model.isWon())
            		showMessageDialog(null, "Gratulacje uda³o Ci siê ukoñczyæ!","" ,JOptionPane.PLAIN_MESSAGE);
            	else
            		showMessageDialog(null, "Nie wszystkie pola s¹ prawid³owe","" ,JOptionPane.ERROR_MESSAGE);
        		}
      		  	else 
      		  	{
        			showMessageDialog(null, "Wype³nij wszystkie pola","" ,JOptionPane.ERROR_MESSAGE);
        		}
            	
            }
        });
    	infoPanel.add(checkGameButton);
    	
    	//Dodanie elementów do panelu
    	mainWindowPanel.setTopComponent(infoPanel);
    	mainWindowPanel.setBottomComponent(gameFieldPanel);

    	
        getContentPane().add(mainWindowPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}

