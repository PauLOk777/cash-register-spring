function convertCoinsToBill() {
    const coins = document.getElementsByClassName("priceToParse");
    for (const coin of coins) {
        const coinText = coin.innerText;
        if (coinText.length === 1) {
            coin.innerText = "0.0" + coinText;
        } else if (coinText.length === 2) {
            coin.innerText = "0." + coinText;
        } else {
            const billPart = coinText.substring(0, coinText.length - 2);
            const coinPart = coinText.substring(coinText.length - 2, coinText.length);
            coin.innerText = billPart + '.' + coinPart;
        }
    }
}