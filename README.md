# PICO-URL
PICO is (another) url shortener written in Java (using Spring) that provides a REST api and a web handler.

_IF_ this was deployed in production behind a domain (let's say, https://pico.url) then it would return
shortened URLs in the form https://pico.url/2DxAzfF5 (which is the shortened form of http://www.google.com).

Then, when a user holding a shortened URL enters it in their browser, the web handler would redirect 
https://pico.url/2DxAzfF5 to http://www.google.com.

(I was also planning to add a web interface for shortening/redirection...maybe in the near future)

## Description
This was implemented in the context of practicing system design as it is one of the systems that is often considered 
very basic in system design.

The implementation is trying to satisfy the following requirements:
- be able to shortne up to 100 million URLs per day
- accept URLs up to 150 characters
- the shortened URLs must contain only the characters a..z, A..Z and 0..9
- the shortened URL cannot be deleted or updated and must be retained for 10 years (no expiring before that)

## Relaxations
The implementation is using an in-memory database for simplicity and does not address the expiration of the shortened url's 
(that is left for another day!).

Also, the implementation does not include provision for real world problems, like handling the
traffic surge after a celebrity (with a few million followers) posts on their social media a shortened link. 
Mechanisms to avoid this are external to the shortening service and although considered, were not included.

As the focus was on the mechanics of the shortening, no extra provision is made for running multiple instances of the service.

## Usage exampes
Build the app and start the service from your command line (or your IDE)

```
./gradlew bootRun
```
The service runs on `localhost:8080`

from your terminal you can call the shortening REST api
```
curl -d "url=https://www.google.com" -H "Content-Type: application/x-www-form-urlencoded" -X POST http://localhost:8080/url
```

that will give you back 

```
http://pico.url/ds7zUba
```

you can resolve the short URL back to the original either by calling the REST api 
```
curl -v localhost:8080/url/ds7zUba
```
or resolving it directly, like
```
curl -v localhost:8080/ds7zUba
```

that will return a redirection to the original URL (www.google.com)
```
> GET /url/ds7zUba HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.69.1
> Accept: */*
> 
* Mark bundle as not supporting multiuse
< HTTP/1.1 301 
< Location: https://www.google.com
< Content-Length: 0
< Date: Sat, 30 Jan 2021 17:07:15 GMT
```

if you instruct curl to follow the redirection, then you will see the contents of Google's homepage
```
curl -L localhost:8080/ds7zUba
```

(HINT: doing this test in Insomnia REST client, actually follows the redirection and you can see the Google home page)

*NOTE: the implementation is using a dummy domain http://pico.url/ , so the shortened URL will not work in your browser* 

## Notes
The implementation was developed in a "clean room" style , any resemblance to existing or similar services is purely coincidental.
(After the fact, I found out that there is another implementation also called pico-url, totally unrelated,but the name now seems a poor choice).

## Licence/Disclaimer
This implementation is released under GNU General Public License v3.0.
Please keep in mind that this implementation is not production tested and comes with absolutely no guarantee that it will work for you.
