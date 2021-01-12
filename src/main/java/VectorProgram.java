import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;

/* Some of the code below is copied or follows patterns from the following sources:

    1. Code examples - 09.graphics - SimpleDraw.java
    2. https://www.baeldung.com/java-write-to-file
    3. https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
    4. Code examples - 08.events - AdapterEvents.java
    5. http://zetcode.com/tutorials/javaswingtutorial/menusandtoolbars/
    6. https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
    7. https://stackoverflow.com/questions/5603966/how-to-make-filefilter-in-java

 */

public class VectorProgram extends JFrame {

    public static void main(String[] args) {

        JFrame f = new JFrame();

        f.setContentPane(new BasePanel());
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setTitle("Picasso Editor");
        f.setSize(new Dimension(1600, 1024));
        f.setMinimumSize(new Dimension(640, 480));
        f.setResizable(false);
        f.setVisible(true);

    }

}


class BasePanel extends JPanel {

    MenuBar menuBar;
    ToolPanel toolPanel;
    DrawingPanel drawingPanel;

    public BasePanel() {

        BorderLayout bl = new BorderLayout();
        bl.setHgap(5);
        bl.setVgap(5);

        menuBar = new MenuBar();
        toolPanel = new ToolPanel(this);
        drawingPanel = new DrawingPanel(toolPanel);

        this.setLayout(bl);
        this.setPreferredSize(new Dimension(1024, 768));
        this.add(menuBar, BorderLayout.PAGE_START);
        this.add(toolPanel, BorderLayout.LINE_START);
        this.add(drawingPanel, BorderLayout.CENTER);

    }

}

class CustomFileFilter extends FileFilter {

    public boolean accept(File file) {

        if (file.isDirectory()) return true;
        else return file.getName().endsWith(".vec");

    }

    public String getDescription() {

        return ".vec";

    }

}

class DrawingPanel extends JPanel {

    ToolPanel toolPanel;
    DrawingPanel drawingPanel;
    Shape[] a;
    Shape curShape;
    String curTool;
    Color curColor;
    int curThickness;
    int i;
    int x1, y1, x2, y2;
    int xprev, yprev;
    boolean preview;
    Action deselectAction = new AbstractAction() {

        public void actionPerformed(ActionEvent e) {

            System.out.println("Escape key pressed");
            if (curShape != null) {

                curShape.isChosen = false;
                curShape = null;
                repaint();

            }

        }

    };

