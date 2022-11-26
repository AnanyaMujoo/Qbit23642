package display;

import geometry.position.Vector;

public class Display extends Drawer {

    public static void main(String[] args) {

//        drawWindow(new Display(), "Display");

        Vector v = new Vector(1.0,0);
        v.rotate(90);
        Vector v2 = v.getRotated(90);
        System.out.println(v);
        System.out.println(v2);
    }

    @Override
    public void define() {

    }
}
