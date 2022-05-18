import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Comparator;


public class Test1 {

    public static void main(String[] arg) {
        Screen screen = new Screen("Screen");
    }

}

class Cod {
    public int x;
    public int y;

    Cod(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Screen extends JFrame {

    public Cartecian cartecian;

    Screen(String title) {
        super(title);
        setSize(417, 440);//사이즈
        setDefaultCloseOperation(EXIT_ON_CLOSE); //닫을 때 끄는
        cartecian = new Cartecian();
        add(cartecian, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);    //창 가운데 위치
        setVisible(true);
    }
}

class Cartecian extends JPanel {

    private Graphics2D screen;
    private int transX, transY;

    protected void paintComponent(Graphics g) {
        screen = (Graphics2D) g;
        screen.translate(200, 200); //원점의 위치

        transX = 0; transY = 0;

        drawXY();
        drawDottedLine();

        // 기울기의 절대값이 1보다 작고 양수인 예제. Translation은(0, -1)만큼 적용되었음
        //setTranslation(0, -1);
        //MidpointLine(5, 3, 9, 6, false);

        //기울기의 절대값이 1보다 작고 음수인 예제. Translation은(3, 4)만큼 적용되었음
        //setTranslation(3, 4);
        //MidpointLine(-6, 3, -1, -1, false);

        // x0 < x1때의 처리 방식을 보여주는 예제, 들어온 순서를 바꾸어 주사의 방향을 유지한다
        //setTranslation(0, 1);
        //MidpointLine(9, 6, -5, 3, false);

        // 기울기가 무한한 예제, 예외처리, Translation은(-3, 1)만큼 적용되었음
        //setTranslation(-3, 1);
        //MidpointLine(1, 1, 1, 5, false);

        // 기울기가 0이고 (-4, 4)만큼 Translation하는 예제
        //setTranslation(-4, 4);
        //MidpointLine(-4, 4, 4, 4, false);

        // 기울기의 절대값이 1인 예제 2개
        /*
        setTranslation(3, 3);
        MidpointLine(1, 1, 3, 3, false);
        setTranslation(3, -1);
        MidpointLine(0, -1, 3, -4, false);
        */

        // 기울기의 절대값이 1보다 크고, 음수인 예제, Translation이후 UI에 의해 자동으로 clipping된다
        //setTranslation(7, -1);
        //MidpointLine(2, -1, 6, -7, false);
    }

    private void setTranslation(int x, int y){ // 원래의 선분을 검게 출력하고, 여기에 세팅된 대로 기존 선분을 Translation해서 붉은색으로 출력한다
        this.transX = x;
        this.transY = y;
    }

    private void drawXY() {
        float[] bold = {1, 0f}; //xy축 설정

        // 굵은 선의 xy축
        screen.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, bold, 0));
        screen.draw(new Line2D.Float(-200, 0, 200, 0));
        screen.draw(new Line2D.Float(0, -200, 0, 200));
    }

    private void drawDottedLine() {
        float[] right = {2, 2f}; //점선 설정

        //점선
        screen.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 1, right, 0));
        for (int i = -200; i <= 200; i = i + 20) {
            screen.draw(new Line2D.Float(-200, i, 200, i));     // x축과 평행선을 그린다.
        }
        for (int j = -200; j <= 200; j = j + 20) {
            screen.draw(new Line2D.Float(j, -200, j, 200));     // y축과 평행선을 그린다.
        }
    }

    public void drawDot(int x, int y) {
        screen.setColor(Color.BLACK);
        screen.fillOval(x - 10, y - 10, 20, 20);
    }

    public void WritePixel(int x, int y) { // 해당 라이브러리의 y는 일반적인 값의 음수, 즉, window top에서 부터의 거리
        screen.setColor(Color.BLACK);
        screen.fillOval(x * 20 - 10, -(y * 20 + 10), 20, 20);

        screen.setColor(Color.RED);
        screen.fillOval((x+transX) * 20 - 10, -((y+transY) * 20 + 10), 20, 20);
    }

    public void MidpointLine(int x0, int y0, int x1, int y1, boolean axisToggle) {
        if (x0 > x1) { // x축을 따라 감소하는 방향으로 가는 선일경우, 스왑
            int tempX = x0;
            x0 = x1;
            x1 = tempX;
            int tempY = y0;
            y0 = y1;
            y1 = tempY;
        }

        int dx = x1 - x0, dy = y1 - y0;
        double a = 0;
        if (dx != 0) { // dx가 0이 아니면 a값을 구해줌
            a = (float) dy / (float) dx;
        } else if (x1 == x0) { // x값이 같은경우, 수직선, 기울기 a는 정의되지않음
            int smallY = 0, bigY = 0;
            if (y0 < y1) {
                smallY = y0;
                bigY = y1;
            } else {
                smallY = y1;
                bigY = y0;
            }
            while (smallY <= bigY) {
                WritePixel(x0, smallY);
                smallY++;
            }
            return;
        }

        if (Math.abs(a) <= 1) {
            int toggle = 1;
            if (a < 0) { // 기울기가 음수라면 선분이 x축에 대칭인것처럼 결정짓고 다시 대칭시켜 구한다
                toggle = -1;
                y0 = -y0;
                y1 = -y1;
                dy = -dy;
            }

            int incrE, incrNE, d, x, y;
            d = dy * 2 - dx; // 시작 결정 변수에 x2를 하여 fraction이 방지된 형태의 초기화
            incrE = dy * 2; // E점 채용시 결정변수 증가량에 x2를 하여 fraction 방지
            incrNE = (dy - dx) * 2; // NE점 채용시 결정변수 증가량에 x2를 하여 fraction 방지
            x = x0;
            y = y0;
            if (!axisToggle) { // 기울기 1이상의 경우 x, y값을 바꾸어 y축에 따른 x값으로 d를 결정지었으므로, 출력 때 다시 축을 바꿔주는 작업을 수행한다
                WritePixel(x, y * toggle); // 시작하는 픽셀
                while (x < x1) { // x축을 따라 전진
                    if (d <= 0) { //E를 집었을 때
                        d += incrE;
                        x++;
                    } else { //NE를 집었을 때
                        d += incrNE;
                        x++;
                        y++;
                    }
                    WritePixel(x, y * toggle); /* The selected pixel closest to the line */
                }
            } else {
                WritePixel(y * toggle, x); // 시작하는 픽셀
                while (x < x1) { // x축을 따라 전진
                    if (d <= 0) { //E를 집었을 때
                        d += incrE;
                        x++;
                    } else { //NE를 집었을 때
                        d += incrNE;
                        x++;
                        y++;
                    }
                    WritePixel(y * toggle, x); /* The selected pixel closest to the line */
                }
            }
        } else if (Math.abs(a) > 1) {
            MidpointLine(y0, x0, y1, x1, true);
        }


    }
}






/*
// 어느 축을 따를것인지 이슈
0 < Math.abs(기울기) < 1의 경우 x축
Math.abs(기울기) > 1의 경우 y축
그 외의 경우는 상관 없음(편의상 x축사용)

 */
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