    public DrawingPanel(ToolPanel tp) {

        this.toolPanel = tp;
        this.setBorder(new LineBorder(Color.BLACK));
        this.setBackground(Color.WHITE);
        a = new Shape[15];
        this.addMouseListener(new CustomMouseAdapter());
        this.addMouseMotionListener(new CustomMouseMotionAdapter());
        i = -1;
        curShape = null;
        this.setFocusable(true);
        this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                "deselect");
        this.getActionMap().put("deselect", deselectAction);

    }

    public void updateCurShape(Color newColor) {

        if (curShape != null) {

            curShape.color = newColor;
            repaint();

        }

    }

    public void updateCurShape(int newThickness) {

        if (curShape != null) {

            curShape.thickness = newThickness;
            repaint();

        }

    }

    public void reset() {

        System.out.println("Resetting the canvas");
        for (int j = 0; j < i; ++j) {

            a[j] = null;

        }
        a = new Shape[15];
        i = -1;
        repaint();

    }

    public void save() throws IOException {

        if (i != -1) {

            final JFileChooser fc = new JFileChooser(".");
            //File filtering example found at https://stackoverflow.com/questions/5603966/how-to-make-filefilter-in-java
            fc.setAcceptAllFileFilterUsed(false);
            fc.setFileFilter(new CustomFileFilter());
            int result = fc.showSaveDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {

                System.out.println("Writing canvas to file " + fc.getSelectedFile().getName());
                File file = fc.getSelectedFile();
                if (!(file.getName().endsWith(".vec"))) {

                    file = new File(file.getName() + ".vec");

                }
                //Follows pattern shown at https://www.baeldung.com/java-write-to-file
                BufferedWriter writer = new BufferedWriter(new FileWriter(file));
                String curLine = i + "\n";
                writer.write(curLine);

                for (int j = 0; j <= i; ++j) {

                    String type = a[j].type;
                    String x1 = Integer.toString(a[j].x1);
                    String y1 = Integer.toString(a[j].y1);
                    String x2 = Integer.toString(a[j].x2);
                    String y2 = Integer.toString(a[j].y2);
                    String red = Integer.toString(a[j].color.getRed());
                    String green = Integer.toString(a[j].color.getGreen());
                    String blue = Integer.toString(a[j].color.getBlue());
                    String thickness = Integer.toString(a[j].thickness);
                    String isFilled = Boolean.toString(a[j].isFilled);

                    curLine = j + "," + type + "," + x1 + "," + y1 + "," + x2 + "," + y2
                            + "," + red + "," + green + "," + blue + "," + thickness + "," + isFilled + "\n";
                    writer.append(curLine);

                }

                writer.close();

            }

        } else {

            //Follows pattern shown at https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
            JOptionPane optionPane = new JOptionPane();
            optionPane.showMessageDialog(this,
                    "Empty canvas cannot be saved", "Error", JOptionPane.WARNING_MESSAGE);

        }


    }

    public void load() throws IOException {

        final JFileChooser fc = new JFileChooser(".");
        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(new CustomFileFilter());
        int result = fc.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {

            //Follows pattern shown at https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
            System.out.println("Reading canvas from file");
            File file = fc.getSelectedFile();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            int length = Integer.parseInt(reader.readLine());
            a = new Shape[length + 2];
            this.i = length;
            for (int j = 0; j <= i; ++j) {

                String curLine = reader.readLine();
                String[] arr = curLine.split(",");
                String type = arr[1];
                int x1 = Integer.parseInt(arr[2]);
                int y1 = Integer.parseInt(arr[3]);
                int x2 = Integer.parseInt(arr[4]);
                int y2 = Integer.parseInt(arr[5]);
                int red = Integer.parseInt(arr[6]);
                int green = Integer.parseInt(arr[7]);
                int blue = Integer.parseInt(arr[8]);
                int thickness = Integer.parseInt(arr[9]);
                boolean isFilled = Boolean.parseBoolean(arr[10]);
                a[j] = new Shape(x1, y1, x2, y2, new Color(red, green, blue), type, thickness, isFilled);

            }

            repaint();

        }

    }

    private int chooseShape(int x, int y) {

        for (int j = i; j >= 0; --j) {

            if (a[j].contains(x, y)) {

                System.out.println("Shape clicked: " + a[j].type + ", " +
                        a[j].color.toString() + ", " + a[j].x1 + ", " + a[j].y1);
                return j;

            }

        }

        return -1;

    }

    //Custom adapter methods follow pattern shown at Code examples - 08.events - AdapterEvents.java
    private class CustomMouseMotionAdapter extends MouseMotionAdapter {

        public void mouseDragged(MouseEvent e) {

            xprev = x2;
            yprev = y2;
            x2 = e.getX();
            y2 = e.getY();
            repaint();

            if (curTool.equals("selection") && curShape != null) {

                curShape.x2 = curShape.x2 + (x2 - xprev);
                curShape.y2 = curShape.y2 + (y2 - yprev);
                curShape.x1 = curShape.x1 + (x2 - xprev);
                curShape.y1 = curShape.y1 + (y2 - yprev);

            }

        }

    }

    private class CustomMouseAdapter extends MouseAdapter {

        public void mousePressed(MouseEvent e) {

            x1 = e.getX();
            y1 = e.getY();
            x2 = x1;
            y2 = y1;
            preview = true;
            curColor = toolPanel.colorPalette.curColor;
            curTool = toolPanel.toolPalette.curTool;

            if(curShape != null) {

                curShape.isChosen = false;
                curShape = null;

            }

            if (curTool.equals("selection") && i != -1) {

                System.out.println("Selecting Shape");
                int curShapeIndex = chooseShape(x1, y1);
                if (curShapeIndex != -1) {

                    curShape = a[curShapeIndex];
                    curShape.isChosen = true;

                    toolPanel.colorPalette.updateCurColor(curShape.color);
                    toolPanel.linePalette.updateCurThickness(curShape.thickness);

                }

            } else if (curTool.equals("fill")) {

                int curShapeIndex = chooseShape(x1, y1);
                if (curShapeIndex != -1) {

                    curShape = a[curShapeIndex];
                    curShape.color = curColor;
                    if (curShape.isFilled) curShape.isFilled = false;
                    else curShape.isFilled = true;
                    curShape = null;

                }

            } else if (curTool.equals("erase")) {

                int curShapeIndex = chooseShape(x1, y1);
                if (curShapeIndex != -1) {

                    a[curShapeIndex] = null;
                    for (int j = curShapeIndex; j < i; ++j) {

                        a[j] = a[j + 1];

                    }
                    --i;

                }

            }
            repaint();

        }

        public void mouseReleased(MouseEvent e) {

            x2 = e.getX();
            y2 = e.getY();
            preview = false;
            curColor = toolPanel.colorPalette.curColor;
            curTool = toolPanel.toolPalette.curTool;
            int thickness = toolPanel.linePalette.curThickness;

            if (curTool.equals("line")) {

                ++i;
                a[i] = new Shape(x1, y1, x2, y2, curColor, curTool, thickness);

            } else if (curTool.equals("rectangle")) {

                ++i;
                a[i] = new Shape(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2),
                        curColor, curTool, thickness);

            } else if (curTool.equals("circle")) {

                ++i;
                a[i] = new Shape(Math.min(x1, x2), Math.min(y1, y2), Math.max(x1, x2), Math.max(y1, y2),
                        curColor, curTool, thickness);

            }

            if (i == a.length - 1) {

                Shape[] newArr = new Shape[a.length + 5];

                for (int j = 0; j < a.length; ++j) {

                    newArr[j] = a[j];

                }
                a = newArr;

            }

            repaint();

        }

    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //Copied from Code Examples - 09.graphics - SimpleDraw.java
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        for (int j = 0; j <= i; ++j) {

            g2.setColor(a[j].color);
            g2.setStroke(new BasicStroke(a[j].thickness));

            int red = a[j].color.getRed();
            int green = a[j].color.getGreen();
            int blue = a[j].color.getBlue();

            if (red <= 127) red += 128;
            else red -= 128;
            if (green <= 127) green += 128;
            else green -= 128;
            if (blue <= 127) blue += 128;
            else blue -= 128;

            if (a[j].type.equals("line")) {

                g2.drawLine(a[j].x1, a[j].y1, a[j].x2, a[j].y2);

                if(a[j].isChosen) {

                    g2.setColor(new Color(red, green, blue));
                    float[] arr = {10.0f};
                    g2.setStroke(new BasicStroke(a[j].thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, arr, 0.0f));
                    g2.drawLine(a[j].x1, a[j].y1, a[j].x2, a[j].y2);

                }

            } else if (a[j].type.equals("rectangle")) {

                if (a[j].isFilled) {

                    g2.fillRect(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                } else {

                    g2.drawRect(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                }

                if(a[j].isChosen) {

                    g2.setColor(new Color(red, green, blue));
                    float[] arr = {10.0f};
                    g2.setStroke(new BasicStroke(a[j].thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, arr, 0.0f));
                    g2.drawRect(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                }

            } else if (a[j].type.equals("circle")) {

                if (a[j].isFilled) {

                    g2.fillOval(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                } else {

                    g2.drawOval(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                }

                if (a[j].isChosen) {

                    g2.setColor(new Color(red, green, blue));
                    float[] arr = {10.0f};
                    g2.setStroke(new BasicStroke(a[j].thickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                            10.0f, arr, 0.0f));
                    g2.drawOval(a[j].x1, a[j].y1, a[j].x2 - a[j].x1, a[j].y2 - a[j].y1);

                }

            }

        }

        if (preview) {

            String curTool = this.toolPanel.toolPalette.curTool;
            g2.setColor(this.toolPanel.colorPalette.curColor);
            g2.setStroke(new BasicStroke(this.toolPanel.linePalette.curThickness));

            if (curTool.equals("line")) {

                g2.drawLine(x1, y1, x2, y2);

            } else if (curTool.equals("rectangle")) {

                g2.drawRect(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));

            } else if (curTool.equals("circle")) {

                g2.drawOval(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));

            }

        }

    }

}

class Shape {

    int x1, y1, x2, y2;
    Color color;
    String type;
    int thickness;
    boolean isFilled;
    boolean isChosen;

    Shape(int x1, int y1, int x2, int y2, Color color, String type, int thickness) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.type = type;
        this.thickness = thickness;
        this.isFilled = false;
        this.isChosen = false;

    }

    Shape(int x1, int y1, int x2, int y2, Color color, String type, int thickness, boolean isFilled) {

        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.color = color;
        this.type = type;
        this.thickness = thickness;
        this.isFilled = isFilled;
        this.isChosen = false;

    }

    public boolean contains(int x, int y ) {

        if (this.x1 != this.x2 && this.y1 != this.y2) {

            Rectangle boundingBox = new Rectangle(new Point(Math.min(x1, x2), Math.min(y1, y2)),
                    new Dimension(Math.abs(x2 - x1), Math.abs(y2 - y1)));

            if (boundingBox.contains(x, y)) return true;
            else return false;

        } else {

            if (this.x1 == x || this.x2 == x || this.y1 == y || this.y2 == y) return true;
            else return false;

        }

    }


}

