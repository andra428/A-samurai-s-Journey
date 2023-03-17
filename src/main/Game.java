package main;
import javax.swing.JFrame;
public class Game {
    public static void main(String[] args){
        //window
        JFrame window = new JFrame("A Ronin's Journey");
        window.setContentPane(new GamePanel());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.pack();
        window.setVisible(true);
    }
}
