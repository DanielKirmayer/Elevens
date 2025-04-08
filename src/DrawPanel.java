import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Point;
import java.util.ArrayList;
import java.awt.Font;
import java.util.Objects;

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
        g.drawString("Scroll Click To Get Rid Of Chosen Cards", 10, 400);

        g.drawRect((int)button.getX(), (int)button.getY(), (int)button.getWidth(), (int)button.getHeight());

        if(Card.DECK.isEmpty())
            g.drawString("YOU WIN!", 20, 200);


        if(!canGameContinue) {
            g.setFont(new Font("Courier New",Font.BOLD,15));
            g.drawString("You Lost!", 350, 200);
            g.drawString("Get New Cards!", 350, 230);


        }

    }

    public void mousePressed(MouseEvent e) {

        Point clicked = e.getPoint();

        // left click
        if (e.getButton() == 1) {
            // if "clicked" is inside the button rectangle
            // aka did you click the button?
            if (button.contains(clicked)) {
                hand = Card.buildHand();
                canGameContinue = true;
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
                    hand.get(i).flipHighlight();
                }
            }
        }

        ArrayList<Card> tradeIns = new ArrayList<Card>();
        ArrayList<Integer> indexes = new ArrayList<Integer>();


        if (e.getButton() == 2) {
            for (int i = 0; i < hand.size(); i++) {
                    if(hand.get(i).getHighlight()){
                        tradeIns.add(hand.get(i));
                        indexes.add(i);
                    }
            }

            if(tradeIns.size() == 2) {
                if(canGoAway(tradeIns.get(0),tradeIns.get(1),null)) {

                    int rand = (int) (Math.random() * Card.DECK.size());
                    hand.set(indexes.getFirst(), Card.DECK.remove(rand));
                    rand = (int) (Math.random() * Card.DECK.size());
                    hand.set(indexes.get(1),Card.DECK.remove(rand));
                }
            }
            if(tradeIns.size() == 3) {
                if(canGoAway(tradeIns.get(0),tradeIns.get(1),tradeIns.get(2))) {

                    int rand = (int) (Math.random() * Card.DECK.size());
                    hand.set(indexes.getFirst(), Card.DECK.remove(rand));
                    rand = (int) (Math.random() * Card.DECK.size());
                    hand.set(indexes.get(1),Card.DECK.remove(rand));
                    rand = (int) (Math.random() * Card.DECK.size());
                    hand.set(indexes.get(2),Card.DECK.remove(rand));
                }
            }




        }


        boolean continuing = isContinuing();


        if(!continuing)
            canGameContinue = false;




    }

    private boolean isContinuing() {
        boolean continuing = false;
        boolean hasJack = false;
        boolean hasQueen = false;
        boolean hasKing = false;
        for (int i = 0; i < hand.size(); i++) {
            for (int j = 0; j < hand.size(); j++) {
                int valToMakeEleven = hand.get(i).getNumValue();
                valToMakeEleven += hand.get(j).getNumValue();
                if(valToMakeEleven == 11)
                    continuing = true;
            }
            if(Objects.equals(hand.get(i).getValue(), "J"))
                hasJack = true;
            if(Objects.equals(hand.get(i).getValue(), "Q"))
                hasQueen = true;
            if(Objects.equals(hand.get(i).getValue(), "K"))
                hasKing = true;




        }
        if(hasJack && hasQueen && hasKing)
            continuing = true;
        return continuing;
    }

    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }
    public void mouseExited(MouseEvent e) { }
    public void mouseClicked(MouseEvent e) { }

    public static boolean canGoAway(Card c1,Card c2,Card c3){
        if(c1.getNumValue() + c2.getNumValue() == 11)
            return true;
        if(c3 == null)
            return false;
        String f = c1.getValue()+c2.getValue()+c3.getValue();

        if(f.contains("J") && f.contains("Q") && f.contains("K"))
            return true;

        return false;

    }
}