class MenuBar extends JMenuBar implements ActionListener {

    public MenuBar() {

        // Follows pattern shown at http://zetcode.com/tutorials/javaswingtutorial/menusandtoolbars/
        JMenu fileMenu = new JMenu("File");
        this.add(fileMenu);

        JMenuItem fileNew = new JMenuItem("New");
        JMenuItem fileLoad = new JMenuItem("Load");
        JMenuItem fileSave = new JMenuItem("Save");

        fileNew.addActionListener(this);
        fileLoad.addActionListener(this);
        fileSave.addActionListener(this);

        fileMenu.add(fileNew);
        fileMenu.add(fileLoad);
        fileMenu.add(fileSave);

    }

    public void actionPerformed(ActionEvent e) {

        JMenuItem source = (JMenuItem) e.getSource();
        BasePanel bp = (BasePanel)this.getParent();
        DrawingPanel dp = bp.drawingPanel;
        if (source.getText().equals("New")) {

            dp.reset();

        } else if (source.getText().equals("Load")) {

            try {

                dp.load();

            } catch (IOException exception) {

                System.out.println("Invalid Load Operation");

            }

        } else if (source.getText().equals("Save")) {

            try {

                dp.save();

            } catch (IOException exception) {

                System.out.println("Invalid Save Operation");

            }

        }

    }

}

