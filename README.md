
#Coding Challenge
Develop an Android application that mimics some functionality of the 'Money transfer' mobile app.

[![Build Status](https://travis-ci.org/dongyuan/WorldRemit-interview.svg)](https://travis-ci.org/dongyuan/WorldRemit-interview)

##Table of Contents
* [Requirements](#0)
* [1. Design decisions](#1)
	* [1.1 Requirement](#1.1)
	* [1.2 Code Style](#1.2)
	* [1.3 Architecture](#1.3)
	* [1.4 Testing](#1.4)
	* [1.5 Some areas need to be improved](#1.5)
	* [1.6 Third-party libraries used in this task](#1.6)
* [2. Which architectural patterns did you use in the past for mobile app development if any?](#2)
* [3. What resources would you recommend to someone starting out in Android development?](#3)
* [4. How do you keep up with the latest in Android development?](#4)
* [5. Describe yourself in JSON format.](#5)
* [6. List some of your favourite libraries with a brief description.](#6)
* [7. What are the top 5 tools that you could not live without?](#7)


<a name="0"></a>
##Requirements
* The user should be able to select a contact from their contact list to send money to
* The user should be able to select send and receive currencies from a list of currencies. To get the available currencies to choose from: GET [https://wr-interview.herokuapp.com/api/currencies]( GET [https://wr-interview.herokuapp.com/api/currencies)
* The user should be able to enter send amount
* The user should be able to press a button which sends a request similar to [https://wr-interview.herokuapp.com/api/calculate?amount=100&sendcurrency=gbp&receivecurrency=usd](https://wr-interview.herokuapp.com/api/calculate?amount=100&sendcurrency=gbp&receivecurrency=usd) to calculate how much the recipient will receive (see below for query parameter description)
* The app should present the receive amount to the user - get this data from the API response
* The user should be able to press a button to send the amount to the selected contact. Call [https://wr-interview.herokuapp.com/api/send](https://wr-interview.herokuapp.com/api/send)with the appropriate JSON payload - described below
* Write unit tests wherever you see fit

---------
##Example requests / responses
###Get currencies
```
GET https://wr-interview.herokuapp.com/api/currencies
```

Sample response

```
 ["GBP", "USD", "PHP", "EUR"]
```
-----
###Calculate
```
GET https://wr-interview.herokuapp.com/api/calculate?amount=a&sendcurrency=c&receivecurrency=c
```

Query parameters


* a: amount is a decimal number (e.g.: 123.45)
* c: currency is a string matching the selected currency code received from the API (e.g.: "GBP")


Sample request:

```
GET https://wr-interview.herokuapp.com/api/calculate?amount=120&sendcurrency=GBP&receivecurrency=EUR
```

Sample response:

```
{
    "sendamount": 120,
    "sendcurrency": "GBP",
    "receiveamount": 162.7716,
    "receivecurrency": "EUR"
}
```
-----
###Send money

```
POST https://wr-interview.herokuapp.com/api/send
```

Sample request:

```
POST https://wr-interview.herokuapp.com/api/send

Content-Type: application/json

{
    "sendamount": 120,
    "sendcurrency": "GBP",
    "receiveamount": 105.12,
    "receivecurrency": "EUR",
    "recipient": "Bob Classy"
}
```

Sample response:

```
201 Created
```

-----

<a name="1"></a>
##1. Design decisions
<a name="1.1"></a>
###1.1 Requirement
--------

The requirement has been tweaked and the *'calculate'* button is no longer needed. A *'done'* button has been added in the keyboard instead while the user is editing the *send* amount field.

The app will calculate the *receive* amount automatically once one of the value in the *send* amount, *send* currency or *receive* currency is changed.

<a name="1.2"></a>
###1.2 Code Style
--------

I use the [code style](https://source.android.com/source/code-style.html) Google Android team used to make my code  consistent with the [Android SDK source code](https://source.android.com/source/index.html).

<a name="1.3"></a>
###1.3 Architecture
--------
Model View Presenter(MPV) pattern is used in the app which allows separate the presentation layer from the logic and also let us test them independently. 

####View
A <code>MainView</code> interface has been created and it's implemented by <code>MainActivity</code>. It has the responsibility to create the <code>MainPresenter</code> object. The only thing that the view will do is calling a method from the <code>presenter</code> every time there is an interface action.

####Presenter
The <code>MainPresenter</code>  is responsible to act as *the middle man between view and model*. It retrieves data from the *model* and returns it formatted to the <code>MainView</code>. It's also decides what happens when the end user interacts with the <code>MainView</code>.

####Model:
In an application with a good layered architecture, this *model* would only be the gateway to the domain layer or business logic. For now, it is enough to see it as the provider of the data we want to display in the view.


#####Rest client
We should never be making network requests directly from an *Activity* or *Fragment*. Even using AsyncTask is asking for trouble. For example, the caller activity could be gone in the stack when the request task finally completes. Holding the caller activity reference is also a common mistake causing memory leak issue.

Therefore, <code>WorldRemitService</code> class has been created, which makes all network requests and handle the responses. [Otto](http://square.github.io/otto/) is used to communicate with the <code>MainPresenter</code> without keeping direct references. [Retroifit](http://square.github.io/retrofit/) is also used to implement the WorldRemit REST client. You can find the implementation from <code>WorldRemitApi</code> class.


My approach is not perfect and I will highlight areas for improvement below.
<a name="1.4"></a>
###1.4 Testing:
--------
In most cases, I would like to use TDD in my project. So I write the Unit tests first based on the requirements.
There are two Test cases created against <code>MainActivity<code> and </code>MainPresenter</code> respectively. Obviously we should add more to make our code robust in the production code, however it's a interview task and it's sufficient to show my Android testing skill.


I also have used [Monkey](http://developer.android.com/tools/help/monkey.html) to test my app. Please give it a try and it's fun.

```
$ adb shell monkey -p worldremit.worldremit.android -v 500

```
<a name="1.5"></a>
###1.5 Some areas need to be improved
--------
Bear in mind this demo app is not a complete solution. The app always re-query data when the activity is resumed - this is not ideal for battery life or necessary for data 'freshness'. We could address this with proper use of <code> onSaveInstanceSate()</code> or some state-keeping to determine if a request is already in-flight.

There are also more areas need to be improved.

#### UI/UX improvement 
It's better to show a progress bar or indicator after sending network requests, so the user knows something is happening. 

#### Localisation
All strings/copies need to be moved to String.xml. So we can localise them for different languages and it's easy to maintain as well.

#### OO design
For example, the <code>String</code> is used to present the *Currency* in the project. It's better to create or use existing <code>[Currency](http://developer.android.com/reference/java/util/Currency.html)</code> class to hold the value. 

#### Error handling
Lots of error handling implementation are missing due to the lack of requirements. We have to handle these errors gracefully in the production code.

#### Caching
Obviously we don't really need to re-query the available currencies all the time. We can set the *cache headers* on the server side and use <code>[HttpResponseCache](http://developer.android.com/reference/android/net/http/HttpResponseCache.html)</code> in the client to handle the caching.

#### Network errors
The app doesn't have much resilience against network errors or loss of connection. The next step for addressing these issues would probably be to introduce a queue/job manager to handle retries and perceiving user input.

<a name="1.6"></a>
###1.6 Third-party libraries used in this task
--------

* [Retroifit](http://square.github.io/retrofit/)
* [Otto](http://square.github.io/otto/)
* [GSon](https://code.google.com/p/google-gson/)


##Additional questions and Answers

<a name="2"></a>
##2. Which architectural patterns did you use in the past for mobile app development if any?


* [MVC](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller)
* [MVP](http://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93presenter)
* [RxJava](https://github.com/ReactiveX/RxJava/wiki)


<a name="3"></a>
##3. What resources would you recommend to someone starting out in Android development?

The official [Android developer website](http://developer.android.com/index.html) is best starting point. It has everything you need. Google also published a free online course on [udacity](https://www.udacity.com/course/ud853).  Just following the [Training](http://developer.android.com/training/index.html) and the [course](https://www.udacity.com/course/ud853), in the meantime doing more practice, you would be a qualified Android developer quickly.

[Google](http://www.google.com) and [Stackoverflow](http://stackoverflow.com/) are the best resources to find answers if you have any question.
<a name="4"></a>
##4. How do you keep up with the latest in Android development?

Personally, I believe practicing is most efficient way to keep your development knowledge up to date.
I listed some resources/activities below which help me to be a *rockstar*.

1. Mobile conferences, such as [Google IO](https://events.google.com/io2015/}) and [Droidcon](http://uk.droidcon.com/2015/). 
2. Android developer's Blogs, such as [Android Developers Blog](http://android-developers.blogspot.co.uk/), [Jake Waston](http://jakewharton.com/) and [Cyril Mottier](http://cyrilmottier.com/).
3. [Andriod Weekly newsletter](http://androidweekly.net/).
4. Google Plus Communities: Such as [Android Development](https://plus.google.com/communities/105153134372062985968), [Android Developer Tools](https://plus.google.com/communities/114791428968349268860) and [Android Design Community](https://plus.google.com/communities/117140012142044995433).
5. Youtube Android development [channel](https://www.youtube.com/user/androiddevelopers).
6. Android developer [meetup](http://www.meetup.com/android/).
7. News aggregator, such as [zite](http://zite.com/) and [Flipboard](https://flipboard.com/)
8. Open source projects: such as Google [Android source code](https://source.android.com/), Google [Github](https://github.com/google) and Square [Github](https://github.com/square).
9. [Stackoverflow](http://stackoverflow.com/).
10. Keep programming, practicing and finding challenges. 


<a name="5"></a>
##5. Describe yourself in JSON format.


```
{
   "name":"Eric Yuan",
   "gender":"Male",
   "coding_years":25,
   "Specialisation":[
      "Business strategy",
      "Innovation",
      "Technical management",
      "Requirement analysis",
      "Software architecture design",
      "Android development",
      "iOS development"
   ],
   "previous_roles":[
      "CEO & Founder",
      "Chief Information Officer",
      "Deputy Director",
      "Principal Development Engineer Lead",
      "Senior Cross Platform Developer",
      "Senior Mobile Application Engineer"
   ],
   "achievements":[
      "Winner of Samsung’s micro Smart App Challenge",
      "The Innovation Star Enterprise reward as the rapid development and innovative service",
      "Winner of China Ningbo Hi-Tech Business planning competition",
      "Finalist of Blueprint Business Planning Competition"
   ],
   "education":{
      "university":[
         "MSc Software Engineering at University Oxford",
         "Master of Business Administration at University of Durham"
      ]
   },
   "interests":[
      "coding",
      "traveling",
      "reading"
   ]
}
```

<a name="6"></a>
##6. List some of your favourite libraries with a brief description.

In the real world, [The British library](http://www.bl.uk/) and my university's [libary](http://www.bodleian.ox.ac.uk/) are my favourite libraries.

In term of the *Mobile* libraries, the content in the list changes very often, it depends which area and the nature of the project I am working on. In general, there are few libraries I would recommend.

### Android:
* [Volley](http://developer.android.com/training/volley/index.html): Google claims that it's the fastest *network library* . It has lots of *advanced* features, such as request prioritisation, request cancellation and automatic scheduling of network requests etc.

* [Retrofit](http://square.github.io/retrofit/): It’s an elegant solution for organising API calls in a project. The request method and relative URL are added with an annotation, which makes code clean and simple.

* [GSon](https://code.google.com/p/google-gson/): It makes serialising and deserialising Java objects from and into JSON much easier.

* [Picasso](http://square.github.io/picasso/): It's a handy library which provides asynchronous, out of the box loading and caching of images. 

* [Otto](http://square.github.io/otto/): It's an event bus designed to decouple different parts of your application while still allowing them to communicate efficiently.


An interesting [article](http://instructure.github.io/blog/2013/12/09/volley-vs-retrofit/) comparing [Volley](http://developer.android.com/training/volley/index.html) and [Retrofit](http://square.github.io/retrofit/)


### iOS:
* [RestKit](http://restkit.org/): It makes implementing RESTfull services clients much easier.

* [AFNetworking](http://afnetworking.com/): Newly introduced NSURLSession has huge improvement over NSURLConnection, but AFNetworking make implementing RESTfull services client much simpler.

* [CocoPods](http://cocoapods.org/): It's not really a *library*, but it makes adding other libraries so much easier.


### Cross-platform
* [Crashlytics](https://try.crashlytics.com/): Very good Crash reporting solution and It's FREE!

* [Google Anaylstic](http://www.google.com/analytics/mobile/): Almost every apps need analytics to get a sense of how people are using them. 

* [Calasbash](http://calaba.sh/): It's cross-platform test automation solution. Writing test case once and it runs on Android and iOS.


<a name="7"></a>
##7. What are the top 5 tools that you could not live without?

New technologies coming every year, you would probably get a complete different list if you ask me the same question  in the next few months

* [Internet](http://en.wikipedia.org/wiki/Internet)

* [Google](http://www.google.co.uk/intl/en/about/products/)

* [Computer](http://en.wikipedia.org/wiki/Computer)

* [Mobile phone](http://en.wikipedia.org/wiki/Mobile_phone)

* [Humour](http://en.wikipedia.org/wiki/Humour): If you are wondering if *Humour* is a *tool*, please check [this](https://www.econ.iastate.edu/sites/default/files/publications/papers/p4506-2000-06-01.pdf). To be honest, I am not using it very well and still learning, but making the customer, the colleague, the boss and my lovely wife smile is  the happiest things in my life.

<b>Thank you for taking the time to read my answer and I would love to hear your thoughts, criticisms or suggestions regarding my answer.
