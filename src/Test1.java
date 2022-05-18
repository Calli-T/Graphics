import java.awt.*;

import javax.swing.*;

import java.awt.geom.*;




public class Test1 {

    public static void main(String[] arg) {

        Screen screen = new Screen("Screen");

    }

}



class Screen extends JFrame {

    Screen(String title) {

        super(title);
        setSize(1017,1040);//사이즈
        setDefaultCloseOperation(EXIT_ON_CLOSE); //닫을 때 끄는
        Cartecian cartecian = new Cartecian();
        add(cartecian, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);    //창 가운데 위치
        setVisible(true);

    }

}


class Cartecian extends JPanel {

    private Graphics2D screen;

    protected void paintComponent(Graphics g) {
        screen=(Graphics2D)g;
        screen.translate(500, 500); //원점의 위치

        drawXY();
        drawDottedLine();
        //drawDot(20, 20);
    }

    private void drawXY(){
        float[] bold = {1,0f}; //xy축 설정

        // 굵은 선의 xy축
        screen.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,bold,0));
        screen.draw(new Line2D.Float(-500, 0, 500, 0));
        screen.draw(new Line2D.Float(0, -500, 0, 500));
    }
    private void drawDottedLine() {
        float[] right = {2,2f}; //점선 설정

        //점선
        screen.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,right,0));
        for (int i=-500; i<=500; i = i+20) {
            screen.draw(new Line2D.Float(-500, i, 500, i));     // x축과 평행선을 그린다.
        }
        for (int j=-500; j<=500; j = j+20) {
            screen.draw(new Line2D.Float(j, -500, j, 500));     // y축과 평행선을 그린다.
        }
    }

    public void drawDotEX(){ //예제
        screen.setColor(Color.RED);

        /*
        for ( int x=20, y=-20, radius=5; x<500; x=x+20,y=y-20 ) {   // 해당 라이브러리 기준 y가 일반적으로 생각하는 좌표계의 음수값
            screen.fillOval(x-radius, y-radius, radius*2, radius*2); // 앞의 2개는 점의 위치를 맞춰주기 위한것, 뒤의 2개는 지름을 규정
        }
        */
        for ( int x=20, y=-20, radius=5; x<500; x=x+20,y=y-20 ) {
            drawDot(x, y);
        }
    }
    public void drawDot(int x, int y){
        screen.setColor(Color.BLACK);
        screen.fillOval(x-5, y-5, 10, 10);
    }
}
/*
import java.awt.*;

import javax.swing.*;

import java.awt.geom.*;




public class Test1 {

    public static void main(String[] arg) {

        MyFrame f = new MyFrame("Main frame");

    }

}



class MyFrame extends JFrame {

    MyFrame(String title) {

        super(title);

        setSize(1000,1000);

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        makeUI();

        setVisible(true);

    }

    private void makeUI() {

        MyPanel p = new MyPanel();

        add(p, BorderLayout.CENTER);

    }

}


class MyPanel extends JPanel {

    protected void paintComponent(Graphics g) {


        Graphics2D g2=(Graphics2D)g;

        float dash0[] = {1,0f};
        float dash3[] = {3,3f};

        g2.translate(500, 500);                // 원점을 (300, 300)로 이동시킨다.


        g2.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,dash3,0));


        for (int i=-500; i<=500; i = i+20) {

            g2.draw(new Line2D.Float(-500, i, 500, i));     // x축과 평행선을 그린다.

        }
        for (int j=-500; j<=500; j = j+20) {

            g2.draw(new Line2D.Float(j, -500, j, 500));     // y축과 평행선을 그린다.


        }


        g2.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,dash0,0));


        g2.draw(new Line2D.Float(-500, 0, 500, 0));     // x축을 그린다.

        g2.draw(new Line2D.Float(0, -500, 0, 500));     // y축을 그린다.


        g2.setColor(Color.BLUE);


        for ( int x=20, y=20, radius=1; x<300; x=x+20,y=y+20,radius++ ) {

            g2.fillOval(x-radius, y-radius, radius*2, radius*2);

        }



    }

}
 */