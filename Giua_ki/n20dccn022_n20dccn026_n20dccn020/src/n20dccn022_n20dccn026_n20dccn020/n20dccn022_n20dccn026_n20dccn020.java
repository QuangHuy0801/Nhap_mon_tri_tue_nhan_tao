//team : n20dccn022 VO QUANG HUY -- n20dccn026 VAN TO HUU -- n20dccn020 VU HUY HUNG



package n20dccn022_n20dccn026_n20dccn020;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class n20dccn022_n20dccn026_n20dccn020 extends JFrame implements ActionListener {
	
		//dùng để lưu bước tiếp theo của  lượt AI
		public class MOVE {
		    private int key1;
		    private int key2;
		 
		   public void setKey1(int key1) {
			this.key1 = key1;
		}
		   public void setKey2(int key2) {
			this.key2 = key2;
		}
		   public int getKey1() {
			return key1;
		}
		   public int getKey2() {
			return key2;
		}
		  
		}
		
		//điểm số đánh giá
		public class SCORES {
			private int O=10;
			private int X=-10;
			private int tie=0;
			public void setO(int o) {
				O = o;
			}
			public void setTie(int tie) {
				this.tie = tie;
			}
			public void setX(int x) {
				X = x;
			}
			public int getO() {
				return O;
			}
			public int getTie() {
				return tie;
			}
			public int getX() {
				return X;
			}
		}
	
	
	
		SCORES scores=new SCORES();
		Color background_cl = Color.white;
		Color x_cl = Color.red;
		Color y_cl = Color.blue;
		int column = 3, row = 3, count = 0;
		String ai="O";
		String human="X";
		int infinity=2147483647;
		String tick[][] = new String [column ][row];
		int Size = 0;
		Container cn;
		JPanel pn, pn2;
		JLabel lb;
		JButton newGame_bt, exit_bt;
		private JButton b[][] = new JButton[column ][row ];
		public n20dccn022_n20dccn026_n20dccn020(String s) {
			super(s);
			cn =this.getContentPane();
			pn = new JPanel();
			pn.setLayout(new GridLayout(column,row));
			for (int i = 0; i < column ; i++)
				for (int j = 0; j <row ; j++) {
					b[i][j] = new JButton(" ");
					b[i][j].setActionCommand(i + " " + j);
					b[i][j].setBackground(background_cl);
					b[i][j].addActionListener(this);
					tick[i][j] ="";
				}
			for (int i = 0; i < column; i++)
				for (int j = 0; j < row; j++)
					pn.add(b[i][j]);
			lb = new JLabel("YOUR TURN");
			newGame_bt = new JButton("New Game");
			exit_bt = new JButton("Exit");
			newGame_bt.addActionListener(this);
			exit_bt.addActionListener(this);
			exit_bt.setForeground(x_cl);
			cn.add(pn);
			pn2 = new JPanel();
			pn2.setLayout(new FlowLayout());
			pn2.add(lb);
			pn2.add(newGame_bt);
			pn2.add(exit_bt);
			cn.add(pn2,"North");
			this.setVisible(true);
			this.setSize(500, 400);
			this.setLocationRelativeTo(null);
			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		}
		
		
		//kiểm tra 3 có phải đều là x hết hoặc o hết không
		boolean equals3(String a,String b,String c) {
			  return a == b && b == c && a != "";
			}

		
		// check điều kiện thắng và trả về x nếu x thắng hoặc o nếu o thắng hoặc tie nếu hòa nhau
		public String checkWinner() {
			String winner = null;

			  // kiểm tra hàng ngang
			  for (int i = 0; i < 3; i++) {
			    if (equals3(tick[i][0], tick[i][1], tick[i][2])) {
			      winner = tick[i][0];
			    }
			  }

			  // kiểm tra cột dọc
			  for (int i = 0; i < 3; i++) {
			    if (equals3(tick[0][i], tick[1][i], tick[2][i])) {
			      winner = tick[0][i];
			    }
			  }

			  // kiểm tra đường chéo
			  if (equals3(tick[0][0], tick[1][1], tick[2][2])) {
			    winner = tick[0][0];
			  }
			  if (equals3(tick[2][0], tick[1][1], tick[0][2])) {
			    winner = tick[2][0];
			  }

			  int openSpots = 0;
			  for (int i = 0; i < 3; i++) {
			    for (int j = 0; j < 3; j++) {
			      if (tick[i][j] == "") {
			        openSpots++;
			      }
			    }
			  }

			  if (winner == null && openSpots == 0) {
			    return "tie";
			  } else {
			    return winner;
			  }
			}
		//tìm và đánh lượt đánh tốt nhất cho AI
		public void bestMoveAi() {
			int alpha=-10,beta=10;
			  int bestScore = -infinity;
			  MOVE move=new MOVE();
			  for (int i = 0; i < 3; i++) {
			    for (int j = 0; j < 3; j++) {
			      // Is the spot available?
			      if (tick[i][j] == "") {
			    	  tick[i][j] = ai;
			        int score = minimax(tick,2, alpha,beta, false);
			        tick[i][j] = "";
			        if (score > bestScore) {
			          bestScore = score;
			          move.setKey1(i);
			          move.setKey2(j);
			        }
			      }
			      alpha = Math.max(alpha, bestScore);
		          if(beta<=alpha)break;
			    }
			  }
			  tick[move.getKey1()][move.getKey2()] =ai;
			count ++;
			b[move.getKey1()][move.getKey2()].setBackground(Color.GRAY);
			  b[move.getKey1()][move.getKey2()].setText(ai);
			  b[move.getKey1()][move.getKey2()].setForeground(y_cl);
			  lb.setText("YOUR TURN");
			}
		
		//hàm minimax _ cắt tỉa alphabeta
		int minimax(String tick[][], int depth,int alpha,int beta, Boolean isMaximizing) {
			  String result = checkWinner();
			  if (result != null) {
			    if(result==ai)
			    	return scores.getO();
			    else if(result==human)
			    	return scores.getX();
			    else return scores.getTie();	
			  }

			  if (isMaximizing) {
			    int bestScore = -infinity;
			    boolean check=false;
			    for (int i = 0; i < 3; i++) {
			      for (int  j = 0; j < 3; j++) {
			        // kiểm tra ô đã được đánh chưa, nếu rồi thì bỏ qua
			        if (tick[i][j] == "") {
			        	tick[i][j] = ai;
			          int score = minimax(tick, depth + 1,alpha,beta, false);
			          tick[i][j] = "";
			          bestScore = Math.max(score, bestScore);
			        }
			        alpha = Math.max(alpha, bestScore);
			          if(beta<=alpha) {check=true;
		        	  break;}
			      }
			      if(check==true)break;
			    }
			    return bestScore;
			  } else {
			    int bestScore = infinity;
			    boolean check=false;
			    for (int i = 0; i < 3; i++) {
			      for (int j = 0; j < 3; j++) {
			        if (tick[i][j] == "") {
			        tick[i][j] = human;
			          int score = minimax(tick, depth + 1,alpha,beta, true);
			          tick[i][j] = "";
			          bestScore = Math.min(score, bestScore);
			        }
			        beta = Math.min(beta, bestScore);
			          if(beta<=alpha) {
			        	  check=true;
			        	  break;
			          }
			          
			      }
			      if(check==true)break;
			    }
			    return bestScore;
			  }
			}
		// lượt đánh của người
		public void addPointHuman(int i, int j) {
			b[i][j].setText(human);
			b[i][j].setForeground(x_cl);
			tick[i][j] =human;
			count++;
			b[i][j].setBackground(Color.GRAY);
			lb.setText("AI TURN");
		}
		
		
		
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand() == "New Game") {
				new n20dccn022_n20dccn026_n20dccn020("GAME CARO n20dccn022_n20dccn026_n20dccn020");
				this.dispose();
			}
			else
			if (e.getActionCommand() == "Exit") {
				System.exit(0);;
			}
			else {
				String s = e.getActionCommand();
				int k = s.indexOf(32);
				int i = Integer.parseInt(s.substring(0, k));
				int j = Integer.parseInt(s.substring(k + 1, s.length()));
				if(tick[i][j]=="" ) {
				addPointHuman(i, j);
				String winner =checkWinner();
				if (winner!=null) {
				lb.setBackground(Color.MAGENTA);
				if(winner=="tie")
					lb.setText( "TIE");
				else
					lb.setText("YOU WIN");
				for ( int u = 0; u < column; u++)
					for ( int v = 0; v < row; v++) 
						b[u][v].setEnabled(false);
						newGame_bt.setBackground(Color.YELLOW);
					}
				bestMoveAi();}
				String winner =checkWinner();
				if (winner!=null) {
					lb.setBackground(Color.MAGENTA);
					if(winner=="O")
						lb.setText( "YOU LOST");
					else lb.setText("TIE");
					for (int u = 0; u < column; u++)
						for (int v = 0; v < row; v++) 
							b[u][v].setEnabled(false);
					newGame_bt.setBackground(Color.YELLOW);
				}
				}
			}
				
		public static void main(String[] args) {
			new n20dccn022_n20dccn026_n20dccn020("GAME CARO n20dccn022_n20dccn026_n20dccn020");
		}
}
