// GUI-related imports
import java.awt.*;
import java.awt.event.*;

public class  MTAlgorithms extends Frame implements ActionListener
{
    String[] description = new String[] {
            "This program examines and displays the effect of MultiThreading via Algorithms."
    };

    static int threshold = 1000;

    // Retrieved command code
    boolean arrayInitialized = false;
    int NDataItems = 10000000;
    int[] a = new int[NDataItems];
    char[] bracket =  new char[NDataItems];

    int maximumSerial;
    int maximumParallel;

    MenuItem miAbout,
            miInitBracket,miInitBalancedBracket,miBalancedBracket, miBalancedBracketThreshold;

    long start, elapsedTimeBalancedBracket;

    String command = "";

    public static void main(String[] args)
    {
        Frame frame = new  MTAlgorithms();

        frame.setResizable(true);
        frame.setSize(800,500);
        frame.setVisible(true);
    }

    public  MTAlgorithms()
    {
        setTitle("Parallel Algorithms");

        // Create Menu Bar
        MenuBar mb = new MenuBar();
        setMenuBar(mb);

        Menu menu = new Menu("Operations");
        // Add it to Menu Bar
        mb.add(menu);

        Menu analysis = new Menu("Analysis");
        // Add it to Menu Bar
        mb.add(analysis);

        // Create Menu Items
        // Add action Listener
        // Add to "File" Menu Group

        miAbout = new MenuItem("About");
        miAbout.addActionListener(this);
        menu.add(miAbout);

        miInitBracket = new MenuItem("Initialize Random Array");
        miInitBracket.addActionListener(this);
        menu.add(miInitBracket);

        miInitBalancedBracket = new MenuItem("Initialize Balanced Array");
        miInitBalancedBracket.addActionListener(this);
        menu.add(miInitBalancedBracket);


        miBalancedBracket = new MenuItem("Check Balanced Bracket ");
        miBalancedBracket.addActionListener(this);
        miBalancedBracket.setEnabled(false);
        menu.add(miBalancedBracket);

        miBalancedBracketThreshold = new MenuItem("MultiThreaded Check Balanced Bracket  Threshold");
        miBalancedBracketThreshold.addActionListener(this);
        miBalancedBracketThreshold.setEnabled(false);
        analysis.add(miBalancedBracketThreshold);

        MenuItem miExit = new MenuItem("Exit");
        miExit.addActionListener(this);
        menu.add(miExit);

        // End program when window is closed
        WindowListener l = new WindowAdapter()
        {
            public void windowClosing(WindowEvent ev)
            {System.exit(0);}

            public void windowActivated(WindowEvent ev)
            {repaint();}

            public void windowStateChanged(WindowEvent ev)
            {repaint();}
        };

        ComponentListener k = new ComponentAdapter()
        {
            public void componentResized(ComponentEvent e)
            {repaint();}
        };

        // register listeners
        this.addWindowListener(l);
        this.addComponentListener(k);
    }

    //******************************************************************************
    //  called by windows manager whenever the application window performs an action
    //  (select a menu item, close, resize, ....
    //******************************************************************************

    public void actionPerformed (ActionEvent ev)
    {
        // figure out which command was issued
        command = ev.getActionCommand();

        // take action accordingly
        if("About".equals(command))
        {
            repaint();
        }
        else if("Initialize Random Array".equals(command)) {
            initializeBrackets();
            arrayInitialized = true;
            miBalancedBracket.setEnabled(true);
            miBalancedBracketThreshold.setEnabled(true);
            repaint();
        }
        else if("Initialize Balanced Array".equals(command)) {
            initializeBalancedBrackets();
            arrayInitialized = true;
            miBalancedBracket.setEnabled(true);
            miBalancedBracketThreshold.setEnabled(true);
            repaint();
        }
        else
        if("Check Balanced Bracket ".equals(command))
        {
            char[] tempBracket = new char[bracket.length] ;
            System.arraycopy(bracket, 0, tempBracket, 0, bracket.length);

            start = System.currentTimeMillis();
            ParallelBalancedBracket.startMainTask(tempBracket,threshold);
            elapsedTimeBalancedBracket = System.currentTimeMillis()-start;
            repaint();

        }
        else if("MultiThreaded Check Balanced Bracket  Threshold".equals(command)) {
            repaint();
        }
        else if("Exit".equals(command))
        {
            System.exit(0);
        }
    }
    //********************************************************
    // called by repaint() to redraw the screen
    //********************************************************

    public void paint(Graphics g)
    {
        g.drawString(
                "Number of processors is "+Integer.toString( Runtime.getRuntime().availableProcessors() ),300,130);
        g.drawString("Number of Data Items = "+Integer.toString(NDataItems),300, 150);

        if( "Check Balanced Bracket ".equals(command))
        {
            g.drawString("Threshold = "+Integer.toString(threshold),300, 170);
            g.drawString("Method", 250, 200); g.drawString("Elapsed Time (ms)", 475, 200);
            g.drawLine(200, 210, 600, 210);
            g.drawString("Check Balanced Bracket ", 225, 230);
            g.drawString(Float.toString(elapsedTimeBalancedBracket), 475, 230);
        }
        else if("MultiThreaded Check Balanced Bracket  Threshold".equals(command)) {
            g.drawString("MultiThreaded Check Balanced Bracket ", 325, 180);
            g.drawString("Threshold", 250, 200); g.drawString("Elapsed Time (ms)", 475, 200);
            g.drawLine(200, 210, 600, 210);

            char[] tempBracket = new char[bracket.length] ;

            int y = 230;
            int intialThreshold = threshold;
            for (int i=1; i<=4; i++)
            {
                g.drawString(Integer.toString(threshold), 250, y);
                System.arraycopy(bracket, 0, tempBracket, 0, bracket.length);
                start = System.currentTimeMillis();
                ParallelBalancedBracket.startMainTask(tempBracket,threshold);
                elapsedTimeBalancedBracket = System.currentTimeMillis()-start;
                g.drawString(Float.toString(elapsedTimeBalancedBracket), 475, y);
                y=y+20;
                threshold = threshold * 10;
            }
            threshold = intialThreshold;
        }
        else if("About".equals(command)) {
            int x = 200;
            int y = 200;
            for(int i = 0; i < description.length; i++)
            {
                g.drawString(description[i], x, y);
                y = y +25;
            }
        }
        else if("Initialize Random Array".equals(command)) {
            g.drawString("Array Initialized",200, 100);
        }
        else if("Initialize Balanced Array".equals(command)) {
            g.drawString("Balanced Array Initialized",200, 100);
        }
    }

    public void initializeBrackets () {
        maximumSerial=	maximumParallel = -1;

        start = elapsedTimeBalancedBracket = 0;

        int num;
        for (int i=0; i< bracket.length; i++){
            num = ((int)(Math.random()*100)) % 4;
            if(num == 0) {
                bracket[i] = '{';
            }
            if(num == 1) {
                bracket[i] = '}';
            }
            if(num == 2) {
                bracket[i] = '(';
            }
            if(num == 3) {
                bracket[i] = ')';
            }
        }
    }

    public void initializeBalancedBrackets () {
        maximumSerial=	maximumParallel = -1;
        start = elapsedTimeBalancedBracket = 0;

        int num;
        for (int i=0; i< bracket.length; i++){
            num = i % 2;
            if(num == 0){
                bracket[i] = '{';
            }else{
                bracket[i] = '}';
            }
        }
    }
}