class ToolPanel extends JPanel {

    ToolPalette toolPalette;
    ColorPalette colorPalette;
    LinePalette linePalette;
    BoxLayout bl;
    BasePanel bp;

    public ToolPanel(BasePanel bp) {

        this.bp = bp;
        bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        toolPalette = new ToolPalette(bp);
        colorPalette = new ColorPalette(bp);
        linePalette = new LinePalette(bp);
        this.setLayout(bl);
        this.add(toolPalette);
        this.add(colorPalette);
        this.add(linePalette);
        this.setFocusable(false);

    }

    ToolPalette getToolPalette() {

        return this.toolPalette;

    }

    ColorPalette getColorPalette() {

        return this.colorPalette;

    }

    LinePalette getLinePalette() {

        return this.linePalette;

    }

}

class LinePalette extends JPanel implements ActionListener {

    String[] thickness = {"5px", "10px", "12px"};
    int curThickness;
    JComboBox thicknessOptions;
    TitledBorder b = new TitledBorder("Line Thickness");
    BasePanel bp;

    public LinePalette(BasePanel bp) {

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        thicknessOptions = new JComboBox(thickness);
        thicknessOptions.setSelectedIndex(0);
        thicknessOptions.addActionListener(this);
        thicknessOptions.setPreferredSize(new Dimension(20, 10));

        this.curThickness = 5;
        this.setBorder(b);
        this.add(thicknessOptions);
        this.bp = bp;

    }

    public void updateCurThickness(int newThickness) {

        this.curThickness = newThickness;
        thicknessOptions.setSelectedItem(newThickness + "px");

    }

    public void actionPerformed(ActionEvent e) {

        JComboBox cb = (JComboBox)e.getSource();
        String chosenVal = (String)cb.getSelectedItem();

        curThickness = Integer.parseInt(chosenVal.substring(0, chosenVal.length() - 2));
        System.out.println("Line thickness selected: " + curThickness);
        bp.drawingPanel.updateCurShape(curThickness);


    }

}

class ColorPalette extends JPanel implements ActionListener {

    Color curColor;
    CustomMouseAdapter cma = new CustomMouseAdapter();
    JButton curButton;
    JPanel colorPanel;
    JButton chooser = new JButton("Chooser");
    JButton[][] colors = new JButton[3][2];
    TitledBorder b = new TitledBorder("Color Palette");
    LineBorder defaultBorder = new LineBorder(Color.BLACK, 1);
    BasePanel bp;

