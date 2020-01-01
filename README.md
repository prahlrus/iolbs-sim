# iolbs-sim
A combat simulator for the IOLBSLOFBG roleplaying game. To add one's own beasts to the 
bestiary, one need only modify the beasts.csv file.

# running
This project uses a hyper-entity-component-system architecture. After compiling and installing 
the [hecs artifact](https://github.com/prahlrus/hecs "com.stinja:hecs") locally, run:

```
mvn package
java -jar target/iolbs-sim beasts.csv
```