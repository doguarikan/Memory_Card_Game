package card_memory_game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class Menu extends JFrame{
	private JPanel pnl_mainpanel;
	private JLabel lbl_name;
	private JLabel lbl_background;
	private JButton btn_startgame;
	private JButton btn_selectlvl;
	private JButton btn_intructions;
	private JButton btn_exitgame;
	
	public Menu() {
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBounds(600, 200, 800, 600);

		pnl_mainpanel = new JPanel();
		pnl_mainpanel.setBackground(Color.WHITE);
		pnl_mainpanel.setBorder(new EmptyBorder(5, 5, 5, 5));

		this.setContentPane(pnl_mainpanel);
		pnl_mainpanel.setLayout(null);

		lbl_name = new JLabel("Memory Card Game");
		lbl_name.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 35));
		lbl_name.setBounds(236,75,312,68);
		pnl_mainpanel.add(lbl_name);

		btn_startgame = new JButton("Start Game");
		btn_startgame.setBackground(Color.WHITE);
		btn_startgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
				new LevelOne();
			}
		});
		btn_startgame.setFont(new Font("Tahoma",Font.ITALIC,14));
		btn_startgame.setBounds(332, 197, 120, 30);
		pnl_mainpanel.add(btn_startgame);

		btn_selectlvl = new JButton("Select Level");
		btn_selectlvl.setBackground(Color.WHITE);
		btn_selectlvl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				String selectedlevel;
				try {
					selectedlevel = (String) JOptionPane.showInputDialog(pnl_mainpanel,"Please select the level 1,2 or 3");
					Level.checkselected(Integer.parseInt(selectedlevel));
					if(selectedlevel.equals("1")){
						dispose();
						new LevelOne();
					}
					else if(selectedlevel.equals("2")){
						dispose();
						new LevelTwo();
					}
					else{
						dispose();
						new LevelThree();
					}
				}catch(IllegalArgumentException exc) {
					JOptionPane.showMessageDialog(pnl_mainpanel, "Invalid input please try again!");
				}
			}
		});
		btn_selectlvl.setFont(new Font("Tahoma",Font.ITALIC,14));
		btn_selectlvl.setBounds(332, 259, 120, 30);
		pnl_mainpanel.add(btn_selectlvl);

		btn_intructions = new JButton("Instrictions");
		btn_intructions.setBackground(Color.WHITE);
		btn_intructions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(pnl_mainpanel,"Instructions: \nThere are 3 levels in this game, It gets gradually harder! \nMatch all pairs to win!",
						"Instructions",JOptionPane.INFORMATION_MESSAGE);
			
			}
		});
		btn_intructions.setFont(new Font("Tahoma",Font.ITALIC,14));
		btn_intructions.setBounds(332, 320, 120, 30);
		pnl_mainpanel.add(btn_intructions);

		btn_exitgame = new JButton("Exit");
		btn_exitgame.setBackground(Color.WHITE);
		btn_exitgame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e){
				dispose();
				System.exit(0);
			}
		});
		btn_exitgame.setFont(new Font("Tahoma",Font.ITALIC,14));
		btn_exitgame.setBounds(332, 383, 120, 30);
		pnl_mainpanel.add(btn_exitgame);

		lbl_background = new JLabel("");
		lbl_background.setIcon(new ImageIcon(getClass().getResource("background.jpg")));
		lbl_background.setBounds(0,0,800,600);
		pnl_mainpanel.add(lbl_background);
		this.setVisible(true);
	}

}
