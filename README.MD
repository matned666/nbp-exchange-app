# Simple currency exchange CRUD app

Shows an exchange value of the given amount from given currency A to given currency B<br>
the exchange is done by the mid-rate from the given date<br>

API url: http://api.nbp.pl/en.html <br>
There is taken las rate from the chosen date.<br>

### Usage:
Run <b>NbpExchangeAppApplication</b> <br>
<code>localhost:8080/convert/{amount}?{currencyA=aaa}&{currencyB=bbb}&{date=yyyy-mm-dd}</code><br>
parameters:<br>
- <i>amount</i> - amount to be exchanged - obligatory
- <i>currencyA</i> - 3 letters code of currency which you'd like to exchange (default PLN) - not obligatory<br>
- <i>currencyB</i> - 3 letters code of currency to which you'd like to exchange (default EUR) - not obligatory<br>
- <i>date</i> - exchange rate date in format yyy-mm-dd (default today) - not obligatory<br>

if currency rate is not found for the current date it throws 404 <br>

### Repository
By default, it uses H2 DB <br>
if chosen rte is absent in DB, it is loaded from API and saved to DB

### Examples:
<code>
localhost:8080/convert/1000?currencyA=USD&currencyB=EUR&date=2022-11-07<br>
localhost:8080/convert/100?currencyB=EUR&date=2022-11-07<br>
localhost:8080/convert/2000?currencyB=EUR<br>
localhost:8080/convert/1000<br>
localhost:8080/convert/99000?date=2022-11-07<br>
localhost:8080/convert/1200?date=2022-11-07&currencyA=CZK<br>
</code>
<br>
<code>
localhost:8080/convert/1?currencyA=czk&currencyB=nok&date=2022-11-04
</code> <br>
expected result format: "<i>Exchange on 2022-11-04: 1 CZK = 0.41 NOK</i>"


