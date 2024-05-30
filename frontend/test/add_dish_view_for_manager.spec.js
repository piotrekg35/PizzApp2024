// Generated by Selenium IDE
const { Builder, By, Key, until } = require('selenium-webdriver')
const assert = require('assert')

describe('manager can enter add dish view', function() {
  this.timeout(30000)
  let driver
  let vars
  beforeEach(async function() {
    driver = await new Builder().forBrowser('firefox').build()
    vars = {}
  })
  afterEach(async function() {
    await driver.quit();
  })
  it('manager can enter add dish view', async function() {
    await driver.get("http://localhost:4200/zaloguj")
    await driver.manage().window().setRect({ width: 989, height: 666 })
    await driver.findElement(By.id("exampleInputEmail1")).sendKeys("man@man.pl")
    await driver.findElement(By.id("exampleInputPassword1")).sendKeys("123456")
    await driver.findElement(By.css(".btn")).click()
    await driver.sleep(3000)
    await driver.findElement(By.linkText("Dodaj")).click()
    vars["url"] = await driver.executeScript("return window.location.href;")
    assert(vars["url"].toString() == "http://localhost:4200/dodaj")
    await driver.findElement(By.css(".btn")).click()
  })
})