package mechanics.GameState;

import java.util.ArrayList;

public class Piece {
    public final boolean firstPlayerOwner;
    private Field position;
    final public boolean king;

    private Element element;
    public boolean visible;

    // if king
    private ArrayList<Element> elements;

    public Piece(Element element, boolean firstPlayerOwner) {
        this.king = false;
        this.position = null;
        this.element = element;
        this.visible = false;
        this.firstPlayerOwner = firstPlayerOwner;
    }

    public Piece(boolean firstPlayerOwner) {
        this.king = true;
        this.position = null;
        this.elements = new ArrayList<>();
        this.elements.add(Element.FIRE);
        this.elements.add(Element.METAL);
        this.elements.add(Element.WOOD);
        this.elements.add(Element.EARTH);
        this.elements.add(Element.WATER);
        this.element = Element.BLANK;
        this.visible = true;
        this.firstPlayerOwner = firstPlayerOwner;
    }

    public void setPosition(Field newPosition) {
        this.position = newPosition;
    }

    public Field getPosition() {
        return this.position;
    }

    public void destroy(Element element) {
        this.elements.remove(element);
        if (!this.elements.isEmpty()) {
            this.setElement(Element.next(this.getElement(), this.elements));
        }
    }

    public void reveal() {
        this.visible = true;
    }

    public void conceal() {
        this.visible = false;
    }

    public Element getElement() { return this.element; }

    public void setElement(Element element) {
        this.element = element;
    }

    public Boolean hasElement(Element element) { return elements.contains(element); }
}