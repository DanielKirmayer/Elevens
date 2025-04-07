import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;

class DrawPanel extends JPanel implements MouseListener {

    private ArrayList<Card> hand;
    private boolean canGameContinue = true;

    // Rectangle object represents a rectangle.
    private Rectangle button;

    public DrawPanel() {
        button = new Rectangle(147, 100, 160, 26);
        this.addMouseListener(this);
        hand = Card.buildHand();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = 120;
        int y = 150;
        for (int i = 0; i < hand.size(); i++) {
            Card c = hand.get(i);
            if (c.getHighlight()) {
                // draw the box around the card
                g.drawRect(x, y, c.getImage().getWidth(), c.getImage().getHeight());
            }

            // establish the location of the rectangle "hitbox"
            c.setRectangleLocation(x, y);

            g.drawImage(c.getImage(), x, y, null);
            x = x + c.getImage().getWidth() + 10;
            if ((i+1) % 3 == 0)
            {
                y += 70;
                x = 120;
            }
        }

        // drawing the bottom button
        // with my favorite font (not comic sans)
        g.setFont(new Font("Courier New", Font.BOLD, 20));
        g.drawString("GET NEW CARDS", 150, 120);
        g.drawString("Cards Left In Deck : "+Card.DECK.size(), 200, 50);
        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());


        if(!canGameContinue)
            g.drawString("You Lost! Must get new cards!", 100, 400);

    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        // left click
        if (e.getButton() == 1) {
            // if "clicked" is inside the button rectangle
            // aka did you click the button?
            if (button.contains(clicked)) {
                hand = Card.buildHand();
            }

            // go through each card
            // check if any were clicked
            // if it was clicked flip the card

            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    hand.get(i).flipCard();
                }
            }
        }


        // right cliok
        if (e.getButton() == 3) {
            for (int i = 0; i < hand.size(); i++) {
                Rectangle box = hand.get(i).getCardBox();
                if (box.contains(clicked)) {
                    if(hand.get(i).getHighlight()){
                        int rand = (int)(Math.random() * Card.DECK.size());
                        hand.set(i,Card.DECK.remove(rand));

                    }


                    hand.get(i).flipHighlight();
                }
            }
        }
        boolean continuing = false;
        for (int i = 0; i < hand.size(); i++) {
            int valToMakeEleven = hand.get(i).getNumValue();
            for (int j = 0; j < hand.size(); j++) {
                valToMakeEleven += hand.get(j).getNumValue();
                if(valToMakeEleven == 11)
                    continuing = true;
            }
        }
        if(!continuing)
            canGameContinue = false;




    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }
}