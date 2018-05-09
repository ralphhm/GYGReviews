# Review Browser
Sample Android Application written in Kotlin to browse reviews of a popular Berlin tour.
Architecture Components ViewModel is used to implement MVVM and support orientation change without losing UI state.
## Dependencies
* [Retrofit 2](http://square.github.io/retrofit)
* [RxJava 2](https://github.com/ReactiveX/RxJava)
* [Groupie](https://github.com/lisawray/groupie)
* [Dagger 2](https://google.github.io/dagger/)
* [Android Architecture Components (ViewModel)](https://developer.android.com/topic/libraries/architecture)

###Open steps
* Add endless scrolling to the review list by fetching the next page when reaching the end of the list to allow users to browse all reviews
* Add caching to provide extended offline functionality

####Caching implementation
The following approach would be taken if caching is a requirement.
* Use a database to store the paged results as review entities with the absolute result position for each review
* On database read sort the reviews by result position
* Use database entries an App start first, eventually try to fetch a fresh version of reviews
* Allow the user to refresh the (stale) content and replace stale database content on success
* Differentiate between initial fetch, next page fetch and refresh fetch

###Submit review POST request structure
To be symmetric to the GET request the submit call could go to the same url but with POST method
````
curl -X POST \
  http://www.getyourguide.com/berlin-l17/tempelhof-2-hour-airport-history-tour-berlin-airlift-more-t23776/reviews.json
````
As the referencing tour is already path encoded the payload only needs to consist of the attributes of the review.
* 'rating' as float value
* 'title' as String
* 'message' as String
* 'reviewerName' as String

If the user needs to be logged in or authenticated to write a review one could use the 'reviewerName', 'languageCode', 'traveler_type' from the profile. Otherwise language code could be set by analyzing the language of the title/message and the country could be determined by the ip address or by using the location of the phone. The 'author' attribute is a concatenation of 'reviewerName' and 'reviewerCountry'. The date attribute can be determined based on the server time. 

###Improvements
* Create view model for SubmitReviewActivity
* Create UI states for SubmitReviewActivity like Submitting, Success, Failure
* Separate api model from app model by creating higher level models (e.g. don't use api Review model in UI)
* Add (remote) logging of error cases
* Parse dates and show them according to the local date format
* Add input validation for review submission

###Challenge improvements
* Test whether API call works in Android environment (Cloudflare dosn't seem to accept slashes "/" in the User-Agent and returns a 403 html page)
* Define what offline functionality means
