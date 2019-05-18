import java.util.concurrent.TimeUnit;
//import java.util.Scanner;
import javax.imageio.ImageIO;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;
import com.asprise.ocr.Ocr;
import java.io.*;
import java.util.zip.*;
import java.util.ArrayList;
import java.util.List;
import java.security.MessageDigest;
import sun.misc.BASE64Encoder;

/**
 *
 * @author user
 */
public class Nodejava {

    public static void main(String args[]) throws Exception {
//                Scanner scan = new Scanner(System.in);
//                System.out.print("Enter url: ");
//                String myUrl = scan.next();
        
//*************Selenium Screenshot****************
        System.setProperty("webdriver.chrome.driver", "/Users/user/Downloads/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.get("https://www.squarespace.com/");
        Thread.sleep(2000);
        Screenshot fpScreenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000)).takeScreenshot(driver);
        ImageIO.write(fpScreenshot.getImage(), "PNG", new File("/Users/user/fyp/scrshot"));
        driver.quit();

//*******************OCR Text*********************
        Ocr.setUp(); 
        Ocr ocr = new Ocr(); // create a new OCR /*engine*/
        ocr.startEngine("eng", Ocr.SPEED_FASTEST); // English
                //String s = ocr.recognize(new File[]{new File("/Users/user/fyp/Screenshots/scrshot")},
                //Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_PLAINTEXT);
        ocr.recognize(new File[]{new File("/Users/user/fyp/scrshot")},
                Ocr.RECOGNIZE_TYPE_ALL, Ocr.OUTPUT_FORMAT_RTF,
                //Directory to save rtf file
                "PROP_RTF_OUTPUT_FILE=/Users/user/fyp/ocr-result.rtf");
                //System.out.println("Result: " + s);

        ocr.stopEngine();
        
///*******************Zip Files***********************
        Nodejava mfe = new Nodejava();
        List<String> files = new ArrayList<>();
        files.add("/Users/user/fyp/ocr-result.rtf");
        files.add("/Users/user/fyp/scrshot");
        mfe.zipFiles(files);
        
//*******************Hash Calculation******************
            byte[] buffer= new byte[8192];
    int count;
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("/Users/user/fyp/ocr-result.rtf"))) {
            while ((count = bis.read(buffer)) > 0) {
                digest.update(buffer, 0, count);
            }   }

    byte[] hash = digest.digest();
    System.out.println(new BASE64Encoder().encode(hash));
    }//Main Function Ends
    
    
    //Zipping Function
        public void zipFiles(List<String> files){
         
        FileOutputStream fos = null;
        ZipOutputStream zipOut = null;
        FileInputStream fis = null;
        try {
            fos = new FileOutputStream("/Users/user/fyp/test.zip");
            zipOut = new ZipOutputStream(new BufferedOutputStream(fos));
            for(String filePath:files){
                File input = new File(filePath);
                fis = new FileInputStream(input);
                ZipEntry ze = new ZipEntry(input.getName());
                System.out.println("Zipping the file: "+input.getName());
                zipOut.putNextEntry(ze);
                byte[] tmp = new byte[4*1024];
                int size = 0;
                while((size = fis.read(tmp)) != -1){
                    zipOut.write(tmp, 0, size);
                }
                zipOut.flush();
                fis.close();
            }
            zipOut.close();
            System.out.println("Done... Zipped the files...");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block

        } catch (IOException e) {
            // TODO Auto-generated catch block

        } finally{
            try{
                if(fos != null) fos.close();
            } catch(IOException ex){
                 
            }
        }
    }
}
