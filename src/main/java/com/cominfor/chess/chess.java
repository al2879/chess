package com.cominfor.chess;

import com.sun.javafx.collections.transformation.SortedList;
import com.sun.jmx.remote.internal.ArrayQueue;
import sun.java2d.loops.FillRect;

import javax.swing.*;
import java.awt.*;
import java.awt.List;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Admin on 17.12.13.
 */
public class chess {

    private static boolean[][] board;
    private static final int BOARD_SIZE = 8;
    private static final int RUN_LENGTH = BOARD_SIZE*BOARD_SIZE;
    private static int runp = 0;
    public cell start_cell;
    public long moves = 0;
    public static java.util.List<cell> result = new ArrayList<cell>();

    public class cell implements Comparable<cell>{
        private int row;
        private int column;
        private int power;

        public boolean get(){
            return board[column][row];
        }
        public void set_true(){
            board[column][row] = true;
        }
        public void set_false(){
            board[column][row] = false;
        }
        public int get_power(){
            return this.power;
        }
        public cell(int column, int row) {
            this.row = row;
            this.column = column;
            this.power = 0;
            if (check_cell(column, row)) {
                if (check_cell(column - 1, row - 2)) this.power++;
                if (check_cell(column + 1, row - 2)) this.power++;
                if (check_cell(column + 2, row - 1)) this.power++;
                if (check_cell(column + 2, row + 1)) this.power++;
                if (check_cell(column + 1, row + 2)) this.power++;
                if (check_cell(column - 1, row + 2)) this.power++;
                if (check_cell(column - 2, row + 1)) this.power++;
                if (check_cell(column - 2, row - 1)) this.power++;
            } else this.power = 100;
        }
        public boolean check_cell(int column, int row){
            if(column < 0      || column > (BOARD_SIZE-1) ||
                    row < 0    || row > (BOARD_SIZE-1))
                return false;
            return board[column][row];
        }

        @Override
        public int compareTo(cell o) {
            int retval = 0;
            if(power <  o.power) retval = -1;
            if(power == o.power) retval =  0;
            if(power >  o.power) retval =  1;
            return retval;
        }
    }

    public boolean check_cell(cell q){
        if(q.column < 0 || q.column > (BOARD_SIZE-1) ||
           q.row < 0    || q.row > (BOARD_SIZE-1))
                return false;
        return board[q.column][q.row];
    }

    public void prdebug(int n, cell c){
        String outs="(%d,%d)%2d:p(%d)";
        for(int i=0; i<n; i++) outs += "+";
        outs += "\n";
        System.out.format(outs,c.column,c.row,n,c.get_power());
    }

    public boolean gorun(cell nc){

        cell [] mvs = {
        new cell(nc.column - 1, nc.row - 2),
        new cell(nc.column + 1, nc.row - 2),
        new cell(nc.column + 2, nc.row - 1),
        new cell(nc.column + 2, nc.row + 1),
        new cell(nc.column + 1, nc.row + 2),
        new cell(nc.column - 1, nc.row + 2),
        new cell(nc.column - 2, nc.row + 1),
        new cell(nc.column - 2, nc.row - 1)
        };
        java.util.List<cell> newmvs = Arrays.asList(mvs);
        java.util.Collections.sort(newmvs);
        /*System.out.println("--------------------------------------------------");
        for(cell i: newmvs){
            System.out.println(String.format("%d:%d:%d", i.power, i.column, i.row ));
        }*/
        runp++;
//        prdebug(runp, nc);
        moves++;
        nc.set_false();
        if(runp >= RUN_LENGTH){
            System.out.println("Solution Found!");
            result.add(nc);
            return true;
        };
        for(cell i: newmvs)
            if(check_cell(i)){
                if(gorun(i)){
                    result.add(nc);
                    return true;
                };
            }
        nc.set_true();
        runp--;
        return false;
    }

    public chess() {
        board = new boolean[BOARD_SIZE][BOARD_SIZE];
        for (int i=0; i<BOARD_SIZE; i++)
            for (int j=0; j<BOARD_SIZE; j++) board[i][j]= true;
        start_cell = new cell(0,0);
    }

    public static void main(String[] args){
        System.out.println("Staring knight jumps!!");
        Date start_time = Calendar.getInstance().getTime();
        chess mc = new chess();

        if(!mc.gorun( mc.start_cell)) System.out.println("Solution Not Found!");
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
        int j=1;
        Collections.reverse(result);
        for(cell i: result){
            System.out.println(String.format("%d:%d,%d",j++, i.column, i.row));
        }
        /*final Frame f = new Frame("Draw string")  {
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
                int keycode = e.getKeyCode();
                System.out.println("kc:"+keycode);
                if(keycode== 100) f.dispose();
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
        f.setSize(550, 550)*/;
    }
}
