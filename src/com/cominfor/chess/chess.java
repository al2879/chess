package com.cominfor.chess;

import sun.java2d.loops.FillRect;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Admin on 17.12.13.
 */
public class chess {

    private static boolean[][] board;
    private static long[] run_stat;
    private static final int BOARD_SIZE = 4;
    private static final int RUN_LENGTH = BOARD_SIZE*BOARD_SIZE;
    private static int runp = 0;
    private static int max_runp = 0;
    public cell start_cell;
    /*Integer.MAX_VALUE =  2147483647
    Integer.MIN_VALUE = -2147483648

    Long.MAX_VALUE =  9223372036854775807
    Long.MIN_VALUE = -9223372036854775808*/
    public long moves = 0;
    public long moves_multiplyer = 0;

    public class cell{
        private int row;
        private int column;

        public boolean get(){
            return board[column][row];
        }
        public void set_true(){
            board[column][row] = true;
        }
        public void set_false(){
            board[column][row] = false;
        }
        public cell(int columnv, int rowv) {
            row = rowv;
            column = columnv;
        }
    }

    public boolean check_cell(cell q){
        if(q.column < 0 || q.column > (BOARD_SIZE-1) ||
           q.row < 0    || q.row > (BOARD_SIZE-1))
                return false;
        return board[q.column][q.row];
    }

    public void prdebug(int n, cell c){
        String outs="(%d,%d)%2d:";
        for(int i=0; i<n; i++) outs += "+";
        outs += "\n";
        System.out.format(outs,c.column,c.row,n);
    }

    public boolean gorun(cell nc){
        cell[] mvs = {  new cell(nc.column-1,nc.row-2),
                        new cell(nc.column+1,nc.row-2),
                        new cell(nc.column+2,nc.row-1),
                        new cell(nc.column+2,nc.row+1),
                        new cell(nc.column+1,nc.row+2),
                        new cell(nc.column-1,nc.row+2),
                        new cell(nc.column-2,nc.row+1),
                        new cell(nc.column-2,nc.row-1)
        };
        runp++;
        moves++;
        run_stat[runp-1]++;
        if(run_stat[runp-1] == 2){
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.format("Down Max run:%2d, Timestamp:%s,Moves:%d\n",runp,timeStamp,moves);
        }
        nc.set_false();
        if(runp >= RUN_LENGTH){
            System.out.println("("+nc.column+","+nc.row+")");
            return true;
        };
        if(runp > max_runp){
            max_runp++;
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
            System.out.format("Max run:%2d, Timestamp:%s,Moves:%d\n",max_runp,timeStamp,moves);
        }
        for(int i=0; i<mvs.length; i++)
            if(check_cell(mvs[i])){
                if(gorun(mvs[i])){
                    System.out.println("("+nc.column+","+nc.row+")");
                    return true;
                };
            }
        nc.set_true();
        runp--;
        return false;
    }

    public chess() {
        board = new boolean[BOARD_SIZE][BOARD_SIZE];
        run_stat = new long[RUN_LENGTH];
        for (int i=0; i<RUN_LENGTH; i++) run_stat[i]=0;
        for (int i=0; i<BOARD_SIZE; i++)
            for (int j=0; j<BOARD_SIZE; j++) board[i][j]= true;
        start_cell = new cell(0,0);
    }

    public static void main(String[] args){
        System.out.println("Staring knight jumps!!");
        Date start_time = Calendar.getInstance().getTime();
        chess mc = new chess();

        if(mc.gorun( mc.start_cell)) System.out.println("Solution Found!");
        else                         System.out.println("Solution Not Found!");
        System.out.println("Finishing knight jumps!!");
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime());
        System.out.format("Timestamp:%s,Moves:%d\n",timeStamp,mc.moves);
        Date finish_time = Calendar.getInstance().getTime();
        long  diff = finish_time.getTime()-start_time.getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        System.out.println("Time in milliseconds: " + diff + " milliseconds.");
        System.out.println("Time in seconds: " + diffSeconds + " seconds.");
        System.out.println("Time in minutes: " + diffMinutes + " minutes.");
        System.out.println("Time in hours: " + diffHours + " hours.");
        final Frame f = new Frame("Draw string")  {
            public void paint (Graphics g) {
                Graphics2D g2 = (Graphics2D) g;

                Font font = new Font("Arial", Font.BOLD, 48);
                g2.setFont(font);
                g2.setColor(Color.RED);
                g2.drawString("Simple drawString", 40, 80);

                g2.setColor(new Color(10, 11,200));
                Rectangle test = new Rectangle(0,200,100,100);
                g2.fill(test);
            }
        };
        f.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                /*int keycode = e.getKeyCode();
                System.out.println("kc:"+keycode);
                if(keycode== 100) f.dispose();*/
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keycode = e.getKeyCode();
//                System.out.println("kc:"+keycode);
                if(keycode== 27) f.dispose();
            }

            @Override
            public void keyReleased(KeyEvent e) {
//                f.dispose();
            }
        });
        f.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                f.dispose();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        f.setVisible(true);
        f.setSize(550, 550);
    }
}
