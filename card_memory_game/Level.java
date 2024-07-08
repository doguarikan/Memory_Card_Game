package card_memory_game;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.*;

public class Level extends JFrame{
	
	private JPanel pnl_lvlpanel;
	private JPanel pnl_top;
	private JMenuBar bar;
	private JMenu mnGame;
	private JMenu mnAbout;
	private JMenu mnExit;
	private JMenuItem restart;
	private JMenuItem highscores;
	private JMenuItem abtdev;
	private JMenuItem abtgame;
	private JMenuItem exit;
	private JLabel labellvl;
	private JLabel labelmove;
	private List<JButton> buttons = new ArrayList<>(16);
	private JButton btndef;
	private JButton firstselectedbtn;
    private JButton secondselectedbtn;
	private List<ImageIcon> cardIcons = new ArrayList<>();
	private ImageIcon defaultıcon;
	private GridLayout grid;
	private BorderLayout border;
	private FlowLayout flow;
	private int tries;
	private int matches = 0;
	private int penalty = 0;
	private int firstbtnindex;
	private int secondbtnindex;
	private String level;
	private Thread shuffleThread;
	
	public Level(String level){
		this.level = level;
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		this.setBounds(600, 200, 670, 700);
		
		pnl_top = new JPanel();
		if(level.equals("one"))
			pnl_top.setBackground(Color.blue);
		else if(level.equals("two"))
			pnl_top.setBackground(Color.pink);
		else if(level.equals("three"))
			pnl_top.setBackground(Color.red);
		pnl_top.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		pnl_lvlpanel = new JPanel();
		pnl_lvlpanel.setLayout(new GridLayout(4, 4, 10, 10));

		bar = new JMenuBar();

		mnGame = new JMenu("Game");
		mnAbout = new JMenu("About");
		mnExit = new JMenu("Exit");

		restart = new JMenuItem("Restart");
		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				create_new_game(level);
			}
		});
		mnGame.add(restart);

		highscores = new JMenuItem("High Scores");
		highscores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				show_scores();
			}
		});
		mnGame.add(highscores);

		abtdev = new JMenuItem("About Developer");
		abtdev.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				put_message("Developer name: Doğu Baha Arıkan\n ID: 20210702090"
				, "About Developer");
			}
		});
		mnAbout.add(abtdev);

		abtgame = new JMenuItem("About Game");
		abtgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				put_message("Instructions: \\nThere are 3 levels in this game, It gets gradually harder! \\nMatch all pairs to win!",
				 "Game Information");
			}
		});
		mnAbout.add(abtgame);

		exit = new JMenuItem("Exit");
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
				System.exit(0);
			}
		});
		mnExit.add(exit);
		
		bar.add(mnGame);
		bar.add(mnAbout);
		bar.add(mnExit);

		this.setJMenuBar(bar);
		

		if(level.equals("one"))
		{
			labellvl = new JLabel("LEVEL 1");
			tries = 18;
		}
		else if(level.equals("two"))
		{
			labellvl = new JLabel("LEVEL 2");
			tries = 15;
		}
		else if(level.equals("three"))
		{
			labellvl = new JLabel("LEVEL 3");
			tries = 12;
		}
		else
		{
			labellvl = new JLabel("LEVEL ?");
			tries = 0;
		}
		labellvl.setBounds(80,20,300,65);
		labellvl.setForeground(Color.WHITE);
		labellvl.setFont(new Font(null,Font.ITALIC,40));

		pnl_top.add(labellvl);

		labelmove = new JLabel("TRIES LEFT: "+tries);		
		labelmove.setBounds(300,20,300,65);
		labelmove.setForeground(Color.WHITE);
		labelmove.setFont(new Font(null,Font.ITALIC,40));

		pnl_top.add(labelmove);

		loadCards(cardIcons, level);

		defaultıcon = new ImageIcon(getClass().getResource("/" + level + "assets/no_image.png"));

		for (int i = 0; i < 16; i++) {
			btndef = new JButton();
            btndef.setIcon(defaultıcon);
            btndef.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e){
				if(tries==0) {
					put_message("You Lost Try Again!", "You Lost");
					if(level.equals("one"))//buraya yenilince ve kazanınca isim girdisi alıp score basan kısım ekle !!!!!!!!!!
						create_new_game("one");//
					else if(level.equals("two"))
						create_new_game("one");
					else if(level.equals("three"))
						create_new_game("one");
				}
				JButton clicked = (JButton) e.getSource();
				int clickedindex = buttons.indexOf(clicked);
				//System.out.println(clickedindex);
				if(firstselectedbtn == null){
					firstselectedbtn = clicked;
					firstbtnindex = clickedindex;
					firstselectedbtn.setIcon(cardIcons.get(firstbtnindex));
					tries--;
				}
				else if(secondselectedbtn == null && clicked != firstselectedbtn){
					secondselectedbtn = clicked;
					secondbtnindex = clickedindex;
					secondselectedbtn.setIcon(cardIcons.get(secondbtnindex));
					tries--;
					if(matches()){ 
						matches++;
						tries+=2;
						buttons.get(firstbtnindex).setEnabled(false);;//duzelt
						buttons.get(secondbtnindex).setEnabled(false);;//duzelt
						firstselectedbtn = null;
						secondselectedbtn = null;
					}
					else{
						penalty++;
						Timer timer = new Timer(1000, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e){
								firstselectedbtn.setIcon(defaultıcon);
								secondselectedbtn.setIcon(defaultıcon);
								firstselectedbtn = null;
								secondselectedbtn = null;

								if(level.equals("three")){
									shuffleThread = new Thread(new ShuffleCards());
									shuffleThread.start();
								}
							}
						});
						timer.setRepeats(false);
						timer.start();
						}
					}
					labelmove.setText("TRIES LEFT: " + tries);
					if(checkForWin())
					{
						if(level.equals("one"))
							create_new_game("two");
						else if(level.equals("two"))
							create_new_game("three");
						else
							dispose();// duzelt;
					}
				}
			});
			this.add(btndef);
            buttons.add(btndef);
            pnl_lvlpanel.add(btndef);
		}
		this.add(pnl_top,BorderLayout.NORTH);
		this.add(pnl_lvlpanel,BorderLayout.CENTER);
		this.setVisible(true);
		
		shuffleThread = new Thread(new ShuffleCards());
		shuffleThread.start();
	}
	
	public void create_new_game(String level){
		this.dispose();
		new Level(level);
	}

	
	public void put_message(String message,String title){
		JOptionPane.showMessageDialog(pnl_lvlpanel, message, title, JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void set_score(String message, String title, int score) {
		String username;
		username = JOptionPane.showInputDialog(pnl_lvlpanel,message,title,JOptionPane.INFORMATION_MESSAGE);
		if (username != null && !username.isEmpty()) {
			saveScore(username, score);
		}
	}
	
	private void saveScore(String name, int score) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("./src/card_memory_game/highscores.txt", true))) {
			writer.write(name + ": " + score);
			writer.newLine();
		} catch (Exception e) {
			System.out.println("Error writing scores from file");
		}
	}

	
	public void loadCards(List<ImageIcon> cards, String level){
		ImageIcon cardIcon;
		for(int i = 0; i < 8; i++)
		{
			cardIcon = new ImageIcon(getClass().getResource("/" + level + "assets/" + i + ".png"));
			cards.add(cardIcon);
			cards.add(cardIcon);
		}
	}
	
	public void show_scores() {
		List<String> scores = new ArrayList<>(10);
		String result = "";
		try (BufferedReader reader = new BufferedReader(new FileReader("./src/card_memory_game/highscores.txt"))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            scores.add(0,line);
	        }
	    } catch (Exception e) {
	        System.out.println("Error reading scores from file");
	    }
		if(scores.size() < 10) {
			for(int i = 0; i < scores.size(); i++) {
				result += scores.get(i) + "\n";
			}
		}
		else {
			for(int i = 0; i < 10; i++) {
				result += scores.get(i) + "\n";
			}
		}
		JOptionPane.showMessageDialog(pnl_lvlpanel,result,"highscores", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
	public boolean matches(){
		return cardIcons.get(firstbtnindex).equals(cardIcons.get(secondbtnindex));
	}
	
	public int calculatePoints(String name, int matches, int penalty) {
		int score = 0;
		if(name.equals("one")){
			score += (5*matches);
			score -= (1*penalty);
			return score;
		}
		if(name.equals("two")){
			score += (4*matches);
			score -= (2*penalty);
			return score;
		}
		if(name.equals("three")){
			score += (3*matches);
			score -= (3*penalty);
			return score;
		}
		
		return score;
	}
	
	private boolean checkForWin() {
        boolean allDisabled = true;
        int score;
        for (JButton button : buttons) {
            if (button.isEnabled()) {
                allDisabled = false;
                break;
            }
        }

        if (allDisabled) {
        	score = calculatePoints(level, matches, penalty);
            put_message("You win!! \nYour Score: "+ score, "You Win");
            set_score("Your score: "+ score +"\nPlease Write Your Name: ", "High Score", score);
            return true;
        }
        else {
        	return false;
        }
    }
	
	public static void checkselected(int selected) throws IllegalArgumentException {
		 if(!(selected == 1 || selected == 2 || selected == 3)) {
			 throw new IllegalArgumentException("Illegal argument");
		 }
	}
	
	class ShuffleCards implements Runnable{
		@Override
		public void run() {
				Collections.shuffle(cardIcons);
		}
	}	
}
