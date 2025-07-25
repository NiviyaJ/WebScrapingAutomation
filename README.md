# XScraper Test Automation

## 📝 Project Overview

**XScraper Test Automation**  is a Java-Selenium based test automation framework designed to perform structured data scraping and verification from web pages. It collects and stores relevant data from sports and film datasets into structured JSON files for further analysis.


---

## ✅ Automated Test Cases

The project currently automates two primary test cases:

### 🧪 testCase01 – Scrape Hockey Team Data

* Navigate to the specified website
* Click on **"Hockey Teams: Forms, Searching and Pagination"**
* Iterate through the **table data** and extract:

  * Team Name
  * Year
  * Win % (only if Win % < 0.40)
* Traverse **4 pages** and store the extracted data in an `ArrayList` of `HashMap` objects
* Convert the `ArrayList` to a **JSON file** named `hockey-team-data.json` using the **Jackson** library
* Each record in the file will include:

  * Epoch Time of Scrape
  * Team Name
  * Year
  * Win %

---

### 🧪 testCase02 – Scrape Oscar-Winning Films

* Navigate to the specified website
* Click on **"Oscar Winning Films"**
* Click on **each year** presented
* For each year:

  * Extract the **top 5 movies**
  * Add `isWinner = true` for the **Best Picture winner**
  * Include the **year** for each entry
* Stores the collected data, along with the "Epoch Time of Scrape", in an `ArrayList` of `HashMap` objects.
* Convert the `ArrayList` to a **JSON file** named `oscar-winner-data.json`
* Each record will contain:

  * Epoch Time of Scrape
  * Year
  * Title
  * Nomination
  * Awards
  * isWinner
* Store the file in the **output folder** in the root directory
* Use **TestNG assertions** to ensure:

  * The file exists
  * The file is **not empty**

---

## 🔧 Tech Stack

* **Language:** Java
* **Automation Framework:** Selenium WebDriver
* **Testing Framework:** TestNG
* **Build Tool:** Gradle Wrapper
* **Browser:** Google Chrome
* **JSON Library:** Jackson

---

## 🌐 System Requirements

* Java 11 or later
* Google Chrome (latest version)
* ChromeDriver (compatible with your Chrome version)
* Internet connection

> Ensure `chromedriver` is in your system `PATH`, or specify its path in your Selenium setup.

---

## 📦 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/NiviyaJ/WebScrapingAutomation.git
cd WebScrapingAutomation
```

### 2. Make the Gradle Wrapper Executable (Linux/macOS)

This project uses the Gradle Wrapper, so you don't need to install Gradle globally. The wrapper scripts (gradlew for Linux/macOS, gradlew.bat for Windows) are included in the repository.

```bash
chmod +x gradlew
```

> Windows users can skip this step and use `gradlew.bat` instead.

### 3. Build the Project
The build.gradle file already contains all necessary dependencies (Selenium, TestNG, Jackson). Gradle will automatically download them when you build or run the project for the first time.

To manually trigger dependency resolution and build the project:

```bash
./gradlew clean build
```
>(on Windows, use gradlew.bat clean build)

---

## 🚀 Running the Tests

To execute the automated test suite:

**Linux/macOS:**

```bash
./gradlew test
```

**Windows:**

```bash
gradlew.bat test
```

You can also run individual tests using your IDE.

---

## 📂 Output Location

JSON output files will be created in the `/resources` directory at the root level:

* `resources/hockey-team-data.json`
* `resources/oscar-winner-data.json`

---

## ❌ Reporting

No external reporting tools are integrated with this project.

---

## 📬 Author

Maintained by **[Niviya Joy]**  
GitHub: [https://github.com/NiviyaJ]

---

