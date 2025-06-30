package demo.wrappers;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.idealized.Javascript;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.time.Duration;

public class Wrappers {
    /*
     * Write your selenium wrappers here
     */
    public static void click(WebDriver driver, WebElement elementToClick){
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // WebElement elementToClick = driver.findElement(locator);
            js.executeScript("arguments[0].scrollIntoView(); arguments[0].click();", elementToClick);
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception occured: "+e.getMessage());
        }
    }

    public static ArrayList<HashMap<String, String>> navigatePageToCollectData(WebDriver driver, double percent, int pageCount){
        ArrayList<HashMap<String, String>> listOfData = new ArrayList<>();
        try {
            do{
                WebElement nextBtn = driver.findElement(By.xpath("//a[@aria-label='Next']"));
                click(driver, nextBtn);
                //get list of all win% less than given parameter from the table
                List<WebElement> winPercentList = driver.findElements(By.xpath("//td[contains(@class, 'pct') and text() < "+percent+ "]"));
                
                for(WebElement winColumn : winPercentList){
                    
                    HashMap<String, String> hm = new HashMap<>();
                    WebElement teamNameCol = winColumn.findElement(By.xpath(".//preceding-sibling::td[@class='name']"));
                    WebElement yearCol = winColumn.findElement(By.xpath(".//preceding-sibling::td[@class='year']"));

                    long epoch = System.currentTimeMillis()/1000;
                    hm.put("epoch_time", String.valueOf(epoch));
                    hm.put("team_name", teamNameCol.getText());
                    hm.put("year", yearCol.getText());
                    hm.put("win%",winColumn.getText());
                    listOfData.add(hm);
                    // System.out.println(System.currentTimeMillis()/1000+" "+teamNameCol.getText()+" "+yearCol.getText()+" "+winColumn.getText());
                }
                
                // Thread.sleep(2000);
                pageCount--;
            }while(pageCount > 0);
            
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception occured: "+e.getMessage());
        }
        return listOfData;
    }

    public static boolean writeToJSONFile(ArrayList<HashMap<String,String>> data, String fileName){
        boolean fileExists = false;
        try {
            ObjectMapper mapper = new ObjectMapper();
            // String prettyJsonString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
            // System.out.println(prettyJsonString);
            //Writing JSON on a file
            String userDir = System.getProperty("user.dir");
            File file = new File(userDir+"/src/test/resources/"+fileName+".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            fileExists = file.exists();
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception occured: "+e.getMessage());
        }
        return fileExists;
    }

    public static ArrayList<HashMap<String, String>> getTopMoviesEachYear(WebDriver driver){
        ArrayList<HashMap<String, String>> listOfMovies = new ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            //get list of years to click
            List<WebElement> yearLinks = driver.findElements(By.className("year-link"));
            
            for(WebElement yearLink : yearLinks){
                String year = yearLink.getText();
                click(driver, yearLink);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table")));
                List<WebElement> tableRows = driver.findElements(By.xpath("//tr[@class='film' and position() <= 5]"));
                // System.out.println("table row count: "+tableRows.size());
                for(WebElement row : tableRows){
                    HashMap<String, String> hm = new HashMap<>();
                    long epoch = System.currentTimeMillis()/1000;
                    boolean isWinner = true;
                    WebElement titleElement = row.findElement(By.className("film-title"));
                    WebElement nominationElement = row.findElement(By.className("film-nominations"));
                    WebElement awardsElement = row.findElement(By.className("film-awards"));
                    try {
                        row.findElement(By.tagName("i"));
                    } catch (Exception ex) {
                        // TODO: handle exception
                        isWinner = false;              
                    }
                    // List<WebElement> winner = row.findElements(By.tagName("i"));
                    // if(winner.size() > 0)
                    //     isWinner = true;
                    hm.put("epoch_time", String.valueOf(epoch));
                    hm.put("year", year);
                    hm.put("title", titleElement.getText());
                    hm.put("nominations", nominationElement.getText());
                    hm.put("awards", awardsElement.getText());
                    hm.put("isWinner", String.valueOf(isWinner));
                    listOfMovies.add(hm);
                    // System.out.println(epoch+" "+year+" "+titleElement.getText()+" "+nominationElement.getText()+" "+awardsElement.getText()+" "+isWinner);
                }
            }
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception occured: "+e.getMessage());
        }
        return listOfMovies;
    }
}
