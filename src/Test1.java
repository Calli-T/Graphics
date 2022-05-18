import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.Comparator;


public class Test1 {

    public static void main(String[] arg) {
        Screen screen = new Screen("Screen");
    }

}

class Cod{
    public int x;
    public int y;

    Cod(int x, int y){
        this.x=x; this.y=y;
    }
}

class Screen extends JFrame {

    public Cartecian cartecian;

    Screen(String title) {
        super(title);
        setSize(417,440);//사이즈
        setDefaultCloseOperation(EXIT_ON_CLOSE); //닫을 때 끄는
        cartecian = new Cartecian();
        add(cartecian, BorderLayout.CENTER);
        this.setLocationRelativeTo(null);    //창 가운데 위치
        setVisible(true);
    }
}
class Cartecian extends JPanel {

    private Graphics2D screen;

    protected void paintComponent(Graphics g) {
        screen=(Graphics2D)g;
        screen.translate(200, 200); //원점의 위치

        drawXY();
        drawDottedLine();

        //MidpointLine(5, 3, 9, 6);
        //MidpointLine(9,6,5,3);
        //MidpointLine(1, 1, 1, 5);
        //MidpointLine(2, -1, 6, -1);
        MidpointLine(2, -1, 6, -7, false);
        MidpointLine(2, 1, 6, 7, false);
    }

    private void drawXY(){
        float[] bold = {1,0f}; //xy축 설정

        // 굵은 선의 xy축
        screen.setStroke(new BasicStroke(2,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,bold,0));
        screen.draw(new Line2D.Float(-200, 0, 200, 0));
        screen.draw(new Line2D.Float(0, -200, 0, 200));
    }
    private void drawDottedLine() {
        float[] right = {2,2f}; //점선 설정

        //점선
        screen.setStroke(new BasicStroke(1,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL,1,right,0));
        for (int i=-200; i<=200; i = i+20) {
            screen.draw(new Line2D.Float(-200, i, 200, i));     // x축과 평행선을 그린다.
        }
        for (int j=-200; j<=200; j = j+20) {
            screen.draw(new Line2D.Float(j, -200, j, 200));     // y축과 평행선을 그린다.
        }
    }

    public void drawDotEX(){ //예제
        screen.setColor(Color.RED);

        for (int x=20, y=-20, radius=5; x<200; x=x+20,y=y-20 ) {
            drawDot(x, y);
        }
    }
    public void drawDot(int x, int y){
        screen.setColor(Color.BLACK);
        screen.fillOval(x-10, y-10, 20, 20);
    }
    public void WritePixel(int x, int y){ // 해당 라이브러리의 y는 일반적인 값의 음수, 즉, window top에서 부터의 거리
        screen.setColor(Color.BLACK);
        screen.fillOval(x*20-10, -(y*20+10), 20, 20);
    }

    public void MidpointLine(int x0, int y0, int x1, int y1, boolean axisToggle)
    {
        if(x0 > x1){ // x축을 따라 감소하는 방향으로 가는 선일경우, 스왑
            int tempX = x0;
            x0=x1;
            x1 = tempX;
            int tempY = y0;
            y0=y1;
            y1 = tempY;
        }

        int dx = x1 - x0, dy = y1 - y0;
        double a = 0;
        if(dx != 0){ // dx가 0이 아니면 a값을 구해줌
            a = (float)dy/(float)dx;
        } else if(x1 == x0){ // x값이 같은경우, 수직선, 기울기 a는 정의되지않음
            int smallY=0, bigY=0;
            if(y0<y1){
                smallY = y0; bigY=y1;
            }
            else {
                smallY = y1; bigY=y0;
            }
            while(smallY <= bigY){
                WritePixel(x0, smallY);
                smallY++;
            }
            return;
        }

        if(Math.abs(a) < 1) {
            int toggle = 1;
            if(a < 0){
                toggle = -1;
                y0 = -y0; y1 = -y1;
                dy = -dy;
            }

            int incrE, incrNE, d, x, y;
            d = dy*2 - dx; // 시작 결정 변수에 x2를 하여 fraction이 방지된 형태의 초기화
            incrE = dy*2; // E점 채용시 결정변수 증가량에 x2를 하여 fraction 방지
            incrNE = (dy-dx)*2; // NE점 채용시 결정변수 증가량에 x2를 하여 fraction 방지
            x = x0;
            y = y0;
            if(!axisToggle) {
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
            } else{
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
        } else if(Math.abs(a) > 1){
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