    public ColorPalette(BasePanel bp) {

        chooser.setAlignmentX(CENTER_ALIGNMENT);
        chooser.setActionCommand("choose_color");
        chooser.addActionListener(this);

        colorPanel = new JPanel();
        BoxLayout bl = new BoxLayout(this, BoxLayout.Y_AXIS);
        GridLayout gl = new GridLayout(3, 2);
        gl.setHgap(5);
        gl.setVgap(5);
        this.setLayout(bl);
        this.setBorder(b);
        colorPanel.setLayout(gl);
        this.bp = bp;

        for (int i = 0; i < colors.length; ++i) {

            for (int j = 0; j < colors[i].length; ++j) {

                colors[i][j] = new JButton();
                colors[i][j].setPreferredSize(new Dimension(80, 80));
                colors[i][j].setActionCommand("color" + i + j);
                colors[i][j].setBorder(defaultBorder);
                colorPanel.add(colors[i][j]);
                colors[i][j].addActionListener(this);
                colors[i][j].addMouseListener(cma);

            }

        }

        colors[0][0].setBorder(new LineBorder(Color.GRAY, 10));
        colors[0][0].setBackground(Color.BLACK);
        curButton = colors[0][0];
        curColor = Color.BLACK;

        colors[0][1].setBackground(Color.MAGENTA);
        colors[1][0].setBackground(Color.RED);
        colors[1][1].setBackground(Color.YELLOW);
        colors[2][0].setBackground(Color.GREEN);
        colors[2][1].setBackground(Color.BLUE);

        this.add(colorPanel);
        this.add(chooser);


    }

    public void updateCurColor(Color c) {

        this.curColor = c;
        curButton.setBackground(curColor);
        int red = curColor.getRed();
        int green = curColor.getGreen();
        int blue = curColor.getBlue();

        if (red <= 127) red += 128;
        else red -= 128;
        if (green <= 127) green += 128;
        else green -= 128;
        if (blue <= 127) blue += 128;
        else blue -= 128;

        curButton.setBorder(new LineBorder(new Color(red, green, blue), 10));

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("choose_color")) {

            Color newColor = JColorChooser.showDialog(this, "Color Picker", curColor);
            if (newColor != null) {

                this.updateCurColor(newColor);
                bp.drawingPanel.updateCurShape(curColor);

            }

        } else {

            for (int i = 0; i < colors.length; ++i) {

                for (int j = 0; j < colors[i].length; ++j) {

                    if (e.getActionCommand().equals("color" + i + j)) {

                        curColor = colors[i][j].getBackground();
                        curButton = colors[i][j];
                        int r = curColor.getRed();
                        int g = curColor.getGreen();
                        int b = curColor.getBlue();

                        if (r <= 127) r += 128;
                        else r -= 128;
                        if (g <= 127) g += 128;
                        else g -= 128;
                        if (b <= 127) b += 128;
                        else b -= 128;

                        colors[i][j].setBorder(new LineBorder(new Color(r, g, b), 10));
                        bp.drawingPanel.updateCurShape(curColor);


                    } else {

                        colors[i][j].setBorder(defaultBorder);

                    }

                }

            }

            System.out.println(curColor.toString());

        }

    }

    public JPanel getColorPanel() {

        return this.colorPanel;

    }

    public Color getCurColor() {

        return this.curColor;

    }

    public JButton getCurButton() {

        return this.curButton;

    }

    private class CustomMouseAdapter extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {

            if (e.getButton() == (MouseEvent.BUTTON3)) {

                Color newColor = JColorChooser.showDialog(null, "Color Picker", curColor);

                if (newColor != null) {

                    JButton buttonClicked = (JButton) e.getSource();
                    buttonClicked.setBackground(newColor);
                    curButton.setBorder(defaultBorder);
                    curButton = buttonClicked;
                    curColor = newColor;
                    int r = curColor.getRed();
                    int g = curColor.getGreen();
                    int b = curColor.getBlue();

                    if (r <= 127) r += 128;
                    else r -= 128;
                    if (g <= 127) g += 128;
                    else g -= 128;
                    if (b <= 127) b += 128;
                    else b -= 128;
                    curButton.setBorder(new LineBorder(new Color(r, g, b), 10));
                    curColor = newColor;
                    bp.drawingPanel.updateCurShape(curColor);

                }

            }

        }

    }

}

