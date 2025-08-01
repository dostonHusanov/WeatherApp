I've built a common Weather App. I used kotlin to build this app. For UI design, i utilized Jetpack compose. 

As For UI, i made the ui design very common using Cards, Columns, Rows,Texts, OutlinedTextField for search, CircularProgressIndicator and Icons

When it comes to api, i used Retrofit to call api. weather data comes from https://api.weatherapi.com.
To use this i created a viewModel called WeatherViewModel and it includes some functions.
For example getData(), it gets datas from api. And saveCity(), this saves searched location to shared preference. 
getSavedCityList is created to read searched cities.


1. Main Screen ✅
○ A text input to enter a city name.✅
○ A "Search" button.✅
○ A card or section to display:✅
■ City name✅
■ Current temperature✅
■ Weather condition (e.g., "Cloudy", "Rainy")✅
■ An icon representing the weather (optional, bonus)✅

2. API
○ Use the free OpenWeatherMap API or similar.✅
○ Hardcode the API key or store it safely✅

4. Tech Requirements
○ Kotlin preferred ✅
○ Use of ViewModel, LiveData/StateFlow, and coroutines or RxJava for
async operations.✅
○ Use either Jetpack ✅.
○ Clean architecture ✅

5. Error Handling
○ Show a basic error message if:✅
■ The city is not found.✅
■ There is a network failure.✅

🧪 Bonus 
● Use Jetpack Compose✅
● Show loading state ✅
● Persist the last searched city using SharedPreferences✅
● Unit test one part (e.g., ViewModel or Repository).✅

● Improve the UI or add one/two small features of your own design

