/**
 * Clase base para los simbolos de la maquina.
 */

public class Symbol
{
    private int xPosition;
    private int yPosition;
    private boolean isVisible;
    private String color;
    private Circle moneda;
    private int symbolSize = 30;

    public Symbol(String color, int x, int y)
    {
        this.color = color;
        this.xPosition = x;
        this.yPosition = y;
        this.isVisible = false;

        moneda = new Circle();
        moneda.changeColor(color);
        moneda.changeSize(symbolSize);
        moneda.moveHorizontal(x - 20);
        moneda.moveVertical(y - 15);
    }

    public String getColor()
    {
        return color;
    }

    public void setPosition(int x, int y)
    {
        int dx = x - xPosition;
        int dy = y - yPosition;

        xPosition = x;
        yPosition = y;

        moneda.moveHorizontal(dx);
        moneda.moveVertical(dy);
    }

    public void makeVisible()
    {
        isVisible = true;
        moneda.makeVisible();
    }

    public void makeInvisible()
    {
        isVisible = false;
        moneda.makeInvisible();
    }

    public boolean isVisible()
    {
        return isVisible;
    }

    public void changeColor(String newColor)
    {
        color = newColor;
        moneda.changeColor(newColor);
    }

    public void onSelected()
    {
        // Comportamiento normal: no hace nada especial.
    }

    protected void resize(int newSize)
    {
        symbolSize = Math.max(5, newSize);
        moneda.changeSize(symbolSize);
    }

    protected int getSymbolSize()
    {
        return symbolSize;
    }

    public boolean isWild()
    {
        return false;
    }

    public Symbol copyAt(int x, int y)
    {
        return new Symbol(color, x, y);
    }
}
