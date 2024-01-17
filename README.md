[![telegramium](https://img.shields.io/badge/telegramium-8.69.0-blue)](https://github.com/apimorphism/telegramium)
[![doobie](https://img.shields.io/badge/doobie-1.0.0--RC1-orange)](https://github.com/tpolecat/doobie)
[![cats-effect](https://img.shields.io/badge/cats--effect-3.4.8-red)](https://github.com/typelevel/cats-effect)



## CinemotionBot

CinemotionBot is a pure functional Scala telegram bot written with ***telegramium*** library.  
The bot recommends movies to users based on their favorite genres.

## Usage

On start bot adds a user to a PostgreSQL database and suggest to select their favorite genres.  
Database interaction is performed with ***doobie***. Then user can get a recommended film.

## Utilities

Films data is got via ***akka-http*** requests to a OMDb Api (https://www.omdbapi.com/) and then parsed with
***circe***.
To run and configure the application ***docker*** and ***pureconfig*** are used.

## Commands

* /film - get a recommended film
* /reset - reset your favorite genres and choose new ones





