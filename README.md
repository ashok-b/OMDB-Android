# OMDB-Android Search
### Goal
Search for movies/TV shows on [The Open Movie Database](http://www.omdbapi.com/)

* Tech-stack
    * [Kotlin](https://kotlinlang.org/) + [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - Perform background operation
    * [Retrofit](https://square.github.io/retrofit/) - Networking
    * [Jetpack](https://developer.android.com/jetpack)
        * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Notify views about database change
        * [Lifecycle](https://developer.android.com/topic/libraries/architecture/lifecycle) - Perform action when lifecycle state changes
        * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Store and manage UI-related data in a lifecycle conscious way
        * [Room](https://developer.android.com/topic/libraries/architecture/room) - The Room persistence library provides an abstraction layer over SQLite
    * [Shimmer effect for Android](https://facebook.github.io/shimmer-android/) - provides an easy way to add a shimmer effect to any view
       
* Architecture
    * Clean Architecture
    * MVVM
    * [Android Architecture components](https://developer.android.com/topic/libraries/architecture)
 
 
 
 
### Home with default search results "friends" & Realtime search over api 
![Shimmer Loading](https://github.com/ashok-b/OMDB-Android/blob/master/shimmer_loading.png)  	![Home](https://github.com/ashok-b/OMDB-Android/blob/master/home.png)

### Realtime search over api 
![Realtime search](https://github.com/ashok-b/OMDB-Android/blob/master/realtime_search.png)

### No intenet connection 
![No internet with retry](https://github.com/ashok-b/OMDB-Android/blob/master/no_internet_when_no_cache.png)    ![No internet without retry](https://github.com/ashok-b/OMDB-Android/blob/master/no_internet_during_search.png)

 
### TODO
 * [Paging](https://developer.android.com/topic/libraries/architecture/paging)
 * Dagger
 
