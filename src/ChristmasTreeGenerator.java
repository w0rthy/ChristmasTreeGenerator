package christmastreegenerator;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ChristmasTreeGenerator {

    static BufferedImage img;
    static Graphics2D g;
    
    
    static int DPI = 300;
    static String IDENTIFIER = "fasf";
    static int HEIGHT = 100;        //%
    static int TRUNK = 5;          //%
    static int PADDINGWIDTH = 5;    //%
    static int PADDINGHEIGHT = 5;   //%
    static int BRANCHES = 10;
    static int BRANCHWIDTHREDUCTIONPER = 10;    //%
    static int BRANCHWIDTHREDUCTIONDURING = 50; //%
    
    static int offsetx;
    static int offsety;
    static int treeheight;
    static int treewidth;
    
    public static void main(String[] args) throws Throwable {
        img = new BufferedImage((int)(DPI*8.5), (int)(DPI*11), BufferedImage.TYPE_INT_ARGB);
        g = (Graphics2D)img.getGraphics();
        
        offsetx = (int)(PADDINGWIDTH/100.0*img.getWidth());
        offsety = (int)((PADDINGHEIGHT+TRUNK)/100.0*img.getHeight());
        treeheight = (int)((100-PADDINGHEIGHT*2-TRUNK)/100.0*img.getHeight());
        treewidth = (int)((100-PADDINGWIDTH*2)/100.0*img.getWidth());
        
        genTree();
        applyPics();
        
        ImageIO.write(img, "png", new File("out.png"));
    }
    
    public static void applyPics() throws Throwable {
        File f = new File("pics");
        if(!f.isDirectory())
            return;
        
        File[] files = f.listFiles();
        for(File fs : files){
            if(!fs.getAbsolutePath().contains(IDENTIFIER))
                continue;
            BufferedImage tmp = ImageIO.read(fs);
            g.drawImage(tmp, (int)(Math.random()*(img.getWidth()-offsetx*2))+offsetx, (int)(Math.random()*(img.getHeight()-offsety-offsetx)+offsetx), 512, 512, null);
        }
    }
    
    public static void genTree() throws Throwable {
        
        int branchwidth = treewidth;
        int branchheight = treeheight/BRANCHES;
        
        for(int i = 0; i < treeheight; i++){
            int branch = (int)((double)i/treeheight*BRANCHES);
            branchwidth = (int)(branchwidth*((100-BRANCHWIDTHREDUCTIONPER)/100.0));
            int endwidth = (int)(branchwidth*((100-BRANCHWIDTHREDUCTIONDURING)/100.0));
            
            if(branch == BRANCHES-1)
                endwidth = 0;
            double slope = (double)(branchwidth-endwidth)/branchheight;
            double sloperem = 0.0;
            int curwidth = branchwidth;
            while((int)((double)i/treeheight*BRANCHES) == branch){
                int tmp = (treewidth-curwidth)/2;
                for(int j = tmp; j-tmp < curwidth; j++){
                    int x = j+offsetx;
                    int y = i+offsety;
                    SP(x, y, SC(x,y));
                }
                curwidth -= (int)slope;
                sloperem += slope-(int)slope;
                if(sloperem>=1.0){
                    curwidth-=(int)sloperem;
                    sloperem=sloperem-(int)sloperem;
                }
                i++;
            }
            if(i>treeheight)
                break;
            i--;
        }
        
        g.setColor(new Color(210,105,30));
        g.fillRect(img.getWidth()/2-DPI,
                treeheight+(int)((double)PADDINGHEIGHT/100.0*img.getHeight()),
                2*DPI,
                (int)((TRUNK)/100.0*img.getHeight()));
    }
    
    public static void SP(int x, int y, Color c){ //SETPIXEL
        g.setColor(c);
        g.fillRect(x, img.getHeight()-y, 1, 1);
    }

    public static Color SC(int x, int y){   //SEEDCOLOR
        int midx = img.getWidth()/2;
        return new Color(0, Math.min(Math.abs(x-midx)/4+128+y/100,255), 0);
    }
    
}
