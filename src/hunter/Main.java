package hunter;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Main extends JFrame {
	public static final File file = new File("generatedScrambles.txt");
	public static PrintWriter printWriter = null;
	
	private static final long serialVersionUID = 1L;
	
	public static int windowWidth = 505;
	public static int windowHeight = 300;
	public static int selectedCube;
	public static int scrambleLength = 25;
	
	public static final int maxScrambleLength = 25;
	
	public static String finalScrambleString;
	
	public static final String threeTurns[] = {"U","U'","R","R'","L","L'","D","D'","B","B'","F","F'","U2","R2","L2","D2","F2","B2"};
	public static final String fourTurns[] = {"U","U'","R","R'","L","L'","D","D'","B","B'","F","F'","U2","R2","L2","D2","F2","B2",
											  "u","u'","r","r'","l","l'","d","d'","b","b'","f","f'","u2","r2","l2","d2","f2","b2"};
	
	ArrayList<String> finalScramble = new ArrayList<String>();
	
	public static JTextField scramble = new JTextField();
	
	JPanel contentPane;
	
	public static void main(String[] args) {
		//Start
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Main();
				
				try {
					printWriter = new PrintWriter(file);
				}catch(FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
		
		//End
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    @Override
		    public void run() {
		    	if(printWriter != null) {
		    		printWriter.close();
		    	}
		    }
		});
	}
	
	public Main() {		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		//Frame Properties
		setTitle("Cube Scrambler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(screenSize.width/2-windowWidth/2, screenSize.height/2-windowHeight/2, windowWidth, windowHeight);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);
		setVisible(true);
		
		//Frame Elements
		JButton generate = new JButton("Generate Scramble");
		generate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				generateScramble(scrambleLength);
			}
		});
		generate.setBounds(323, 227, 159, 25);
		contentPane.add(generate);
		
		JLabel title = new JLabel("Cube Scrambler");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 32));
		title.setBounds(0, 0, 494, 63);
		contentPane.add(title);
		
		scramble.setHorizontalAlignment(SwingConstants.CENTER);
		scramble.setEditable(false);
		scramble.setBounds(12, 62, 470, 22);
		contentPane.add(scramble);
		scramble.setColumns(10);
		
		JLabel options = new JLabel("Options:");
		options.setFont(new Font("Tahoma", Font.BOLD, 20));
		options.setBounds(12, 105, 470, 25);
		contentPane.add(options);
		
		JLabel optionScrambleLength = new JLabel("Scramble Length:");
		optionScrambleLength.setBounds(34, 136, 107, 16);
		contentPane.add(optionScrambleLength);
		
		JTextField optionsScrambleLengthBox;
		optionsScrambleLengthBox = new JTextField();
		optionsScrambleLengthBox.setText("25");
		optionsScrambleLengthBox.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				scrambleLength = Integer.parseInt(optionsScrambleLengthBox.getText());
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		optionsScrambleLengthBox.setBounds(138, 133, 57, 22);
		contentPane.add(optionsScrambleLengthBox);
		optionsScrambleLengthBox.setColumns(10);
		
		JLabel optionsCube = new JLabel("Cube:");
		optionsCube.setBounds(34, 161, 57, 16);
		contentPane.add(optionsCube);
		
		JComboBox<String> optionsCubeBox = new JComboBox<String>();
		optionsCubeBox.setBounds(70, 158, 89, 22);
		optionsCubeBox.addItem("3x3");
		optionsCubeBox.addItem("4x4");
		optionsCubeBox.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				selectedCube = optionsCubeBox.getSelectedIndex();
			}
			
			@Override
			public void focusGained(FocusEvent e) {
			}
		});
		contentPane.add(optionsCubeBox);
	}
	
	/**
	 * Generates a Scramble
	 * @param length The length of the scramble you want to generate.
	 */
	public void generateScramble(int length) {		
		Random random = new Random();
		
		//Test if Requested Scramble Length is Larger than the Max Scramble Length
		if(length > maxScrambleLength) {
			scramble.setText("Scramble Too Large!");
			return;
		}else {
			finalScramble.clear();
		}
		
		//Generate Turns
		if(selectedCube == 0) {     //3x3x3
			for(int i=1; i<=length;i++) {
				int x = random.nextInt(threeTurns.length);
				finalScramble.add(threeTurns[x]+" ");
			}
		}else if(selectedCube == 1) {     //4x4x4
			for(int i=1; i<=length;i++) {
				int x = random.nextInt(fourTurns.length);
				finalScramble.add(fourTurns[x]+" ");
			}
		}
		
		scramble.setText(cleanScramble(finalScramble, true));
		printWriter.println(cleanScramble(finalScramble, false));
	}
	
	/**
	 * Cleans an array list of notations to just a single line of notations.
	 * 
	 * @param textToClean Array list of notations.
	 * @param twoSpaces Whether to place two spaces between each element or not.
	 * @return Final string with removed brackets and commas and optionally, the double space.
	 */
	public String cleanScramble(ArrayList<String> textToClean, boolean twoSpaces) {
		finalScrambleString = textToClean.toString();
		finalScrambleString = finalScrambleString.replace("[", "");
		finalScrambleString = finalScrambleString.replace("]", "");
		finalScrambleString = finalScrambleString.replace(",", "");
		
		if(twoSpaces) {
			return finalScrambleString;
		}else if(!twoSpaces) {
			finalScrambleString = finalScrambleString.replace("  ", " ");
			return finalScrambleString;
		}
		
		return null;
	}
}

