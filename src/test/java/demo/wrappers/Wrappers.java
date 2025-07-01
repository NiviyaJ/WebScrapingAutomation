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
                //get the next btn to navigate through the pages
                WebElement nextBtn = driver.findElement(By.xpath("//a[@aria-label='Next']"));
                //click on the next button
                click(driver, nextBtn);
                //get list of all win% less than given parameter from the table
                List<WebElement> winPercentList = driver.findElements(By.xpath("//td[contains(@class, 'pct') and text() < "+percent+ "]"));
                
                for(WebElement winColumn : winPercentList){
                    //creating a hashmap for storing row data
                    HashMap<String, String> hm = new HashMap<>();
                    //get the team name column
                    WebElement teamNameCol = winColumn.findElement(By.xpath(".//preceding-sibling::td[@class='name']"));
                    //get the year Column
                    WebElement yearCol = winColumn.findElement(By.xpath(".//preceding-sibling::td[@class='year']"));

                    long epoch = System.currentTimeMillis()/1000;
                    //adding all the fetched data into the hashmap
                    hm.put("epoch_time", String.valueOf(epoch));
                    hm.put("team_name", teamNameCol.getText());
                    hm.put("year", yearCol.getText());
                    hm.put("win%",winColumn.getText());
                    //adding the hashmap into the parent list
                    listOfData.add(hm);
                    // System.out.println(System.currentTimeMillis()/1000+" "+teamNameCol.getText()+" "+yearCol.getText()+" "+winColumn.getText());
                }
                
                // decrementing page count to track no. of pages navigated
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
            /*Writing JSON on a file*/
            String userDir = System.getProperty("user.dir");
            File file = new File(userDir+"/src/test/resources/"+fileName+".json");
            //writing json string into the file
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, data);
            //checking if the file exists
            fileExists = file.exists();
            Thread.sleep(2000);
        } catch (Exception e) {
            // TODO: handle exception
            System.out.println("Exception occured: "+e.getMessage());
        }
        return fileExists;
    }

    public static ArrayList<HashMap<String, String>> getTopMoviesEachYear(WebDriver driver){
        //list to be returned
        ArrayList<HashMap<String, String>> listOfMovies = new ArrayList<>();
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            //get list of years to click
            List<WebElement> yearLinks = driver.findElements(By.className("year-link"));
            
            //loop through the years
            for(WebElement yearLink : yearLinks){
                //get the year text
                String year = yearLink.getText();
                //click on the year link
                click(driver, yearLink);
                //wait until table is displayed or visible
                wait.until(ExpectedConditions.presenceOfElementLocated(By.className("table")));
                //get list of top 5 rows from the table
                List<WebElement> tableRows = driver.findElements(By.xpath("//tr[@class='film' and position() <= 5]"));
                // System.out.println("table row count: "+tableRows.size());
                //loop through each row
                for(WebElement row : tableRows){
                    //creating a hashmap for storing row data
                    HashMap<String, String> hm = new HashMap<>();
                    //getting epoch time
                    long epoch = System.currentTimeMillis()/1000;
                    //setting default value as true for boolean isWinner variable 
                    boolean isWinner = true;
                    //get the title element
                    WebElement titleElement = row.findElement(By.className("film-title"));
                    //get the nomination element
                    WebElement nominationElement = row.findElement(By.className("film-nominations"));
                    //get awards element
                    WebElement awardsElement = row.findElement(By.className("film-awards"));
                    //trying to fetch the best picture flag, if element not found, 
                    // the exception is catched and isWinner variable is set to false
                    try {
                        row.findElement(By.tagName("i"));
                    } catch (Exception ex) {
                        // TODO: handle exception
                        isWinner = false;              
                    }
                    //alternative way to set isWinner value
                    // List<WebElement> winner = row.findElements(By.tagName("i"));
                    // if(winner.size() == 0)
                    //     isWinner = false;
                    //adding all the fetched data into the hashmap
                    hm.put("epoch_time", String.valueOf(epoch));
                    hm.put("year", year);
                    hm.put("title", titleElement.getText());
                    hm.put("nominations", nominationElement.getText());
                    hm.put("awards", awardsElement.getText());
                    hm.put("isWinner", String.valueOf(isWinner));
                    //adding the hashmap into the parent list
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
