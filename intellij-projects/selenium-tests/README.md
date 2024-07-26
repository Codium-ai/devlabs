# Selenium Functional Testing 

## Overview
This project is a Functional Test using Selenium testing an example project in public domain on github.
The reactJS weather application is for demonstration purpose.

## Assumptions
* You have some knowledge of Selenium and how it works. 
* You have Docker desktop installed some understanding of how it works.
* You have some Javascript and Java experience but minimal needed.

## Getting Started
* Ensure chromedriver is installed.
  * https://googlechromelabs.github.io/chrome-for-testing/#stable
    * I used https://storage.googleapis.com/chrome-for-testing-public/127.0.6533.72/mac-arm64/chromedriver-mac-arm64.zip
* Ensure you have Docker Desktop installed.
  * https://www.docker.com/products/docker-desktop/
* If you do not have the Codiumate installed recommend watching [Getting Started with Codiumate](https://youtu.be/tNs67CLbXOg?feature=shared)
## Prompts:

### HomePageTest
#### Context:
- Home.jsx focused
- Extra context
  - BaseTest
  - HomePageTest
```
create a java selenium test validating the output of Home.jsx and test all actions on the page, store evidence of test exceptions/assertionfailures 
```

### Home.jsx fix the bug found
#### Context:
- Home.jsx:35-61

#### Command
- /improve

##### additional information
```
 flow and readability by ensuring the database operations occur only after the Swal toast notification has completed
```
