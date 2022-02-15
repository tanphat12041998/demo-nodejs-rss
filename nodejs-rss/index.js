const express = require('express')
const bodyParser = require('body-parser')
const Parser = require('rss-parser')
const cheerio = require('cheerio')
const parser = new Parser();
const app = express()
const port = process.env.PORT || 1998
const USERNAME_DEFAULT = "test"
const PASSWORD_DEFAULT = "123"
const FEED_URL = "https://vnexpress.net/rss/tin-moi-nhat.rss"

class RssItem {
    constructor(title, link, image, description, pubDate) {
        this.title = title;
        this.link = link;
        this.image = image;
        this.description = description;
        this.pubDate = pubDate;
    }
}

app.use(bodyParser.json())
app.use(bodyParser.urlencoded({extended: false}))

app.listen(port, async () => {
    console.log(`Example app listening at http://localhost:${port}`)
});

app.post('/login', async (req, res) => {
    const username = req.body.username;
    const password = req.body.password;
    console.log(username);
    console.log(password);

    if(username === USERNAME_DEFAULT && password === PASSWORD_DEFAULT) {
        return res.send(true);
    }
    return  res.send(false);
});

app.get('/fetchNews', async (req, res) => {
    const rss = await parser.parseURL(FEED_URL);
    console.log(rss.title);

    const rssList = [];
    rss.items.forEach(item => {
        const $ = cheerio.load(item.content);
        let rssItem = new RssItem(
            item.title,
            item.link,
            $('img').attr('src'),
            $('body').text(),
            item.pubDate
        );
        rssList.push(rssItem);
    });
    console.log(rssList);
    return res.send(rssList);
});