class ToolPalette extends JPanel implements ActionListener {

    String curTool;
    JButton curButton;
    LineBorder defaultBorder = new LineBorder(Color.BLACK, 1);
    TitledBorder b = new TitledBorder("Drawing Tools");
    BasePanel bp;

    public ToolPalette(BasePanel bp) {

        this.bp = bp;
        ImageIcon selectionIcon = new ImageIcon("resources/selection_tool.png");
        ImageIcon lineIcon = new ImageIcon("resources/straight_line.png");
        ImageIcon eraserIcon = new ImageIcon("resources/eraser_tool.png");
        ImageIcon circleIcon = new ImageIcon("resources/circle_tool.png");
        ImageIcon rectangleIcon = new ImageIcon("resources/rectangle_tool.png");
        ImageIcon fillIcon = new ImageIcon("resources/fill_tool.png");

        GridLayout gl = new GridLayout(3,2);
        gl.setHgap(1);
        gl.setVgap(2);
        this.setLayout(gl);
        this.setMinimumSize(new Dimension(320, 240));
        this.setBorder(b);

        JButton selectionTool = new JButton(selectionIcon);
        selectionTool.setFocusPainted(false);
        selectionTool.setBorder(new LineBorder(Color.BLACK, 5));
        this.curTool = "selection";
        this.curButton = selectionTool;
        selectionTool.setActionCommand("selection");
        selectionTool.addActionListener(this);
        selectionTool.setPreferredSize(new Dimension(80,80));
        this.add(selectionTool);

        JButton eraseTool = new JButton(eraserIcon);
        eraseTool.setFocusPainted(false);
        eraseTool.setActionCommand("erase");
        eraseTool.addActionListener(this);
        eraseTool.setBorder(defaultBorder);
        this.add(eraseTool);

        JButton lineTool = new JButton(lineIcon);
        lineTool.setFocusPainted(false);
        lineTool.setActionCommand("line");
        lineTool.addActionListener(this);
        lineTool.setBorder(defaultBorder);
        this.add(lineTool);

        JButton circleTool = new JButton(circleIcon);
        circleTool.setFocusPainted(false);
        circleTool.setActionCommand("circle");
        circleTool.addActionListener(this);
        circleTool.setBorder(defaultBorder);
        this.add(circleTool);

        JButton rectangleTool = new JButton(rectangleIcon);
        rectangleTool.setFocusPainted(false);
        rectangleTool.setActionCommand("rectangle");
        rectangleTool.addActionListener(this);
        rectangleTool.setBorder(defaultBorder);
        this.add(rectangleTool);

        JButton fillTool = new JButton(fillIcon);
        fillTool.setFocusPainted(false);
        fillTool.setActionCommand("fill");
        fillTool.addActionListener(this);
        fillTool.setBorder(defaultBorder);
        this.add(fillTool);

    }

    public void actionPerformed(ActionEvent e) {

        Component[] arr = this.getComponents();
        curButton = (JButton)e.getSource();
        curTool = curButton.getActionCommand();
        curButton.setBorder(new LineBorder(Color.BLACK, 5));

        for (int i = 0; i < arr.length; ++i) {

            JButton b = (JButton)arr[i];

            if (!(b.getActionCommand().equals(e.getActionCommand()))) {

                b.setBorder(defaultBorder);

            }

        }

        if (curTool.equals("erase")) {

            for (int i = 0; i < bp.toolPanel.colorPalette.colors.length; ++i) {

                for (int j = 0; j < bp.toolPanel.colorPalette.colors[i].length; ++j) {

                    bp.toolPanel.colorPalette.colors[i][j].setEnabled(false);

                }

            }
            bp.toolPanel.linePalette.thicknessOptions.setEnabled(false);

        } else {

            for (int i = 0; i < bp.toolPanel.colorPalette.colors.length; ++i) {

                for (int j = 0; j < bp.toolPanel.colorPalette.colors[i].length; ++j) {

                    bp.toolPanel.colorPalette.colors[i][j].setEnabled(true);

                }

            }
            bp.toolPanel.linePalette.thicknessOptions.setEnabled(true);

        }

        if (bp.drawingPanel.curShape != null) {

            bp.drawingPanel.curShape.isChosen = false;
            bp.drawingPanel.curShape = null;
            bp.drawingPanel.repaint();

        }

    }

}